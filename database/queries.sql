-- Cofrinho de Metas — queries principais utilizadas pela aplicação.
-- Cada uma tem correspondência direta num @Query no Room (ver MetaDao.kt e DepositoDao.kt).

------------------------------------------------------------
-- Q1: Listar metas com total poupado e n.º de depósitos
------------------------------------------------------------
SELECT m.*,
       IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
       COUNT(d.id)               AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
GROUP BY m.id
ORDER BY m.dataCriacao DESC;

------------------------------------------------------------
-- Q2: Obter uma meta específica com o seu total
------------------------------------------------------------
SELECT m.*,
       IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
       COUNT(d.id)               AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
WHERE m.id = ?
GROUP BY m.id;

------------------------------------------------------------
-- Q3: Pesquisa por nome
------------------------------------------------------------
SELECT m.*,
       IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
       COUNT(d.id)               AS numDepositos
FROM meta m
LEFT JOIN deposito d ON d.metaId = m.id
WHERE m.nome LIKE '%' || ? || '%'
GROUP BY m.id
ORDER BY m.dataCriacao DESC;

------------------------------------------------------------
-- Q4: Total global poupado em todas as metas
------------------------------------------------------------
SELECT IFNULL(SUM(valor), 0.0) FROM deposito;

------------------------------------------------------------
-- Q5: Depósitos de uma meta (mais recentes primeiro)
------------------------------------------------------------
SELECT * FROM deposito
WHERE metaId = ?
ORDER BY data DESC;

------------------------------------------------------------
-- Q6: Contar metas
------------------------------------------------------------
SELECT COUNT(*) FROM meta;

------------------------------------------------------------
-- Inserções / atualizações / eliminações (executadas via Room)
------------------------------------------------------------
-- INSERT INTO meta (nome, valorObjetivo, dataLimite, corHex, iconeChave, dataCriacao) VALUES (?,?,?,?,?,?);
-- INSERT INTO deposito (metaId, valor, data, nota) VALUES (?,?,?,?);
-- UPDATE meta SET nome=?, valorObjetivo=?, dataLimite=?, corHex=?, iconeChave=? WHERE id=?;
-- DELETE FROM meta WHERE id=?;      -- CASCADE remove os depósitos associados
-- DELETE FROM deposito WHERE id=?;
