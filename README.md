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
  spa, Wi-Fi) e de descontos no preço, sem alterar a reserva original.
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

O projeto exige Java 17 e Maven 3.6.3 ou superior. Nesta máquina, o terminal
usa Java 8 e Maven 3.5.4 por padrão. Selecione as versões instaladas antes de
executar os comandos:

```shell
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH="/opt/mvnd/mvn/bin:$JAVA_HOME/bin:$PATH"
java -version
mvn -version
```

```shell
mvn test
```

Para gerar o pacote JAR:

```shell
mvn package
```

### Executar a demonstração

Na raiz do repositório, compile e execute a reserva de exemplo no console.
O `mvn package` também executa os testes:

```shell
mvn package
java -cp target/classes br.com.hotel.Main
```

Trecho da saída esperada:

```text
Hospedagem: R$ 750,00
Café da manhã: R$ 990,00
Late checkout: R$ 1.070,00
Transfer: R$ 1.190,00
Taxa de 10%: R$ 1.309,00
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

A reserva também aceita a quantidade de hóspedes (de 1 até a capacidade do
quarto) e uma observação de até 200 caracteres:

```java
Reserva reserva = new ReservaBuilder()
        .quarto(new Quarto(12, "Standard", new BigDecimal("250.00"), 2))
        .hospede(hospede)
        .checkIn(LocalDate.of(2026, 12, 1))
        .checkOut(LocalDate.of(2026, 12, 4))
        .quantidadeHospedes(2)
        .observacao("chegada depois das 22h")
        .build();
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
| `WifiDecorator` | 15,00 por diária | 45,00 |
| `DescontoPercentualDecorator` | abate um percentual do total acumulado | depende da cadeia |
| `PetDecorator` | 25,00 por pet e por diária | 150,00 (2 pets) |
| `LateCheckoutDecorator` | 80,00 por estadia | 80,00 |
| `TransferAeroportoDecorator` | 60,00 por trecho (1 ou 2 trechos) | 120,00 (ida e volta) |
| `TaxaDeServicoDecorator` | soma um percentual (0 a 20%) sobre o total acumulado | depende da cadeia |

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
import br.com.hotel.model.WifiDecorator;
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
  - `CafeDaManhaDecorator`, `EstacionamentoDecorator`, `SpaDecorator`, `WifiDecorator`,
    `PetDecorator` e `DescontoPercentualDecorator`: decoradores concretos, cada um com sua
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
│   ├── LateCheckoutDecorator.java
│   ├── Quarto.java
│   ├── Reserva.java
│   ├── ReservaBuilder.java
│   ├── ReservaCalculavel.java
│   ├── ReservaDecorator.java
│   ├── SpaDecorator.java
│   ├── TaxaDeServicoDecorator.java
│   ├── TransferAeroportoDecorator.java
│   ├── WifiDecorator.java
│   └── PetDecorator.java
└── test/java/br/com/hotel/model/
    ├── ExtensaoParte2Test.java
    ├── QuartoTest.java
    ├── ReservaBuilderPassosTest.java
    ├── ReservaBuilderServicosTest.java
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
- **Ísis Enya D'Agostin Flor:** implementação do WifiDecorator.

## Extensão (Parte 2) — equipe Cantina

Esta seção foi acrescentada pela equipe do projeto Cantina, como extensão do
trabalho de Design Patterns. Nenhuma assinatura pública que já existia foi
alterada, e os testes originais continuam como estavam. As seções anteriores
deste README são da equipe dona do projeto e seguem intactas.

### O que foi adicionado

**Builder: novos passos de montagem**

- `Quarto` ganhou a capacidade (`getCapacidade()`). O construtor de três
  parâmetros continua existindo e assume capacidade 2.
- `ReservaBuilder.quantidadeHospedes(int)`: padrão 1, precisa ficar entre 1 e a
  capacidade do quarto.
- `ReservaBuilder.observacao(String)`: padrão vazio, aceita até 200 caracteres
  e remove espaços nas pontas.
- `Reserva` expõe `getQuantidadeHospedes()` e `getObservacao()`.
- Problema que resolve: a reserva passa a ter mais dados opcionais sem
  precisar de construtores longos, e a validação continua toda no `build()`.

**Decorator: novos serviços**

- `LateCheckoutDecorator`: 80,00 por estadia.
- `TransferAeroportoDecorator`: 60,00 por trecho, aceita 1 (ida) ou 2 (ida e
  volta) trechos.
- `TaxaDeServicoDecorator`: soma um percentual de 0 a 20% sobre o total
  acumulado, o oposto do `DescontoPercentualDecorator`.

**Builder e Decorator juntos**

- `ReservaBuilder.comServico(UnaryOperator<ReservaCalculavel>)` guarda os
  serviços na ordem em que foram pedidos.
- `ReservaBuilder.buildComServicos()` valida a reserva pelo `build()` e depois
  envolve o resultado com cada serviço, na mesma ordem.
- O `build()` continua devolvendo uma `Reserva` simples, sem serviços.
- Problema que resolve: monta a reserva e a cadeia de serviços em um único
  fluxo, sem o chamador encadear os decoradores à mão.

**Demonstração no console**

- `br.com.hotel.Main` monta o cenário completo da extensão, lista os serviços
  contratados e mostra o total acumulado após cada serviço.

### Exemplo de uso

Quarto 12 Standard (250,00, capacidade 2), Ana com 2 hóspedes, de 01/12 a
04/12 (3 diárias):

```java
ReservaCalculavel reserva = new ReservaBuilder()
        .quarto(new Quarto(12, "Standard", new BigDecimal("250.00"), 2))
        .hospede(new Hospede("Ana Silva", "ana@example.com"))
        .checkIn(LocalDate.of(2026, 12, 1))
        .checkOut(LocalDate.of(2026, 12, 4))
        .quantidadeHospedes(2)
        .observacao("chegada depois das 22h")
        .comServico(r -> new CafeDaManhaDecorator(r, 2))
        .comServico(LateCheckoutDecorator::new)
        .comServico(r -> new TransferAeroportoDecorator(r, 2))
        .comServico(r -> new TaxaDeServicoDecorator(r, new BigDecimal("10")))
        .buildComServicos();

System.out.println(reserva.getValorTotal()); // 1309.00
System.out.println(reserva.getServicos());
// [Café da manhã para 2 pessoa(s), Late checkout,
//  Transfer aeroporto (2 trecho(s)), Taxa de serviço de 10%]
```

| Passo | Valor |
|---|---|
| Hospedagem (3 × 250,00) | 750,00 |
| + Café da manhã para 2 (40,00 × 3 × 2) | 990,00 |
| + Late checkout | 1.070,00 |
| + Transfer aeroporto, 2 trechos (60,00 × 2) | 1.190,00 |
| + Taxa de serviço de 10% | 1.309,00 |

Como a taxa incide sobre o total acumulado, a ordem dos `comServico` muda o
resultado. Com a taxa logo depois do café da manhã, o mesmo cenário fecha em
1.289,00.

### Como testar

```shell
mvn test
mvn package
java -cp target/classes br.com.hotel.Main
```

Testes acrescentados na extensão:

- `QuartoTest`: capacidade padrão e capacidade informada.
- `ReservaBuilderPassosTest`: hóspedes, observação e seus limites.
- `ReservaBuilderServicosTest`: `comServico`, `buildComServicos` e
  `TaxaDeServicoDecorator`.
- `ExtensaoParte2Test`: o cenário acima de ponta a ponta, a ordem dos serviços
  e a validação do builder na reserva com serviços.

### Contribuição da equipe extensora

- **Alexandre Tibes ([@Xandetds](https://github.com/Xandetds)):** novos passos
  do Builder e capacidade do `Quarto`, teste de ponta a ponta, README e PR.
- **Frank Cardoso ([@frank-cardoso](https://github.com/frank-cardoso)):**
  `LateCheckoutDecorator`, `TransferAeroportoDecorator` e a demonstração
  executável em `br.com.hotel.Main`.
- **Gustavo Nunes Teixeira ([@ogustavonunes](https://github.com/ogustavonunes)):**
  `comServico`, `buildComServicos` e `TaxaDeServicoDecorator`.
