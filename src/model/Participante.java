package model;

/**
 * Classe que representa um Participante
 * @author Seu Nome
 */
public class Participante {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private TipoParticipante tipo;
    
    // Enum para tipos de participante
    public enum TipoParticipante {
        ESTUDANTE("Estudante"),
        PALESTRANTE("Palestrante"),
        CONVIDADO("Convidado");
        
        private String descricao;
        
        TipoParticipante(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtores
    public Participante() {
    }
    
    public Participante(String nome, String email, String telefone, TipoParticipante tipo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public TipoParticipante getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoParticipante tipo) {
        this.tipo = tipo;
    }
    
    // Métodos de validação
    public boolean validarEmail() {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    public boolean validarTelefone() {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        // Formato: (xx)xxxxx-xxxx
        return telefone.matches("\\(\\d{2}\\)\\d{5}-\\d{4}");
    }
    
    @Override
    public String toString() {
        return nome + " - " + tipo.getDescricao() + " (" + email + ")";
    }
}