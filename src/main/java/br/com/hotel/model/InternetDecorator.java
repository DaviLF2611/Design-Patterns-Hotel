package br.com.hotel.model;

public class InternetDecorator extends WifiDecorator {
    public InternetDecorator(ReservaCalculavel reserva) {
        super(reserva);
    }

    @Override
    protected String descricao() {
        return "Internet";
    }
}
