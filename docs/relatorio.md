# Relatório — Cofrinho de Metas
**UC Programação de Aplicação do Lado do Cliente (PAC)**
Curso de Tecnologias Web e Dispositivos Móveis — ESTIG Beja
Autor: Ronaldino Fernandes Chihita
Docentes: Luís Garcia, Luís Rosário, Mihai Lupan

---

## 1. Introdução

O presente relatório documenta o desenvolvimento do projeto **Cofrinho de Metas**, uma aplicação móvel Android que auxilia o utilizador na gestão das suas poupanças pessoais organizadas por objetivos ("metas"). O trabalho foi desenvolvido individualmente no âmbito da UC PAC.

O documento segue a estrutura de 12 secções indicada no enunciado.

## 2. Revisão da aplicação desktop

*(Descrever brevemente o projeto desktop do 1º semestre. Refletir sobre pontos positivos, dificuldades, e justificar a evolução para o tema atual — poupanças pessoais — como forma de aplicar de raiz os conhecimentos adquiridos de Room, Compose e MVVM, num âmbito bem delimitado para uma solução móvel.)*

## 3. Organização do trabalho e ferramentas

- **Repositório GitHub:** *(URL a preencher após publicação)*
- **IDE:** Android Studio Ladybug (2024.2)
- **Controlo de versões:** Git + GitHub
- **Prototipagem:** *(Balsamiq / Figma / lápis e papel)*
- **Gestão de tarefas:** *(Trello / Notion / equivalente)*
- **Comunicação com o docente:** aulas presenciais + Moodle

Estrutura mono-repo (ver `README.md` na raiz).

## 4. Análise do problema

### 4.1 Problema
Muitas pessoas guardam dinheiro sem um destino claro, o que reduz a motivação para poupar. Uma app simples que permita **atribuir cada euro a uma meta concreta** e visualizar o progresso pode aumentar a disciplina de poupança.

### 4.2 Sistemas semelhantes
- **Monefy** — foco em despesas, não em metas. Positivo: UI muito rápida. Negativo: não gere objetivos.
- **Piggy Savings Tracker** — dedicado a metas. Positivo: visualização de progresso. Negativo: publicidade agressiva, sem PT-PT.
- **Revolut Pockets/Vaults** — bom, mas depende de conta bancária.

### 4.3 Utilizadores (persona)
- **Estudante universitário / jovem trabalhador**, 18–30 anos, telemóvel Android.
- Objetivo: poupar para itens concretos (viagem, portátil, fundo emergência).
- Fricção atual: notas soltas, folhas de Excel.

### 4.4 Cenários de utilização
1. *"Criei uma meta para uma viagem a Lisboa (400 €) daqui a 3 meses. Todas as semanas registo o que consigo poupar."*
2. *"Quero ver ao fim do mês quanto já poupei no total, entre todas as metas."*
3. *"A minha meta 'Portátil' foi concluída — quero marcá-la e concentrar-me nas outras."*

## 5. Funcionalidades da aplicação

| # | Funcionalidade | Descrição |
|---|---|---|
| F1 | Listar metas | Ver todas as metas com progresso visual |
| F2 | Criar meta | Nome, valor objetivo, prazo, cor, ícone |
| F3 | Editar meta | Modificar qualquer campo |
| F4 | Eliminar meta | Remove meta e depósitos associados (cascade) |
| F5 | Pesquisar meta | Filtro por nome |
| F6 | Ver detalhe | Progresso + lista de depósitos |
| F7 | Adicionar depósito | Valor + nota opcional |
| F8 | Eliminar depósito | Remove um depósito individual |
| F9 | Tema claro/escuro | Segue definição do sistema |

## 6. Desenho da interface da aplicação

Três ecrãs principais (detalhados no PDF de layout entregue à parte):

1. **Lista de Metas** — cards com barra de progresso, pesquisa, FAB "Nova meta".
2. **Detalhe da Meta** — resumo com barra, prazo, lista de depósitos, FAB "Adicionar depósito".
3. **Criar / Editar Meta** — formulário (nome, valor, data, cor, ícone); em modo edição inclui botão para eliminar.

Ver também: `design/` (protótipos) e o **PDF de layout**.

### 6.1 Sítio web
Página única, estática, com secções: Hero + mockup, Funcionalidades (grid de 6), Screencast, Tecnologias, Autor. Ficheiros: `website/index.html` e `website/style.css`.

## 7. Desenho da base de dados

### Modelo conceptual

Duas entidades:

- **Meta** (id PK, nome, valorObjetivo, dataLimite, corHex, iconeChave, dataCriacao)
- **Deposito** (id PK, metaId FK → Meta, valor, data, nota)

**Relação:** uma Meta tem 0..N Depósitos (1:N). Eliminação de Meta em cascata elimina Depósitos.

### Diagrama (ER textual)

```
Meta (1) ────< Deposito (N)
  id                id
  nome              metaId (FK)
  valorObjetivo     valor
  dataLimite        data
  corHex            nota
  iconeChave
  dataCriacao
```

## 8. Conceção da base de dados

Implementada com **Room** (biblioteca oficial Android sobre SQLite).

### 8.1 Comandos de criação (SQL equivalente gerado pelo Room)

```sql
CREATE TABLE meta (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    nome           TEXT    NOT NULL,
    valorObjetivo  REAL    NOT NULL,
    dataLimite     INTEGER,
    corHex         TEXT    NOT NULL DEFAULT '#4CAF50',
    iconeChave     TEXT    NOT NULL DEFAULT 'savings',
    dataCriacao    INTEGER NOT NULL
);

CREATE TABLE deposito (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    metaId  INTEGER NOT NULL,
    valor   REAL    NOT NULL,
    data    INTEGER NOT NULL,
    nota    TEXT    NOT NULL DEFAULT '',
    FOREIGN KEY (metaId) REFERENCES meta(id) ON DELETE CASCADE
);

CREATE INDEX idx_deposito_metaId ON deposito(metaId);
```

### 8.2 Principais queries (extraídas de `MetaDao.kt` e `DepositoDao.kt`)

**Q1 — Listar metas com total poupado e nº depósitos**
```sql
SELECT m.*,
       IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
       COUNT(d.id)               AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
GROUP BY m.id
ORDER BY m.dataCriacao DESC;
```
*Usa `LEFT JOIN` para incluir metas sem depósitos e `IFNULL` para tratar `NULL` do agregado.*

**Q2 — Obter uma meta com o seu total**
```sql
SELECT m.*, IFNULL(SUM(d.valor), 0.0) AS totalDepositado, COUNT(d.id) AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
WHERE m.id = :id
GROUP BY m.id;
```

**Q3 — Pesquisa por nome (LIKE parcial)**
```sql
SELECT m.*, IFNULL(SUM(d.valor), 0.0) AS totalDepositado, COUNT(d.id) AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
WHERE m.nome LIKE '%' || :termo || '%'
GROUP BY m.id
ORDER BY m.dataCriacao DESC;
```

**Q4 — Total poupado em todas as metas**
```sql
SELECT IFNULL(SUM(valor), 0.0) FROM deposito;
```

**Q5 — Depósitos de uma meta (mais recentes primeiro)**
```sql
SELECT * FROM deposito WHERE metaId = :metaId ORDER BY data DESC;
```

## 9. Programação da lógica da aplicação

Arquitetura **MVVM** com camadas bem separadas:

- **Model (Data):** `entity/Meta`, `entity/Deposito`, `dao/*Dao`, `AppDatabase`, `MetaRepository`.
- **ViewModel:** `MetasListViewModel`, `DetalheMetaViewModel`, `EditarMetaViewModel`, `EstatisticasViewModel`. Expõem `StateFlow` para a UI observar.
- **View:** Composables (`*Screen.kt`) em Jetpack Compose. Puros — só desenham estado e emitem eventos para o VM.

### Fluxo de dados (unidireccional)

```
UI (Compose)  ← StateFlow ←  ViewModel  ↔  Repository  ↔  DAO (Room)
       │                         ▲
       └──── eventos (callbacks) ┘
```

### Operações CRUD implementadas
- **Create:** `MetaDao.inserir()`, `DepositoDao.inserir()`
- **Read:** `observarComTotais()`, `observarPorMeta()`, `pesquisar()`
- **Update:** `MetaDao.atualizar()`
- **Delete:** `MetaDao.eliminar()`, `DepositoDao.eliminar()` (+ CASCADE)

### Uso de Coroutines/Flow
- Queries de leitura devolvem `Flow<T>` — a UI atualiza-se automaticamente quando os dados mudam.
- Operações de escrita são `suspend` — executam fora da thread principal.

## 10. Testes com a aplicação

Testes com **5 utilizadores-colegas**, cada um a executar as 5 tarefas abaixo. Métricas: tempo de conclusão, nº de erros, sucesso (S/N).

| # | Tarefa | Tempo médio | Erros | Sucesso |
|---|---|---|---|---|
| T1 | Criar nova meta "Bicicleta" com 300 € | *(preencher)* | *(preencher)* | *(preencher)* |
| T2 | Adicionar depósito de 50 € a essa meta | | | |
| T3 | Pesquisar meta pelo nome | | | |
| T4 | Ver estatísticas globais | | | |
| T5 | Eliminar uma meta | | | |

Estado de cada funcionalidade: F1–F10 → **Funcional**.

## 11. Página web da aplicação e screencast

- **Página web** em `website/` — página estática single-page, responsiva (mobile-first), com secções Hero, Funcionalidades, Screencast, Tecnologias e Autor. Cores alinhadas com a app (verde + dourado).
- **Screencast** (~3 min) em `screencast/screencast.mp4`. Guião em `screencast/guiao.md`.

## 12. Conclusão

O projeto cumpre todos os requisitos do enunciado: aplicação Android com CRUD completo, base de dados Room com queries documentadas, arquitetura MVVM, sítio web de divulgação, screencast, apresentação e relatório.

Como reflexão pessoal: *(preencher — o que aprendi, dificuldades, o que faria diferente).*

---
