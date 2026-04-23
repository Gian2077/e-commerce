import dao.*;
import model.*;
import util.MenuUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal do sistema de gerenciamento.
 * <p>
 * Esta classe contém o método main e implementa o menu interativo
 * para operações CRUD com usuários.
 * <p>
 * O sistema demonstra:
 * - Integração entre camadas (Model, DAO, Util)
 * - Operações CRUD completas
 * - Tratamento de exceções
 * - Interface de usuário via console
 * - Conexão com banco de dados PostgreSQL
 *
 * @author Professores POO + BD
 * @version 1.0
 */
public class Main {

    /**
     * DAO para operações com clientes.
     */
    private static ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * DAO para operações com produtos.
     */
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    //    Carrinho
    private static List<ItemCarrinho> carrinho = new ArrayList<>();

    //    Pedido
    private static PedidoDAO pedidoDAO = new PedidoDAO();

    //    Pedido Atual
    private static Pedido pedidoAtual = null;

    /**
     * Metodo principal que inicia a aplicação.
     *
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Exibe cabeçalho do sistema
        MenuUtil.exibirCabecalho();

        // Testa a conexão com o banco de dados
        System.out.println("Testando conexão com o banco de dados...");
        if (!ConexaoBD.testarConexao()) {
            MenuUtil.exibirErro("Não foi possível conectar ao banco de dados!");
            MenuUtil.exibirAviso("Verifique se o PostgreSQL está rodando e as configurações em ConexaoBD.java");
            return;
        }

        System.out.println();
        MenuUtil.pausar();

        // Loop principal do menu
        boolean continuar = true;
        while (continuar) {
            MenuUtil.exibirCabecalho();
            MenuUtil.exibirMenuPrincipal();

            int opcao = MenuUtil.lerOpcao("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    cadastrarCliente();
                    break;
                case 4:
                    listarClientes();
                    break;
                case 5:
                    adicionarProdutoCarrinho();
                    break;
                case 6:
                    verCarrinho();
                    break;
                case 7:
                    criarPedido();
                    break;
                case 8:
                    finalizarCompra();
                    break;
                case 9:
                    //listarPedidos();
                    break;
                case 0:
                    continuar = false;
                    encerrarSistema();
                    break;
                default:
                    MenuUtil.exibirAviso("Opção inválida! Tente novamente.");
                    MenuUtil.pausar();
            }
        }
    }

    /**
     * Cadastra um novo produto no sistema.
     */
    private static void cadastrarProduto() {
        MenuUtil.exibirTitulo("Cadastrar Novo Produto");

        // Coleta os dados do produto
        String nome = MenuUtil.lerStringNaoVazia("Nome do Produto: ");
        String descricao = MenuUtil.lerStringNaoVazia("Descrição: ");
        double preco = MenuUtil.lerDoublePositivo("Preço: ");
        int qtd = MenuUtil.lerIntPositivo("Quantidade em Estoque: ");

        // Cria o objeto Produto
        Produto produto = new Produto(nome, descricao, preco, qtd);

        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Resumo do Produto:");
        System.out.println("  Nome: " + produto.getNome());
        System.out.println("  Descrição: " + produto.getDescricao());
        System.out.println("  Preço: " + produto.getPreco());
        System.out.println("  Quantidade em Estoque: " + produto.getQuantidadeEstoque());
        System.out.println();

        if (MenuUtil.confirmar("Confirma o cadastro?")) {
            if (produtoDAO.inserir(produto)) {
                MenuUtil.exibirSucesso("Produto cadastrado com sucesso! ID: " + produto.getId());
            } else {
                MenuUtil.exibirErro("Erro ao cadastrar cliente.");
            }
        } else {
            MenuUtil.exibirAviso("Cadastro cancelado.");
        }

        MenuUtil.pausar();
    }

    /**
     * Lista todos os produtos cadastrados no sistema.
     */
    private static void listarProdutos() {
        MenuUtil.exibirTitulo("Listar Produtos");

        List<Produto> produtos = produtoDAO.listarTodos();

        if (produtos.isEmpty()) {
            MenuUtil.exibirAviso("Nenhum produto cadastrado no sistema.");
        } else {
            System.out.println("Total de produtos: " + produtos.size());
            System.out.println();

            // Exibe cabeçalho da tabela
            System.out.printf("%-5s %-30s %-30s %-11s %-30s%n",
                    "ID", "Nome", "Descrição", "Preço", "Quantidade em Estoque");
            MenuUtil.exibirSeparador();

            // Exibe cada cliente
            for (Produto p : produtos) {
                System.out.printf("%-5d %-30s %-30s %-10.2f %-3d%n",
                        p.getId(),
                        truncar(p.getNome(), 30),
                        truncar(p.getDescricao(), 30),
                        p.getPreco(),
                        p.getQuantidadeEstoque()
                );
            }
        }

        System.out.println();
        MenuUtil.pausar();
    }

    /**
     * Cadastra um novo cliente no sistema.
     */
    private static void cadastrarCliente() {
        MenuUtil.exibirTitulo("Cadastrar Novo Cliente");

        // Coleta os dados do cliente
        String nome = MenuUtil.lerStringNaoVazia("Nome completo: ");
        String email = MenuUtil.lerStringNaoVazia("Email: ");
        String cpf = MenuUtil.lerStringNaoVazia("CPF: ");
        String endereco = MenuUtil.lerStringNaoVazia("Endereço: ");

        // Cria o objeto Cliente
        Cliente cliente = new Cliente(nome, email, cpf, endereco);

        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Resumo do Cliente:");
        System.out.println("  Nome: " + cliente.getNome());
        System.out.println("  Email: " + cliente.getEmail());
        System.out.println("  CPF: " + cliente.getCpf());
        System.out.println("  Endereço: " + cliente.getEndereco());
        System.out.println();

        if (MenuUtil.confirmar("Confirma o cadastro?")) {
            if (clienteDAO.inserir(cliente)) {
                MenuUtil.exibirSucesso("Cliente cadastrado com sucesso! ID: " + cliente.getId());
            } else {
                MenuUtil.exibirErro("Erro ao cadastrar cliente.");
            }
        } else {
            MenuUtil.exibirAviso("Cadastro cancelado.");
        }

        MenuUtil.pausar();
    }

    /**
     * Lista todos os clientes cadastrados no sistema.
     */
    private static void listarClientes() {
        MenuUtil.exibirTitulo("Listar Clientes");

        List<Cliente> clientes = clienteDAO.listarTodos();

        if (clientes.isEmpty()) {
            MenuUtil.exibirAviso("Nenhum cliente cadastrado no sistema.");
        } else {
            System.out.println("Total de clientes: " + clientes.size());
            System.out.println();

            // Exibe cabeçalho da tabela
            System.out.printf("%-5s %-30s %-30s %-11s %-30s%n",
                    "ID", "Nome", "Email", "CPF", "Endereço");
            MenuUtil.exibirSeparador();

            // Exibe cada cliente
            for (Cliente c : clientes) {
                System.out.printf("%-5d %-30s %-30s %-11s %-30s%n",
                        c.getId(),
                        truncar(c.getNome(), 30),
                        truncar(c.getEmail(), 30),
                        c.getCpf(),
                        truncar(c.getEndereco(), 30)
                );
            }
        }

        System.out.println();
        MenuUtil.pausar();
    }

    /**
     * Busca um cliente específico pelo ID.
     */
    private static void buscarCliente() {
        MenuUtil.exibirTitulo("Buscar Usuário");

        int id = MenuUtil.lerIntPositivo("Digite o ID do cliente: ");

        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente != null) {
            System.out.println();
            cliente.exibirDetalhes();
        } else {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
        }

        MenuUtil.pausar();
    }

    /**
     * Atualiza os dados de um cliente existente.
     */
    private static void atualizarCliente() {
        MenuUtil.exibirTitulo("Atualizar Cliente");

        int id = MenuUtil.lerIntPositivo("Digite o ID do cliente a atualizar: ");

        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente == null) {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
            MenuUtil.pausar();
            return;
        }

        // Exibe dados atuais
        System.out.println();
        System.out.println("Dados atuais:");
        cliente.exibirDetalhes();
        System.out.println();

        // Solicita novos dados (permite manter os atuais pressionando ENTER)
        System.out.println("Digite os novos dados (pressione ENTER para manter o valor atual):");
        System.out.println();

        String nome = MenuUtil.lerString("Nome [" + cliente.getNome() + "]: ");
        if (!nome.isEmpty()) {
            cliente.setNome(nome);
        }

        String email = MenuUtil.lerString("Email [" + cliente.getEmail() + "]: ");
        if (!email.isEmpty()) {
            cliente.setEmail(email);
        }

        String cpf = MenuUtil.lerString("CPF [" + cliente.getCpf() + "]: ");
        if (!cpf.isEmpty()) {
            cliente.setCpf(cpf);
        }

        String endereco = MenuUtil.lerString("Endereço [" + cliente.getEndereco() + "]: ");
        if (!endereco.isEmpty()) {
            cliente.setEndereco(endereco);
        }

        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Novos dados:");
        cliente.exibirDetalhes();
        System.out.println();

        if (MenuUtil.confirmar("Confirma a atualização?")) {
            if (clienteDAO.atualizar(cliente)) {
                MenuUtil.exibirSucesso("Cliente atualizado com sucesso!");
            } else {
                MenuUtil.exibirErro("Erro ao atualizar cliente.");
            }
        } else {
            MenuUtil.exibirAviso("Atualização cancelada.");
        }

        MenuUtil.pausar();
    }

    /**
     * Exclui um cliente do sistema.
     */
    private static void excluirCliente() {
        MenuUtil.exibirTitulo("Excluir Cliente");

        int id = MenuUtil.lerIntPositivo("Digite o ID do cliente a excluir: ");

        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente == null) {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
            MenuUtil.pausar();
            return;
        }

        // Exibe dados do cliente
        System.out.println();
        System.out.println("Cliente a ser excluído:");
        cliente.exibirDetalhes();
        System.out.println();

        // Solicita confirmação
        if (MenuUtil.confirmar("Tem certeza que deseja excluir este cliente?")) {
            if (clienteDAO.excluir(id)) {
                MenuUtil.exibirSucesso("Cliente excluído com sucesso!");
            } else {
                MenuUtil.exibirErro("Erro ao excluir cliente.");
            }
        } else {
            MenuUtil.exibirAviso("Exclusão cancelada.");
        }

        MenuUtil.pausar();
    }

    //    Adiciona Produtos ao Carrinho
    private static void adicionarProdutoCarrinho() {
        listarProdutos();
        MenuUtil.exibirTitulo("Adicionar Produto ao Carrinho");

        int idProduto = MenuUtil.lerIntPositivo("Digite o ID do produto: ");
        Produto p = produtoDAO.buscarPorId(idProduto);

        if (p == null) {
            MenuUtil.exibirErro("Produto não encontrado!");
            MenuUtil.pausar();
            return;
        }

        int quantidade = MenuUtil.lerIntPositivo("Quantidade: ");

        ItemCarrinho item = new ItemCarrinho();
        item.setProduto(p);
        item.setQuantidade(quantidade);

        carrinho.add(item);

        MenuUtil.exibirSucesso("Produto adicionado ao carrinho!");
        MenuUtil.pausar();
    }

    /**
     * Lista Produtos no Carrinho de Compras
     */
    private static void verCarrinho() {
        MenuUtil.exibirTitulo("Carrinho de Compras");

        if (carrinho.isEmpty()) {
            MenuUtil.exibirAviso("Carrinho vazio.");
        } else {
            double total = 0;

            for (ItemCarrinho item : carrinho) {
                System.out.println(item);
                total += item.getSubtotal();
            }

            System.out.println("\nTOTAL: R$ " + total);
        }

        MenuUtil.pausar();
    }

    /**
     * Cria um novo Pedido
     */

    private static void criarPedido() {
        MenuUtil.exibirTitulo("Criar Pedido");

        if (carrinho.isEmpty()) {
            MenuUtil.exibirAviso("Carrinho vazio!");
            MenuUtil.pausar();
            return;
        }

        listarClientes();
        int idCliente = MenuUtil.lerIntPositivo("Digite o ID do cliente: ");

        Cliente cliente = clienteDAO.buscarPorId(idCliente);

        if (cliente == null) {
            MenuUtil.exibirErro("Cliente não encontrado!");
            MenuUtil.pausar();
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItens(new ArrayList<>(carrinho));

        if (MenuUtil.confirmar("Confirmar criação do pedido?")) {
            if (pedidoDAO.inserir(pedido)) {
                pedidoAtual = pedido;
                carrinho.clear();

                MenuUtil.exibirSucesso("Pedido criado com sucesso! ID: " + pedido.getId());
            } else {
                MenuUtil.exibirErro("Erro ao criar pedido.");
            }
        } else {
            MenuUtil.exibirAviso("Criação cancelada.");
        }

        MenuUtil.pausar();
    }

    private static void finalizarCompra() {
        MenuUtil.exibirTitulo("Finalizar Compra");

        if (pedidoAtual == null) {
            MenuUtil.exibirAviso("Nenhum pedido criado!");
            MenuUtil.pausar();
            return;
        }

        String formaPagamento = MenuUtil.lerStringNaoVazia("Forma de pagamento (CARTAO/PIX/DINHEIRO): ");

        Pagamento pagamento = new Pagamento(formaPagamento);

        pedidoAtual.finalizar(pagamento);

        if (pedidoDAO.atualizarStatus(pedidoAtual)) {
            MenuUtil.exibirSucesso("Compra finalizada com sucesso!");
            pedidoAtual = null;
        } else {
            MenuUtil.exibirErro("Erro ao finalizar compra.");
        }

        MenuUtil.pausar();
    }

    /**
     * Encerra o sistema de forma adequada.
     */
    private static void encerrarSistema() {
        MenuUtil.exibirTitulo("Encerrando Sistema");

        System.out.println("Fechando conexão com o banco de dados...");
        ConexaoBD.fecharConexao();

        System.out.println("Fechando recursos do sistema...");
        MenuUtil.fecharScanner();

        System.out.println();
        System.out.println("Sistema encerrado com sucesso!");
        System.out.println("Até logo! 👋");
    }

    /**
     * Método auxiliar para truncar strings longas.
     *
     * @param texto   Texto a ser truncado
     * @param tamanho Tamanho máximo
     * @return Texto truncado com "..." se necessário
     */
    private static String truncar(String texto, int tamanho) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 3) + "...";
    }
}
