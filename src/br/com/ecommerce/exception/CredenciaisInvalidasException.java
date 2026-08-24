package br.com.ecommerce.exception;

/**
 * Exceção lançada quando as credenciais do banco de dados estão incorretas.
 *
 * ORA-01017: invalid username/password; logon denied
 *
 * Esta exceção permite tratamento específico para erros de autenticação,
 * evitando que o programa quebre com uma RuntimeException genérica.
 */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String message) {
        super(message);
    }

    public CredenciaisInvalidasException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Mensagem amigável para o usuário.
     * @return Dica de como corrigir o problema
     */
    public String getDicaCorrecao() {
        return """

        ═══════════════════════════════════════════════════════════
        ⚠️  ERRO DE AUTENTICAÇÃO NO BANCO DE DADOS
        ═══════════════════════════════════════════════════════════

        O sistema não conseguiu conectar ao Oracle com as credenciais fornecidas.

        🔧  COMO CORRIGIR:

        Opção 1: Usar variáveis de ambiente (RECOMENDADO)
        --------------------------------------------------
        No terminal (Linux/Mac):
          export DB_USER=seu_usuario
          export DB_PASSWORD=sua_senha
          java -cp "bin:lib/ojdbc17.jar" br.com.ecommerce.main.SistemaPrincipal

        No PowerShell (Windows):
          $env:DB_USER="seu_usuario"
          $env:DB_PASSWORD="sua_senha"
          java -cp "bin;lib/ojdbc17.jar" br.com.ecommerce.main.SistemaPrincipal

        Opção 2: Editar o arquivo ConexaoBanco.java
        -------------------------------------------
        Altere as linhas:
          private static final String USER = "seu_usuario";      // ← seu usuário
          private static final String PASSWORD = "sua_senha";     // ← sua senha

        ────────────────────────────────────────────────────────────────
        📖 Referência: https://docs.oracle.com/error-help/db/ora-01017/
        ═══════════════════════════════════════════════════════════
        """;
    }
}