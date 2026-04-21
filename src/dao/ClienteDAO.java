package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para gerenciar operações de banco de dados
 * relacionadas à entidade Cliente.
 * 
 * Esta classe implementa o padrão DAO, separando a lógica de acesso a dados
 * da lógica de negócio. Todas as operações CRUD são implementadas aqui.
 * 
 * Conceitos aplicados:
 * - DAO Pattern: Separação de responsabilidades
 * - CRUD: Create, Read, Update, Delete
 * - PreparedStatement: Prevenção de SQL Injection
 * - Try-with-resources: Gerenciamento automático de recursos
 * 
 * @author Professores POO + BD
 * @version 1.0
 */
public class ClienteDAO {
    
    /**
     * Insere um novo cliente no banco de dados.
     * 
     * O ID do usuário é gerado automaticamente pelo banco (SERIAL).
     * Após a inserção, o ID gerado é atribuído ao objeto cliente.
     * 
     * @param cliente Objeto Cliente a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     */
    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, email, cpf, endereco) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getEndereco());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
                System.out.println("✓ Cliente inserido com sucesso! ID: " + cliente.getId());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao inserir cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca um cliente pelo ID.
     * 
     * @param id ID do cliente a ser buscado
     * @return Objeto Cliente se encontrado, null caso contrário
     */
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairClienteDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar usuário: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Lista todos os usuários cadastrados no banco de dados.
     * 
     * @return Lista de usuários (vazia se não houver usuários)
     */
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(extrairClienteDoResultSet(rs));
            }
            
            System.out.println("✓ " + clientes.size() + " cliente(s) encontrado(s)");
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao listar clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    /**
     * Atualiza os dados de um cliente existente.
     * 
     * @param cliente Objeto Cliente com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, email = ?, cpf = ?, endereco = ?, WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getEndereco());
            stmt.setInt(5, cliente.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✓ Cliente atualizado com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum cliente encontrado com o ID: " + cliente.getId());
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao atualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exclui um cliente do banco de dados.
     * 
     * @param id ID do cliente a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✓ Cliente excluído com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum cliente encontrado com o ID: " + id);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca clientes por nome (busca parcial, case-insensitive).
     * 
     * @param nome Nome ou parte do nome do produto
     * @return Lista de clientes encontrados
     */
    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(extrairClienteDoResultSet(rs));
                }
            }
            
            System.out.println("✓ " + clientes.size() + " clientes(s) encontrado(s) com o nome '" + nome + "'");
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar clientes por nome: " + e.getMessage());
        }
        
        return clientes;
    }
    
    /**
     * Conta o total de clientes cadastrados.
     * 
     * @return Número total de clientes
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM cliente";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao contar cliente: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Método auxiliar para extrair um objeto Cliente de um ResultSet.
     * 
     * @param rs ResultSet contendo os dados do usuário
     * @return Objeto Cliente com os dados extraídos
     * @throws SQLException se houver erro ao acessar os dados
     */
    private Cliente extrairClienteDoResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setEndereco(rs.getString("endereco"));
        return cliente;
    }
    
    /**
     * Verifica se existe um cliente com o ID especificado.
     * 
     * @param id ID do cliente
     * @return true se o cliente existe, false caso contrário
     */
    public boolean existe(int id) {
        return buscarPorId(id) != null;
    }
}
