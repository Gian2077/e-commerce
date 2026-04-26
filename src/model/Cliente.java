package model;

public class Cliente extends Usuario {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String endereco;

    public Cliente() {}

    public Cliente(String nome, String email, String cpf, String endereco) {
        super(nome, email);
        setCpf(cpf);
        setEndereco(endereco);
    }

    public Cliente(int id, String nome, String email, String cpf, String endereco) {
        this.id = id;
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setEndereco(endereco);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email = email.trim();
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if (cpf == null || cpf.replaceAll("[^0-9]", "").length() != 11) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos.");
        }
        this.cpf = cpf.trim();
    }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço não pode ser vazio.");
        }
        this.endereco = endereco.trim();
    }

    @Override
    public void exibirPermissoes() {
        System.out.println("Tipo de Usuário: Cliente");
        System.out.println("Autorização: Adicionar Produtos ao Carrinho, Ver Carrinho, Criar Pedido, Finalizar Compra");
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("========================================");
        System.out.println("           DETALHES DO USUÁRIO");
        System.out.println("========================================");
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("CPF: " + cpf);
        System.out.println("Endereço: " + endereco);
        System.out.println("========================================");
    }

    @Override
    public String toString() {
        return String.format("| ID: %d | Nome: %-20s | Email: %-25s | CPF: %s | Endereço: %s |",
                id, nome, email, cpf, endereco);
    }
}
