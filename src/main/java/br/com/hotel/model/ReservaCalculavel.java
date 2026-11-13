package br.com.hotel.model;

import java.math.BigDecimal;
import java.util.List;

public interface ReservaCalculavel {
    BigDecimal getValorTotal();

    long getQuantidadeDiarias();

    List<String> getServicos();
}
