package br.com.hotel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservaDecoratorTest {
    private Reserva reserva;

    @BeforeEach
    void criaReserva() {
        reserva = new ReservaBuilder()
                .quarto(new Quarto(12, "Standard", new BigDecimal("250.00")))
                .hospede(new Hospede("Ana Silva", "ana@example.com"))
                .checkIn(LocalDate.of(2026, 12, 1))
                .checkOut(LocalDate.of(2026, 12, 4))
                .build();
    }

    @Test
    void reservaSemDecoradoresNaoTemServicos() {
        assertTrue(reserva.getServicos().isEmpty());
    }

    @Test
    void cafeDaManhaCobraPorPessoaEPorDiaria() {
        ReservaCalculavel comCafe = new CafeDaManhaDecorator(reserva, 2);

        assertEquals(new BigDecimal("990.00"), comCafe.getValorTotal());
        assertEquals(List.of("Café da manhã para 2 pessoa(s)"), comCafe.getServicos());
    }

    @Test
    void estacionamentoCobraPorDiaria() {
        ReservaCalculavel comEstacionamento = new EstacionamentoDecorator(reserva);

        assertEquals(new BigDecimal("825.00"), comEstacionamento.getValorTotal());
        assertEquals(List.of("Estacionamento"), comEstacionamento.getServicos());
    }

    @Test
    void spaCobraValorFixoPorEstadia() {
        ReservaCalculavel comSpa = new SpaDecorator(reserva);

        assertEquals(new BigDecimal("850.00"), comSpa.getValorTotal());
        assertEquals(List.of("Spa"), comSpa.getServicos());
    }

    @Test
    void descontoAbatePercentualDoTotalAcumulado() {
        ReservaCalculavel comDesconto = new DescontoPercentualDecorator(
                reserva, new BigDecimal("10"));

        assertEquals(new BigDecimal("675.00"), comDesconto.getValorTotal());
        assertEquals(List.of("Desconto de 10%"), comDesconto.getServicos());
    }

    @Test
    void descontoArredondaParaDuasCasas() {
        ReservaCalculavel comDesconto = new DescontoPercentualDecorator(
                reserva, new BigDecimal("12.5"));

        assertEquals(new BigDecimal("656.25"), comDesconto.getValorTotal());
    }

    @Test
    void combinaDecoradoresEmCadeiaPreservandoAOrdemDosServicos() {
        ReservaCalculavel completa = new EstacionamentoDecorator(
                new CafeDaManhaDecorator(reserva, 1));

        assertEquals(new BigDecimal("945.00"), completa.getValorTotal());
        assertEquals(
                List.of("Café da manhã para 1 pessoa(s)", "Estacionamento"),
                completa.getServicos());
    }

    @Test
    void ordemDoDescontoNaCadeiaAlteraOResultado() {
        ReservaCalculavel descontoDepoisDoSpa = new DescontoPercentualDecorator(
                new SpaDecorator(reserva), new BigDecimal("10"));
        ReservaCalculavel descontoAntesDoSpa = new SpaDecorator(
                new DescontoPercentualDecorator(reserva, new BigDecimal("10")));

        assertEquals(new BigDecimal("765.00"), descontoDepoisDoSpa.getValorTotal());
        assertEquals(new BigDecimal("775.00"), descontoAntesDoSpa.getValorTotal());
    }

    @Test
    void decoradoresMantemQuantidadeDeDiarias() {
        ReservaCalculavel completa = new SpaDecorator(new EstacionamentoDecorator(reserva));

        assertEquals(3, completa.getQuantidadeDiarias());
    }

    @Test
    void naoAlteramAReservaOriginal() {
        new SpaDecorator(new CafeDaManhaDecorator(reserva, 2));

        assertEquals(new BigDecimal("750.00"), reserva.getValorTotal());
        assertTrue(reserva.getServicos().isEmpty());
    }

    @Test
    void listaDeServicosEhImutavel() {
        ReservaCalculavel comSpa = new SpaDecorator(reserva);

        assertThrows(UnsupportedOperationException.class, () -> comSpa.getServicos().add("Outro"));
    }

    @Test
    void rejeitaReservaNula() {
        assertThrows(NullPointerException.class, () -> new CafeDaManhaDecorator(null, 1));
        assertThrows(NullPointerException.class, () -> new EstacionamentoDecorator(null));
        assertThrows(NullPointerException.class, () -> new SpaDecorator(null));
        assertThrows(NullPointerException.class,
                () -> new DescontoPercentualDecorator(null, BigDecimal.TEN));
    }

    @Test
    void cafeDaManhaRejeitaQuantidadeDePessoasInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new CafeDaManhaDecorator(reserva, 0));
        assertThrows(IllegalArgumentException.class, () -> new CafeDaManhaDecorator(reserva, -1));
    }

    @Test
    void descontoRejeitaPercentualInvalido() {
        assertThrows(NullPointerException.class,
                () -> new DescontoPercentualDecorator(reserva, null));
        assertThrows(IllegalArgumentException.class,
                () -> new DescontoPercentualDecorator(reserva, new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class,
                () -> new DescontoPercentualDecorator(reserva, new BigDecimal("100.01")));
    }

    @Test
    void descontoDeCemPorCentoZeraOTotal() {
        ReservaCalculavel gratis = new DescontoPercentualDecorator(reserva, new BigDecimal("100"));

        assertEquals(0, gratis.getValorTotal().signum());
    }
}
