package client;

import utils.Consts;
import utils.Lecteurs;
import utils.Livres;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.InitializeClient();
        client.ClientInteraction();
        client.CloseClient();
    }

    Socket socket;
    DataOutputStream dataOutputStream;
    BufferedReader consoleInput;

    /**
     * Méthode principale du client
     * Permet le choix de l'interaction
     */
    public void ClientInteraction() {
        System.out.println("Bonjour et bienvenue, tous les systèmes sont opérationnels.");
        int code = -1;
        do { // Affichage du menu à l'utilisateur
            System.out.println("Que voulez-vous faire ?");
            System.out.println(Consts.NETWORK_PROTOCOL_EXIT_CODE + ") Quitter");
            System.out.println(Consts.NETWORK_PROTOCOL_LECTEUR_CODE + ") Ajouter un lecteur");
            System.out.println(Consts.NETWORK_PROTOCOL_LIVRE_CODE + ") Ajouter un livre");
            try {
                code = Integer.parseInt(consoleInput.readLine()); // Lecture du nombre rentré par l'utilisateur
            } catch (NumberFormatException e) { // Si une erreur survient on l'affiche et on fait recommencer l'utilisateur
                System.out.println("Merci d'écrire uniquement un chiffre correspondant à votre choix.");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            switch (code) { // Selon le chiffre indiqué par l'utilisateur, on lance la bonne action
                case Consts.NETWORK_PROTOCOL_LIVRE_CODE -> CreateLivre();
                case Consts.NETWORK_PROTOCOL_LECTEUR_CODE -> CreateLecteur();
                default -> code = Consts.NETWORK_PROTOCOL_EXIT_CODE;
            }
        } while (code != Consts.NETWORK_PROTOCOL_EXIT_CODE); // On recommence tant que l'utilisateur ne veut pas quitter
        System.out.println("Bonne journée !");
    }

    /**
     * Intéragit avec l'utilisateur pour récupérer les informations et les envoies au serveur
     */
    private void CreateLecteur() {
        String nom;
        String prenom;
        int age;
        try {
            System.out.println("Merci de remplir les informations suivantes :");
            System.out.print("Nom : ");
            nom = consoleInput.readLine();
            System.out.print("Prénom : ");
            prenom = consoleInput.readLine();
            System.out.print("Age : ");
            age = Integer.parseInt(consoleInput.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Lecteurs lecteurs = new Lecteurs(nom, prenom, age);
        lecteurs.toStream(dataOutputStream);
    }

    /**
     * Intéragit avec l'utilisateur pour récupérer les informations et les envoies au serveur
     */
    private void CreateLivre() {
        String titre;
        String auteur;
        int nbPages;
        try {
            System.out.println("Merci de remplir les informations suivantes :");
            System.out.print("Titre : ");
            titre = consoleInput.readLine();
            System.out.print("Auteur : ");
            auteur = consoleInput.readLine();
            System.out.print("Nombre de page : ");
            nbPages = Integer.parseInt(consoleInput.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Livres livre = new Livres(titre, auteur, nbPages);
        livre.toStream(dataOutputStream);
    }

    /**
     * Initialise la connexion avec le serveur
     */
    private void InitializeClient() {
        try {
            socket = new Socket("localhost", 28436);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return;
        }
        consoleInput = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Ferme proprement la connexion avec le serveur
     */
    private void CloseClient() {
        try {
            dataOutputStream.writeInt(Consts.NETWORK_PROTOCOL_EXIT_CODE);
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
