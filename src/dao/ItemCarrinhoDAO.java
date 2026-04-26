package dao;

import model.ItemCarrinho;
import model.Produto;
import dao.ProdutoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCarrinhoDAO {

    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    // Inserir um item
    public boolean inserir(ItemCarrinho item, int idPedido) {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getProduto().getPreco());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir item: " + e.getMessage());
            return false;
        }
    }

    // Inserir vários itens
    public void inserirBatch(Connection conn, List<ItemCarrinho> itens, int idPedido) throws SQLException {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemCarrinho item : itens) {
                stmt.setInt(1, idPedido);
                stmt.setInt(2, item.getProduto().getId());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getProduto().getPreco());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    //    Buscar pedido por ID do Pedido
    public List<ItemCarrinho> buscarPorPedido(int idPedido) {
        List<ItemCarrinho> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE id_pedido = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(extrairItem(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens: " + e.getMessage());
        }

        return itens;
    }

    //    Atualiza Item, retorna true se bem sucedido, false se algo deu errado
    public boolean atualizar(ItemCarrinho item, int id) {
        String sql = "UPDATE item_pedido SET quantidade = ?, preco = ? WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade());
            stmt.setDouble(2, item.getProduto().getPreco());
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item: " + e.getMessage());
            return false;
        }
    }

    //    Deleta Item, retorna true se bem sucedido, false se algo deu errado
    public boolean excluir(int id) {
        String sql = "DELETE FROM item_pedido WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir item: " + e.getMessage());
            return false;
        }
    }

    //    Metodo Auxiliar para extrair item do ResultSet
    private ItemCarrinho extrairItem(ResultSet rs) throws SQLException {
        ItemCarrinho item = new ItemCarrinho();
        item.setId(rs.getInt("id"));
        item.setId_pedido(rs.getInt("id_pedido"));
        item.setId_produto(rs.getInt("id_produto"));
        Produto produto = new Produto();
        int produtoId = rs.getInt("id_produto");
        produto.setId(produtoId);
        produto.setNome(produtoDAO.buscarPorId(rs.getInt("id_produto")).getNome());
        produto.setPreco(rs.getDouble("preco"));
        item.setProduto(produto);
        item.setQuantidade(rs.getInt("quantidade"));
        return item;
    }
}