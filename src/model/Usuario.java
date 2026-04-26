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
public abstract class Usuario {
    protected int id;
    protected String nome;
    protected String email;
    
    /**
     * Construtor padrão (sem parâmetros).
     * Necessário para algumas operações de criação de objetos.
     */
    public Usuario() {
    }
    public Usuario(String nome, String email) {
        setNome(nome);
        setEmail(email);
    }
    public Usuario(int id, String nome, String email) {
        setId(id);
        setNome(nome);
        setEmail(email);
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
            id, nome, email
        );
    }
    
    /**
     * Exibe os dados do usuário de forma detalhada.
     * Metodo auxiliar para facilitar a visualização no console.
     */
    public void exibirDetalhes() {
        System.out.println("========================================");
        System.out.println("           DETALHES DO USUÁRIO");
        System.out.println("========================================");
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("========================================");
    }
    public abstract void exibirPermissoes();
}
