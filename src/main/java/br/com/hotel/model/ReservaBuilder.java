package br.com.hotel.model;

import java.time.LocalDate;

public final class ReservaBuilder {
    private Quarto quarto;
    private Hospede hospede;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public ReservaBuilder quarto(Quarto quarto) {
        this.quarto = quarto;
        return this;
    }

    public ReservaBuilder hospede(Hospede hospede) {
        this.hospede = hospede;
        return this;
    }

    public ReservaBuilder checkIn(LocalDate checkIn) {
        this.checkIn = checkIn;
        return this;
    }

    public ReservaBuilder checkOut(LocalDate checkOut) {
        this.checkOut = checkOut;
        return this;
    }

    public Reserva build() {
        if (quarto == null || hospede == null || checkIn == null || checkOut == null) {
            throw new IllegalStateException(
                    "Quarto, hóspede, check-in e check-out devem ser informados.");
        }
        return new Reserva(quarto, hospede, checkIn, checkOut);
    }
}
