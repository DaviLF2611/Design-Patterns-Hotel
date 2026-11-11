package br.com.hotel.model;

public final class Hospede {
    private final String nome;
    private final String email;

    public Hospede(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do hóspede é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail do hóspede é obrigatório.");
        }

        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
