package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL.
 * 
 * Esta classe implementa o padrão Singleton para garantir que apenas uma
 * instância da conexão seja criada e reutilizada em toda a aplicação.
 * 
 * Conceitos aplicados:
 * - Singleton Pattern: Garante uma única instância de conexão
 * - Encapsulamento: Detalhes de conexão isolados nesta classe
 * - Tratamento de Exceções: Captura e trata erros de conexão
 * 
 * @author Professores POO + BD
 * @version 1.0
 */
public class ConexaoBD {
    
    /**
     * URL de conexão com o banco de dados PostgreSQL.
     * Formato: jdbc:postgresql://host:porta/nome_banco
     */
    private static final String URL = "jdbc:postgresql://localhost:5432/projeto_integrado";
    
    /**
     * Usuário do banco de dados.
     * IMPORTANTE: Altere conforme suas configurações locais.
     */
    private static final String USER = "postgres";
    
    /**
     * Senha do banco de dados.
     * IMPORTANTE: Altere conforme suas configurações locais.
     */
    private static final String PASSWORD = "postgres";
    
    /**
     * Instância única da conexão (Singleton).
     */
    private static Connection conexao = null;
    
    /**
     * Construtor privado para impedir instanciação externa.
     * Parte do padrão Singleton.
     */
    private ConexaoBD() {
    }
    
    /**
     * Obtém uma conexão com o banco de dados.
     * 
     * Se a conexão ainda não existe ou está fechada, cria uma nova.
     * Caso contrário, retorna a conexão existente.
     * 
     * Este método implementa o padrão Singleton para reutilizar a mesma
     * conexão em toda a aplicação, economizando recursos.
     * 
     * @return Connection objeto de conexão com o banco de dados
     * @throws SQLException se houver erro ao conectar com o banco
     */
    public static Connection getConexao() throws SQLException {
        try {
            // Verifica se a conexão não existe ou está fechada
            if (conexao == null || conexao.isClosed()) {
                // Carrega o driver JDBC do PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Estabelece a conexão
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                
                System.out.println("✓ Conexão com banco de dados estabelecida com sucesso!");
            }
            
            return conexao;
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Erro: Driver JDBC do PostgreSQL não encontrado!");
            System.err.println("  Verifique se o arquivo postgresql-XX.jar está na pasta lib/");
            throw new SQLException("Driver JDBC não encontrado", e);
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao conectar com o banco de dados!");
            System.err.println("  Mensagem: " + e.getMessage());
            System.err.println("  Verifique:");
            System.err.println("  - Se o PostgreSQL está rodando");
            System.err.println("  - Se o banco 'projeto_integrado' existe");
            System.err.println("  - Se usuário e senha estão corretos");
            throw e;
        }
    }
    
    /**
     * Fecha a conexão com o banco de dados.
     * 
     * É importante fechar a conexão quando a aplicação for encerrada
     * para liberar recursos do banco de dados.
     * 
     * Este método deve ser chamado no final da execução do programa.
     */
    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("✓ Conexão com banco de dados fechada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao fechar conexão: " + e.getMessage());
        }
    }
    
    /**
     * Testa a conexão com o banco de dados.
     * 
     * Método auxiliar para verificar se a conexão está funcionando.
     * Útil para diagnóstico de problemas.
     * 
     * @return true se a conexão foi estabelecida com sucesso, false caso contrário
     */
    public static boolean testarConexao() {
        try {
            Connection conn = getConexao();
            boolean valida = conn != null && !conn.isClosed();
            
            if (valida) {
                System.out.println("✓ Teste de conexão: SUCESSO");
                System.out.println("  Banco: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("  Versão: " + conn.getMetaData().getDatabaseProductVersion());
            }
            
            return valida;
            
        } catch (SQLException e) {
            System.err.println("✗ Teste de conexão: FALHOU");
            System.err.println("  Erro: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtém informações sobre a conexão atual.
     * Útil para debug e monitoramento.
     */
    public static void exibirInformacoesConexao() {
        try {
            Connection conn = getConexao();
            System.out.println("========================================");
            System.out.println("    INFORMAÇÕES DA CONEXÃO");
            System.out.println("========================================");
            System.out.println("URL: " + URL);
            System.out.println("Usuário: " + USER);
            System.out.println("Status: " + (conn.isClosed() ? "Fechada" : "Aberta"));
            System.out.println("Banco: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Versão: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + conn.getMetaData().getDriverName());
            System.out.println("========================================");
        } catch (SQLException e) {
            System.err.println("✗ Erro ao obter informações: " + e.getMessage());
        }
    }
}
