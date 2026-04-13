import dao.ConexaoBD;
import dao.UsuarioDAO;
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
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    
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
                    listarUsuarios();
                    break;
                case 2:
                    buscarUsuario();
                    break;
                case 3:
                    cadastrarUsuario();
                    break;
                case 4:
                    atualizarUsuario();
                    break;
                case 5:
                    excluirUsuario();
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
    private static void listarUsuarios() {
        MenuUtil.exibirTitulo("Listar Usuários");
        
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        
        if (usuarios.isEmpty()) {
            MenuUtil.exibirAviso("Nenhum usuário cadastrado no sistema.");
        } else {
            System.out.println("Total de usuários: " + usuarios.size());
            System.out.println();
            
            // Exibe cabeçalho da tabela
            System.out.printf("%-5s %-25s %-30s %-15s%n", 
                "ID", "Nome", "Email", "Telefone");
            MenuUtil.exibirSeparador();
            
            // Exibe cada usuário
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
     * Busca um usuário específico pelo ID.
     */
    private static void buscarUsuario() {
        MenuUtil.exibirTitulo("Buscar Usuário");
        
        int id = MenuUtil.lerIntPositivo("Digite o ID do usuário: ");
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario != null) {
            System.out.println();
            usuario.exibirDetalhes();
        } else {
            MenuUtil.exibirAviso("Usuário não encontrado com o ID: " + id);
        }
        
        MenuUtil.pausar();
    }
    
    /**
     * Cadastra um novo usuário no sistema.
     */
    private static void cadastrarUsuario() {
        MenuUtil.exibirTitulo("Cadastrar Novo Usuário");
        
        // Coleta os dados do usuário
        String nome = MenuUtil.lerStringNaoVazia("Nome completo: ");
        String email = MenuUtil.lerStringNaoVazia("Email: ");
        String telefone = MenuUtil.lerStringNaoVazia("Telefone: ");
        
        // Cria o objeto Usuario
        Usuario usuario = new Usuario(nome, email, telefone);
        
        // Exibe resumo e solicita confirmação
        System.out.println();
        System.out.println("Resumo do usuário:");
        System.out.println("  Nome: " + usuario.getNome());
        System.out.println("  Email: " + usuario.getEmail());
        System.out.println("  Telefone: " + usuario.getTelefone());
        System.out.println();
        
        if (MenuUtil.confirmar("Confirma o cadastro?")) {
            if (usuarioDAO.inserir(usuario)) {
                MenuUtil.exibirSucesso("Usuário cadastrado com sucesso! ID: " + usuario.getId());
            } else {
                MenuUtil.exibirErro("Erro ao cadastrar usuário.");
            }
        } else {
            MenuUtil.exibirAviso("Cadastro cancelado.");
        }
        
        MenuUtil.pausar();
    }
    
    /**
     * Atualiza os dados de um usuário existente.
     */
    private static void atualizarUsuario() {
        MenuUtil.exibirTitulo("Atualizar Usuário");
        
        int id = MenuUtil.lerIntPositivo("Digite o ID do usuário a atualizar: ");
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            MenuUtil.exibirAviso("Usuário não encontrado com o ID: " + id);
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
            if (usuarioDAO.atualizar(usuario)) {
                MenuUtil.exibirSucesso("Usuário atualizado com sucesso!");
            } else {
                MenuUtil.exibirErro("Erro ao atualizar usuário.");
            }
        } else {
            MenuUtil.exibirAviso("Atualização cancelada.");
        }
        
        MenuUtil.pausar();
    }
    
    /**
     * Exclui um usuário do sistema.
     */
    private static void excluirUsuario() {
        MenuUtil.exibirTitulo("Excluir Usuário");
        
        int id = MenuUtil.lerIntPositivo("Digite o ID do usuário a excluir: ");
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            MenuUtil.exibirAviso("Usuário não encontrado com o ID: " + id);
            MenuUtil.pausar();
            return;
        }
        
        // Exibe dados do usuário
        System.out.println();
        System.out.println("Usuário a ser excluído:");
        usuario.exibirDetalhes();
        System.out.println();
        
        // Solicita confirmação
        if (MenuUtil.confirmar("Tem certeza que deseja excluir este usuário?")) {
            if (usuarioDAO.excluir(id)) {
                MenuUtil.exibirSucesso("Usuário excluído com sucesso!");
            } else {
                MenuUtil.exibirErro("Erro ao excluir usuário.");
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
