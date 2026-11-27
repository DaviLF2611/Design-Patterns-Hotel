package br.com.hotel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservaBuilderPassosTest {
    private static final Hospede ANA = new Hospede("Ana Silva", "ana@example.com");
    private static final Quarto STANDARD = new Quarto(12, "Standard", new BigDecimal("250.00"));
    private static final Quarto FAMILIA = new Quarto(30, "Família", new BigDecimal("400.00"), 4);

    private static ReservaBuilder reservaBase(Quarto quarto) {
        return new ReservaBuilder()
                .quarto(quarto)
                .hospede(ANA)
                .checkIn(LocalDate.of(2026, 12, 1))
                .checkOut(LocalDate.of(2026, 12, 4));
    }

    @Test
    void semOsNovosPassosAssumeUmHospedeESemObservacao() {
        Reserva reserva = reservaBase(STANDARD).build();

        assertEquals(1, reserva.getQuantidadeHospedes());
        assertEquals("", reserva.getObservacao());
    }

    @Test
    void montaReservaComHospedesEObservacao() {
        Reserva reserva = reservaBase(FAMILIA)
                .quantidadeHospedes(4)
                .observacao("  chegada depois das 22h  ")
                .build();

        assertEquals(4, reserva.getQuantidadeHospedes());
        assertEquals("chegada depois das 22h", reserva.getObservacao());
        assertEquals(new BigDecimal("1200.00"), reserva.getValorTotal());
    }

    @Test
    void observacaoNulaViraTextoVazio() {
        Reserva reserva = reservaBase(STANDARD).observacao(null).build();

        assertEquals("", reserva.getObservacao());
    }

    @Test
    void rejeitaReservaSemHospede() {
        ReservaBuilder builder = reservaBase(STANDARD).quantidadeHospedes(0);

        IllegalStateException erro = assertThrows(IllegalStateException.class, builder::build);

        assertEquals("A reserva deve ter pelo menos 1 hóspede.", erro.getMessage());
    }

    @Test
    void rejeitaMaisHospedesQueACapacidadeDoQuarto() {
        ReservaBuilder builder = reservaBase(STANDARD).quantidadeHospedes(3);

        IllegalStateException erro = assertThrows(IllegalStateException.class, builder::build);

        assertEquals("O quarto 12 comporta no máximo 2 hóspede(s).", erro.getMessage());
    }

    @Test
    void aceitaExatamenteACapacidadeDoQuarto() {
        Reserva reserva = reservaBase(STANDARD).quantidadeHospedes(2).build();

        assertEquals(2, reserva.getQuantidadeHospedes());
    }

    @Test
    void rejeitaObservacaoLongaDemais() {
        ReservaBuilder builder = reservaBase(STANDARD).observacao("a".repeat(201));

        IllegalStateException erro = assertThrows(IllegalStateException.class, builder::build);

        assertEquals("A observação deve ter no máximo 200 caracteres.", erro.getMessage());
    }

    @Test
    void aceitaObservacaoComOLimiteExato() {
        Reserva reserva = reservaBase(STANDARD).observacao("a".repeat(200)).build();

        assertEquals(200, reserva.getObservacao().length());
    }
}
