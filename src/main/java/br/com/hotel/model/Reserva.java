package br.com.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class Reserva {
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

    public Quarto getQuarto() {
        return quarto;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public long getQuantidadeDiarias() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public BigDecimal getValorTotal() {
        return quarto.getDiaria().multiply(BigDecimal.valueOf(getQuantidadeDiarias()));
    }
}
