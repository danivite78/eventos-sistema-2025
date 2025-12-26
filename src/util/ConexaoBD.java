package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe para gerenciar conexão com banco de dados MySQL
 */
public class ConexaoBD {
    
    // Configurações do banco
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_eventos?useTimezone=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; //coloque aqui sua senha//
    
    /**
     * Obtém conexão com o banco de dados
     */
    public static Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado!", e);
        }
    }
    
    /**
     * Fecha a conexão
     */
    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    /**
     * Testa a conexão
     */
    public static boolean testarConexao() {
        try (Connection conn = getConexao()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }
}