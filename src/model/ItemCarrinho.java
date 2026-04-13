package model;

public class ItemCarrinho {
    private Produto produto;
    private int quantidade;

    public ItemCarrinho() {}

    public ItemCarrinho(Produto produto, int quantidade) {
        setProduto(produto);
        setQuantidade(quantidade);
    }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        this.produto = produto;
    }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return String.format("  - %-20s | Qtd: %d | Subtotal: R$ %.2f",
                produto.getNome(), quantidade, getSubtotal());
    }
}
