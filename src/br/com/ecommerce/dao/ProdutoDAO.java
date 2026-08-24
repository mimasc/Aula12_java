package br.com.ecommerce.dao;

import br.com.ecommerce.database.ConexaoBanco;
import br.com.ecommerce.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    /**
     * Cria a tabela produtos no Oracle 12c+ com IDENTITY.
     */
    public void criarTabela() {
        String sqlTabela = "CREATE TABLE produtos (" +
                           "id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                           "nome VARCHAR2(100) NOT NULL, " +
                           "preco NUMBER NOT NULL, " +
                           "categoria VARCHAR2(50), " +
                           "estoque NUMBER" +
                           ")";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();

            try {
                stmt.execute(sqlTabela);
                System.out.println("📋 Tabela 'produtos' criada com IDENTITY.");
            } catch (SQLException e) {
                System.out.println("📋 Tabela 'produtos' já existe.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage(), e);
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * Insere produto usando IDENTITY (CREATE).
     */
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, categoria, estoque) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql, new String[]{"ID"});

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setString(3, produto.getCategoria());
            pstmt.setInt(4, produto.getEstoque());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recupera o ID gerado automaticamente pelo IDENTITY
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
                System.out.println("✅ Produto inserido: " + produto.getNome() + " (ID: " + produto.getId() + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * Busca produto por ID (READ).
     */
    public Produto buscarPorId(Long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairProduto(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * Lista todos os produtos (READ).
     */
    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produtos ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexaoBanco.getConexao();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                produtos.add(extrairProduto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }

        return produtos;
    }

    /**
     * Atualiza produto (UPDATE).
     */
    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, categoria = ?, estoque = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            conn.setAutoCommit(false);  // Inicia transação manual
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setString(3, produto.getCategoria());
            pstmt.setInt(4, produto.getEstoque());
            pstmt.setLong(5, produto.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Produto atualizado: " + produto.getNome());
            } else {
                System.out.println("⚠️ Produto não encontrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * Deleta produto (DELETE).
     */
    public void deletar(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexaoBanco.getConexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Produto deletado (ID: " + id + ")");
            } else {
                System.out.println("⚠️ Produto não encontrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    private Produto extrairProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getLong("id"));
        p.setNome(rs.getString("nome"));
        p.setPreco(rs.getDouble("preco"));
        p.setCategoria(rs.getString("categoria"));
        p.setEstoque(rs.getInt("estoque"));
        return p;
    }
}