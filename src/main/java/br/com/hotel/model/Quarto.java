package br.com.hotel.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Quarto {//Classe representando um quarto de hotel
    private final int numero;
    private final String categoria;
    private final BigDecimal diaria;

    public Quarto(int numero, String categoria, BigDecimal diaria) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número do quarto deve ser positivo.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria do quarto é obrigatória.");
        }
        Objects.requireNonNull(diaria, "A diária é obrigatória.");
        if (diaria.signum() < 0) {
            throw new IllegalArgumentException("A diária não pode ser negativa.");
        }

        this.numero = numero;
        this.categoria = categoria;
        this.diaria = diaria;
    }

    public int getNumero() {//metodo para pegar numero do quarto
        return numero;
    }

    public String getCategoria() {//metodo para pegar categoria do quarto
        return categoria;
    }

    public BigDecimal getDiaria() {//metodo para pegar diaria do quarto
        return diaria;
    }
}
