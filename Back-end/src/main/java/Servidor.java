import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Servidor {

    private static Connection con;

    public static void main(String[] args) throws Exception {

        // Conectar ao SQLite (arquivo conteudo.db na pasta do projeto)
        con = DriverManager.getConnection("jdbc:sqlite:conteudo.db");

        // Criar tabela (se não existir)
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
        s.createContext("/login.html", t -> enviar(t, "login.html")); // mostra login
        s.createContext("/cadastro", Servidor::cadastro);     // cadastro
        s.createContext("/aluno", Servidor::aluno); // lista cards
        s.createContext("/acesso-professor", Servidor::acesso_professor); // lista cards
        s.createContext("/concluido_aluno", Servidor::concluido_aluno);       // curtir / não curtir
        s.createContext("/concluido_professor", Servidor::concluido_professor);       // curtir / não curtir
        s.createContext("/excluir", Servidor::excluir);       //excluir
        s.createContext("/Atividades", t -> enviar(t, "Atividades.html"));   // Aluno
        //s.createContext("/acesso-professor", t -> enviar(t, "acesso-professor.html"));   // Professor

        //Rotas de estilo
        s.createContext("/style.css", t -> enviarCSS(t, "style.css")); // CSS
        s.createContext("/login.css", t -> enviarCSS(t, "login.css")); // CSS
        s.createContext("/geral.css", t -> enviarCSS(t, "geral.css")); // CSS
        s.createContext("/atividades.css", t -> enviarCSS(t, "atividades.css")); // CSS
        s.createContext("/listar.css", t -> enviarCSS(t, "listar.css")); // CSS

        //Rotas de arquivos javascript
        s.createContext("/script.js", t -> enviar(t, "script.js")); //JS geral
        s.createContext("/login.js", t -> enviar(t, "login.js")); //JS login

        //Rotas de imagem
        s.createContext("/academyflow-logo.png", t -> enviarImagem(t, "AcademyFlow-logo.png")); // Imagem
        s.createContext("/academyflow-nome.png", t -> enviarImagem(t, "AcademyFlow-nome (1).png")); // Imagem
        s.createContext("/escola-primaria.jpg", t -> enviarImagem(t, "criancas-felizes-na-escola-primaria.jpg")); // Imagem
        s.createContext("/dark-theme.svg", t -> enviarImagem(t, "dark-theme.svg")); // Imagem
        s.createContext("/git-hub.png", t -> enviarImagem(t, "git-hub.png")); // Imagem
        s.createContext("/guilherme.png", t -> enviarImagem(t, "guilherme.png")); // Imagem
        s.createContext("/icon-apagar.svg", t -> enviarImagem(t, "icon-apagar.svg")); // Imagem
        s.createContext("/icon-feito.svg", t -> enviarImagem(t, "icon-feito.svg")); // Imagem
        s.createContext("/instagram.png", t -> enviarImagem(t, "instagram.png")); // Imagem
        s.createContext("/joao.png", t -> enviarImagem(t, "joao.png")); // Imagem
        s.createContext("/laguna-school.png", t -> enviarImagem(t, "LagunaSchool.png")); // Imagem
        s.createContext("/light-theme.svg", t -> enviarImagem(t, "light-theme.svg")); // Imagem
        s.createContext("/logo-laguna.png", t -> enviarImagem(t, "logo.png")); // Imagem
        s.createContext("/nicolas.png", t -> enviarImagem(t, "nicolas.png")); // Imagem
        s.createContext("/pietro.png", t -> enviarImagem(t, "pietro.png")); // Imagem
        s.createContext("/sair.png", t -> enviarImagem(t, "sair.png")); // Imagem
        s.createContext("/sigma100.png", t -> enviarImagem(t, "sigma100-removebg-previw.png")); // Imagem
        s.createContext("/estudante.jpg", t -> enviarImagem(t, "estudante.jpg")); // Imagem
        s.createContext("/White-seta.svg", t -> enviarImagem(t, "White-seta.svg")); // Imagem

        s.start();
        System.out.println("Servidor rodando em http://localhost:8082/");
    }

    // -------------------- LOGIN --------------------


    // -------------------- PRODUTOR --------------------

    private static void cadastro(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            enviar(t, "/acesso-professor.html");
            return;
        }

        String c = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);

        String nome = pega(c, "nome");
        String desc = pega(c, "descricao");
        String data = pega(c, "data");

        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO dados (nome, descricao, data, curtida) VALUES (?,?,?,?)")) {

            ps.setString(1, nome);
            ps.setString(2, desc);
            ps.setString(3, data);
            ps.setString(4, "nenhuma"); // ainda não curtido
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        redirecionar(t, "/cadastro");
        System.out.println("Cadastro realizado");
    }

    // -------------------- CONSUMIDOR (lista todos os cards) --------------------

    private static void aluno(HttpExchange t) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>AcademyFlow | Mural de Atividades</title>");
        html.append("<link rel=\"stylesheet\" href=\"/atividades.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/geral.css\">");
        html.append("<link rel=\"icon\" href=\"/academyflow-logo.png\">");
        html.append("</head><body>");
        html.append("<header>");
        html.append("<div class=\"logo-header\">");
        html.append("<a href=\"#\">");
        html.append("<img src=\"/academyflow-nome.png\" alt=\"Logo AcademyFlow\">");
        html.append("</a>");
        html.append("</div>");

        html.append("<div class=\"nav\">");
        html.append("<div class=\"container-btn-header\">");
        html.append("<h4>Bem-Vindo Aluno</h4>");
        html.append("<p id=\"nome-usuario\"></p>");
        html.append("</div>");

        html.append("<div class=\"buttons\">");
        html.append("<button class=\"dark-mode-button\" id=\"toggle-theme\" aria-label=\"Alternar modo escuro\">");
        html.append("<img class=\"dark-theme-img\" src=\"/light-theme.svg\" alt=\"Ícone Claro\">");
        html.append("<img class=\"light-theme-img\" src=\"/dark-theme.svg\" alt=\"Ícone Escuro\">");
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

            boolean vazio = true;

            while (rs.next()) {
                vazio = false;

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String desc = rs.getString("descricao");
                String data = rs.getString("data");
                String curtida = rs.getString("curtida");

                // Classe extra para cor do card
                String classeExtra = "";
                if ("curtir".equals(curtida)) {
                    classeExtra = " card-curtido";
                } else if ("nao".equals(curtida)) {
                    classeExtra = " card-nao-curtido";
                }

                html.append("<div class=\"bloco").append(classeExtra).append("\">");

                html.append("<div class=\"bloco-content\">");
                //html.append("<p><strong>ID:</strong> ").append(id).append("</p>");
                html.append("<p><strong>Nome:</strong> ").append(nome).append("</p>");
                html.append("<p><strong>Descrição:</strong> ").append(desc).append("</p>");
                html.append("<p><strong>Data:</strong> ").append(data).append("</p>");
                html.append("<p><strong>Status:</strong> ").append(curtida).append("</p>");
                html.append("</div>");
                html.append("<div class=\"botoes\">");
                // Botão CURTIR
                html.append("<form method=\"POST\" action=\"/concluido_aluno\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Concluido\">");
                html.append("<button type=\"submit\">Concluido</button>");
                html.append("</form>");

                // Botão NÃO CURTIR
                html.append("<form method=\"POST\" action=\"/concluido_aluno\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Não Concluido\">");
                html.append("<button type=\"submit\">Não concluido</button>");
                html.append("</form>");

                html.append("</div>");
            }

            if (vazio) {
                html.append("<p>Nenhuma atividade cadastrada ainda.</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            html.append("<p>Erro ao carregar atividades.</p>");
        }
        html.append("<script src=\"/script.js\"></script>");
        html.append("</body></html>");

        // Enviar HTML gerado
        byte[] b = html.toString().getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }
    private static void acesso_professor(HttpExchange t) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>AcademyFlow | Mural de Atividades</title>");
        html.append("<link rel=\"stylesheet\" href=\"/atividades.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/geral.css\">");
        html.append("<link rel=\"icon\" href=\"/academyflow-logo.png\">");
        html.append("</head><body>");
        html.append("<header>");
        html.append("<div class=\"logo-header\">");
        html.append("<a href=\"#\">");
        html.append("<img src=\"/academyflow-nome.png\" alt=\"Logo AcademyFlow\">");
        html.append("</a>");
        html.append("</div>");

        html.append("<div class=\"nav\">");
        html.append("<div class=\"container-btn-header\">");
        html.append("<h4>Bem-Vindo Professor</h4>");
        html.append("<p id=\"nome-usuario\"></p>");
        html.append("</div>");

        html.append("<div class=\"buttons\">");
        html.append("<button class=\"dark-mode-button\" id=\"toggle-theme\" aria-label=\"Alternar modo escuro\">");
        html.append("<img class=\"dark-theme-img\" src=\"/light-theme.svg\" alt=\"Ícone Claro\">");
        html.append("<img class=\"light-theme-img\" src=\"/dark-theme.svg\" alt=\"Ícone Escuro\">");
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
        html.append("<input type=\"text\" name=\"nome\" placeholder=\"Insira o nome da atividade\" required><br>");
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
                String classeExtra = "";
                if ("curtir".equals(curtida)) {
                    classeExtra = " card-curtido";
                } else if ("nao".equals(curtida)) {
                    classeExtra = " card-nao-curtido";
                }

                html.append("<div class=\"bloco").append(classeExtra).append("\">");

                html.append("<div class=\"bloco-content\">");
                //html.append("<p><strong>ID:</strong> ").append(id).append("</p>");
                html.append("<h2><strong></strong> ").append(nome).append("</h2>");
                html.append("<p><strong>Data:</strong> ").append(data).append("</p>");
                html.append("<p><strong>Status:</strong> ").append(curtida).append("</p>");
                html.append("<p><strong></strong> ").append(desc).append("</p>");
                html.append("</div>");

                html.append("<div class=\"botoes\">");
                // Botão CURTIR
                html.append("<form method=\"POST\" action=\"/concluido_professor\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"Concluido\">");
                html.append("<button type=\"submit\">Concluido</button>");
                html.append("</form>");

                // Botão NÃO CURTIR
                html.append("<form method=\"POST\" action=\"/concluido_professor\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"nao\">");
                html.append("<button type=\"submit\">Não concluido</button>");
                html.append("</form>");

                html.append("<form method=\"POST\" action=\"/excluir\">");
                html.append("<input type=\"hidden\" name=\"id\" value=\"").append(id).append("\">");
                html.append("<input type=\"hidden\" name=\"acao\" value=\"excluir\">");
                html.append("<button class=\"excluir\" type=\"submit\">Excluir</button>");
                html.append("</form>");
                html.append("</div>");

                html.append("</div>");
            }

            if (vazio) {
                html.append("<p>Nenhuma atividade cadastrada ainda.</p>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            html.append("<p>Erro ao carregar atividades.</p>");
        }
        html.append("<script src=\"/script.js\"></script>");
        html.append("</body></html>");

        // Enviar HTML gerado
        byte[] b = html.toString().getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    // -------------------- AVALIAR (curtir / não curtir um card específico) --------------------

    private static void concluido_aluno(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/aluno");
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

        redirecionar(t, "/aluno");
    }
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

    private static void excluir(HttpExchange t) throws IOException {

        if (!t.getRequestMethod().equalsIgnoreCase("POST")) {
            redirecionar(t, "/acesso-professor");
            return;
        }

        String corpo = URLDecoder.decode(ler(t), StandardCharsets.UTF_8);
        String idStr = pega(corpo, "id");

        try {
            int id = Integer.parseInt(idStr);

            // Usando apenas o ID para realizar a exclusão
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM dados WHERE id = ?")) {
                ps.setInt(1, id);  // Passando apenas o ID para a PreparedStatement
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private static String ler(HttpExchange t) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)
        );
        String linha = br.readLine();
        return (linha == null) ? "" : linha;
    }

    private static void enviar(HttpExchange t, String arq) throws IOException {
        File f = new File("src/main/java/" + arq);
        byte[] b = java.nio.file.Files.readAllBytes(f.toPath());
        t.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    private static void enviarCSS(HttpExchange t, String arq) throws IOException {
        File f = new File("src/main/java/" + arq);
        byte[] b = java.nio.file.Files.readAllBytes(f.toPath());
        t.getResponseHeaders().add("Content-Type", "text/css; charset=UTF-8");
        t.sendResponseHeaders(200, b.length);
        t.getResponseBody().write(b);
        t.close();
    }

    private static void redirecionar(HttpExchange t, String rota) throws IOException {
        t.getResponseHeaders().add("Location", rota);
        t.sendResponseHeaders(302, -1);
        t.close();
    }
}
