package br.com.hotel.model;

import java.math.BigDecimal;

public final class SpaDecorator extends ReservaDecorator {//camada de decoração para adicionar spa à reserva
    private static final BigDecimal VALOR_POR_ESTADIA = new BigDecimal("100.00");

    public SpaDecorator(ReservaCalculavel reserva) {
        super(reserva);
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_ESTADIA;
    }

    @Override
    protected String descricao() {
        return "Spa";
    }
}
