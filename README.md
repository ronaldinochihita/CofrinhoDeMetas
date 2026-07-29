# Cofrinho de Metas

🌐 **Site em produção:** https://cofrinho-met.netlify.app/
🎬 **Screencast:** [screencast/screencast.webm](screencast/screencast.webm) *(também disponível na secção "Screencast" do site)*

App Android desenvolvida no âmbito da UC **Programação de Aplicação do Lado do Cliente (PAC)** — Curso de Tecnologias Web e Dispositivos Móveis, Escola Superior de Tecnologia e Gestão de Beja (IPBeja).

Aplicação para gestão de poupanças pessoais por meta: o utilizador define objetivos (viagem, portátil, fundo de emergência…), regista depósitos ao longo do tempo e vê o progresso de cada meta.

## Estrutura do repositório

```
CofrinhoDeMetas/
├── android-app/       Projeto Android Studio (Kotlin + Compose + Room)
├── website/           Sítio web de divulgação (HTML/CSS estático)
├── database/          schema.sql + queries.sql documentadas
├── design/            Screenshots dos ecrãs + diagramas ER e navegação
├── docs/              Relatório PAC (docx) + Apresentação PAC (pptx)
├── screencast/        Vídeo demo da aplicação (~3 min)
└── README.md          Este ficheiro
```

## Como abrir

**Android app:** abrir a pasta `android-app/` no Android Studio (Ladybug 2024.2 ou superior). Sync do Gradle → *Run* no emulador ou telemóvel físico (mínimo Android 8 / API 26).

**Website:** aceder online em **https://cofrinho-met.netlify.app/** ou abrir `website/index.html` localmente num browser.

## Stack

- **Linguagem:** Kotlin 2.0
- **UI:** Jetpack Compose + Material 3
- **Base de dados:** Room 2.6 (SQLite local)
- **Arquitetura:** MVVM (Model – View – ViewModel) com StateFlow
- **Navegação:** Navigation Compose 2.8

## Autor

Ronaldino Fernandes Chihita — n.º 27757 — IPBeja
