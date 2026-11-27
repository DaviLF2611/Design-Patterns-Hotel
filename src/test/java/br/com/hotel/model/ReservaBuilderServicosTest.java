package br.com.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservaBuilderServicosTest {

    private Quarto quarto;
    private Hospede hospede;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        quarto = new Quarto(12, "Standard", new BigDecimal("250.00"), 2);
        hospede = new Hospede("Ana", "12345678900");
        checkIn = LocalDate.of(2026, 12, 1);
        checkOut = LocalDate.of(2026, 12, 4); // 3 diárias = 750,00
    }

    private ReservaBuilder builderBase() {
        return new ReservaBuilder()
                .quarto(quarto)
                .hospede(hospede)
                .checkIn(checkIn)
                .checkOut(checkOut);
    }

    @Test
    void buildComServicosSemServicoDevolveMesmoTotalDaReserva() {
        ReservaCalculavel reserva = builderBase().buildComServicos();

        assertEquals(0, new BigDecimal("750.00").compareTo(reserva.getValorTotal()));
        assertTrue(reserva.getServicos().isEmpty());
    }

    @Test
    void ordemDosServicosMantidaEmGetServicos() {
        ReservaCalculavel reserva = builderBase()
                .comServico(r -> new CafeDaManhaDecorator(r, 2))
                .comServico(SpaDecorator::new)
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .buildComServicos();

        List<String> servicos = reserva.getServicos();
        assertEquals(3, servicos.size());
        assertTrue(servicos.get(0).toLowerCase().contains("café"));
        assertTrue(servicos.get(1).toLowerCase().contains("spa"));
        assertEquals("Taxa de serviço de 10%", servicos.get(2));
    }

    @Test
    void ordemMudaTotalComDescontoETaxa() {
        // Caso 1: 750,00 -> +10% taxa (825,00) -> -10% desconto (742,50)
        ReservaCalculavel taxaDepoisDesconto = builderBase()
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .comServico(r -> new DescontoPercentualDecorator(r, new BigDecimal("10")))
                .buildComServicos();

        // Caso 2: 750,00 -> +100,00 spa (850,00) -> +10% taxa (935,00)
        ReservaCalculavel spaDepoisTaxa = builderBase()
                .comServico(SpaDecorator::new)
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .buildComServicos();

        // Caso 3: 750,00 -> +10% taxa (825,00) -> +100,00 spa (925,00)
        ReservaCalculavel taxaDepoisSpa = builderBase()
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .comServico(SpaDecorator::new)
                .buildComServicos();

        assertEquals(0, new BigDecimal("742.50").compareTo(taxaDepoisDesconto.getValorTotal()));
        assertNotEquals(0, spaDepoisTaxa.getValorTotal().compareTo(taxaDepoisSpa.getValorTotal()));
    }

    @Test
    void servicoNuloRecusado() {
        ReservaBuilder builder = builderBase();
        assertThrows(NullPointerException.class, () -> builder.comServico(null));
    }

    @Test
    void builderInvalidoContinuaFalhandoNoBuildComServicos() {
        ReservaBuilder builderIncompleto = new ReservaBuilder()
                .quarto(quarto)
                .comServico(SpaDecorator::new);

        assertThrows(IllegalStateException.class, builderIncompleto::buildComServicos);
    }

    @Test
    void taxaDeServicoCalculaEValidaPercentual() {
        ReservaCalculavel reservaBase = builderBase().build();

        ReservaCalculavel comTaxa = new TaxaDeServicoDecorator(reservaBase, new BigDecimal("10"));
        assertEquals(0, new BigDecimal("825.00").compareTo(comTaxa.getValorTotal()));
        assertEquals(List.of("Taxa de serviço de 10%"), comTaxa.getServicos());

        assertThrows(IllegalArgumentException.class,
                () -> new TaxaDeServicoDecorator(reservaBase, new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class,
                () -> new TaxaDeServicoDecorator(reservaBase, new BigDecimal("21")));
        assertThrows(NullPointerException.class,
                () -> new TaxaDeServicoDecorator(reservaBase, null));
    }
}