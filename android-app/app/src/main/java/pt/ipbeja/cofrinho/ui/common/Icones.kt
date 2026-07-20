package pt.ipbeja.cofrinho.ui.common

/**
 * Cada meta tem um "ícone" que é, na verdade, um emoji Unicode.
 * Vantagens vs. Material Icons:
 *  - Cor própria do sistema (Noto Color Emoji no Android) — não precisa de tint
 *  - Reconhecível de imediato
 *  - Escala sem perda (é um caractere)
 *
 * A ordem determina a apresentação no seletor horizontal do "Editar meta".
 * A chave é o identificador guardado na BD (retro-compatível).
 */
data class IconeOpcao(
    val chave: String,
    val etiqueta: String,
    val emoji: String,
    val categoria: String
)

val IconesDisponiveis: List<IconeOpcao> = listOf(
    // Poupança
    IconeOpcao("savings",    "Cofre",         "💰", "Poupança"),  // 💰
    IconeOpcao("bank",       "Banco",         "🏦", "Poupança"),  // 🏦
    IconeOpcao("card",       "Cartão",        "💳", "Poupança"),  // 💳
    IconeOpcao("shield",     "Emergência",    "🛡️", "Poupança"), // 🛡️
    IconeOpcao("gift",       "Presente",      "🎁", "Poupança"),  // 🎁

    // Transporte
    IconeOpcao("flight",     "Viagem",        "✈️",             "Transporte"), // ✈️
    IconeOpcao("car",        "Carro",         "🚗",             "Transporte"), // 🚗
    IconeOpcao("bike",       "Bicicleta",     "🚲",             "Transporte"), // 🚲
    IconeOpcao("train",      "Comboio",       "🚆",             "Transporte"), // 🚆

    // Casa & Vida
    IconeOpcao("home",       "Casa",          "🏠", "Casa"), // 🏠
    IconeOpcao("restaurant", "Comida",        "🍽️", "Casa"), // 🍽️
    IconeOpcao("hospital",   "Saúde",         "🏥", "Casa"), // 🏥

    // Tecnologia
    IconeOpcao("computer",   "Portátil",      "💻", "Tecnologia"), // 💻
    IconeOpcao("smartphone", "Telemóvel",     "📱", "Tecnologia"), // 📱
    IconeOpcao("headphones", "Auscultadores", "🎧", "Tecnologia"), // 🎧

    // Estudos & Lazer
    IconeOpcao("school",     "Estudos",       "🎓", "Estudos"),  // 🎓
    IconeOpcao("book",       "Livros",        "📚", "Estudos"),  // 📚
    IconeOpcao("game",       "Jogos",         "🎮", "Lazer")     // 🎮
)

/** Devolve o emoji da meta; fallback para 💰 (cofre) se a chave não existir. */
fun emojiPorChave(chave: String): String =
    IconesDisponiveis.firstOrNull { it.chave == chave }?.emoji ?: "💰"
