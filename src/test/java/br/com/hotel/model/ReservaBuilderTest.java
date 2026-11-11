package br.com.hotel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservaBuilderTest {
    @Test
    void constroiReservaECalculaDiarias() {
        Quarto quarto = new Quarto(12, "Standard", new BigDecimal("250.00"));
        Hospede hospede = new Hospede("Ana Silva", "ana@example.com");

        Reserva reserva = new ReservaBuilder()
                .quarto(quarto)
                .hospede(hospede)
                .checkIn(LocalDate.of(2026, 12, 1))
                .checkOut(LocalDate.of(2026, 12, 4))
                .build();

        assertEquals(3, reserva.getQuantidadeDiarias());
        assertEquals(new BigDecimal("750.00"), reserva.getValorTotal());
        assertEquals(hospede, reserva.getHospede());
        assertEquals(quarto, reserva.getQuarto());
    }

    @Test
    void rejeitaReservaSemTodosOsDadosObrigatorios() {
        assertThrows(IllegalStateException.class, () -> new ReservaBuilder().build());
    }

    @Test
    void rejeitaCheckoutAnteriorOuIgualAoCheckin() {
        Quarto quarto = new Quarto(12, "Standard", new BigDecimal("250.00"));
        Hospede hospede = new Hospede("Ana Silva", "ana@example.com");
        LocalDate data = LocalDate.of(2026, 12, 1);

        assertThrows(IllegalArgumentException.class, () -> new ReservaBuilder()
                .quarto(quarto)
                .hospede(hospede)
                .checkIn(data)
                .checkOut(data)
                .build());
    }
}
