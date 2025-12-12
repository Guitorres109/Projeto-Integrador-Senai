// Importações de bibliotecas necessárias para o funcionamento do servidor HTTP, leitura e gravação de dados
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;                                       // Para manipulação de arquivos
import java.net.InetSocketAddress;                      // Para definir o endereço do servidor
import java.net.URLDecoder;                             // Para decodificar parâmetros URL
import java.nio.charset.StandardCharsets;               // Para trabalhar com codificação de caracteres
import java.sql.*;                                      // Para manipulação do banco de dados

public class Servidor {

    private static Connection con;
    public static String usuarioDigitado = "";
    public static String erro;
    public static void main(String[] args) throws Exception {

        // Conectar ao SQLite (arquivo conteudo.db na pasta do projeto)
        con = DriverManager.getConnection("jdbc:sqlite:conteudo.db");

        // Criar tabela no Banco de dados (se não existir)
        String sql = "CREATE TABLE IF NOT EXISTS dados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "descricao TEXT," +
                "data TEXT," +
                "curtida TEXT" +
                ")";
        con.createStatement().execute(sql);

        // Criar servidor HTTP
        HttpServer s = HttpServer.create(new InetSocketAddress(8082), 0);

        // Rotas básicas
        s.createContext("/", t -> enviar(t, "index.html"));
        s.createContext("/login", t -> enviar(t, "login.html")); // mostra login
        s.createContext("/cadastro", Servidor::cadastro);     // cadastro de atividades
        s.createContext("/aluno", Servidor::aluno); // lista atividades para o aluno
        s.createContext("/acesso-professor", Servidor::acesso_professor); // lista atividades para o professor
        s.createContext("/concluido_aluno", Servidor::concluido_aluno);       // Concluido
        s.createContext("/concluido_professor", Servidor::concluido_professor);       //Não
        s.createContext("/editar", Servidor::editar);       //editar atividades
        s.createContext("/excluir", Servidor::excluir);       //excluir atividades
        s.createContext("/Atividades", t -> enviar(t, "Atividades.html"));   // Pagina do aluno
        s.createContext("/verificar", Servidor::verificar);

        //Rotas de estilo
        s.createContext("/css/style.css", t -> enviarCSS(t, "css/style.css")); // CSS
        s.createContext("/css/login.css", t -> enviarCSS(t, "css/login.css")); // CSS
        s.createContext("/css/geral.css", t -> enviarCSS(t, "css/geral.css")); // CSS
        s.createContext("/css/atividades.css", t -> enviarCSS(t, "css/atividades.css")); // CSS
        s.createContext("/css/carregando.css", t -> enviarCSS(t, "css/carregando.css")); // CSS

        //Rotas de arquivos javascript
        s.createContext("/js/script.js", t -> enviar(t, "js/script.js")); //JS geral
        s.createContext("/js/login.js", t -> enviar(t, "js/login.js")); //JS login

        //Rotas de imagem
        s.createContext("/img/academyflow-logo.png", t -> enviarImagem(t, "img/AcademyFlow-logo.png")); // Imagem
        s.createContext("/academyflow-nome.png", t -> enviarImagem(t, "img/AcademyFlow-nome (1).png")); // Imagem
        s.createContext("/escola-primaria.jpg", t -> enviarImagem(t, "img/criancas-felizes-na-escola-primaria.jpg")); // Imagem
        s.createContext("/img/dark-theme.svg", t -> enviarImagem(t, "img/dark-theme.svg")); // Imagem
        s.createContext("/img/git-hub.png", t -> enviarImagem(t, "img/git-hub.png")); // Imagem
        s.createContext("/img/guilherme.png", t -> enviarImagem(t, "img/guilherme.png")); // Imagem
        s.createContext("/img/icon-apagar.svg", t -> enviarImagem(t, "img/icon-apagar.svg")); // Imagem
        s.createContext("/img/icon-feito.svg", t -> enviarImagem(t, "img/icon-feito.svg")); // Imagem
        s.createContext("/img/instagram.png", t -> enviarImagem(t, "img/instagram.png")); // Imagem
        s.createContext("/img/joao.png", t -> enviarImagem(t, "img/joao.png")); // Imagem
        s.createContext("/laguna-school.png", t -> enviarImagem(t, "img/LagunaSchool.png")); // Imagem
        s.createContext("/img/light-theme.svg", t -> enviarImagem(t, "img/light-theme.svg")); // Imagem
        s.createContext("/logo-laguna.png", t -> enviarImagem(t, "img/logo.png")); // Imagem
        s.createContext("/img/nicolas.png", t -> enviarImagem(t, "img/nicolas.png")); // Imagem
        s.createContext("/img/pietro.png", t -> enviarImagem(t, "img/pietro.png")); // Imagem
        s.createContext("/img/sair.png", t -> enviarImagem(t, "img/sair.png")); // Imagem
        s.createContext("/sigma100.png", t -> enviarImagem(t, "sigma100-removebg-previw.png")); // Imagem
        s.createContext("/img/estudante.jpg", t -> enviarImagem(t, "img/estudante.jpg")); // Imagem
        s.createContext("/img/White-seta.svg", t -> enviarImagem(t, "img/White-seta.svg")); // Imagem

        s.start();
        System.out.println("Servidor Iniciado");
        System.out.println("Rodando em http://localhost:8082/");
    }

    // -------------------- Pagina de login em HTML dinamico  --------------------

    public static void login (HttpExchange t) throws  IOException {
        StringBuilder html = new StringBuilder();
        System.out.println(erro);

        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"pt-BR\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.append("<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">");
        html.append("<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>");
        html.append("<link href=\"https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&display=swap\" rel=\"stylesheet\">");
        html.append("<link rel=\"stylesheet\" href=\"/css/login.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/css/geral.css\">");
        html.append("<link rel=\"icon\" href=\"/img/academyflow-logo.png\">");
        html.append("<title>AcademyFlow | Login</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<header>");
        html.append("<div class=\"navegation\">");
        html.append("<a href=\"/\">Home</a>");
        html.append("<button class=\"dark-mode-button\" id=\"toggle-theme\" aria-label=\"Alternar modo escuro\">");
        html.append("<img class=\"light-theme-img\" src=\"/img/dark-theme.svg\" alt=\"Ícone Claro\">");
        html.append("<img class=\"dark-theme-img\" src=\"/img/light-theme.svg\" alt=\"Ícone Escuro\">");
        html.append("</button>");
        html.append("</div>");
        html.append("</header>");
        html.append("<main>");
        html.append("<div id=\"customAlert\" class=\"alert\" style=\"display: none;\">");
        html.append("<div class=\"alert-content\">");
        html.append("<span id=\"alertMessage\"></span>");
        html.append("<button type=\"button\" onclick=\"hideAlert()\">OK</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("<section class=\"container reveal\">");
        html.append("<form method=\"post\" action=\"/verificar\">");
        html.append("<div class=\"bloco\">");
        html.append("<label for=\"usuario\">Usuário</label>");
        html.append("<input type=\"text\" placeholder=\"Nome Completo\" id=\"usuario\" name=\"usuario\" required>");
        html.append("</div>");
        html.append("<div class=\"bloco\">");
        html.append("<label for=\"senha\">Senha</label>");
        html.append("<div class=\"inputsenha\">");
        html.append("<input type=\"password\" placeholder=\"Mínimo 8 caracteres\" id=\"senha\" name=\"senha\" minlength=\"8\" required>");
        html.append("<button type=\"button\" id=\"botaosenha\" class=\"revelar-senha\" onclick=\"togglePassword()\">👁️</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div class=\"bloco\">");
        html.append("<label for=\"tipo\">Perfil</label>");
        html.append("<select id=\"tipo\" name=\"tipo\" required>");
        html.append("<option value=\"\">Selecionar</option>");
        html.append("<option value=\"aluno\">Aluno</option>");
        html.append("<option value=\"professor\">Professor</option>");
        html.append("</select>");
        html.append("</div>");
        html.append("<div>");
        html.append("<button type=\"submit\" id=\"enviar\">Enviar</button>");
        html.append("</div>");
        html.append("</form>");
        html.append("<p>").append(erro).append("</p>");
        html.append("</section>");
        html.append("<section class=\"infos reveal\">");
        html.append("<div class=\"linha reveal\">");
        html.append("<img src=\"/img/estudante.jpg\" alt=\"\">");
        html.append("<div class=\"infos-content\">");
        html.append("<h2>Atendemos <span>escolas</span> de <span>alto nivel</span></h2>");
        html.append("<p>Atendemos escolas de alto nível, oferecendo soluções de excelência que elevam a qualidade do ensino, aprimoram a gestão e fortalecem a experiência educacional. Nosso compromisso é apoiar instituições que buscam inovação, eficiência e resultados superiores.</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div class=\"linha reveal\">");
        html.append("<div class=\"infos-content\">");
        html.append("<h2><span>Facilidade</span> para todas as idades!</h2>");
        html.append("<p>Uma experiência simples e intuitiva, pensada para oferecer facilidade e acessibilidade a pessoas de todas as idades, garantindo que todos possam aproveitar sem complicação.</p>");
        html.append("</div>");
        html.append("<img src=\"/escola-primaria.jpg\" alt=\"\">");
        html.append("</div>");
        html.append("</section>");
        html.append("</main>");
        html.append("<script src=\"/js/script.js\"></script>");
        html.append("</body>");
        html.append("</html>");
    }

    // -------------------- Função de verificação de login  --------------------

    public static void verificar (HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            enviar(t, "/login"); //envia aquivo do professor
            return;
        }

        String c = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);

        String[] usuarios = {
                "Guilherme Torres", "Nicolas Tordino", "João Pedro", "Pietro Pardim",
                "Mariana Adel Ayoub", "Giovanna Alves de Almeida", "Ana Carolina Santos Denobi",
                "Felipe Chagas Machado", "Fernanda Cristina Rodrigues Ferreira",
                "Carina Cristina Teixeira de Souza", "Henrique da Silva Lima",
                "Eduardo de Figueiredo Ferreira Gandra", "Rayka Dom Pedro Hirata",
                "Henrique Duarte de Sousa", "Maria Eduarda Silva Freitas",
                "Raphaela Felix de Araujo", "Lucas Gabriel de Moura Adorni",
                "André Gustavo Pavanelli", "Ana Julia Lima dos Santos",
                "Sarah Kohn Baldoini", "Matheus Lopes Ferreira",
                "Yasmin Lopes Souza", "Arthur Marques Duarte Silva",
                "Gustavo Nunes da Silva", "Isabelle Queiroz Rodrigues",
                "Felipe Santos Silva", "Otávio Silva Garbin",
                "Isabelly Sofia Domingues", "Kauã Teles Santos", "Luiza Timporini Frazão",
                "Gabriel Viana dos Reis", "João Vitor Ulisses Bacurau", "Eduardo Fallabela"
        };

        // Arrays para armazenar as senhas correspondentes
        String[] senhas = {
                "12345678", "neymarjunior", "horadopaupreto", "naofechocomx9",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678", "12345678",
                "12345678", "12345678", "12345678"
        };

        // Arrays para armazenar os tipos de usuário correspondentes
        String[] tipos = {
                "professor", "professor", "professor", "professor",
                "aluno", "aluno", "aluno", "aluno", "aluno", "aluno",
                "aluno", "aluno", "aluno", "aluno", "aluno", "aluno",
                "aluno", "aluno", "aluno", "aluno", "aluno", "aluno",
                "aluno", "aluno", "aluno", "aluno", "aluno", "aluno",
                "aluno", "aluno", "aluno", "professor"
        };

        usuarioDigitado = pega(c, "usuario");
        String senhaDigitada = pega(c, "senha");
        String tipoDigitado = pega(c, "tipo");

        int indexUsuario = -1;

        // 1. Verificar se usuário existe no array
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].equals(usuarioDigitado)) {
                indexUsuario = i;
                break;
            }
        }

        if (indexUsuario == -1) {
            System.out.println("Usuario não encontrado");
            erro = "Usuario não encontrado";
            redirecionar(t, "/login");
        }

        // 2. Verificar senha na mesma posição do usuário
        if (!senhas[indexUsuario].equals(senhaDigitada)) {
            System.out.println("senha incorreta");
            erro = "Senha incorreta";
            redirecionar(t, "/login");
        }

        // 3. Verificar tipo (professor/aluno)
        if (!tipos[indexUsuario].equals(tipoDigitado)) {
            System.out.println( "Tipo de usuário incorreto!");
            erro = "Tipo de usuario incorreto!";
            redirecionar(t, "/login");
        }

        // 4. Login correto
        if (usuarios[indexUsuario].equals(usuarioDigitado) && senhas[indexUsuario].equals(senhaDigitada) && tipos[indexUsuario].equals(tipoDigitado)) {
            if (tipoDigitado.equals("professor")){
                redirecionar(t,"/acesso-professor");
                System.out.println("Login realizado");
                System.out.println("Professor(a) "+usuarioDigitado);
            }
            if (tipoDigitado.equals("aluno")){
                redirecionar(t, "/aluno");
                System.out.println("Login realizado");
                System.out.println("Aluno(a) "+usuarioDigitado);
            }
        }
    }

    // -------------------- Função de cadastro de atividades --------------------

    private static void cadastro(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            enviar(t, "/carregando.html"); //envia aquivo do professor
            return;
        }

        String c = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);

        String nome = pega(c, "nome"); //pega nome escrito
        String desc = pega(c, "descricao"); //pega descrição da atividade
        String data = pega(c, "data"); //pega data da atividade
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO dados (nome, descricao, data, curtida) VALUES (?,?,?,?)")) { //insere dados no banco de dados

            ps.setString(1, nome);
            ps.setString(2, desc);
            ps.setString(3, data);
            ps.setString(4, "null"); // ainda não curtido
            ps.executeUpdate(); //atualiza o banco de dados

        } catch (SQLException e) {
            e.printStackTrace();
        }

        redirecionar(t, "/cadastro");
        System.out.println("-------------------------------------");
        System.out.println("Cadastro realizado");
        System.out.println(" ");
        System.out.println("Atividade: " + nome);
        System.out.println("Descrição: " + desc);
        System.out.println("Data: " + data);
        System.out.println("-------------------------------------");
    }

    // -------------------- Aluno (lista todos os cards) --------------------

    private static void aluno(HttpExchange t) throws IOException {
        StringBuilder html = new StringBuilder();

        //Insere arquivo HTMl do aluno (mesma coisa que HTML padrão soq colocado a partir do JAVA)

        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta http-equiv=\"refresh\" content=\"120\">");
        html.append("<title>AcademyFlow | Mural de Atividades</title>");
        html.append("<link rel=\"stylesheet\" href=\"/css/atividades.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/css/geral.css\">");
        html.append("<link rel=\"icon\" href=\"/img/academyflow-logo.png\">");
        html.append("</head><body>");
        html.append("<header>");
        html.append("<div class=\"logo-header\">");
        html.append("<a href=\"#\">");
        html.append("<img src=\"/academyflow-nome.png\" alt=\"Logo AcademyFlow\">");
        html.append("</a>");
        html.append("</div>");
        html.append("<div></div>");
        html.append("<div class=\"nav\">");
        html.append("<div class=\"container-btn-header\">");
        html.append("<h4>Bem-Vindo Aluno(a)</h4>");
        html.append("<p id=\"nome-usuario\">").append(usuarioDigitado).append("</p>");
        html.append("</div>");

        html.append("<div class=\"buttons\">");
        html.append("<button class=\"dark-mode-button\" id=\"toggle-theme\" aria-label=\"Alternar modo escuro\">");
        html.append("<img class=\"dark-theme-img\" src=\"/img/light-theme.svg\" alt=\"Ícone Claro\">");
        html.append("<img class=\"light-theme-img\" src=\"/img/dark-theme.svg\" alt=\"Ícone Escuro\">");
        html.append("</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("</header>");

        html.append("<main>");
        html.append("<section class=\"titulo\">");
        html.append("<div class=\"tituloh1\">");
        html.append("<h1>Mural de Atividades</h1>");
        html.append("</div>");
        html.append("</section>");

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nome, descricao, data, curtida FROM dados ORDER BY id DESC")) {
            //Organiza os arquivos a partir do ID

            boolean vazio = true;

            while (rs.next()) {
                vazio = false;

                //Insere o conteudo do Banco de Dados em variaveis
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String desc = rs.getString("descricao");
                String data = rs.getString("data");
                String curtida = rs.getString("curtida");

                // Trocar a cor se concluido ou não
                String classeExtra = "bloco";
                if ("Concluido".equals(curtida)) {
                    classeExtra = "card-curtido";
                } else if ("Não Concluido".equals(curtida)) {
                    classeExtra = "card-nao-curtido";
                }
                //Insere o conteudo do Banco de dados em blocos no HTML
                html.append("<div class=\"")
                        .append(classeExtra)
                        .append("\">");
                html.append("<div class=\"bloco-content\">");
                html.append("<h2>").append(nome).append("</h2>");
                html.append("<p><strong> ").append(data).append("</strong></p>");
//                html.append("<p><strong>Status:</strong> ").append(curtida).append("</p>");
                html.append("<p>").append(desc).append("</p>");
                html.append("</div>");
                html.append("<div class=\"botoes\">");

                // Botão CONCLUIDO
                html.append("<form method=\"POST\" action=\"/concluido_aluno\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Concluido\">");
                html.append("<button type=\"submit\">Concluído</button>");
                html.append("</form>");

                // Botão NÃO CONCLUIDO
                html.append("<form method=\"POST\" action=\"/concluido_aluno\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Não Concluido\">");
                html.append("<button type=\"submit\" id=\"nao-concluido\">Não concluído</button>");
                html.append("</form>");


                html.append("</div>");
                html.append("</div>");
            }

            if (vazio) {
                html.append("<p class=\"Vazio\">Nenhuma atividade cadastrada ainda.</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            html.append("<h1>Erro ao carregar atividades.</h1>");
        }
        html.append("<script src=\"/js/script.js\"></script>");
        html.append("</body></html>");

        // Enviar HTML gerado
        byte[] b = html.toString().getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    // -------------------- Professor (lista, adiciona e exclui atividades) --------------------

    private static void acesso_professor(HttpExchange t) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta http-equiv=\"refresh\" content=\"120\">");
        html.append("<title>AcademyFlow | Mural de Atividades</title>");
        html.append("<link rel=\"stylesheet\" href=\"/css/atividades.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/css/geral.css\">");
        html.append("<link rel=\"icon\" href=\"/img/academyflow-logo.png\">");
        html.append("</head><body>");
        html.append("<header>");
        html.append("<div class=\"logo-header\">");
        html.append("<a href=\"#\">");
        html.append("<img src=\"/academyflow-nome.png\" alt=\"Logo AcademyFlow\">");
        html.append("</a>");
        html.append("</div>");
        html.append("<div></div>");
        html.append("<div class=\"nav\">");
        html.append("<div class=\"container-btn-header\">");
        html.append("<h4>Bem-Vindo Professor(a)</h4>");
        html.append("<p id=\"nome-usuario\">").append(usuarioDigitado).append("</p>");
        html.append("</div>");

        html.append("<div class=\"buttons\">");
        html.append("<button class=\"dark-mode-button\" id=\"toggle-theme\" aria-label=\"Alternar modo escuro\">");
        html.append("<img class=\"dark-theme-img\" src=\"/img/light-theme.svg\" alt=\"Ícone Claro\">");
        html.append("<img class=\"light-theme-img\" src=\"/img/dark-theme.svg\" alt=\"Ícone Escuro\">");
        html.append("</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("</header>");

        html.append("<main>");
        html.append("<section class=\"titulo\">");
        html.append("<div class=\"tituloh1\">");
        html.append("<h1>Mural de Atividades</h1>");
        html.append("</div>");
        html.append("<button class=\"editar\" id=\"mostrar-bloco\">✏️ Editar Mural</button>");
        html.append("<div id=\"overlay\"></div>");
        html.append("<div class=\"bloco-atividade\" id=\"bloco-atividade\">");
        html.append("<h2>Cadastrar Atividade</h2>");
        html.append("<form method=\"POST\" action=\"/cadastro\">");
        html.append("<div class=\"input\">");
        html.append("<label>Nome:</label><br>");
        html.append("<input type=\"text\" name=\"nome\" placeholder=\"Insira o nome da atividade\" maxlength=\"30\" required><br>");
        html.append("</div>");
        html.append("<div class=\"input\">");
        html.append("<label>Descrição:</label><br>");
        html.append("<input type=\"text\" name=\"descricao\" maxlength=\"50\" placeholder=\"Insira uma breve descrição da atividade\" required><br>");
        html.append("</div>");
        html.append("<div class=\"input\">");
        html.append("<label>Data:</label><br>");
        html.append("<input type=\"date\" name=\"data\" required><br>");
        html.append("</div>");
        html.append("<button type=\"submit\">Cadastrar</button>");
        html.append("</form>");
        html.append("<button id=\"voltar-bloco\">Voltar</button>");
        html.append("</div>");
        html.append("</section>");

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nome, descricao, data, curtida FROM dados ORDER BY id DESC")) {

            boolean vazio = true;

            while (rs.next()) {
                vazio = false;

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String desc = rs.getString("descricao");
                String data = rs.getString("data");
                String curtida = rs.getString("curtida");

                // Classe extra para cor do card
                String classeExtra = "bloco";
                if ("Concluido".equals(curtida)) {
                    classeExtra = "card-curtido";
                } else if ("Não Concluido".equals(curtida)) {
                    classeExtra = "card-nao-curtido";
                }

                html.append("<div class=\"")
                        .append(classeExtra)
                        .append("\">");
                html.append("<div id=\"id-bloco\" style=\"display:none;\">").append(id).append("\n</div>");
                html.append("<div class=\"bloco-content\">");
                html.append("<h2>").append(nome).append("</h2>");
                html.append("<p><strong> ").append(data).append("</strong></p>");
                //html.append("<p><strong>Status:</strong> ").append(curtida).append("</p>");*/
                html.append("<p>").append(desc).append("</p>");
                html.append("</div>");
                html.append("<div class=\"botoes\">");

                // Botão CONCLUIDO
                html.append("<form method=\"POST\" action=\"/concluido_professor\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Concluido\">");
                html.append("<button type=\"submit\">Concluído</button>");
                html.append("</form>");

                // Botão NÃO CONCLUIDO
                html.append("<form method=\"POST\" action=\"/concluido_professor\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Não Concluido\">");
                html.append("<button type=\"submit\">Não concluído</button>");
                html.append("</form>");

                html.append("<form method=\"POST\" action=\"/excluir\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"excluir\">");
                html.append("<button class=\"excluir\" type=\"submit\">Excluir</button>");
                html.append("</form>");

                html.append("</div>");

                // Botão EXCLUIR
                html.append("<input type=\"checkbox\" id=\"toggle-expansao-").append(id).append("\" class=\"toggle-expansao\">");
                html.append("<label for=\"toggle-expansao-").append(id).append("\" class=\"botao-expansao\">Editar</label>");

                html.append("<div class=\"conteudo-expandido\">");

                html.append("<form method=\"post\" action=\"/editar\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<div class=\"editar-header\">");
                html.append("<div>");
                html.append("<input type=\"text\" name=\"nome\" placeholder=\"Editar título\" required>");
                html.append("</div>");
                html.append("<div>");
                html.append("<input type=\"date\" name=\"data\" required>");
                html.append("</div>");
                html.append("</div>");
                html.append("<div class=\"editar-descricao\">");
                html.append("<input type=\"text\" name=\"desc\" placeholder=\"Editar Descrição\" required>");
                html.append("</div>");
                html.append("<button type=\"submit\">Confirmar</button>");
                html.append("</form>");

                html.append("</div>"); // conteudo-expandido
                html.append("</div>"); // fecha div com classeExtra
            }

            if (vazio) {
                html.append("<p>Nenhuma atividade cadastrada ainda.</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            html.append("<p>Erro ao carregar atividades.</p>");
        }
        html.append("<script src=\"/js/script.js\"></script>");
        html.append("</body></html>");

        // Enviar HTML gerado
        byte[] b = html.toString().getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    // -------------------- Conclusão (Concluido / Não concluido) --------------------

    private static void concluido_aluno(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/aluno"); //Redireciona para apagina do aluno
            return;
        }

        String corpo = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);
        String acao = pega(corpo, "acao"); // "concluido" ou "nao"
        String idStr = pega(corpo, "id");

        try {
            int id = Integer.parseInt(idStr);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE dados SET curtida = ? WHERE id = ?")) {  //atualiza o banco de dados
                ps.setString(1, acao);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        redirecionar(t, "/aluno");
    }

    // -------------------- Conclusão (Concluido / Não concluido) --------------------

    private static void concluido_professor(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/acesso-professor");
            return;
        }

        String corpo = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);
        String acao = pega(corpo, "acao"); // "curtir" ou "nao"
        String idStr = pega(corpo, "id");

        try {
            int id = Integer.parseInt(idStr);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE dados SET curtida = ? WHERE id = ?")) {
                ps.setString(1, acao);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        redirecionar(t, "/acesso-professor");
    }

    private static void editar(HttpExchange t) throws IOException {
        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/acesso-professor");
            System.out.println("Método errado");
            return;
        }

        String corpo = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);
        String idStr = pega(corpo, "id");
        String nome = pega(corpo, "nome"); // Novo nome
        String desc = pega(corpo, "desc"); // Nova descrição
        String dataStr = pega(corpo, "data"); // Nova data
        System.out.println("ID recebido: '" + idStr + "'");

        try {
            int id = Integer.parseInt(idStr);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE dados SET nome = ?, descricao = ?, data = ? WHERE id = ?")) {
                ps.setString(1, nome);
                ps.setString(2, desc);
                ps.setString(3, dataStr);
                ps.setInt(4, id);
                ps.executeUpdate();
                System.out.println("Dados editados com sucesso");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao editar os dados");
        }

        redirecionar(t, "/acesso-professor");
    }

    // -------------------- Função de excluir atividades --------------------

    private static void excluir(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/acesso-professor"); //ao excluir redireciona para:
            return;
        }

        String corpo = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);
        String idStr = pega(corpo, "id"); //pega o id da atividade a ser excluida

        try {
            int id = Integer.parseInt(idStr);

            // Usando apenas o ID para realizar a exclusão
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM dados WHERE id = ?")) { //deleta apenas a atividade com o id especifico
                ps.setInt(1, id);  // Passando apenas o ID para a PreparedStatement
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Atividade excluida, ID: " + idStr);

        redirecionar(t, "/acesso-professor");
    }

    // -------------------- Funções auxiliares --------------------

    private static String pega(String corpo, String campo) {
        // corpo no formato: campo1=valor1&campo2=valor2...
        for (String s : corpo.split("&")) {
            String[] p = s.split("=");
            if (p.length == 2 && p[0].equals(campo)) return p[1];
        }
        return "";
    }

    // -------------------- ENVIAR IMAGEM --------------------

    private static void enviarImagem(HttpExchange t, String arquivo) throws IOException {
        File f = new File("src/main/java/" + arquivo);

        if (!f.exists()) {
            t.sendResponseHeaders(404, -1);
            return;
        }

        // Detecta o MIME type pela extensão
        String contentType;
        if (arquivo.endsWith(".png")) {
            contentType = "image/png";
        } else if (arquivo.endsWith(".svg")) {
            contentType = "image/svg+xml";
        } else if (arquivo.endsWith(".jpg") || arquivo.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else {
            contentType = "application/octet-stream"; // fallback
        }

        byte[] bytes = java.nio.file.Files.readAllBytes(f.toPath());
        t.getResponseHeaders().set("Content-Type", contentType);
        t.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = t.getResponseBody()) {
            os.write(bytes);
        }
    }

    // -------------------- Função para ler dados --------------------

    private static String ler(HttpExchange t) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)
        );
        String linha = br.readLine();
        return (linha == null) ? "" : linha;
    }

    // -------------------- Função para enviar arquivos (html e JS) --------------------

    private static void enviar(HttpExchange t, String arq) throws IOException {
        File f = new File("src/main/java/" + arq);
        byte[] b = java.nio.file.Files.readAllBytes(f.toPath());
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    // -------------------- Função para enviar arquivos CSS --------------------

    private static void enviarCSS(HttpExchange t, String arq) throws IOException {
        File f = new File("src/main/java/" + arq);
        byte[] b = java.nio.file.Files.readAllBytes(f.toPath());
        t.getResponseHeaders().add("Content-Type", "text/css; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    // -------------------- Função para redirecionar rotas --------------------

    private static void redirecionar(HttpExchange t, String rota) throws IOException {
        t.getResponseHeaders().add("Location", rota);
        t.sendResponseHeaders(302, -1);
        t.close();
    }
}