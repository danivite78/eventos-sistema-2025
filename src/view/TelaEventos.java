package view;

import dao.EventoDAO;
import model.Evento;
import util.ConexaoBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela de Gerenciamento de Eventos - CONECTADA AO BANCO
 */
public class TelaEventos extends JFrame {
    
    private TelaPrincipal telaPrincipal;
    private JTextField txtNome, txtLocal, txtDataInicio, txtDataFim;
    private JTextArea txtDescricao;
    private JTable tableEventos;
    private DefaultTableModel tableModel;
    private EventoDAO eventoDAO;
    private JButton btnSalvar, btnEditar, btnExcluir, btnCancelar;
    private Evento eventoSelecionado = null;
    
    public TelaEventos(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;
        this.eventoDAO = new EventoDAO();
        initComponents();
        carregarEventos();
        
        // Testar conexão
        if (!ConexaoBD.testarConexao()) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao conectar com o banco de dados!\nVerifique se o MySQL está rodando.",
                "Erro de Conexão", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setTitle("Gerenciar Eventos");
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
        
        JLabel formTitle = new JLabel("Cadastrar Novo Evento");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(criarCampo("Nome do Evento *", txtNome = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        JLabel lblDesc = new JLabel("Descrição *");
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblDesc);
        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setBackground(new Color(236, 240, 241));
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        formPanel.add(scrollDesc);
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Data de Início (AAAA-MM-DD) *", txtDataInicio = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Data de Fim (AAAA-MM-DD) *", txtDataFim = new JTextField()));
        formPanel.add(Box.createVerticalStrut(10));
        
        formPanel.add(criarCampo("Local *", txtLocal = new JTextField()));
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
        btnSalvar.addActionListener(e -> salvarEvento());
        
        btnEditar = new JButton("Atualizar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setVisible(false);
        btnEditar.addActionListener(e -> atualizarEvento());
        
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
        
        JLabel tableTitle = new JLabel("Eventos Cadastrados");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        
        String[] colunas = {"ID", "Nome", "Local", "Data Início", "Data Fim"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEventos = new JTable(tableModel);
        tableEventos.setRowHeight(30);
        tableEventos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableEventos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableEventos.getTableHeader().setBackground(new Color(52, 73, 94));
        tableEventos.getTableHeader().setForeground(Color.WHITE);
        tableEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTable = new JScrollPane(tableEventos);
        tablePanel.add(scrollTable, BorderLayout.CENTER);
        
        // Botões de ação na tabela
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        
        btnExcluir = new JButton("Excluir Selecionado");
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirEvento());
        
        JButton btnEditarSel = new JButton("Editar Selecionado");
        btnEditarSel.setBackground(new Color(52, 152, 219));
        btnEditarSel.setForeground(Color.WHITE);
        btnEditarSel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarSel.setFocusPainted(false);
        btnEditarSel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditarSel.addActionListener(e -> prepararEdicao());
        
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
    
    private void carregarEventos() {
        tableModel.setRowCount(0);
        List<Evento> eventos = eventoDAO.listarTodos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Evento e : eventos) {
            Object[] row = {
                e.getId(),
                e.getNome(),
                e.getLocal(),
                e.getDataInicio().format(formatter),
                e.getDataFim().format(formatter)
            };
            tableModel.addRow(row);
        }
    }
    
    private void salvarEvento() {
        if (!validarCampos()) return;
        
        try {
            Evento evento = new Evento();
            evento.setNome(txtNome.getText().trim());
            evento.setDescricao(txtDescricao.getText().trim());
            evento.setDataInicio(LocalDate.parse(txtDataInicio.getText().trim()));
            evento.setDataFim(LocalDate.parse(txtDataFim.getText().trim()));
            evento.setLocal(txtLocal.getText().trim());
            
            if (!evento.validarDatas()) {
                JOptionPane.showMessageDialog(this,
                    "Datas inválidas!\n" +
                    "• Data de início deve ser hoje ou futura\n" +
                    "• Data de fim deve ser igual ou após data de início",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (eventoDAO.inserir(evento)) {
                JOptionPane.showMessageDialog(this,
                    "Evento cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar evento!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Data inválida!\nUse o formato: AAAA-MM-DD\nExemplo: 2025-01-20",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void prepararEdicao() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um evento na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        eventoSelecionado = eventoDAO.buscarPorId(id);
        
        if (eventoSelecionado != null) {
            txtNome.setText(eventoSelecionado.getNome());
            txtDescricao.setText(eventoSelecionado.getDescricao());
            txtDataInicio.setText(eventoSelecionado.getDataInicio().toString());
            txtDataFim.setText(eventoSelecionado.getDataFim().toString());
            txtLocal.setText(eventoSelecionado.getLocal());
            
            btnSalvar.setVisible(false);
            btnEditar.setVisible(true);
            btnCancelar.setVisible(true);
        }
    }
    
    private void atualizarEvento() {
        if (!validarCampos()) return;
        
        try {
            eventoSelecionado.setNome(txtNome.getText().trim());
            eventoSelecionado.setDescricao(txtDescricao.getText().trim());
            eventoSelecionado.setDataInicio(LocalDate.parse(txtDataInicio.getText().trim()));
            eventoSelecionado.setDataFim(LocalDate.parse(txtDataFim.getText().trim()));
            eventoSelecionado.setLocal(txtLocal.getText().trim());
            
            if (eventoDAO.atualizar(eventoSelecionado)) {
                JOptionPane.showMessageDialog(this,
                    "Evento atualizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                cancelarEdicao();
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar evento!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Data inválida! Use: AAAA-MM-DD",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirEvento() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um evento na tabela!",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este evento?\n" +
            "Esta ação NÃO pode ser desfeita!\n" +
            "Todas as atividades vinculadas também serão excluídas.",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            
            if (eventoDAO.excluir(id)) {
                JOptionPane.showMessageDialog(this,
                    "Evento excluído com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                carregarEventos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir evento!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEdicao() {
        eventoSelecionado = null;
        limparCampos();
        btnSalvar.setVisible(true);
        btnEditar.setVisible(false);
        btnCancelar.setVisible(false);
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() ||
            txtDescricao.getText().trim().isEmpty() ||
            txtDataInicio.getText().trim().isEmpty() ||
            txtDataFim.getText().trim().isEmpty() ||
            txtLocal.getText().trim().isEmpty()) {
            
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
        txtDescricao.setText("");
        txtDataInicio.setText("");
        txtDataFim.setText("");
        txtLocal.setText("");
        txtNome.requestFocus();
    }
    
    private void voltarMenu() {
        telaPrincipal.setVisible(true);
        this.dispose();
    }
}