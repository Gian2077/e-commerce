-- ========================================
-- Script de Criação do Banco de Dados
-- Projeto Integrado POO + BD
-- ========================================

-- Conecte-se ao PostgreSQL e execute:
-- CREATE DATABASE projeto_integrado;
-- \c projeto_integrado

-- ========================================
-- Tabela: usuario
-- ========================================
-- Esta é uma tabela de exemplo que demonstra a estrutura básica
-- para o projeto. Os alunos devem criar tabelas similares para
-- suas entidades específicas (Delivery, E-commerce ou Academia).

DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- Comentários nas colunas (Documentação)
-- ========================================

COMMENT ON TABLE usuario IS 'Tabela de exemplo para demonstrar estrutura CRUD básica';
COMMENT ON COLUMN usuario.id IS 'Identificador único do usuário (chave primária)';
COMMENT ON COLUMN usuario.nome IS 'Nome completo do usuário';
COMMENT ON COLUMN usuario.email IS 'Email do usuário (único)';
COMMENT ON COLUMN usuario.telefone IS 'Telefone do usuário';
COMMENT ON COLUMN usuario.data_cadastro IS 'Data e hora de cadastro do usuário';
COMMENT ON COLUMN usuario.data_atualizacao IS 'Data e hora da última atualização';

-- ========================================
-- Índices para melhorar performance
-- ========================================

CREATE INDEX idx_usuario_nome ON usuario(nome);
CREATE INDEX idx_usuario_email ON usuario(email);

-- ========================================
-- Trigger para atualizar data_atualizacao
-- ========================================

CREATE OR REPLACE FUNCTION atualizar_data_modificacao()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_usuario
    BEFORE UPDATE ON usuario
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_data_modificacao();

-- ========================================
-- Verificação da criação
-- ========================================

-- Exibe informações sobre a tabela criada
SELECT 
    table_name,
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'usuario'
ORDER BY ordinal_position;

-- ========================================
-- Mensagem de sucesso
-- ========================================

DO $$
BEGIN
    RAISE NOTICE '✓ Tabela usuario criada com sucesso!';
    RAISE NOTICE '✓ Índices criados com sucesso!';
    RAISE NOTICE '✓ Trigger de atualização criado com sucesso!';
    RAISE NOTICE '';
    RAISE NOTICE 'Próximo passo: Execute insert_sample_data.sql para inserir dados de teste';
END $$;
