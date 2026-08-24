package br.com.ecommerce.main;

import br.com.ecommerce.dao.ProdutoDAO;
import br.com.ecommerce.exception.CredenciaisInvalidasException;
import br.com.ecommerce.model.Produto;

import java.util.List;

public class SistemaPrincipal {
    public static void main(String[] args) {

        System.out.println("\n💾 E-COMMERCE - JDBC + Oracle Database\n");
        System.out.println("========================================");

        try {
            ProdutoDAO dao = new ProdutoDAO();

            // 1. Criar tabela
            System.out.println("\n📋 Configurando Oracle...");
            dao.criarTabela();

            // 2. Inserir produtos (CREATE)
            System.out.println("\n📦 Inserindo produtos...");
            Produto p1 = new Produto("iPhone 15", 5999.00, "ELETRÔNICO", 10);
            Produto p2 = new Produto("Camisa Polo", 149.90, "ROUPA", 50);
            Produto p3 = new Produto("Clean Code", 89.90, "LIVRO", 30);

            dao.inserir(p1);
            dao.inserir(p2);
            dao.inserir(p3);

            // 3. Listar todos (READ)
            System.out.println("\n📋 Todos os produtos:");
            List<Produto> produtos = dao.listarTodos();
            produtos.forEach(System.out::println);

            // 4. Buscar por ID
            System.out.println("\n🔍 Buscando produto ID 2:");
            Produto encontrado = dao.buscarPorId(2L);
            System.out.println(encontrado);

            // 5. Atualizar (UPDATE)
            System.out.println("\n✏️ Atualizando produto ID 2...");
            encontrado.setPreco(199.90);
            encontrado.setEstoque(45);
            dao.atualizar(encontrado);

            // 6. Verificar atualização
            System.out.println("\n🔍 Produto após atualização:");
            System.out.println(dao.buscarPorId(2L));

            // 7. Deletar (DELETE)
            System.out.println("\n🗑️ Deletando produto ID 3...");
            dao.deletar(3L);

            // 8. Listar final
            System.out.println("\n📋 Produtos restantes:");
            dao.listarTodos().forEach(System.out::println);

            System.out.println("\n========================================");
            System.out.println("✅ Sistema finalizado com sucesso!");

        } catch (CredenciaisInvalidasException e) {
            // Tratamento específico para erro de credenciais
            System.err.println(e.getMessage());
            System.err.println(e.getDicaCorrecao());

        } catch (RuntimeException e) {
            // Outros erros genéricos
            System.err.println("\n❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}