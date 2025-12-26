package dao;

import model.Evento;
import util.ConexaoBD;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) para Evento
 * @author Seu Nome
 */
public class EventoDAO {
    
    /**
     * Insere um novo evento no banco de dados
     * @param evento objeto Evento a ser inserido
     * @return true se inserido com sucesso, false caso contrário
     */
    public boolean inserir(Evento evento) {
        String sql = "INSERT INTO evento (nome, descricao, data_inicio, data_fim, local) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, Date.valueOf(evento.getDataInicio()));
            stmt.setDate(4, Date.valueOf(evento.getDataFim()));
            stmt.setString(5, evento.getLocal());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir evento: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lista todos os eventos do banco de dados
     * @return Lista de eventos
     */
    public List<Evento> listarTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento ORDER BY data_inicio DESC";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setNome(rs.getString("nome"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setDataInicio(rs.getDate("data_inicio").toLocalDate());
                evento.setDataFim(rs.getDate("data_fim").toLocalDate());
                evento.setLocal(rs.getString("local"));
                eventos.add(evento);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Busca um evento pelo ID
     * @param id ID do evento
     * @return Objeto Evento ou null se não encontrado
     */
    public Evento buscarPorId(int id) {
        String sql = "SELECT * FROM evento WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("id"));
                evento.setNome(rs.getString("nome"));
                evento.setDescricao(rs.getString("descricao"));
                evento.setDataInicio(rs.getDate("data_inicio").toLocalDate());
                evento.setDataFim(rs.getDate("data_fim").toLocalDate());
                evento.setLocal(rs.getString("local"));
                return evento;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar evento: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Atualiza um evento no banco de dados
     * @param evento objeto Evento com dados atualizados
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean atualizar(Evento evento) {
        String sql = "UPDATE evento SET nome = ?, descricao = ?, data_inicio = ?, data_fim = ?, local = ? WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, Date.valueOf(evento.getDataInicio()));
            stmt.setDate(4, Date.valueOf(evento.getDataFim()));
            stmt.setString(5, evento.getLocal());
            stmt.setInt(6, evento.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exclui um evento do banco de dados
     * @param id ID do evento a ser excluído
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM evento WHERE id = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir evento: " + e.getMessage());
            return false;
        }
    }
}