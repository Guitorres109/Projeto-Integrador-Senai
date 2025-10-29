import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ServidorHttpSimples {
    public static void main(String[] args) throws Exception {
        // Cria servidor HTTP escutando na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        /* INICIO DO CÓDIGO */

        servidor.createContext("/sucesso" , troca ->{
            enviarArquivo(troca,"sucesso.html","text/html");
        });
        servidor.createContext("/professor" , troca ->{
            enviarArquivo(troca, "professor.html","text/html");
        });
        servidor.createContext("/aluno" , troca ->{
            enviarArquivo(troca, "aluno.html","text/html");
        });
        servidor.createContext("/style.css" , troca ->{
            enviarArquivo(troca,"style.css","text/css");
        });
        servidor.createContext("/script.js" , troca ->{
            enviarArquivo(troca, "script.js","text/js");
        });

        servidor.createContext("/" , troca ->{
            enviarArquivo(troca, "index.html", "text/html");
            String query = troca.getRequestURI().getQuery();
            String[] partes;
            partes = query.split("&");
            String perfil = partes[0].replace("tipo=","");
            String usuario = partes[1].replace("usuario=","");
            String senha = partes[2].replace("senha=","");
            System.out.println(" ");
            System.out.println("Perfil= "+perfil);
            System.out.println("Nome= "+usuario);
            System.out.println("Senha= "+senha);

            if (perfil.equals("professor")){
                if (usuario.equals("Guilherme+Torres")&&senha.equals("1234567890")){
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location","/professor");
                troca.sendResponseHeaders(302, -1);
                }
                else{
                    System.out.println("Acesso Negado");
                    troca.getResponseHeaders().set("Location","/professor");
                    troca.sendResponseHeaders(302, -1);
                }
            }
            else if (perfil.equals("Aluno")) {
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location","/aluno");
                troca.sendResponseHeaders(302, -1);
            }
        });


        /* FIM DO CÓDIGO */

        servidor.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }

    // Envia um arquivo (HTML ou CSS)
    private static void enviarArquivo(com.sun.net.httpserver.HttpExchange troca, String caminho, String tipo) throws IOException {
        File arquivo = new File("Login/untitled/src/" + caminho);
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }
        byte[] bytes = Files.readAllBytes(arquivo.toPath());
        troca.getResponseHeaders().set("Content-Type", tipo + "; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Envia resposta HTML gerada no código
    private static void enviarTexto(com.sun.net.httpserver.HttpExchange troca, String texto) throws IOException {
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }
}
