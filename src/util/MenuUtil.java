package util;

import java.util.Scanner;

/**
 * Classe utilitária para operações relacionadas ao menu e entrada de dados.
 * 
 * Esta classe centraliza métodos auxiliares para:
 * - Exibição de menus formatados
 * - Leitura e validação de entrada do usuário
 * - Formatação de saída no console
 * 
 * Conceitos aplicados:
 * - Utility Class: Métodos estáticos para funcionalidades comuns
 * - Validação de Entrada: Garante que dados inseridos sejam válidos
 * - Reutilização de Código: Evita duplicação em várias partes do sistema
 * 
 * @author Professores POO + BD
 * @version 1.0
 */
public class MenuUtil {
    
    /**
     * Scanner compartilhado para leitura de entrada do usuário.
     * Usar uma única instância evita problemas com múltiplos scanners.
     */
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Construtor privado para impedir instanciação.
     * Esta é uma classe utilitária com apenas métodos estáticos.
     */
    private MenuUtil() {
    }
    
    /**
     * Exibe o cabeçalho do sistema.
     */
    public static void exibirCabecalho() {
        limparTela();
        System.out.println("========================================");
        System.out.println("    SISTEMA DE E-COMMERCE");
        System.out.println("    Projeto Integrado POO + BD");
        System.out.println("========================================");
        System.out.println();
    }
    
    /**
     * Exibe o menu principal do sistema.
     */
    public static void exibirMenuPrincipal() {
        System.out.println("========================================");
        System.out.println("           MENU PRINCIPAL");
        System.out.println("========================================");
        System.out.println("  1 - Cadastrar Produto");
        System.out.println("  2 - Listar Produtos");
        System.out.println("  3 - Cadastrar Cliente");
        System.out.println("  4 - Listar Clientes");
        System.out.println("  5 - Adicionar Produto ao Carrinho");
        System.out.println("  6 - Ver Carrinho");
        System.out.println("  7 - Criar Pedido");
        System.out.println("  8 - Finalizar Compra");
        System.out.println("  9 - Listar Pedidos");
        System.out.println("  0 - Sair");
        System.out.println("========================================");
    }
    
    /**
     * Lê uma opção inteira do usuário com validação.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número inteiro digitado pelo usuário
     */
    public static int lerOpcao(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                return opcao;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Entrada inválida! Digite um número inteiro.");
            }
        }
    }
    
    /**
     * Lê uma string do usuário.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return String digitada pelo usuário
     */
    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
    
    /**
     * Lê uma string não vazia do usuário.
     * Continua pedindo até que o usuário digite algo.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return String não vazia digitada pelo usuário
     */
    public static String lerStringNaoVazia(String mensagem) {
        String valor;
        do {
            valor = lerString(mensagem);
            if (valor.isEmpty()) {
                System.out.println("⚠ Este campo não pode estar vazio!");
            }
        } while (valor.isEmpty());
        return valor;
    }
    
    /**
     * Lê um número double do usuário com validação.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número double digitado pelo usuário
     */
    public static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = Double.parseDouble(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Entrada inválida! Digite um número decimal (use ponto ou vírgula).");
            }
        }
    }
    
    /**
     * Lê um número double positivo do usuário.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número double positivo digitado pelo usuário
     */
    public static double lerDoublePositivo(String mensagem) {
        double valor;
        do {
            valor = lerDouble(mensagem);
            if (valor < 0) {
                System.out.println("⚠ O valor deve ser positivo!");
            }
        } while (valor < 0);
        return valor;
    }
    
    /**
     * Lê um número inteiro do usuário com validação.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número inteiro digitado pelo usuário
     */
    public static int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Entrada inválida! Digite um número inteiro.");
            }
        }
    }
    
    /**
     * Lê um número inteiro positivo do usuário.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número inteiro positivo digitado pelo usuário
     */
    public static int lerIntPositivo(String mensagem) {
        int valor;
        do {
            valor = lerInt(mensagem);
            if (valor < 0) {
                System.out.println("⚠ O valor deve ser positivo!");
            }
        } while (valor < 0);
        return valor;
    }
    
    /**
     * Solicita confirmação do usuário (S/N).
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return true se o usuário confirmar (S), false caso contrário (N)
     */
    public static boolean confirmar(String mensagem) {
        while (true) {
            System.out.print(mensagem + " (S/N): ");
            String resposta = scanner.nextLine().trim().toUpperCase();
            
            if (resposta.equals("S") || resposta.equals("SIM")) {
                return true;
            } else if (resposta.equals("N") || resposta.equals("NAO") || resposta.equals("NÃO")) {
                return false;
            } else {
                System.out.println("⚠ Resposta inválida! Digite S para Sim ou N para Não.");
            }
        }
    }
    
    /**
     * Pausa a execução e aguarda o usuário pressionar ENTER.
     */
    public static void pausar() {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Exibe uma linha separadora.
     */
    public static void exibirSeparador() {
        System.out.println("=".repeat(110));
    }
    
    /**
     * Exibe uma mensagem de sucesso formatada.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    public static void exibirSucesso(String mensagem) {
        System.out.println();
        System.out.println("✓ " + mensagem);
        System.out.println();
    }
    
    /**
     * Exibe uma mensagem de erro formatada.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    public static void exibirErro(String mensagem) {
        System.out.println();
        System.out.println("✗ " + mensagem);
        System.out.println();
    }
    
    /**
     * Exibe uma mensagem de aviso formatada.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    public static void exibirAviso(String mensagem) {
        System.out.println();
        System.out.println("⚠ " + mensagem);
        System.out.println();
    }
    
    /**
     * Limpa a tela do console.
     * 
     * Nota: Este método tenta limpar a tela, mas pode não funcionar
     * em todos os ambientes (IDEs, terminais diferentes, etc.).
     */
    public static void limparTela() {
        try {
            // Tenta limpar a tela no Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Tenta limpar a tela no Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Se falhar, apenas adiciona linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Fecha o scanner.
     * Deve ser chamado apenas ao encerrar a aplicação.
     */
    public static void fecharScanner() {
        scanner.close();
    }
    
    /**
     * Exibe um título formatado.
     * 
     * @param titulo Título a ser exibido
     */
    public static void exibirTitulo(String titulo) {
        System.out.println();
        exibirSeparador();
        System.out.println("  " + titulo.toUpperCase());
        exibirSeparador();
        System.out.println();
    }
}
