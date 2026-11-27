package br.com.hotel;

import br.com.hotel.model.CafeDaManhaDecorator;
import br.com.hotel.model.Hospede;
import br.com.hotel.model.LateCheckoutDecorator;
import br.com.hotel.model.Quarto;
import br.com.hotel.model.Reserva;
import br.com.hotel.model.ReservaBuilder;
import br.com.hotel.model.ReservaCalculavel;
import br.com.hotel.model.TaxaDeServicoDecorator;
import br.com.hotel.model.TransferAeroportoDecorator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ReservaBuilder builder = new ReservaBuilder()
                .quarto(new Quarto(12, "Standard", new BigDecimal("250.00"), 2))
                .hospede(new Hospede("Ana Silva", "ana@example.com"))
                .checkIn(LocalDate.of(2026, 12, 1))
                .checkOut(LocalDate.of(2026, 12, 4))
                .quantidadeHospedes(2)
                .observacao("chegada depois das 22h");

        Reserva hospedagem = builder.build();
        DateTimeFormatter data = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Quarto: " + hospedagem.getQuarto().getNumero() + " ("
                + hospedagem.getQuarto().getCategoria() + ", capacidade "
                + hospedagem.getQuarto().getCapacidade() + ")");
        System.out.println("Hóspede: " + hospedagem.getHospede().getNome());
        System.out.println("Período: " + hospedagem.getCheckIn().format(data) + " a "
                + hospedagem.getCheckOut().format(data) + " ("
                + hospedagem.getQuantidadeDiarias() + " diárias)");
        System.out.println("Quantidade de hóspedes: " + hospedagem.getQuantidadeHospedes());
        System.out.println("Observação: " + hospedagem.getObservacao());
        System.out.println();

        System.out.println("Totais por etapa:");
        imprimirTotal("Hospedagem", builder.buildComServicos());
        builder.comServico(r -> new CafeDaManhaDecorator(r, 2));
        imprimirTotal("Café da manhã", builder.buildComServicos());
        builder.comServico(LateCheckoutDecorator::new);
        imprimirTotal("Late checkout", builder.buildComServicos());
        builder.comServico(r -> new TransferAeroportoDecorator(r, 2));
        imprimirTotal("Transfer", builder.buildComServicos());
        builder.comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")));
        ReservaCalculavel reserva = builder.buildComServicos();
        imprimirTotal("Taxa de 10%", reserva);

        System.out.println();
        System.out.println("Serviços: " + reserva.getServicos());
    }

    private static void imprimirTotal(String etapa, ReservaCalculavel reserva) {
        System.out.printf(Locale.forLanguageTag("pt-BR"), "%s: R$ %,.2f%n",
                etapa, reserva.getValorTotal());
    }
}
