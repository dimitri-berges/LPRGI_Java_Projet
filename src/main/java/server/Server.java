package server;

import utils.Consts;
import utils.Lecteurs;
import utils.Livres;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        Server server = new Server();
        server.InitializeServer(databaseManager);
        server.ClientInteraction();
        databaseManager.CloseConnection();
        server.CloseConnection();
    }

    ServerSocket serverSocket;
    DatabaseManager databaseManager;

    /**
     * Initialise la ServerSocket et le DatabaseManager
     * @param databaseManager
     */
    public void InitializeServer(DatabaseManager databaseManager) {
        databaseManager.InitializeConnexion();
        this.databaseManager = databaseManager;
        try {
            serverSocket = new ServerSocket(28436);
            System.out.println("Server running on port 28436...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode principale du serveur.
     * Attend la connexion d'un client puis intéragit avec lui comme prévu, lors de la déconnexion du client se remet en attente d'un nouveau client
     */
    public void ClientInteraction() {
        while (true) {
            // Attente de la connexion d'un client
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            // Client connecté, instantiation du flux de données
            DataInputStream in = null;
            try {
                in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            // Flux instancié, début de l'interaction
            int code = 0;
            do {
                try {
                    code = in.readInt(); // Lecture du code envoyé par le client
                } catch (EOFException e) { // Si on reçoit un EndOfFile le client s'est déconnecté, on ferme donc la socket
                    System.out.println("Client disconnected");
                    code = Consts.NETWORK_PROTOCOL_EXIT_CODE;
                } catch (IOException e) { // Autre erreur ayant pu survenir, dans ce cas on quitte la connexion avec le client et on ferme le serveur
                    e.printStackTrace();
                    return;
                }
                switch (code) {
                    case Consts.NETWORK_PROTOCOL_LECTEUR_CODE -> ReadLecteur(in); // Si le code correspond on applique la méthode correspondante
                    case Consts.NETWORK_PROTOCOL_LIVRE_CODE -> ReadLivre(in);
                    default -> code = Consts.NETWORK_PROTOCOL_EXIT_CODE; // Sinon on quitte le système
                }
            } while (code != Consts.NETWORK_PROTOCOL_EXIT_CODE); // On attend des informations du client tant qu'il nous parle

            // Fermeture propre de la socket
            try {
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Lis les données du flux entrant, créer l'objet et le met en base de données
     * @param in Flux de données entrantes
     */
    private void ReadLivre(DataInputStream in) {
        Livres livres = Livres.fromStream(in);
        databaseManager.InsertLivre(livres);
    }

    /**
     * Lis les données du flux entrant, créer l'objet et le met en base de données
     * @param in Flux de données entrantes
     */
    private void ReadLecteur(DataInputStream in) {
        Lecteurs lecteurs = Lecteurs.fromStream(in);
        databaseManager.InsertLecteur(lecteurs);
    }

    /**
     * Ferme proprement le serveur
     */
    public void CloseConnection() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
