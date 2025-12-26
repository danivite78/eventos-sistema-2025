package model;

import java.time.LocalDate;

/**
 * Classe que representa um Evento
 * @author Seu Nome
 */
public class Evento {
    private int id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String local;
    
    // Construtores
    public Evento() {
    }
    
    public Evento(String nome, String descricao, LocalDate dataInicio, LocalDate dataFim, String local) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public LocalDate getDataFim() {
        return dataFim;
    }
    
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    
    public String getLocal() {
        return local;
    }
    
    public void setLocal(String local) {
        this.local = local;
    }
    
    // Métodos de validação
    public boolean validarDatas() {
        if (dataInicio == null || dataFim == null) {
            return false;
        }
        // Data de início não pode ser anterior à data atual
        if (dataInicio.isBefore(LocalDate.now())) {
            return false;
        }
        // Data de fim deve ser igual ou posterior à data de início
        return !dataFim.isBefore(dataInicio);
    }
    
    @Override
    public String toString() {
        return nome + " - " + local + " (" + dataInicio + " a " + dataFim + ")";
    }
}