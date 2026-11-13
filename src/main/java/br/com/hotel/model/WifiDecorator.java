package br.com.hotel.model;

import java.math.BigDecimal;

public class WifiDecorator extends ReservaDecorator {
    private static final BigDecimal VALOR_POR_DIARIA = new BigDecimal("15.00");

    public WifiDecorator(ReservaCalculavel reserva) {
        super(reserva);
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_DIARIA.multiply(BigDecimal.valueOf(getQuantidadeDiarias()));
    }

    @Override
    protected String descricao() {
        return "Wi-Fi";
    }
}
