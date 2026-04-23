package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pagamento {
    private int id;
    private double valor;
    private String formaPagamento; // "CARTAO", "BOLETO", "PIX"
    private LocalDateTime dataHora;
    private boolean confirmado;

    public Pagamento() {
        this.dataHora = LocalDateTime.now();
        this.confirmado = false;
    }

    public Pagamento(String formaPagamento) {
        setFormaPagamento(formaPagamento);
        this.dataHora = LocalDateTime.now();
        this.confirmado = false;
    }

    public Pagamento(int id, double valor, String formaPagamento) {
        this.id = id;
        setValor(valor);
        setFormaPagamento(formaPagamento);
        this.dataHora = LocalDateTime.now();
        this.confirmado = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getValor() { return valor; }
    public void setValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser positivo.");
        }
        this.valor = valor;
    }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.trim().isEmpty()) {
            throw new IllegalArgumentException("Forma de pagamento não pode ser vazia.");
        }
        String forma = formaPagamento.trim().toUpperCase();
        if (!forma.equals("CARTAO") && !forma.equals("BOLETO") && !forma.equals("PIX")) {
            throw new IllegalArgumentException("Forma de pagamento inválida. Use: CARTAO, BOLETO ou PIX.");
        }
        this.formaPagamento = forma;
    }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public boolean isConfirmado() { return confirmado; }
    public void confirmar() { this.confirmado = true; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("| Pagamento #%d | Valor: R$ %.2f | Forma: %-7s | Data: %s | Status: %s |",
                id, valor, formaPagamento, dataHora.format(fmt),
                confirmado ? "CONFIRMADO" : "PENDENTE");
    }
}
