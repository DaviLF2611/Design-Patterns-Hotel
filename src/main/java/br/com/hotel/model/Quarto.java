package br.com.hotel.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Quarto {//Classe representando um quarto de hotel
    private static final int CAPACIDADE_PADRAO = 2;

    private final int numero;
    private final String categoria;
    private final BigDecimal diaria;
    private final int capacidade;

    public Quarto(int numero, String categoria, BigDecimal diaria) {
        this(numero, categoria, diaria, CAPACIDADE_PADRAO);
    }

    public Quarto(int numero, String categoria, BigDecimal diaria, int capacidade) {
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
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade do quarto deve ser positiva.");
        }

        this.numero = numero;
        this.categoria = categoria;
        this.diaria = diaria;
        this.capacidade = capacidade;
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

    public int getCapacidade() {
        return capacidade;
    }
}
