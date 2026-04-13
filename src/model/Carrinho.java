package model;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private List<ItemCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    public List<ItemCarrinho> getItens() { return itens; }

    /**
     * Adiciona um produto ao carrinho. Se já existir, incrementa a quantidade.
     */
    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto inválido ou quantidade inválida.");
        }
        // Verifica se o produto já está no carrinho
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemCarrinho(produto, quantidade));
    }

    /**
     * Remove um produto do carrinho pelo ID.
     */
    public boolean removerProduto(int produtoId) {
        return itens.removeIf(item -> item.getProduto().getId() == produtoId);
    }

    /**
     * Calcula o valor total do carrinho.
     */
    public double calcularTotal() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void limpar() {
        itens.clear();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    @Override
    public String toString() {
        if (itens.isEmpty()) {
            return "  Carrinho vazio.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  === CARRINHO DE COMPRAS ===\n");
        for (ItemCarrinho item : itens) {
            sb.append(item.toString()).append("\n");
        }
        sb.append(String.format("  --- TOTAL: R$ %.2f ---", calcularTotal()));
        return sb.toString();
    }
}
