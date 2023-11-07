/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package triquibackend;

import com.sun.net.httpserver.HttpServer;
import controllers.AgentController;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author Cristian Henao
 */
public class TriquiBackend {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 8081; // Puerto en el que se ejecutará el servidor
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new AgentController());
        server.setExecutor(null); // Usar el valor predeterminado
        System.out.println("Iniciando servidor en el puerto " + port);
        server.start();
    }

}
