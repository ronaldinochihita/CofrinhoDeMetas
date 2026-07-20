-- Cofrinho de Metas — script SQL equivalente ao gerado pelo Room
-- Utilizado como referência no relatório (secção 8).

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS meta (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    nome           TEXT    NOT NULL,
    valorObjetivo  REAL    NOT NULL,
    dataLimite     INTEGER,
    corHex         TEXT    NOT NULL DEFAULT '#4CAF50',
    iconeChave     TEXT    NOT NULL DEFAULT 'savings',
    dataCriacao    INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS deposito (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    metaId  INTEGER NOT NULL,
    valor   REAL    NOT NULL,
    data    INTEGER NOT NULL,
    nota    TEXT    NOT NULL DEFAULT '',
    FOREIGN KEY (metaId) REFERENCES meta(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_deposito_metaId ON deposito(metaId);
