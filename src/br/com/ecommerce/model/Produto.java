package br.com.ecommerce.model;

public class Produto {

    private Long id;
    private String nome;
    private double preco;
    private String categoria;
    private int estoque;

    public Produto(String nome, double preco, String categoria, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.estoque = estoque;
    }

    public Produto() {
		// TODO Auto-generated constructor stub
	}

	// Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    @Override
    public String toString() {
        return String.format("Produto[id=%d, nome=%s, preco=R$%.2f, cat=%s, estoque=%d]",
            id, nome, preco, categoria, estoque);
    }
}