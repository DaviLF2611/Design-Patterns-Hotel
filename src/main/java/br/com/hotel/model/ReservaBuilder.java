package br.com.hotel.model;

import java.time.LocalDate;

public final class ReservaBuilder {//Classe para construir uma reserva de hotel
    static final int TAMANHO_MAXIMO_OBSERVACAO = 200;

    private Quarto quarto;
    private Hospede hospede;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int quantidadeHospedes = 1;
    private String observacao = "";

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

    public ReservaBuilder quantidadeHospedes(int quantidadeHospedes) {
        this.quantidadeHospedes = quantidadeHospedes;
        return this;
    }

    public ReservaBuilder observacao(String observacao) {
        this.observacao = observacao == null ? "" : observacao.strip();
        return this;
    }

    public Reserva build() {
        if (quarto == null || hospede == null || checkIn == null || checkOut == null) {
            throw new IllegalStateException(
                    "Quarto, hóspede, check-in e check-out devem ser informados.");
        }
        if (quantidadeHospedes < 1) {
            throw new IllegalStateException("A reserva deve ter pelo menos 1 hóspede.");
        }
        if (quantidadeHospedes > quarto.getCapacidade()) {
            throw new IllegalStateException("O quarto " + quarto.getNumero() + " comporta no máximo "
                    + quarto.getCapacidade() + " hóspede(s).");
        }
        if (observacao.length() > TAMANHO_MAXIMO_OBSERVACAO) {
            throw new IllegalStateException(
                    "A observação deve ter no máximo " + TAMANHO_MAXIMO_OBSERVACAO + " caracteres.");
        }
        return new Reserva(quarto, hospede, checkIn, checkOut, quantidadeHospedes, observacao);
    }
}
