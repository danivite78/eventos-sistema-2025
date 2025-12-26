package view;

import dao.ParticipanteDAO;
import model.Participante;
import model.Participante.TipoParticipante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de Gerenciamento de Participantes - CONECTADA AO BANCO
 */
public class TelaParticipantes extends JFrame {
    
    private TelaPrincipal telaPrincipal;
    private JTextField txtNome, txtEmail, txtTelefone;
    private JComboBox<String> cmbTipo;
    private JTable tableParticipantes;
    private DefaultTableModel tableModel;
    private ParticipanteDAO participanteDAO;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar;
    private Participante participanteSelecionado = null;
    
    public TelaParticipantes(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;
        this.participanteDAO = new ParticipanteDAO();
        initComponents();
        carregarParticipantes();
    }
    
    private void initComponents() {
        setTitle("Gerenciar Participantes");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(74, 144, 226));
        headerPanel.setPreferredSize(new Dimension(1200, 80));
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
        
        JLabel formTitle = new JLabel("Cadastrar Novo Participante");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(criarCampo("Nome Completo *", txtNome = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Email *", txtEmail = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Telefone (XX)XXXXX-XXXX *", txtTelefone = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel lblTipo = new JLabel("Tipo de Participante *");
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblTipo);
        
        String[] tipos = {"Selecione...", "Estudante", "Palestrante", "Convidado"};
        cmbTipo = new JComboBox<>(tipos);
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbTipo.setBackground(new Color(236, 240, 241));
        cmbTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(cmbTipo);
        formPanel.add(Box.createVerticalStrut(20));
        
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
        btnSalvar.addActionListener(e -> salvarParticipante());
        
        btnEditar = new JButton("Atualizar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setVisible(false);
        btnEditar.addActionListener(e -> atualizarParticipante());
        
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
        
        JLabel tableTitle = new JLabel("Participantes Cadastrados");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        String[] colunas = {"ID", "Nome", "Email", "Telefone", "Tipo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableParticipantes = new JTable(tableModel);
        tableParticipantes.setRowHeight(30);
        tableParticipantes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableParticipantes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableParticipantes.getTableHeader().setBackground(new Color(52, 73, 94));
        tableParticipantes.getTableHeader().setForeground(Color.WHITE);
        tableParticipantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTable = new JScrollPane(tableParticipantes);
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
        btnExcluir.addActionListener(e -> excluirParticipante());
        
        actionPanel.add(btnEditarSel);
        actionPanel.add(btnExcluir);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(formPanel);
        mainPanel.add(tablePanel);
        
        // Rodapé
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.setPreferredSize(new Dimension(1200, 40));
        
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
    
    private void carregarParticipantes() {
        tableModel.setRowCount(0);
        List<Participante> participantes = participanteDAO.listarTodos();
        
        for (Participante p : participantes) {
            Object[] row = {
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getTelefone(),
                p.getTipo().getDescricao()
            };
            tableModel.addRow(row);
        }
    }
    
    private void salvarParticipante() {
        if (!validarCampos()) return;
        
        Participante participante = new Participante();
        participante.setNome(txtNome.getText().trim());
        participante.setEmail(txtEmail.getText().trim());
        participante.setTelefone(txtTelefone.getText().trim());
        
        String tipoStr = ((String) cmbTipo.getSelectedItem()).toUpperCase();
        participante.setTipo(TipoParticipante.valueOf(tipoStr));
        
        if (!participante.validarEmail()) {
            JOptionPane.showMessageDialog(this,
                "Email inválido!\nUse o formato: email@exemplo.com",
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!participante.validarTelefone()) {
            JOptionPane.showMessageDialog(this,
                "Telefone inválido!\nUse o formato: (XX)XXXXX-XXXX",
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (participanteDAO.emailExiste(participante.getEmail())) {
            JOptionPane.showMessageDialog(this,
                "Este email já está cadastrado!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (participanteDAO.inserir(participante)) {
            JOptionPane.showMessageDialog(this,
                "Participante cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarParticipantes();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erro ao cadastrar participante!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void prepararEdicao() {
        int row = tableParticipantes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um participante na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        participanteSelecionado = participanteDAO.buscarPorId(id);
        
        if (participanteSelecionado != null) {
            txtNome.setText(participanteSelecionado.getNome());
            txtEmail.setText(participanteSelecionado.getEmail());
            txtTelefone.setText(participanteSelecionado.getTelefone());
            cmbTipo.setSelectedItem(participanteSelecionado.getTipo().getDescricao());
            
            btnSalvar.setVisible(false);
            btnEditar.setVisible(true);
            btnCancelar.setVisible(true);
        }
    }
    
    private void atualizarParticipante() {
        if (!validarCampos()) return;
        
        participanteSelecionado.setNome(txtNome.getText().trim());
        participanteSelecionado.setEmail(txtEmail.getText().trim());
        participanteSelecionado.setTelefone(txtTelefone.getText().trim());
        
        String tipoStr = ((String) cmbTipo.getSelectedItem()).toUpperCase();
        participanteSelecionado.setTipo(TipoParticipante.valueOf(tipoStr));
        
        if (participanteDAO.atualizar(participanteSelecionado)) {
            JOptionPane.showMessageDialog(this,
                "Participante atualizado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            cancelarEdicao();
            carregarParticipantes();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erro ao atualizar participante!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirParticipante() {
        int row = tableParticipantes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um participante na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este participante?\n" +
            "Esta ação NÃO pode ser desfeita!",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            
            if (participanteDAO.excluir(id)) {
                JOptionPane.showMessageDialog(this,
                    "Participante excluído com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                carregarParticipantes();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir participante!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEdicao() {
        participanteSelecionado = null;
        limparCampos();
        btnSalvar.setVisible(true);
        btnEditar.setVisible(false);
        btnCancelar.setVisible(false);
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtTelefone.getText().trim().isEmpty() ||
            cmbTipo.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this,
                "Todos os campos são obrigatórios!",
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        cmbTipo.setSelectedIndex(0);
        txtNome.requestFocus();
    }
    
    private void voltarMenu() {
        telaPrincipal.setVisible(true);
        this.dispose();
    }
}