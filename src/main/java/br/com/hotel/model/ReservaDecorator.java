package br.com.hotel.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ReservaDecorator implements ReservaCalculavel {
    private final ReservaCalculavel reserva;

    protected ReservaDecorator(ReservaCalculavel reserva) {
        this.reserva = Objects.requireNonNull(reserva, "A reserva é obrigatória.");
    }

    protected final ReservaCalculavel getReserva() {
        return reserva;
    }

    protected abstract BigDecimal calcularAcrescimo();

    protected abstract String descricao();

    @Override
    public final BigDecimal getValorTotal() {
        return reserva.getValorTotal().add(calcularAcrescimo());
    }

    @Override
    public final long getQuantidadeDiarias() {
        return reserva.getQuantidadeDiarias();
    }

    @Override
    public final List<String> getServicos() {
        List<String> servicos = new ArrayList<>(reserva.getServicos());
        servicos.add(descricao());
        return Collections.unmodifiableList(servicos);
    }
}
