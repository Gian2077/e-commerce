package model;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;

    // Construtor padrão
    public Produto() {}

    // Construtor parametrizado
    public Produto(int id, String nome, String descricao, double preco, int quantidadeEstoque) {
        this.id = id;
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
    }
    public Produto (String nome, String descricao, double preco, int quantidadeEstoque) {
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    // Getters e Setters com validações
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {
        this.descricao = (descricao == null) ? "" : descricao.trim();
    }

    public double getPreco() { return preco; }
    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    @Override
    public String toString() {
        return String.format("| ID: %d | Nome: %-20s | Preço: R$ %8.2f | Estoque: %d | Descrição: %s |",
                id, nome, preco, quantidadeEstoque, descricao);
    }
}
