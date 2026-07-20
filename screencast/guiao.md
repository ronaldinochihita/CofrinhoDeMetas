# Screencast — Guião (~3 minutos)

**Ferramenta sugerida:** OBS Studio (gratuito). Gravar ecrã do telemóvel via scrcpy espelhado, ou emulador Android.
**Resolução:** 1080x1920 vertical ou 1920x1080 landscape.
**Voz:** narração em português de Portugal, calma, sem música de fundo forte.

---

## 00:00 — 00:15 · Abertura
- Slide/título com nome da app: **"Cofrinho de Metas"** e nome do autor.
- Voz: *"Olá. Sou o Ronaldino Chihita, do curso de Tecnologias Web e Dispositivos Móveis. Esta é a aplicação Cofrinho de Metas, um gestor de poupanças pessoais desenvolvido em Kotlin com Jetpack Compose."*

## 00:15 — 00:45 · Ecrã principal (Lista)
- Abrir a app. Mostrar as 3 metas seed: Viagem a Lisboa, Portátil, Fundo emergência.
- Voz: *"Ao abrir a aplicação, vemos a lista de metas com barra de progresso, valor poupado e percentagem. Podemos pesquisar pelo nome."*
- Escrever "port" na barra de pesquisa → filtra em tempo real.
- Limpar a pesquisa.

## 00:45 — 01:20 · Criar meta
- Tocar em **Nova meta** (FAB).
- Preencher: nome "Bicicleta", valor "250", escolher data, cor laranja, ícone carro.
- Guardar.
- Voz: *"Para criar uma meta, indicamos nome, valor objetivo, prazo, cor e ícone. Os únicos campos obrigatórios são o nome e o valor."*

## 01:20 — 01:55 · Detalhe + adicionar depósito
- Abrir a meta "Bicicleta".
- Mostrar cartão de resumo, prazo, "faltam X dias".
- Tocar em **Adicionar depósito** → valor 30 €, nota "primeira poupança".
- Ver barra a atualizar em tempo real.
- Voz: *"Cada depósito atualiza automaticamente o progresso da meta."*

## 01:55 — 02:25 · Editar meta
- Tocar no ícone de editar (topo).
- Alterar valor para 300 €. Guardar.
- Voltar à lista, confirmar novo valor.
- Voz: *"Podemos editar qualquer campo da meta. A alteração fica visível imediatamente."*

## 02:25 — 02:50 · Eliminar
- Detalhe → botão editar → ícone lixo no topo → confirmar.
- Voz: *"Ao eliminar uma meta, todos os depósitos associados são também removidos, através da regra de cascade da base de dados."*
- Também demonstrar eliminar depósito individual: abrir Detalhe → ícone lixo numa linha de depósito.

## 02:50 — 03:00 · Arquitetura + fecho
- Slide final com: **Kotlin · Jetpack Compose · Room · MVVM · Navigation**.
- Voz: *"A aplicação segue o padrão MVVM, com base de dados local via Room e interface reativa em Compose. Todo o código está no GitHub. Obrigado."*
