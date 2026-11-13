package br.com.hotel.model;

import java.math.BigDecimal;

public final class EstacionamentoDecorator extends ReservaDecorator {
    private static final BigDecimal VALOR_POR_DIARIA = new BigDecimal("25.00");

    public EstacionamentoDecorator(ReservaCalculavel reserva) {
        super(reserva);
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_DIARIA.multiply(BigDecimal.valueOf(getQuantidadeDiarias()));
    }

    @Override
    protected String descricao() {
        return "Estacionamento";
    }
}
