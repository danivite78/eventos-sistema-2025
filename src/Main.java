import dao.EventoDAO;
import model.Evento;
import util.ConexaoBD;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal do sistema - Protótipo funcional
 * @author Seu Nome
 */
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EventoDAO eventoDAO = new EventoDAO();
        
        System.out.println("========================================");
        System.out.println("  SISTEMA DE GERENCIAMENTO DE EVENTOS  ");
        System.out.println("========================================");
        System.out.println();
        
        // Testar conexão com banco de dados
        System.out.println("Testando conexão com o banco de dados...");
        if (ConexaoBD.testarConexao()) {
            System.out.println("? Conexão estabelecida com sucesso!");
        } else {
            System.out.println("? Erro ao conectar ao banco de dados!");
            System.out.println("Verifique as configurações em ConexaoBD.java");
            return;
        }
        
        System.out.println();
        System.out.println("========================================");
        System.out.println("  PROTÓTIPO: CADASTRO DE EVENTO        ");
        System.out.println("========================================");
        System.out.println();
        
        // Protótipo da funcionalidade de cadastro de evento
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Cadastrar novo evento");
            System.out.println("2. Listar todos os eventos");
            System.out.println("3. Buscar evento por ID");
            System.out.println("4. Excluir evento");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch (opcao) {
                case 1:
                    cadastrarEvento(scanner, eventoDAO);
                    break;
                    
                case 2:
                    listarEventos(eventoDAO);
                    break;
                    
                case 3:
                    buscarEvento(scanner, eventoDAO);
                    break;
                    
                case 4:
                    excluirEvento(scanner, eventoDAO);
                    break;
                    
                case 0:
                    continuar = false;
                    System.out.println("\nEncerrando sistema...");
                    break;
                    
                default:
                    System.out.println("\nOpção inválida!");
            }
        }
        
        scanner.close();
        System.out.println("Sistema encerrado com sucesso!");
    }
    
    /**
     * Protótipo: Cadastrar novo evento
     */
    private static void cadastrarEvento(Scanner scanner, EventoDAO eventoDAO) {
        System.out.println("\n--- CADASTRAR NOVO EVENTO ---");
        
        try {
            System.out.print("Nome do evento: ");
            String nome = scanner.nextLine();
            
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            
            System.out.print("Data de início (AAAA-MM-DD): ");
            String dataInicioStr = scanner.nextLine();
            LocalDate dataInicio = LocalDate.parse(dataInicioStr);
            
            System.out.print("Data de fim (AAAA-MM-DD): ");
            String dataFimStr = scanner.nextLine();
            LocalDate dataFim = LocalDate.parse(dataFimStr);
            
            System.out.print("Local: ");
            String local = scanner.nextLine();
            
            // Criar objeto Evento
            Evento evento = new Evento(nome, descricao, dataInicio, dataFim, local);
            
            // Validar datas
            if (!evento.validarDatas()) {
                System.out.println("\n? ERRO: Datas inválidas!");
                System.out.println("  - Data de início não pode ser anterior à data atual");
                System.out.println("  - Data de fim deve ser igual ou posterior à data de início");
                return;
            }
            
            // Inserir no banco de dados
            if (eventoDAO.inserir(evento)) {
                System.out.println("\n? Evento cadastrado com sucesso!");
            } else {
                System.out.println("\n? Erro ao cadastrar evento!");
            }
            
        } catch (Exception e) {
            System.out.println("\n? Erro: " + e.getMessage());
            System.out.println("Verifique o formato dos dados informados.");
        }
    }
    
    /**
     * Protótipo: Listar todos os eventos
     */
    private static void listarEventos(EventoDAO eventoDAO) {
        System.out.println("\n--- LISTA DE EVENTOS ---");
        
        List<Evento> eventos = eventoDAO.listarTodos();
        
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        } else {
            System.out.println("\nTotal de eventos: " + eventos.size());
            System.out.println("-----------------------------------");
            
            for (Evento evento : eventos) {
                System.out.println("\nID: " + evento.getId());
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Local: " + evento.getLocal());
                System.out.println("Período: " + evento.getDataInicio() + " a " + evento.getDataFim());
                System.out.println("Descrição: " + evento.getDescricao());
                System.out.println("-----------------------------------");
            }
        }
    }
    
    /**
     * Protótipo: Buscar evento por ID
     */
    private static void buscarEvento(Scanner scanner, EventoDAO eventoDAO) {
        System.out.println("\n--- BUSCAR EVENTO ---");
        System.out.print("Digite o ID do evento: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        
        Evento evento = eventoDAO.buscarPorId(id);
        
        if (evento != null) {
            System.out.println("\n? Evento encontrado:");
            System.out.println("ID: " + evento.getId());
            System.out.println("Nome: " + evento.getNome());
            System.out.println("Local: " + evento.getLocal());
            System.out.println("Período: " + evento.getDataInicio() + " a " + evento.getDataFim());
            System.out.println("Descrição: " + evento.getDescricao());
        } else {
            System.out.println("\n? Evento não encontrado!");
        }
    }
    
    /**
     * Protótipo: Excluir evento
     */
    private static void excluirEvento(Scanner scanner, EventoDAO eventoDAO) {
        System.out.println("\n--- EXCLUIR EVENTO ---");
        System.out.print("Digite o ID do evento a ser excluído: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        
        // Buscar evento antes de excluir
        Evento evento = eventoDAO.buscarPorId(id);
        
        if (evento == null) {
            System.out.println("\n? Evento não encontrado!");
            return;
        }
        
        System.out.println("\nEvento a ser excluído:");
        System.out.println("Nome: " + evento.getNome());
        System.out.println("Local: " + evento.getLocal());
        System.out.print("\nConfirma exclusão? (S/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            if (eventoDAO.excluir(id)) {
                System.out.println("\n? Evento excluído com sucesso!");
            } else {
                System.out.println("\n? Erro ao excluir evento!");
            }
        } else {
            System.out.println("\nExclusão cancelada.");
        }
    }
}