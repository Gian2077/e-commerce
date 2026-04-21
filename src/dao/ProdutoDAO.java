package dao;

import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para gerenciar operações de banco de dados
 * relacionadas à entidade Produto.
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
 * @author Gianlucca
 * @version 1.0
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados.
     *
     * O ID do produto é gerado automaticamente pelo banco (SERIAL).
     * Após a inserção, o ID gerado é atribuído ao objeto produto.
     *
     * @param produto Objeto PRoduto a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     */
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, preco, qtd_estoque) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, String.valueOf(produto.getPreco()));
            stmt.setString(4, String.valueOf(produto.getQuantidadeEstoque()));

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                    }
                }
                System.out.println("✓ Produto inserido com sucesso! ID: " + produto.getId());
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("✗ Erro ao inserir produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id ID do produto a ser buscado
     * @return Objeto Produto se encontrado, null caso contrário
     */
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProdutoDoResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    /**
     * Lista todos os produtos cadastrados no banco de dados.
     *
     * @return Lista de produtos (vazia se não houver usuários)
     */
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }

            System.out.println("✓ " + produtos.size() + " produto(s) encontrado(s)");

        } catch (SQLException e) {
            System.err.println("✗ Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param produto Objeto Produto com os dados atualizados
     * @return true se a atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, qtd_estoque = ?, WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, String.valueOf(produto.getPreco()));
            stmt.setString(4, String.valueOf(produto.getQuantidadeEstoque()));
            stmt.setInt(5, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Produto atualizado com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum produto encontrado com o ID: " + produto.getId());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exclui um produto do banco de dados.
     *
     * @param id ID do produto a ser excluído
     * @return true se a exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Produto excluído com sucesso!");
                return true;
            } else {
                System.out.println("⚠ Nenhum produto encontrado com o ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca produtos por nome (busca parcial, case-insensitive).
     *
     * @param nome Nome ou parte do nome do produto
     * @return Lista de clientes encontrados
     */
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(extrairProdutoDoResultSet(rs));
                }
            }

            System.out.println("✓ " + produtos.size() + " produto(s) encontrado(s) com o nome '" + nome + "'");

        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar produto por nome: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Conta o total de produtos cadastrados.
     *
     * @return Número total de produtos
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM produto";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("✗ Erro ao contar produto: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Método auxiliar para extrair um objeto Produto de um ResultSet.
     *
     * @param rs ResultSet contendo os dados do usuário
     * @return Objeto Produto com os dados extraídos
     * @throws SQLException se houver erro ao acessar os dados
     */
    private Produto extrairProdutoDoResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(Double.parseDouble(rs.getString("preco")));
        produto.setQuantidadeEstoque(Integer.parseInt(rs.getString("qtd_estoque")));
        return produto;
    }

    /**
     * Verifica se existe um produto com o ID especificado.
     *
     * @param id ID do produto
     * @return true se o produto existe, false caso contrário
     */
    public boolean existe(int id) {
        return buscarPorId(id) != null;
    }
}
