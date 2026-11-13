package br.com.hotel.model;

import java.math.BigDecimal;
import java.util.List;

public interface ReservaCalculavel {//Interface que define os métodos para calcular o valor total da reserva, a quantidade de diárias e os serviços adicionais
    BigDecimal getValorTotal();

    long getQuantidadeDiarias();

    List<String> getServicos();
}
