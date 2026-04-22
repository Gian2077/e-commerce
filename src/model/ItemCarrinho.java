package model;

public class ItemCarrinho {
    private int id;
    private int id_pedido;
    private int id_produto;
    private Produto produto;
    private int quantidade;

    public ItemCarrinho() {}

    public ItemCarrinho(Produto produto, int quantidade) {
        setProduto(produto);
        setQuantidade(quantidade);
    }

    public ItemCarrinho(int id, int id_pedido, int id_produto, Produto produto, int quantidade) {
        setId(id);
        setId_pedido(id_pedido);
        setId_produto(id_produto);
        setProduto(produto);
        setQuantidade(quantidade);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId_pedido() {
        return id_pedido;
    }
    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_produto() {
        return id_produto;
    }
    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
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
