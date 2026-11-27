package br.com.hotel.model;

import java.math.BigDecimal;

public final class TransferAeroportoDecorator extends ReservaDecorator {
    private static final BigDecimal VALOR_POR_TRECHO = new BigDecimal("60.00");

    private final int trechos;

    public TransferAeroportoDecorator(ReservaCalculavel reserva, int trechos) {
        super(reserva);
        if (trechos < 1 || trechos > 2) {
            throw new IllegalArgumentException("O transfer deve ter 1 ou 2 trechos.");
        }
        this.trechos = trechos;
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_TRECHO.multiply(BigDecimal.valueOf(trechos));
    }

    @Override
    protected String descricao() {
        return "Transfer aeroporto (" + trechos + " trecho(s))";
    }
}
