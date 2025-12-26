package dao;

import model.Participante;
import model.Participante.TipoParticipante;
import util.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Participante - CRUD Completo
 */
public class ParticipanteDAO {
    
    /**
     * Inserir novo participante
     */
    public boolean inserir(Participante participante) {
        String sql = "INSERT INTO participante (nome, email, telefone, tipo) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, participante.getNome());
            stmt.setString(2, participante.getEmail());
            stmt.setString(3, participante.getTelefone());
            stmt.setString(4, participante.getTipo().name());
            
            int linhas = stmt.executeUpdate();
            
            if (linhas > 0) {
                // Obter ID gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    participante.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir participante: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Listar todos os participantes
     */
    public List<Participante> listarTodos() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM participante ORDER BY nome";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Participante p = new Participante();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setEmail(rs.getString("email"));
                p.setTelefone(rs.getString("telefone"));
                p.setTipo(TipoParticipante.valueOf(rs.getString("tipo")));
                lista.add(p);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar participantes: " + e.getMessage());
        }
        
        return lista;
    }
    
    /**
     * Buscar participante por ID
     */
    public Participante buscarPorId(int id) {
        String sql = "SELECT * FROM participante WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Participante p = new Participante();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setEmail(rs.getString("email"));
                p.setTelefone(rs.getString("telefone"));
                p.setTipo(TipoParticipante.valueOf(rs.getString("tipo")));
                return p;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar participante: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Atualizar participante
     */
    public boolean atualizar(Participante participante) {
        String sql = "UPDATE participante SET nome = ?, email = ?, telefone = ?, tipo = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, participante.getNome());
            stmt.setString(2, participante.getEmail());
            stmt.setString(3, participante.getTelefone());
            stmt.setString(4, participante.getTipo().name());
            stmt.setInt(5, participante.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar participante: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Excluir participante
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM participante WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir participante: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Verificar se email já existe
     */
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM participante WHERE email = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao verificar email: " + e.getMessage());
        }
        return false;
    }
}