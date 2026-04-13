package model;

/**
 * Classe que representa um Usuario no sistema.
 * 
 * Esta é uma entidade de exemplo que demonstra os conceitos de POO:
 * - Encapsulamento: atributos privados com getters/setters
 * - Abstração: representa um usuário do mundo real
 * 
 * Esta classe é genérica e pode ser adaptada para qualquer tema do projeto
 * (Delivery: clientes/entregadores, E-commerce: clientes, Academia: alunos).
 * 
 * @author Professores POO + BD
 * @version 1.0
 */
public class Usuario {
    
    /**
     * Identificador único do usuário (chave primária no banco de dados)
     */
    private int id;
    
    /**
     * Nome completo do usuário
     */
    private String nome;
    
    /**
     * Email do usuário
     */
    private String email;
    
    /**
     * Telefone do usuário
     */
    private String telefone;
    
    /**
     * Construtor padrão (sem parâmetros).
     * Necessário para algumas operações de criação de objetos.
     */
    public Usuario() {
    }
    
    /**
     * Construtor completo com todos os atributos.
     * 
     * @param id Identificador único do usuário
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param telefone Telefone do usuário
     */
    public Usuario(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
    
    /**
     * Construtor sem ID (usado ao criar novos usuários).
     * O ID será gerado automaticamente pelo banco de dados.
     * 
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param telefone Telefone do usuário
     */
    public Usuario(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    /**
     * Obtém o ID do usuário.
     * 
     * @return ID do usuário
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do usuário.
     * 
     * @param id ID do usuário
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome do usuário.
     * 
     * @return Nome do usuário
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do usuário.
     * 
     * @param nome Nome do usuário
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém o email do usuário.
     * 
     * @return Email do usuário
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Define o email do usuário.
     * 
     * @param email Email do usuário
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtém o telefone do usuário.
     * 
     * @return Telefone do usuário
     */
    public String getTelefone() {
        return telefone;
    }
    
    /**
     * Define o telefone do usuário.
     * 
     * @param telefone Telefone do usuário
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Retorna uma representação em String do usuário.
     * Útil para exibir informações do usuário de forma formatada.
     * 
     * @return String formatada com os dados do usuário
     */
    @Override
    public String toString() {
        return String.format(
            "Usuario [ID=%d, Nome=%s, Email=%s, Telefone=%s]",
            id, nome, email, telefone
        );
    }
    
    /**
     * Exibe os dados do usuário de forma detalhada.
     * Método auxiliar para facilitar a visualização no console.
     */
    public void exibirDetalhes() {
        System.out.println("========================================");
        System.out.println("           DETALHES DO USUÁRIO");
        System.out.println("========================================");
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("Telefone: " + telefone);
        System.out.println("========================================");
    }
}
