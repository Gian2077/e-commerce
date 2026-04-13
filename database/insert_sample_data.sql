-- ========================================
-- Script de Inserção de Dados de Exemplo
-- Projeto Integrado POO + BD
-- ========================================

-- Este script insere dados de exemplo na tabela usuario
-- para facilitar os testes do sistema.

-- ========================================
-- Limpar dados existentes (opcional)
-- ========================================

-- TRUNCATE TABLE usuario RESTART IDENTITY CASCADE;

-- ========================================
-- Inserir usuários de exemplo
-- ========================================

INSERT INTO usuario (nome, email, telefone) VALUES
('João Silva', 'joao.silva@email.com', '(11) 98765-4321'),
('Maria Santos', 'maria.santos@email.com', '(11) 97654-3210'),
('Pedro Oliveira', 'pedro.oliveira@email.com', '(11) 96543-2109'),
('Ana Costa', 'ana.costa@email.com', '(11) 95432-1098'),
('Carlos Souza', 'carlos.souza@email.com', '(11) 94321-0987'),
('Juliana Lima', 'juliana.lima@email.com', '(11) 93210-9876'),
('Roberto Alves', 'roberto.alves@email.com', '(11) 92109-8765'),
('Fernanda Rocha', 'fernanda.rocha@email.com', '(11) 91098-7654'),
('Lucas Martins', 'lucas.martins@email.com', '(11) 90987-6543'),
('Patricia Ferreira', 'patricia.ferreira@email.com', '(11) 89876-5432');

-- ========================================
-- Verificar inserção
-- ========================================

SELECT 
    COUNT(*) as total_usuarios
FROM usuario;

-- ========================================
-- Exibir usuários inseridos
-- ========================================

SELECT 
    id,
    nome,
    email,
    telefone
FROM usuario
ORDER BY nome;

-- ========================================
-- Mensagem de sucesso
-- ========================================

DO $$
DECLARE
    total_usuarios INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_usuarios FROM usuario;
    RAISE NOTICE '✓ Dados de exemplo inseridos com sucesso!';
    RAISE NOTICE '✓ Total de usuários cadastrados: %', total_usuarios;
    RAISE NOTICE '';
    RAISE NOTICE 'Agora você pode executar a aplicação Java e testar o CRUD!';
END $$;
