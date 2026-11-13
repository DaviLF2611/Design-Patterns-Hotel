package br.com.hotel.model;

import java.math.BigDecimal;

public final class PetDecorator extends ReservaDecorator {//camada de decoração para adicionar pet à reserva
    private static final BigDecimal VALOR_POR_ESTADIA = new BigDecimal("25.00");

    private final int pets;

    public PetDecorator(ReservaCalculavel reserva, int pets) {
        super(reserva);
        if (pets <= 0) {
            throw new IllegalArgumentException("A quantidade de pets deve ser positiva.");
        }
        this.pets = pets;
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_ESTADIA.multiply(BigDecimal.valueOf(pets));
    }

    @Override
    protected String descricao() {
        return pets + " Pet(s)";
    }
}
