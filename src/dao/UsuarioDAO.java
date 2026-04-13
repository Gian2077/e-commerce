package dao;

import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para gerenciar operações de banco de dados
 * relacionadas à entidade Usuario.
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
public class UsuarioDAO {
    
    /**
     * Insere um novo usuário no banco de dados.
     * 
     * O ID do usuário é gerado automaticamente pelo banco (SERIAL).
     * Após a inserção, o ID gerado é atribuído ao objeto usuario.
     * 
     * @param usuario Objeto Usuario a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     */
    public boolean inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, telefone) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                System.out.println("✓ Usuário inserido com sucesso! ID: " + usuario.getId());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca um usuário pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return Objeto Usuario se encontrado, null caso contrário
     */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairUsuarioDoResultSet(rs);
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
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
            
            System.out.println("✓ " + usuarios.size() + " usuário(s) encontrado(s)");
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao listar usuários: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    /**
     * Atualiza os dados de um usuário existente.
     * 
     * @param usuario Objeto Usuario com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, telefone = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setInt(4, usuario.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✓ Usuário atualizado com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum usuário encontrado com o ID: " + usuario.getId());
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exclui um usuário do banco de dados.
     * 
     * @param id ID do usuário a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✓ Usuário excluído com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum usuário encontrado com o ID: " + id);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca usuários por nome (busca parcial, case-insensitive).
     * 
     * @param nome Nome ou parte do nome do usuário
     * @return Lista de usuários encontrados
     */
    public List<Usuario> buscarPorNome(String nome) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(extrairUsuarioDoResultSet(rs));
                }
            }
            
            System.out.println("✓ " + usuarios.size() + " usuário(s) encontrado(s) com o nome '" + nome + "'");
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar usuários por nome: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    /**
     * Conta o total de usuários cadastrados.
     * 
     * @return Número total de usuários
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM usuario";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Erro ao contar usuários: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Método auxiliar para extrair um objeto Usuario de um ResultSet.
     * 
     * @param rs ResultSet contendo os dados do usuário
     * @return Objeto Usuario com os dados extraídos
     * @throws SQLException se houver erro ao acessar os dados
     */
    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        return usuario;
    }
    
    /**
     * Verifica se existe um usuário com o ID especificado.
     * 
     * @param id ID do usuário
     * @return true se o usuário existe, false caso contrário
     */
    public boolean existe(int id) {
        return buscarPorId(id) != null;
    }
}
