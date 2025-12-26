package view;

import dao.AtividadeDAO;
import dao.EventoDAO;
import dao.ParticipanteDAO;
import model.Atividade;
import model.Atividade.TipoAtividade;
import model.Evento;
import model.Participante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela de Gerenciamento de Atividades - CONECTADA AO BANCO
 */
public class TelaAtividades extends JFrame {
    
    private TelaPrincipal telaPrincipal;
    private JComboBox<String> cmbEvento, cmbTipo, cmbResponsavel;
    private JTextField txtTitulo, txtDataHoraInicio, txtDataHoraFim;
    private JTextArea txtDescricao;
    private JTable tableAtividades;
    private DefaultTableModel tableModel;
    private AtividadeDAO atividadeDAO;
    private EventoDAO eventoDAO;
    private ParticipanteDAO participanteDAO;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar;
    private Atividade atividadeSelecionada = null;
    private List<Evento> eventos;
    private List<Participante> participantes;
    
    public TelaAtividades(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;
        this.atividadeDAO = new AtividadeDAO();
        this.eventoDAO = new EventoDAO();
        this.participanteDAO = new ParticipanteDAO();
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setTitle("Gerenciar Atividades");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(74, 144, 226));
        headerPanel.setPreferredSize(new Dimension(1300, 80));
        JLabel titleLabel = new JLabel("SISTEMA DE GERENCIAMENTO DE EVENTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // === FORMULÁRIO ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        JLabel formTitle = new JLabel("Cadastrar Nova Atividade");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Selecionar Evento
        JLabel lblEvento = new JLabel("Selecionar Evento *");
        lblEvento.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblEvento);
        
        cmbEvento = new JComboBox<>();
        cmbEvento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbEvento.setBackground(new Color(236, 240, 241));
        cmbEvento.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(cmbEvento);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Título da Atividade *", txtTitulo = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel lblDesc = new JLabel("Descrição *");
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblDesc);
        txtDescricao = new JTextArea(2, 20);
        txtDescricao.setBackground(new Color(236, 240, 241));
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        formPanel.add(scrollDesc);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Data/Hora Início (AAAA-MM-DD HH:MM) *", txtDataHoraInicio = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Data/Hora Fim (AAAA-MM-DD HH:MM) *", txtDataHoraFim = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel lblTipo = new JLabel("Tipo de Atividade *");
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblTipo);
        
        String[] tipos = {"Selecione...", "Workshop", "Palestra", "Oficina"};
        cmbTipo = new JComboBox<>(tipos);
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbTipo.setBackground(new Color(236, 240, 241));
        cmbTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(cmbTipo);
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel lblResp = new JLabel("Responsável (Opcional)");
        lblResp.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblResp);
        
        cmbResponsavel = new JComboBox<>();
        cmbResponsavel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbResponsavel.setBackground(new Color(236, 240, 241));
        cmbResponsavel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(cmbResponsavel);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarAtividade());
        
        btnEditar = new JButton("Atualizar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setVisible(false);
        btnEditar.addActionListener(e -> atualizarAtividade());
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setVisible(false);
        btnCancelar.addActionListener(e -> cancelarEdicao());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnCancelar);
        formPanel.add(buttonPanel);
        
        // === TABELA ===
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        
        JLabel tableTitle = new JLabel("Atividades Cadastradas");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        String[] colunas = {"ID", "Título", "Evento", "Data/Hora Início", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableAtividades = new JTable(tableModel);
        tableAtividades.setRowHeight(30);
        tableAtividades.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableAtividades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableAtividades.getTableHeader().setBackground(new Color(52, 73, 94));
        tableAtividades.getTableHeader().setForeground(Color.WHITE);
        tableAtividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTable = new JScrollPane(tableAtividades);
        tablePanel.add(scrollTable, BorderLayout.CENTER);
        
        // Botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnEditarSel = new JButton("Editar Selecionado");
        btnEditarSel.setBackground(new Color(52, 152, 219));
        btnEditarSel.setForeground(Color.WHITE);
        btnEditarSel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarSel.setFocusPainted(false);
        btnEditarSel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarSel.addActionListener(e -> prepararEdicao());
        
        btnExcluir = new JButton("Excluir Selecionado");
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirAtividade());
        
        actionPanel.add(btnEditarSel);
        actionPanel.add(btnExcluir);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(formPanel);
        mainPanel.add(tablePanel);
        
        // Rodapé
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.setPreferredSize(new Dimension(1300, 40));
        
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setBackground(new Color(52, 152, 219));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> voltarMenu());
        footerPanel.add(btnVoltar);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarCampo(String label, JTextField campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campo.setBackground(new Color(236, 240, 241));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(campo);
        
        return panel;
    }
    
    private void carregarDados() {
        // Carregar eventos
        cmbEvento.removeAllItems();
        cmbEvento.addItem("Selecione um evento...");
        eventos = eventoDAO.listarTodos();
        for (Evento e : eventos) {
            cmbEvento.addItem(e.getId() + " - " + e.getNome());
        }
        
        // Carregar participantes
        cmbResponsavel.removeAllItems();
        cmbResponsavel.addItem("Nenhum responsável");
        participantes = participanteDAO.listarTodos();
        for (Participante p : participantes) {
            cmbResponsavel.addItem(p.getId() + " - " + p.getNome());
        }
        
        // Carregar atividades
        carregarAtividades();
    }
    
    private void carregarAtividades() {
        tableModel.setRowCount(0);
        List<Atividade> atividades = atividadeDAO.listarTodos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Atividade a : atividades) {
            // Buscar nome do evento
            Evento evento = eventoDAO.buscarPorId(a.getEventoId());
            String eventoNome = evento != null ? evento.getNome() : "Evento não encontrado";
            
            Object[] row = {
                a.getId(),
                a.getTitulo(),
                eventoNome,
                a.getDataHoraInicio().format(formatter),
                a.getTipo().getDescricao()
            };
            tableModel.addRow(row);
        }
    }
    
    private void salvarAtividade() {
        if (!validarCampos()) return;
        
        try {
            Atividade atividade = new Atividade();
            atividade.setTitulo(txtTitulo.getText().trim());
            atividade.setDescricao(txtDescricao.getText().trim());
            
            // Parse de data/hora
            String dataHoraInicio = txtDataHoraInicio.getText().trim() + ":00";
            String dataHoraFim = txtDataHoraFim.getText().trim() + ":00";
            atividade.setDataHoraInicio(LocalDateTime.parse(dataHoraInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            atividade.setDataHoraFim(LocalDateTime.parse(dataHoraFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Tipo
            String tipoStr = ((String) cmbTipo.getSelectedItem()).toUpperCase();
            atividade.setTipo(TipoAtividade.valueOf(tipoStr));
            
            // Evento
            String eventoSel = (String) cmbEvento.getSelectedItem();
            int eventoId = Integer.parseInt(eventoSel.split(" - ")[0]);
            atividade.setEventoId(eventoId);
            
            // Responsável (opcional)
            if (cmbResponsavel.getSelectedIndex() > 0) {
                String respSel = (String) cmbResponsavel.getSelectedItem();
                int respId = Integer.parseInt(respSel.split(" - ")[0]);
                atividade.setResponsavelId(respId);
            }
            
            if (!atividade.validarHorarios()) {
                JOptionPane.showMessageDialog(this,
                    "Horários inválidos!\nO horário de fim deve ser posterior ao de início.",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (atividadeDAO.inserir(atividade)) {
                JOptionPane.showMessageDialog(this,
                    "Atividade cadastrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarAtividades();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar atividade!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Data/Hora inválida!\nUse o formato: AAAA-MM-DD HH:MM\nExemplo: 2025-01-20 14:00",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void prepararEdicao() {
        int row = tableAtividades.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma atividade na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        atividadeSelecionada = atividadeDAO.buscarPorId(id);
        
        if (atividadeSelecionada != null) {
            txtTitulo.setText(atividadeSelecionada.getTitulo());
            txtDescricao.setText(atividadeSelecionada.getDescricao());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            txtDataHoraInicio.setText(atividadeSelecionada.getDataHoraInicio().format(formatter));
            txtDataHoraFim.setText(atividadeSelecionada.getDataHoraFim().format(formatter));
            
            cmbTipo.setSelectedItem(atividadeSelecionada.getTipo().getDescricao());
            
            // Selecionar evento
            for (int i = 0; i < cmbEvento.getItemCount(); i++) {
                String item = cmbEvento.getItemAt(i);
                if (item.startsWith(atividadeSelecionada.getEventoId() + " - ")) {
                    cmbEvento.setSelectedIndex(i);
                    break;
                }
            }
            
            // Selecionar responsável
            if (atividadeSelecionada.getResponsavelId() != null) {
                for (int i = 0; i < cmbResponsavel.getItemCount(); i++) {
                    String item = cmbResponsavel.getItemAt(i);
                    if (item.startsWith(atividadeSelecionada.getResponsavelId() + " - ")) {
                        cmbResponsavel.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                cmbResponsavel.setSelectedIndex(0);
            }
            
            btnSalvar.setVisible(false);
            btnEditar.setVisible(true);
            btnCancelar.setVisible(true);
        }
    }
    
    private void atualizarAtividade() {
        if (!validarCampos()) return;
        
        try {
            atividadeSelecionada.setTitulo(txtTitulo.getText().trim());
            atividadeSelecionada.setDescricao(txtDescricao.getText().trim());
            
            String dataHoraInicio = txtDataHoraInicio.getText().trim() + ":00";
            String dataHoraFim = txtDataHoraFim.getText().trim() + ":00";
            atividadeSelecionada.setDataHoraInicio(LocalDateTime.parse(dataHoraInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            atividadeSelecionada.setDataHoraFim(LocalDateTime.parse(dataHoraFim, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            String tipoStr = ((String) cmbTipo.getSelectedItem()).toUpperCase();
            atividadeSelecionada.setTipo(TipoAtividade.valueOf(tipoStr));
            
            String eventoSel = (String) cmbEvento.getSelectedItem();
            int eventoId = Integer.parseInt(eventoSel.split(" - ")[0]);
            atividadeSelecionada.setEventoId(eventoId);
            
            if (cmbResponsavel.getSelectedIndex() > 0) {
                String respSel = (String) cmbResponsavel.getSelectedItem();
                int respId = Integer.parseInt(respSel.split(" - ")[0]);
                atividadeSelecionada.setResponsavelId(respId);
            } else {
                atividadeSelecionada.setResponsavelId(null);
            }
            
            if (atividadeDAO.atualizar(atividadeSelecionada)) {
                JOptionPane.showMessageDialog(this,
                    "Atividade atualizada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                cancelarEdicao();
                carregarAtividades();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar atividade!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirAtividade() {
        int row = tableAtividades.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma atividade na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir esta atividade?\n" +
            "Esta ação NÃO pode ser desfeita!",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            
            if (atividadeDAO.excluir(id)) {
                JOptionPane.showMessageDialog(this,
                    "Atividade excluída com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                carregarAtividades();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir atividade!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEdicao() {
        atividadeSelecionada = null;
        limparCampos();
        btnSalvar.setVisible(true);
        btnEditar.setVisible(false);
        btnCancelar.setVisible(false);
    }
    
    private boolean validarCampos() {
        if (cmbEvento.getSelectedIndex() == 0 ||
            txtTitulo.getText().trim().isEmpty() ||
            txtDescricao.getText().trim().isEmpty() ||
            txtDataHoraInicio.getText().trim().isEmpty() ||
            txtDataHoraFim.getText().trim().isEmpty() ||
            cmbTipo.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this,
                "Todos os campos obrigatórios devem ser preenchidos!",
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void limparCampos() {
        cmbEvento.setSelectedIndex(0);
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtDataHoraInicio.setText("");
        txtDataHoraFim.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbResponsavel.setSelectedIndex(0);
        cmbEvento.requestFocus();
    }
    
    private void voltarMenu() {
        telaPrincipal.setVisible(true);
        this.dispose();
    }
}