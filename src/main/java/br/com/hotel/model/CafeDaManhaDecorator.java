package br.com.hotel.model;

import java.math.BigDecimal;

public final class CafeDaManhaDecorator extends ReservaDecorator {
    private static final BigDecimal VALOR_POR_PESSOA_E_DIARIA = new BigDecimal("40.00");

    private final int pessoas;

    public CafeDaManhaDecorator(ReservaCalculavel reserva, int pessoas) {
        super(reserva);
        if (pessoas <= 0) {
            throw new IllegalArgumentException("A quantidade de pessoas deve ser positiva.");
        }
        this.pessoas = pessoas;
    }

    @Override
    protected BigDecimal calcularAcrescimo() {
        return VALOR_POR_PESSOA_E_DIARIA
                .multiply(BigDecimal.valueOf(getQuantidadeDiarias()))
                .multiply(BigDecimal.valueOf(pessoas));
    }

    @Override
    protected String descricao() {
        return "Café da manhã para " + pessoas + " pessoa(s)";
    }
}
