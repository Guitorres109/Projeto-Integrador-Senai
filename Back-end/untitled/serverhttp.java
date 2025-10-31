import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.InetSocketAddress;

public class serverhttp{
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/dados", new MeuHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado na porta 8080");
        System.out.println("Servidor rodando em http://localhost:8080/api/dados");
    }

    static class MeuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                // Lê o corpo da requisição (JSON)
                InputStream is = exchange.getRequestBody();
                String json = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .reduce("", (acc, line) -> acc + line);

                System.out.println("Recebido JSON: " + json);

                // Responde ao cliente
                String resposta = "JSON recebido com sucesso!";
                exchange.sendResponseHeaders(200, resposta.length());
                OutputStream os = exchange.getResponseBody();
                os.write(resposta.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Método não permitido
            }
        }
    }
}
