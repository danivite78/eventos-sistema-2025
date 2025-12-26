package dao;

import model.Atividade;
import model.Atividade.TipoAtividade;
import util.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Atividade - CRUD Completo
 */
public class AtividadeDAO {
    
    /**
     * Inserir nova atividade
     */
    public boolean inserir(Atividade atividade) {
        String sql = "INSERT INTO atividade (titulo, descricao, data_hora_inicio, data_hora_fim, tipo, evento_id, responsavel_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, atividade.getTitulo());
            stmt.setString(2, atividade.getDescricao());
            stmt.setTimestamp(3, Timestamp.valueOf(atividade.getDataHoraInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(atividade.getDataHoraFim()));
            stmt.setString(5, atividade.getTipo().name());
            stmt.setInt(6, atividade.getEventoId());
            
            if (atividade.getResponsavelId() != null) {
                stmt.setInt(7, atividade.getResponsavelId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            int linhas = stmt.executeUpdate();
            
            if (linhas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    atividade.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir atividade: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Listar todas as atividades
     */
    public List<Atividade> listarTodos() {
        List<Atividade> lista = new ArrayList<>();
        String sql = "SELECT * FROM atividade ORDER BY data_hora_inicio";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Atividade a = new Atividade();
                a.setId(rs.getInt("id"));
                a.setTitulo(rs.getString("titulo"));
                a.setDescricao(rs.getString("descricao"));
                a.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                a.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                a.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                a.setEventoId(rs.getInt("evento_id"));
                
                Integer respId = rs.getInt("responsavel_id");
                if (!rs.wasNull()) {
                    a.setResponsavelId(respId);
                }
                
                lista.add(a);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar atividades: " + e.getMessage());
        }
        
        return lista;
    }
    
    /**
     * Listar atividades por evento
     */
    public List<Atividade> listarPorEvento(int eventoId) {
        List<Atividade> lista = new ArrayList<>();
        String sql = "SELECT * FROM atividade WHERE evento_id = ? ORDER BY data_hora_inicio";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Atividade a = new Atividade();
                a.setId(rs.getInt("id"));
                a.setTitulo(rs.getString("titulo"));
                a.setDescricao(rs.getString("descricao"));
                a.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                a.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                a.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                a.setEventoId(rs.getInt("evento_id"));
                
                Integer respId = rs.getInt("responsavel_id");
                if (!rs.wasNull()) {
                    a.setResponsavelId(respId);
                }
                
                lista.add(a);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar atividades por evento: " + e.getMessage());
        }
        
        return lista;
    }
    
    /**
     * Buscar atividade por ID
     */
    public Atividade buscarPorId(int id) {
        String sql = "SELECT * FROM atividade WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Atividade a = new Atividade();
                a.setId(rs.getInt("id"));
                a.setTitulo(rs.getString("titulo"));
                a.setDescricao(rs.getString("descricao"));
                a.setDataHoraInicio(rs.getTimestamp("data_hora_inicio").toLocalDateTime());
                a.setDataHoraFim(rs.getTimestamp("data_hora_fim").toLocalDateTime());
                a.setTipo(TipoAtividade.valueOf(rs.getString("tipo")));
                a.setEventoId(rs.getInt("evento_id"));
                
                Integer respId = rs.getInt("responsavel_id");
                if (!rs.wasNull()) {
                    a.setResponsavelId(respId);
                }
                
                return a;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atividade: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Atualizar atividade
     */
    public boolean atualizar(Atividade atividade) {
        String sql = "UPDATE atividade SET titulo = ?, descricao = ?, data_hora_inicio = ?, data_hora_fim = ?, tipo = ?, evento_id = ?, responsavel_id = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, atividade.getTitulo());
            stmt.setString(2, atividade.getDescricao());
            stmt.setTimestamp(3, Timestamp.valueOf(atividade.getDataHoraInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(atividade.getDataHoraFim()));
            stmt.setString(5, atividade.getTipo().name());
            stmt.setInt(6, atividade.getEventoId());
            
            if (atividade.getResponsavelId() != null) {
                stmt.setInt(7, atividade.getResponsavelId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setInt(8, atividade.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atividade: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Excluir atividade
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM atividade WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir atividade: " + e.getMessage());
        }
        return false;
    }
}