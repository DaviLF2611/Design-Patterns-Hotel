# Design Patterns Hotel

Sistema de domínio para reservas de hotel, desenvolvido em Java. O projeto
representa hóspedes, quartos e reservas, calcula o preço da hospedagem e permite
compor o valor da reserva com serviços adicionais.

## Funcionalidades

- Criação e validação de hóspedes e quartos.
- Construção fluente de reservas com hóspede, quarto e datas.
- Validação de campos obrigatórios e do período da reserva.
- Cálculo de diárias e valor total da hospedagem.
- Inclusão combinável de serviços adicionais no preço, sem alterar a reserva
  original.

## Tecnologias e pré-requisitos

- Java 17 ou superior.
- Apache Maven 3.9 ou superior.

Verifique as instalações com:

```shell
java -version
mvn -version
```

## Compilar e executar os testes

Na raiz do repositório, compile o projeto e execute os testes:

```shell
mvn test
```

Para gerar o pacote JAR:

```shell
mvn package
```

## Exemplos de uso

### Criar uma reserva

```java
Quarto quarto = new Quarto(12, "Standard", new BigDecimal("250.00"));
Hospede hospede = new Hospede("Ana Silva", "ana@example.com");

Reserva reserva = new ReservaBuilder()
        .quarto(quarto)
        .hospede(hospede)
        .checkIn(LocalDate.of(2026, 12, 1))
        .checkOut(LocalDate.of(2026, 12, 4))
        .build();

System.out.println(reserva.getQuantidadeDiarias()); // 3
System.out.println(reserva.getValorTotal());        // 750.00
```

### Compor serviços adicionais

Cada `ServicoAdicionalDecorator` acrescenta um serviço ao componente recebido.
Decoradores podem ser encadeados:

```java
ReservaCalculavel reservaComServicos = new ServicoAdicionalDecorator(
        new ServicoAdicionalDecorator(
                reserva, "Café da manhã", new BigDecimal("40.00")),
        "Estacionamento", new BigDecimal("25.00"));

System.out.println(reservaComServicos.getValorTotal()); // 815.00
```

Imports para os exemplos:

```java
import br.com.hotel.model.Hospede;
import br.com.hotel.model.Quarto;
import br.com.hotel.model.Reserva;
import br.com.hotel.model.ReservaBuilder;
import br.com.hotel.model.ReservaCalculavel;
import br.com.hotel.model.ServicoAdicionalDecorator;
import java.math.BigDecimal;
import java.time.LocalDate;
```

## Padrões de projeto

### Builder

- **Classes envolvidas:** `ReservaBuilder` e `Reserva`, usando os modelos
  `Hospede` e `Quarto`.
- **Problema resolvido:** permite montar uma reserva com vários dados
  obrigatórios de forma legível e validar a reserva antes de criá-la, evitando
  construtores com muitos parâmetros.

### Decorator

- **Classes envolvidas:** `ReservaCalculavel` define o componente; `Reserva` é
  o componente concreto; `ServicoAdicionalDecorator` envolve esse componente e
  implementa a mesma interface.
- **Problema resolvido:** permite adicionar custos de serviços opcionais e
  combiná-los em cadeia sem modificar a classe `Reserva`. Cada serviço valida
  sua descrição e seu valor.

## Estrutura do projeto

```text
src/
├── main/java/br/com/hotel/model/
│   ├── Hospede.java
│   ├── Quarto.java
│   ├── Reserva.java
│   ├── ReservaBuilder.java
│   ├── ReservaCalculavel.java
│   └── ServicoAdicionalDecorator.java
└── test/java/br/com/hotel/model/
    └── ReservaBuilderTest.java
```

## Declaração de uso de IA

O Copilot foi utilizado para auxiliar na criação dos códigos do projeto e na
elaboração deste README.

## Contribuição dos integrantes

- **Davi Luiz Francisco:** domínio do sistema e implementação do padrão Builder.
