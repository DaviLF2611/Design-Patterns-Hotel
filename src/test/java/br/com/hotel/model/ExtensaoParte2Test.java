package br.com.hotel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtensaoParte2Test {
    private Quarto quarto;
    private Hospede ana;

    @BeforeEach
    void preparaCenario() {
        quarto = new Quarto(12, "Standard", new BigDecimal("250.00"), 2);
        ana = new Hospede("Ana Silva", "ana@example.com");
    }

    private ReservaBuilder reservaDeAna() {
        return new ReservaBuilder()
                .quarto(quarto)
                .hospede(ana)
                .checkIn(LocalDate.of(2026, 12, 1))
                .checkOut(LocalDate.of(2026, 12, 4))
                .quantidadeHospedes(2)
                .observacao("chegada depois das 22h");
    }

    @Test
    void cenarioCompletoFechaEm1309ComOsServicosNaOrdemEscolhida() {
        ReservaCalculavel reserva = reservaDeAna()
                .comServico(r -> new CafeDaManhaDecorator(r, 2))
                .comServico(LateCheckoutDecorator::new)
                .comServico(r -> new TransferAeroportoDecorator(r, 2))
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .buildComServicos();

        assertEquals(new BigDecimal("1309.00"), reserva.getValorTotal());
        assertEquals(3, reserva.getQuantidadeDiarias());
        assertEquals(List.of(
                "Café da manhã para 2 pessoa(s)",
                "Late checkout",
                "Transfer aeroporto (2 trecho(s))",
                "Taxa de serviço de 10%"), reserva.getServicos());
    }

    @Test
    void cadaServicoSomaSobreOTotalAcumuladoDaCadeia() {
        ReservaBuilder builder = reservaDeAna();
        assertEquals(new BigDecimal("750.00"), builder.buildComServicos().getValorTotal());

        builder.comServico(r -> new CafeDaManhaDecorator(r, 2));
        assertEquals(new BigDecimal("990.00"), builder.buildComServicos().getValorTotal());

        builder.comServico(LateCheckoutDecorator::new);
        assertEquals(new BigDecimal("1070.00"), builder.buildComServicos().getValorTotal());

        builder.comServico(r -> new TransferAeroportoDecorator(r, 2));
        assertEquals(new BigDecimal("1190.00"), builder.buildComServicos().getValorTotal());

        builder.comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")));
        assertEquals(new BigDecimal("1309.00"), builder.buildComServicos().getValorTotal());
    }

    @Test
    void taxaAplicadaAntesDosOutrosServicosGeraTotalDiferente() {
        ReservaCalculavel taxaPrimeiro = reservaDeAna()
                .comServico(r -> new CafeDaManhaDecorator(r, 2))
                .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
                .comServico(LateCheckoutDecorator::new)
                .comServico(r -> new TransferAeroportoDecorator(r, 2))
                .buildComServicos();

        assertEquals(new BigDecimal("1289.00"), taxaPrimeiro.getValorTotal());
    }

    @Test
    void buildSimplesGuardaOsPassosDeMontagemSemAplicarServicos() {
        Reserva reserva = reservaDeAna()
                .comServico(SpaDecorator::new)
                .build();

        assertEquals(2, reserva.getQuantidadeHospedes());
        assertEquals("chegada depois das 22h", reserva.getObservacao());
        assertEquals(new BigDecimal("750.00"), reserva.getValorTotal());
        assertEquals(List.of(), reserva.getServicos());
    }

    @Test
    void validacaoDoBuilderValeTambemParaAReservaComServicos() {
        ReservaBuilder hospedesDemais = reservaDeAna()
                .quantidadeHospedes(3)
                .comServico(LateCheckoutDecorator::new);

        IllegalStateException erro = assertThrows(
                IllegalStateException.class, hospedesDemais::buildComServicos);

        assertEquals("O quarto 12 comporta no máximo 2 hóspede(s).", erro.getMessage());
    }

    @Test
    void servicoInvalidoNaCadeiaInterrompeAMontagem() {
        ReservaBuilder builder = reservaDeAna()
                .comServico(r -> new TransferAeroportoDecorator(r, 3));

        assertThrows(IllegalArgumentException.class, builder::buildComServicos);
    }
}
