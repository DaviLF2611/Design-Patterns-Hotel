package br.com.hotel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class QuartoTest {
    @Test
    void construtorAntigoMantemCapacidadePadraoDeDois() {
        Quarto quarto = new Quarto(12, "Standard", new BigDecimal("250.00"));

        assertEquals(2, quarto.getCapacidade());
    }

    @Test
    void aceitaCapacidadeInformada() {
        Quarto quarto = new Quarto(30, "Família", new BigDecimal("400.00"), 4);

        assertEquals(4, quarto.getCapacidade());
    }

    @Test
    void rejeitaCapacidadeZeroOuNegativa() {
        BigDecimal diaria = new BigDecimal("250.00");

        assertThrows(IllegalArgumentException.class, () -> new Quarto(12, "Standard", diaria, 0));
        assertThrows(IllegalArgumentException.class, () -> new Quarto(12, "Standard", diaria, -1));
    }
}
