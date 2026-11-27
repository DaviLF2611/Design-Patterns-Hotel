package br.com.hotel.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class TaxaDeServicoDecorator extends ReservaDecorator {
    private static final BigDecimal CEM = BigDecimal.valueOf(100);
    private static final BigDecimal PERCENTUAL_MAXIMO = BigDecimal.valueOf(20);

    private final BigDecimal percentual;

    public TaxaDeServicoDecorator(ReservaCalculavel reserva, BigDecimal percentual) {
        super(reserva);
        Objects.requireNonNull(percentual, "O percentual da taxa de serviço é obrigatório.");
        if (percentual.signum() < 0 || percentual.compareTo(PERCENTUAL_MAXIMO) > 0) {
            throw new IllegalArgumentException("O percentual da taxa de serviço deve estar entre 0 e 20.");
        }
        this.percentual = percentual;
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return getReserva().getValorTotal()
                .multiply(percentual)
                .divide(CEM, 2, RoundingMode.HALF_UP);
    }

    @Override
    protected String descricao() {
        return "Taxa de serviço de " + percentual.stripTrailingZeros().toPlainString() + "%";
    }
}