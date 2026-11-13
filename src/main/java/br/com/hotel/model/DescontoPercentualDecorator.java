package br.com.hotel.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class DescontoPercentualDecorator extends ReservaDecorator {
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final BigDecimal percentual;

    public DescontoPercentualDecorator(ReservaCalculavel reserva, BigDecimal percentual) {
        super(reserva);
        Objects.requireNonNull(percentual, "O percentual de desconto é obrigatório.");
        if (percentual.signum() < 0 || percentual.compareTo(CEM) > 0) {
            throw new IllegalArgumentException("O percentual de desconto deve estar entre 0 e 100.");
        }
        this.percentual = percentual;
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return getReserva().getValorTotal()
                .multiply(percentual)
                .divide(CEM, 2, RoundingMode.HALF_UP)
                .negate();
    }

    @Override
    protected String descricao() {
        return "Desconto de " + percentual.stripTrailingZeros().toPlainString() + "%";
    }
}
