package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemCarrinho> itens;
    private Pagamento pagamento;
    private LocalDateTime dataCriacao;
    private String status; // "ABERTO", "FINALIZADO", "CANCELADO"

    public Pedido() {
        this.itens = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.status = "ABERTO";
    }

    public Pedido(int id, Cliente cliente, List<ItemCarrinho> itens) {
        this.id = id;
        setCliente(cliente);
        this.itens = new ArrayList<>(itens);
        this.dataCriacao = LocalDateTime.now();
        this.status = "ABERTO";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        this.cliente = cliente;
    }

    public List<ItemCarrinho> getItens() { return itens; }
    public void setItens(List<ItemCarrinho> itens) { this.itens = itens; }

    public Pagamento getPagamento() { return pagamento; }
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double calcularTotal() {
        double total = 0;
        for (ItemCarrinho item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void finalizar(Pagamento pagamento) {
        this.pagamento = pagamento;
        this.pagamento.confirmar();
        this.status = "FINALIZADO";
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════╗\n");
        sb.append(String.format("║ PEDIDO #%-5d | Status: %-12s             ║\n", id, status));
        sb.append(String.format("║ Data: %-43s║\n", dataCriacao.format(fmt)));
        sb.append(String.format("║ Cliente: %-40s║\n", cliente.getNome()));
        sb.append("╠══════════════════════════════════════════════════╣\n");
        sb.append("║ ITENS:                                           ║\n");
        for (ItemCarrinho item : itens) {
            sb.append(String.format("║ %-49s║\n", item.toString().trim()));
        }
        sb.append("╠══════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ TOTAL: R$ %-39.2f║\n", calcularTotal()));
        if (pagamento != null) {
            sb.append(String.format("║ Pagamento: %-7s | %s              ║\n",
                    pagamento.getFormaPagamento(),
                    pagamento.isConfirmado() ? "CONFIRMADO" : "PENDENTE"));
        }
        sb.append("╚══════════════════════════════════════════════════╝");
        return sb.toString();
    }
}
