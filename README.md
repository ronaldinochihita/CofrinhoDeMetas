# Cofrinho de Metas

App Android desenvolvida no âmbito da UC **Programação de Aplicação do Lado do Cliente (PAC)** — Curso de Tecnologias Web e Dispositivos Móveis, Escola Superior de Tecnologia e Gestão de Beja (IPBeja).

Aplicação para gestão de poupanças pessoais por meta: o utilizador define objetivos (viagem, portátil, fundo de emergência…), regista depósitos ao longo do tempo e vê o progresso de cada meta.

## Estrutura do repositório

```
CofrinhoDeMetas/
├── android-app/       Projeto Android Studio (Kotlin + Compose + Room)
├── website/           Sítio web de divulgação (HTML/CSS estático)
├── database/          Script SQL de criação + queries documentadas
├── design/            Protótipos e mockups
├── docs/              Relatório (12 secções), plano de testes
├── screencast/        Guião e ficheiro de vídeo (~3 min)
├── ../si/             (fora deste repo) Documentação Sistemas Interativos (TG2)
└── README.md          Este ficheiro
```

## Como abrir

**Android app:** abrir a pasta `android-app/` no Android Studio (Ladybug 2024.2 ou superior). Sync do Gradle → *Run* no emulador ou telemóvel físico (mínimo Android 8 / API 26).

**Website:** abrir `website/index.html` num browser.

## Stack

- **Linguagem:** Kotlin 2.0
- **UI:** Jetpack Compose + Material 3
- **Base de dados:** Room 2.6 (SQLite local)
- **Arquitetura:** MVVM (Model – View – ViewModel) com StateFlow
- **Navegação:** Navigation Compose 2.8

## Autor

Ronaldino Fernandes Chihita — IPBeja
