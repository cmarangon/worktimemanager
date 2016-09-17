package ch.hftm.wtm.persistence;

import java.io.IOException;
import java.net.ServerSocket;

import org.hsqldb.server.Server;

/**
 *
 * @author HOEFI
 * @since 04.07.2016
 * @version 1.0
 */
public class EmbeddedHSQL {
    
    private Server hsqlServer = new Server();

    public void startServer() {
        System.out.println("Starte SERVER");
        try {
            // 2. mal starten verhindern.
            // Somit ist ein Recompile bei einem laufendem Jetty-Server kein Problem.
            ServerSocket serverSocket = new ServerSocket(9001);
            serverSocket.close();
        } catch (IOException e) {
            return;
        }
        System.out.println("Socket gestartet");
        hsqlServer.setLogWriter(null);
        hsqlServer.setNoSystemExit(false);
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, "wtm");
        hsqlServer.setDatabaseName(1, "wtm_test");
        // Daten in den Target-Ordner, damit ausserhalb der GIT-Kontrolle
        hsqlServer.setDatabasePath(0, "file:target/db/wtm");
        hsqlServer.setDatabasePath(1, "file:target/db/wtm_test");
        hsqlServer.setPort(9001);
        hsqlServer.start();
        System.out.println("server verbunden");
    }
    
    public void stopServer() {
        hsqlServer.stop();
    }
    
}
