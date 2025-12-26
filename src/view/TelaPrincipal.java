package view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela Principal - Menu do Sistema
 */
public class TelaPrincipal extends JFrame {
    
    public TelaPrincipal() {
        initComponents();
    }
    
    private void initComponents() {
        // Configura  es da janela
        setTitle("Sistema de Gerenciamento de Eventos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Cabe alho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(74, 144, 226)); // Azul
        headerPanel.setPreferredSize(new Dimension(800, 80));
        JLabel titleLabel = new JLabel("SISTEMA DE GERENCIAMENTO DE EVENTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Painel central com bot es
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // T tulo de boas-vindas
        JLabel welcomeLabel = new JLabel("Bem-vindo ao Sistema!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(welcomeLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Escolha uma das op  es abaixo:", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridy = 1;
        centerPanel.add(subtitleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Bot o Eventos
        JButton btnEventos = criarBotao("Eventos", "Gerenciar eventos, datas e locais");
        gbc.gridy = 2;
        centerPanel.add(btnEventos, gbc);
        btnEventos.addActionListener(e -> abrirTelaEventos());
        
        // Bot o Participantes
        JButton btnParticipantes = criarBotao("Participantes", "Cadastrar estudantes, palestrantes e convidados");
        gbc.gridy = 3;
        centerPanel.add(btnParticipantes, gbc);
        btnParticipantes.addActionListener(e -> abrirTelaParticipantes());
        
        // Bot o Atividades
        JButton btnAtividades = criarBotao("Atividades", "Organizar workshops, palestras e oficinas");
        gbc.gridy = 4;
        centerPanel.add(btnAtividades, gbc);
        btnAtividades.addActionListener(e -> abrirTelaAtividades());
        
        // Bot o Sair
        JButton btnSair = criarBotao("Sair", "Fechar o sistema");
        btnSair.setBackground(new Color(231, 76, 60)); // Vermelho
        gbc.gridy = 5;
        centerPanel.add(btnSair, gbc);
        btnSair.addActionListener(e -> System.exit(0));
        
        // Rodap 
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.setPreferredSize(new Dimension(800, 40));
        JLabel footerLabel = new JLabel("Sistema de Eventos   2025 - v1.1");
        footerLabel.setForeground(new Color(52, 73, 94));
        footerPanel.add(footerLabel);
        
        // Adicionar pain is   janela
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton criarBotao(String texto, String descricao) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(400, 80));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(52, 73, 94)); // Cinza escuro
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(400, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel descLabel = new JLabel(descricao, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        
        buttonPanel.add(button, BorderLayout.CENTER);
        buttonPanel.add(descLabel, BorderLayout.SOUTH);
        
        return button;
    }
    
    private void abrirTelaEventos() {
        TelaEventos tela = new TelaEventos(this);
        tela.setVisible(true);
        this.setVisible(false);
    }
    
    private void abrirTelaParticipantes() {
        TelaParticipantes tela = new TelaParticipantes(this);
        tela.setVisible(true);
        this.setVisible(false);
    }
    
    private void abrirTelaAtividades() {
        TelaAtividades tela = new TelaAtividades(this);
        tela.setVisible(true);
        this.setVisible(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}