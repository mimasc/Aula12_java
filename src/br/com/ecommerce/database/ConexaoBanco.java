package br.com.ecommerce.database;

import br.com.ecommerce.exception.CredenciaisInvalidasException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    // Credenciais do banco Oracle FIAP
    private static final String HOST = "oracle.fiap.com.br";
    private static final String PORT = "1521";
    private static final String SID = "ORCL";
    private static final String USER = "RM562092";  // Substituir
    private static final String PASSWORD = "090906"; // Substituir

    /**
     * Cria uma nova conexão com o Oracle.
     */
    public static Connection getConexao() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;

            Connection conexao = DriverManager.getConnection(url, USER, PASSWORD);

            System.out.println("✅ Conexão criada com sucesso!");
            return conexao;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver Oracle não encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            // Verifica se é erro de credenciais inválidas (ORA-01017)
            if (e.getErrorCode() == 1017) {
                String usuario = USER.substring(0, Math.min(3, USER.length())) + "***";
                throw new CredenciaisInvalidasException(
                    "Credenciais inválidas para o usuário '" + usuario + "'. " +
                    "Verifique suas credenciais em ConexaoBanco.java ou use variáveis de ambiente DB_USER e DB_PASSWORD.",
                    e
                );
            }
            throw new RuntimeException("Erro ao conectar ao Oracle: " + e.getMessage(), e);
        }
    }

    /**
     * Fecha uma conexão aberta.
     */
    public static void fechar(Connection conexao) {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("🔌 Conexão fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}