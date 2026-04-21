package dao;

import model.ItemCarrinho;
import model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para gerenciar operações de banco de dados
 * relacionadas à entidade Pedido.
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
public class PedidoDAO {

    /**
     * Insere um novo pedido no banco de dados.
     *
     * O ID do pedido é gerado automaticamente pelo banco (SERIAL).
     * Após a inserção, o ID gerado é atribuído ao objeto pedido.
     *
     * @param pedido Objeto Pedido a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     */
    public boolean inserir(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, data_criacao, status, total) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(pedido.getDataCriacao()));
            stmt.setString(3, pedido.getStatus());
            stmt.setDouble(4, pedido.calcularTotal());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    }
                }

                // Inserir itens do pedido
                inserirItens(conn, pedido);

                System.out.println("✓ Pedido inserido com sucesso! ID: " + pedido.getId());
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("✗ Erro ao inserir pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cria uma tabela relacional vinculando um ou mais produtos a um pedido
     */

    private void inserirItens(Connection conn, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemCarrinho item : pedido.getItens()) {
                stmt.setInt(1, pedido.getId());
                stmt.setInt(2, item.getProduto().getId());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getProduto().getPreco());

                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    /**
     * Busca um pedido pelo ID.
     *
     * @param id ID do pedido a ser buscado
     * @return Objeto Cliente se encontrado, null caso contrário
     */
    public Pedido buscarPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setStatus(rs.getString("status"));

                    return pedido;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedido: " + e.getMessage());
        }

        return null;
    }

    /**
     * Lista todos os pedidos cadastrados no banco de dados.
     *
     * @return Lista de pedidos (vazia se não houver usuários)
     */
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY data_criacao DESC";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setStatus(rs.getString("status"));

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    /**
     * Atualiza os dados de um pedido existente.
     *
     * @param pedido Objeto Pedido com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarStatus(Pedido pedido) {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getStatus());
            stmt.setInt(2, pedido.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exclui um Pedido do banco de dados.
     *
     * @param id ID do pedido a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca pedidos por nome (busca parcial, case-insensitive).
     *
     * @param nome Nome ou parte do nome do produto
     * @return Lista de clientes encontrados
     */
    public List<Pedido> buscarPorNome(String nome) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(extrairPedidoDoResultSet(rs));
                }
            }

            System.out.println("✓ " + pedidos.size() + " pedido(s) encontrado(s) com o nome '" + nome + "'");

        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar pedidos por nome: " + e.getMessage());
        }

        return pedidos; 
    }

    /**
     * Conta o total de pedidos cadastrados.
     *
     * @return Número total de pedidos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM pedido";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("✗ Erro ao contar pedido: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Método auxiliar para extrair um objeto Pedido de um ResultSet.
     *
     * @param rs ResultSet contendo os dados do usuário
     * @return Objeto Cliente com os dados extraídos
     * @throws SQLException se houver erro ao acessar os dados
     */
    private Pedido extrairPedidoDoResultSet(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();;
        pedido.setId(rs.getInt("id"));
        return pedido;
    }

    /**
     * Verifica se existe um pedido com o ID especificado.
     *
     * @param id ID do pedido
     * @return true se o pedido existe, false caso contrário
     */
    public boolean existe(int id) {
        return buscarPorId(id) != null;
    }
}
