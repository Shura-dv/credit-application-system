# Projeto de Desafio "Documentando e Testando sua API Rest com Kotlin" pela Plataforma DIO (Digital Innovation One).

## Repositório do Projeto original: https://github.com/cami-la/credit-application-system

## Em aula foram criados pela Software Developer, Camila Cavalcante, os testes `CustomerServiceTest`, `CreditRepositoryTest` e `CustomerResourceTest` para suas respectivas classes. Então, o objetivo do Desafio era criar testes para as classes: `CreditResourceTest` e `CreditServiceTest`.

# Classe `CreditResourceTest`

## Teste: `should create a credit and return 201 status`

### Objetivo: Verificar se o endpoint de criação de um crédito está funcionando corretamente e retorna um status HTTP 201 (Created) quando um crédito é criado com sucesso.

* Cria um objeto CreditDto com valores de crédito específicos.
* Realiza uma solicitação POST para o endpoint de criação de crédito.
* Verifica se a resposta possui um status 201 e contém os valores esperados do crédito criado.

## Teste: `should return list of credits by customerId`

### Objetivo: Verificar se o endpoint que retorna uma lista de créditos para um cliente específico está funcionando corretamente.

* Especifica o ID de cliente.
* Realiza uma solicitação GET para o endpoint de listagem de créditos por cliente.
* Verifica se a resposta possui um status 200 e se a lista de créditos contém os valores esperados.

## Teste: `should return a specific credit by creditCode`

### Objetivo: Verificar se o endpoint que retorna um crédito específico com base em seu código de crédito e ID de cliente está funcionando corretamente.

* Especifica o ID de cliente e o código de crédito.
* Realiza uma solicitação GET para o endpoint de consulta de crédito por código de crédito.
* Verifica se a resposta possui um status 200 e se os detalhes do crédito correspondem aos valores esperados.

# Classe `CreditServiceTest`

## Teste `should create credit`

### Objetivo: Testar se o método save da `CreditService` cria um crédito corretamente.

* Este teste cria um cliente falso e um crédito falso associado a esse cliente. Ele simula o comportamento dos métodos `findById` e `save` do repositório de crédito e verifica se o crédito retornado é igual ao crédito falso e se os métodos do repositório foram chamados corretamente.

## Teste `should find credit by credit code and customer id`

### Objetivo: Testar se o método `findByCreditCode` da `CreditService` encontra um crédito com base no código de crédito e no ID do cliente.

* Este teste cria um código de crédito falso e um crédito falso associado a um cliente. Ele simula o comportamento do método `findByCreditCode` do repositório de crédito e verifica se o crédito retornado é igual ao crédito falso e se o método do repositório foi chamado corretamente.

## Teste `should not find credit by invalid credit code`

### Objetivo: Testar se o método `findByCreditCode` da `CreditService` lança uma exceção BusinessException quando o código de crédito não é encontrado.

* Este teste simula o comportamento do método `findByCreditCode` do repositório de crédito para retornar null, indicando que o código de crédito não foi encontrado. Em seguida, verifica se uma exceção BusinessException é lançada com a mensagem correta.

## Teste `should find all credits by customer`

### Objetivo: Testar se o método `findAllByCustomer` da `CreditService` retorna todos os créditos associados a um cliente.

* Este teste cria um cliente falso e uma lista de créditos falsos associados a esse cliente. Ele simula o comportamento do método `findAllByCustomerId` do repositório de crédito e verifica se a lista de créditos retornada pelo serviço é igual à lista de créditos falsos e se o método do repositório foi chamado corretamente.
