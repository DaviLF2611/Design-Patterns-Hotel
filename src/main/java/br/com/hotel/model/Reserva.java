package br.com.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public final class Reserva implements ReservaCalculavel {
    private final Quarto quarto;
    private final Hospede hospede;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    Reserva(Quarto quarto, Hospede hospede, LocalDate checkIn, LocalDate checkOut) {
        this.quarto = Objects.requireNonNull(quarto, "O quarto é obrigatório.");
        this.hospede = Objects.requireNonNull(hospede, "O hóspede é obrigatório.");
        this.checkIn = Objects.requireNonNull(checkIn, "A data de check-in é obrigatória.");
        this.checkOut = Objects.requireNonNull(checkOut, "A data de check-out é obrigatória.");
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("O check-out deve ocorrer depois do check-in.");
        }
    }

    public Quarto getQuarto() {//pega os dados do Quarto.java
        return quarto;
    }

    public Hospede getHospede() {//pega os dados do Hospede.java
        return hospede;
    }

    public LocalDate getCheckIn() {//pega a data de check-in
        return checkIn;
    }

    public LocalDate getCheckOut() {//pega a data de check-out
        return checkOut;
    }

    @Override
    public long getQuantidadeDiarias() {//calcula quantidade de dias de reserva
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @Override
    public BigDecimal getValorTotal() {//calcula o valor total da reserva multiplicando a diária do quarto pelo número de diárias
        return quarto.getDiaria().multiply(BigDecimal.valueOf(getQuantidadeDiarias()));
    }

    @Override
    public List<String> getServicos() {//lista de serviços adicionais, como café da manhã, estacionamento e desconto percentual
        return List.of();
    }
}
