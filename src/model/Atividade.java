package model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma Atividade
 * @author Seu Nome
 */
public class Atividade {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private TipoAtividade tipo;
    private int eventoId;
    private Integer responsavelId; // Pode ser null
    
    // Enum para tipos de atividade
    public enum TipoAtividade {
        WORKSHOP("Workshop"),
        PALESTRA("Palestra"),
        OFICINA("Oficina");
        
        private String descricao;
        
        TipoAtividade(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtores
    public Atividade() {
    }
    
    public Atividade(String titulo, String descricao, LocalDateTime dataHoraInicio, 
                     LocalDateTime dataHoraFim, TipoAtividade tipo, int eventoId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.tipo = tipo;
        this.eventoId = eventoId;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }
    
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
    
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
    
    public TipoAtividade getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoAtividade tipo) {
        this.tipo = tipo;
    }
    
    public int getEventoId() {
        return eventoId;
    }
    
    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }
    
    public Integer getResponsavelId() {
        return responsavelId;
    }
    
    public void setResponsavelId(Integer responsavelId) {
        this.responsavelId = responsavelId;
    }
    
    // Métodos de validação
    public boolean validarHorarios() {
        if (dataHoraInicio == null || dataHoraFim == null) {
            return false;
        }
        // Horário de fim deve ser posterior ao horário de início
        return dataHoraFim.isAfter(dataHoraInicio);
    }
    
    @Override
    public String toString() {
        return titulo + " - " + tipo.getDescricao() + " (" + dataHoraInicio.toLocalDate() + ")";
    }
}