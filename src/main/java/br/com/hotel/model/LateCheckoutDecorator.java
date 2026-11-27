package br.com.hotel.model;

import java.math.BigDecimal;

public final class LateCheckoutDecorator extends ReservaDecorator {
    private static final BigDecimal VALOR_POR_ESTADIA = new BigDecimal("80.00");

    public LateCheckoutDecorator(ReservaCalculavel reserva) {
        super(reserva);
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_ESTADIA;
    }

    @Override
    protected String descricao() {
        return "Late checkout";
    }
}
