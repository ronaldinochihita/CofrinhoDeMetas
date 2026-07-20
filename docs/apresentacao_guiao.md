# Guião da apresentação (≈ 9 slides / 8-10 min)

Sugestão de estrutura para os slides finais. Fica pronta para levar para PowerPoint/Keynote/Slides.

## Slide 1 — Capa
- Título: **Cofrinho de Metas**
- Subtítulo: Aplicação Android para gestão de poupanças pessoais
- Autor: Ronaldino F. Chihita
- UC PAC — ESTIG Beja
- Data da apresentação

## Slide 2 — Contexto e problema
- Muitas pessoas poupam sem destino claro.
- Aplicações concorrentes: Monefy, Piggy, Revolut Pockets → não cobrem bem o caso "meta com prazo".
- Nasce a ideia: uma app **simples, offline, em PT-PT**.

## Slide 3 — Utilizador e cenários
- Persona: estudante/jovem trabalhador, Android.
- Cenário chave: *"Quero poupar 400 € em 3 meses para a viagem a Lisboa."*

## Slide 4 — Funcionalidades (F1–F9)
- Listar / Criar / Editar / Eliminar metas
- Pesquisar
- Depósitos (adicionar + eliminar)
- Tema claro/escuro

## Slide 5 — Ecrãs (3)
- Mockups lado-a-lado: **Lista**, **Detalhe**, **Editar/Criar**.
- Cada ecrã tem uma responsabilidade clara — sem ecrãs "de conforto".

## Slide 6 — Modelo de dados
- Diagrama ER: Meta (1) ─── (N) Deposito.
- Chave estrangeira com `ON DELETE CASCADE`.

## Slide 7 — Arquitetura
- Diagrama: UI (Compose) ↔ ViewModel ↔ Repository ↔ DAO ↔ Room/SQLite.
- Palavras-chave: **MVVM · Coroutines · StateFlow**.

## Slide 8 — Base de dados (queries)
- Mostrar Q1 (JOIN + SUM + COUNT) como exemplo.
- Referir CRUD completo implementado.

## Slide 9 — Testes + Página web + Conclusão
- Tabela com 5 tarefas, 5 utilizadores, tempos médios e erros.
- Screenshot do sítio web + QR/link para o screencast.
- Reflexão final: o que aprendi, o que faria diferente.

---

## Perguntas prováveis do júri (com resposta modelo)

**Q: Porquê Room e não SQLite diretamente?**
R: Room é a biblioteca oficial da Google que assenta sobre SQLite. Elimina boilerplate, valida queries em tempo de compilação (`@Query`), integra-se com Coroutines/Flow para reatividade e reduz risco de erros manuais.

**Q: O que é MVVM?**
R: Padrão que separa Model (dados), View (UI) e ViewModel (lógica de apresentação). A View observa estado; o ViewModel prepara e expõe esse estado; nunca há chamadas diretas da UI ao repositório.

**Q: O que é StateFlow?**
R: Um fluxo observável com valor atual sempre disponível. A UI Compose subscreve via `collectAsStateWithLifecycle()` e recompõe-se automaticamente quando o valor muda.

**Q: O que acontece quando eliminas uma meta com depósitos?**
R: A chave estrangeira tem `ON DELETE CASCADE`, portanto o SQLite elimina automaticamente todos os depósitos associados. Não fica lixo na tabela `deposito`.

**Q: Como é que a app funciona sem internet?**
R: Todos os dados estão numa base local SQLite gerida pelo Room, dentro do sandbox da própria app. Não há servidor, não há sync.

**Q: Onde está o CRUD?**
R: `MetaDao` e `DepositoDao` — Create (`inserir`), Read (`observarComTotais`, `observarPorMeta`, `pesquisar`), Update (`atualizar`), Delete (`eliminar`). Na UI: criar/editar pelo ecrã Editar (botão principal), eliminar meta pelo ícone lixo na barra desse ecrã (com confirmação), eliminar depósito pelo ícone lixo em cada linha do Detalhe.

**Q: Como fazes pesquisa?**
R: Cada carácter digitado atualiza um `MutableStateFlow` no ViewModel. `flatMapLatest` cancela a query anterior e emite uma nova com `LIKE '%termo%'`, e a UI recebe a nova lista sem código extra.

**Q: Porquê apenas 3 ecrãs?**
R: Simplicidade e foco. Cada ecrã tem uma responsabilidade única — listar, detalhar, editar. Adicionar mais ecrãs para funcionalidades acessórias (ex.: estatísticas separadas) aumentaria a carga cognitiva sem trazer valor essencial. Se necessário, um KPI global pode ser incorporado no topo da Lista sem novo ecrã.
