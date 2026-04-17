import dao.ConexaoBD;
import dao.ClienteDAO;
import model.Usuario;
import util.MenuUtil;

import java.util.List;

/**
 * Classe principal do sistema de gerenciamento.
 * 
 * Esta classe contém o método main e implementa o menu interativo
 * para operações CRUD com usuários.
 * 
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
     * DAO para operações com usuários.
     */
    private static ClienteDAO clienteDAO = new ClienteDAO();
    
    /**
     * Método principal que inicia a aplicação.
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
                    listarClientes();
                    break;
                case 2:
                    buscarCliente();
                    break;
                case 3:
                    cadastrarCliente();
                    break;
                case 4:
                    atualizarCliente();
                    break;
                case 5:
                    excluirCliente();
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
     * Lista todos os usuários cadastrados no sistema.
     */
    private static void listarClientes() {
        MenuUtil.exibirTitulo("Listar Clientes");
        
        List<Usuario> usuarios = clienteDAO.listarTodos();
        
        if (usuarios.isEmpty()) {
            MenuUtil.exibirAviso("Nenhum cliente cadastrado no sistema.");
        } else {
            System.out.println("Total de clientes: " + usuarios.size());
            System.out.println();
            
            // Exibe cabeçalho da tabela
            System.out.printf("%-5s %-25s %-30s %-15s%n", 
                "ID", "Nome", "Email", "Telefone");
            MenuUtil.exibirSeparador();
            
            // Exibe cada cliente
            for (Usuario u : usuarios) {
                System.out.printf("%-5d %-25s %-30s %-15s%n",
                    u.getId(),
                    truncar(u.getNome(), 25),
                    truncar(u.getEmail(), 30),
                    u.getTelefone()
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
        
        Usuario usuario = clienteDAO.buscarPorId(id);
        
        if (usuario != null) {
            System.out.println();
            usuario.exibirDetalhes();
        } else {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
        }
        
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
        String telefone = MenuUtil.lerStringNaoVazia("Telefone: ");
        
        // Cria o objeto Cliente
        Usuario usuario = new Usuario(nome, email, telefone);
        
        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Resumo do Cliente:");
        System.out.println("  Nome: " + usuario.getNome());
        System.out.println("  Email: " + usuario.getEmail());
        System.out.println("  Telefone: " + usuario.getTelefone());
        System.out.println();
        
        if (MenuUtil.confirmar("Confirma o cadastro?")) {
            if (clienteDAO.inserir(usuario)) {
                MenuUtil.exibirSucesso("Cliente cadastrado com sucesso! ID: " + usuario.getId());
            } else {
                MenuUtil.exibirErro("Erro ao cadastrar cliente.");
            }
        } else {
            MenuUtil.exibirAviso("Cadastro cancelado.");
        }
        
        MenuUtil.pausar();
    }
    
    /**
     * Atualiza os dados de um cliente existente.
     */
    private static void atualizarCliente() {
        MenuUtil.exibirTitulo("Atualizar Cliente");
        
        int id = MenuUtil.lerIntPositivo("Digite o ID do cliente a atualizar: ");
        
        Usuario usuario = clienteDAO.buscarPorId(id);
        
        if (usuario == null) {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
            MenuUtil.pausar();
            return;
        }
        
        // Exibe dados atuais
        System.out.println();
        System.out.println("Dados atuais:");
        usuario.exibirDetalhes();
        System.out.println();
        
        // Solicita novos dados (permite manter os atuais pressionando ENTER)
        System.out.println("Digite os novos dados (pressione ENTER para manter o valor atual):");
        System.out.println();
        
        String nome = MenuUtil.lerString("Nome [" + usuario.getNome() + "]: ");
        if (!nome.isEmpty()) {
            usuario.setNome(nome);
        }
        
        String email = MenuUtil.lerString("Email [" + usuario.getEmail() + "]: ");
        if (!email.isEmpty()) {
            usuario.setEmail(email);
        }
        
        String telefone = MenuUtil.lerString("Telefone [" + usuario.getTelefone() + "]: ");
        if (!telefone.isEmpty()) {
            usuario.setTelefone(telefone);
        }
        
        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Novos dados:");
        usuario.exibirDetalhes();
        System.out.println();
        
        if (MenuUtil.confirmar("Confirma a atualização?")) {
            if (clienteDAO.atualizar(usuario)) {
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
        
        Usuario usuario = clienteDAO.buscarPorId(id);
        
        if (usuario == null) {
            MenuUtil.exibirAviso("Cliente não encontrado com o ID: " + id);
            MenuUtil.pausar();
            return;
        }
        
        // Exibe dados do cliente
        System.out.println();
        System.out.println("Usuário a ser excluído:");
        usuario.exibirDetalhes();
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
     * @param texto Texto a ser truncado
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
