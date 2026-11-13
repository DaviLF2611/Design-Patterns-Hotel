# Design Patterns Hotel

Sistema de domínio para reservas de hotel, desenvolvido em Java. O projeto
representa hóspedes, quartos e reservas, calcula o preço da hospedagem e permite
compor o valor da reserva com serviços adicionais.

## Funcionalidades

- Criação e validação de hóspedes e quartos.
- Construção fluente de reservas com hóspede, quarto e datas.
- Validação de campos obrigatórios e do período da reserva.
- Cálculo de diárias e valor total da hospedagem.
- Inclusão combinável de serviços adicionais (café da manhã, estacionamento,
  spa) e de descontos no preço, sem alterar a reserva original.
- Listagem dos serviços contratados em cada reserva.

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

### Compor serviços e descontos

Cada decorador envolve um `ReservaCalculavel` (uma `Reserva` ou outro
decorador) e aplica a sua própria regra de cálculo. Os decoradores podem ser
encadeados:

```java
ReservaCalculavel comServicos = new EstacionamentoDecorator(
        new CafeDaManhaDecorator(reserva, 2));

System.out.println(comServicos.getValorTotal()); // 1065.00
System.out.println(comServicos.getServicos());
// [Café da manhã para 2 pessoa(s), Estacionamento]
```

Regras de cada decorador (considerando uma reserva de 3 diárias a 250,00):

| Decorador | Regra | Valor adicionado |
|---|---|---|
| `CafeDaManhaDecorator` | 40,00 por pessoa e por diária | 240,00 (2 pessoas) |
| `EstacionamentoDecorator` | 25,00 por diária | 75,00 |
| `SpaDecorator` | 100,00 por estadia | 100,00 |
| `DescontoPercentualDecorator` | abate um percentual do total acumulado | depende da cadeia |
| `PetDecorator` | 25,00 por pet e por diária | 150,00 (2 pets) |

O desconto incide sobre o total acumulado até aquele ponto, portanto a ordem
da cadeia altera o resultado:

```java
ReservaCalculavel descontoDepoisDoSpa = new DescontoPercentualDecorator(
        new SpaDecorator(reserva), new BigDecimal("10"));
ReservaCalculavel descontoAntesDoSpa = new SpaDecorator(
        new DescontoPercentualDecorator(reserva, new BigDecimal("10")));

System.out.println(descontoDepoisDoSpa.getValorTotal()); // 765.00
System.out.println(descontoAntesDoSpa.getValorTotal());  // 775.00
```

Imports para os exemplos:

```java
import br.com.hotel.model.CafeDaManhaDecorator;
import br.com.hotel.model.DescontoPercentualDecorator;
import br.com.hotel.model.EstacionamentoDecorator;
import br.com.hotel.model.Hospede;
import br.com.hotel.model.Quarto;
import br.com.hotel.model.Reserva;
import br.com.hotel.model.ReservaBuilder;
import br.com.hotel.model.ReservaCalculavel;
import br.com.hotel.model.SpaDecorator;
import br.com.hotel.model.PetDecorator;
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

- **Classes envolvidas:**
  - `ReservaCalculavel`: componente. Contrato comum com `getValorTotal()`,
    `getQuantidadeDiarias()` e `getServicos()`.
  - `Reserva`: componente concreto, com o cálculo base da hospedagem.
  - `ReservaDecorator`: decorador abstrato (Template Method). Guarda o
    componente envolvido, soma `calcularAcrescimo()` ao total dele e acrescenta
    `descricao()` à lista de serviços.
  - `CafeDaManhaDecorator`, `EstacionamentoDecorator`, `SpaDecorator`,`PetDecorator` e
    `DescontoPercentualDecorator`: decoradores concretos, cada um com sua
    própria regra de cálculo.
- **Problema resolvido:** permite adicionar serviços e descontos, combiná-los
  em cadeia e listar o que foi contratado sem modificar a classe `Reserva` nem
  criar uma subclasse para cada combinação.

## Estrutura do projeto

```text
src/
├── main/java/br/com/hotel/model/
│   ├── CafeDaManhaDecorator.java
│   ├── DescontoPercentualDecorator.java
│   ├── EstacionamentoDecorator.java
│   ├── Hospede.java
│   ├── Quarto.java
│   ├── Reserva.java
│   ├── ReservaBuilder.java
│   ├── ReservaCalculavel.java
│   ├── ReservaDecorator.java
│   ├── SpaDecorator.java
│   └── PetDecorator.java
└── test/java/br/com/hotel/model/
    ├── ReservaBuilderTest.java
    └── ReservaDecoratorTest.java
```

## Declaração de uso de IA

O Copilot foi utilizado para auxiliar na criação dos códigos do projeto e na
elaboração deste README.

## Contribuição dos integrantes

- **Davi Luiz Francisco:** domínio do sistema e implementação do padrão Builder.
- **Ismael Damasceno:** implementação do padrão Decorator.
- **Enzo Coelho Morona:** implementação do PetDecorator.
