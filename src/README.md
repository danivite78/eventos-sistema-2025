Sistema de Gerenciamento de Eventos


Sobre o Projeto - Sistema desktop desenvolvido em Java para gerenciamento completo de eventos acadêmicos e corporativos. Permite o cadastro e controle de eventos, participantes e atividades com interface gráfica intuitiva e banco de dados MySQL.


Status do Projeto - Em Desenvolvimento - Versão 1.0

- Etapa 1: Modelagem de dados e protótipo console
- Etapa 2: Design UX/UI
- Etapa 3: Interface gráfica com Swing
- Etapa 4: Integração com banco de dados MySQL
- Etapa 5: Versionamento com Git/GitHub


Tecnologias Utilizadas

Backend
- Java 8+ - Linguagem de programação
- JDBC - Conexão com banco de dados
- MySQL 8.0 - Sistema gerenciador de banco de dados

Frontend
- Java Swing - Interface gráfica desktop
- AWT - Componentes visuais

Ferramentas
- NetBeans IDE - Ambiente de desenvolvimento
- MySQL Workbench - Gerenciamento de banco de dados
- GitHub Desktop - Versionamento de código
- Figma - Prototipação de interfaces


Time de Desenvolvedores

- Danielle Vitelbo Aparicio
- Projeto Individual - Desenvolvido como trabalho acadêmico


Objetivo do Software

O Sistema de Gerenciamento de Eventos foi desenvolvido para:

- Facilitar o planejamento e organização de eventos acadêmicos e corporativos
- Centralizar informações de eventos, participantes e atividades em um único sistema
- Automatizar processos de cadastro e controle de participantes
- Otimizar a gestão de workshops, palestras e oficinas
- Fornecer interface intuitiva para usuários sem conhecimento técnico



Funcionalidades do Sistema

Gestão de Eventos
- Cadastrar novos eventos
- Listar todos os eventos cadastrados
- Editar informações de eventos existentes
- Excluir eventos
- Validação de datas (início e fim)
- Visualização em formato de tabela

Gestão de Participantes
- Cadastrar participantes (Estudantes, Palestrantes, Convidados)
- Listar todos os participantes
- Editar dados de participantes
- Excluir participantes
- Validação de email único
- Validação de formato de telefone (XX)XXXXX-XXXX
- Categorização por tipo de participante

Gestão de Atividades
- Cadastrar atividades vinculadas a eventos
- Tipos de atividade: Workshop, Palestra, Oficina
- Associar responsável (participante) à atividade
- Definir data/hora de início e fim
- Editar e excluir atividades
- Validação de horários

Segurança e Validações
- Validação de campos obrigatórios
- Validação de formatos (email, telefone, datas)
- Confirmação antes de exclusões
- Mensagens de erro e sucesso claras
- Tratamento de exceções

Persistência de Dados
- Conexão com MySQL via JDBC
- CRUD completo (Create, Read, Update, Delete)
- Integridade referencial (Foreign Keys)
- Transações seguras
- Pool de conexões


Estrutura do Banco de Dados

Tabelas

1. evento: id, nome, descricao, data_inicio, data_fim, local

2. participante: id, nome, email, telefone, tipo (ENUM)

3. atividade: id, titulo, descricao, data_hora_inicio, data_hora_fim, tipo (ENUM), evento_id, responsavel_id

4. evento_participante: id, evento_id, participante_id (relação N:N)

Relacionamentos
- 1 Evento possui N Atividades (1:N)
- 1 Participante pode ser responsável por N Atividades (1:N)
- N Eventos podem ter N Participantes (N:N)


Estrutura do Projeto

```
SistemaEventos/
├── src/
│   ├── model/
│   │   ├── Evento.java
│   │   ├── Participante.java
│   │   └── Atividade.java
│   ├── dao/
│   │   ├── EventoDAO.java
│   │   ├── ParticipanteDAO.java
│   │   └── AtividadeDAO.java
│   ├── util/
│   │   └── ConexaoBD.java
│   └── view/
│       ├── TelaPrincipal.java
│       ├── TelaEventos.java
│       ├── TelaParticipantes.java
│       └── TelaAtividades.java
├── lib/
│   └── mysql-connector-j-8.x.x.jar
├── database/
│   └── script_banco_dados.sql
├── docs/
│   ├── DOCUMENTO_PROJETO_JAVA.md
│   ├── DOCUMENTO_UX_UI.md
│   └── DIAGRAMA_UML.png
├── .gitignore
└── README.md


Como Executar o Projeto

Pré-requisitos
- Java JDK 8 ou superior
- MySQL 8.0 ou superior
- NetBeans IDE (ou outra IDE Java)

Passo 1: Clonar o Repositório
bash
git clone https://github.com/seu-usuario/sistema-eventos.git
cd sistema-eventos


Passo 2: Configurar o Banco de Dados
1. Abra o MySQL Workbench
2. Execute o script `database/script_banco_dados.sql`
3. Verifique se o banco `sistema_eventos` foi criado

Passo 3: Configurar a Conexão
Edite o arquivo `src/util/ConexaoBD.java` e ajuste a senha do MySQL:
java
private static final String SENHA = "sua_senha_mysql";


Passo 4: Adicionar Biblioteca MySQL
1. No NetBeans, clique com botão direito em "Libraries"
2. Escolha "Add JAR/Folder"
3. Selecione "lib/mysql-connector-j-8.x.x.jar"

Passo 5: Executar
1. Abra o projeto no NetBeans
2. Clique com botão direito em "TelaPrincipal.java"
3. Escolha "Run File"

Regras de Negócio
RN01 - Validação de Datas de Evento
- Data de início deve ser igual ou posterior à data atual
- Data de fim deve ser igual ou posterior à data de início

RN02 - Email Único de Participante
- Não pode haver dois participantes com o mesmo email

RN03 - Formato de Telefone
- Telefone deve seguir o padrão (XX)XXXXX-XXXX

RN04 - Validação de Horários de Atividade
- Horário de início deve ser anterior ao horário de fim
- Atividade deve estar dentro do período do evento

RN05 - Exclusão em Cascata
- Ao excluir um evento, todas as atividades vinculadas são excluídas
- Ao excluir um participante responsável, o campo responsável_id é definido como NULL


Testes Realizados

- Cadastro de eventos com validações
- Edição de eventos existentes
- Exclusão de eventos com confirmação
- Cadastro de participantes com email único
- Validação de telefone e email
- Cadastro de atividades vinculadas a eventos
- Associação de responsáveis às atividades
- Exclusão em cascata
- Tratamento de erros de conexão


Histórico de Versões
v1.0.0 (Dezembro 2025)
- Sistema completo com CRUD para todas as entidades
- Interface gráfica Swing
- Integração com MySQL
- Validações completas
- Versionamento com Git


Licença - Este projeto foi desenvolvido para fins acadêmicos.


Contato:
Desenvolvedora: Danielle Vitelbo Aparicio  
Email: danivite78@gmail.com  
GitHub: https://github.com/danivite78


