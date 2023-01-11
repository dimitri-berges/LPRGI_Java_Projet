package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Livres {
    private String titre;
    private String auteur;
    private int nbPages;

    public Livres(String titre, String auteur, int nbPages) {
        this.titre = titre;
        this.auteur = auteur;
        this.nbPages = nbPages;
    }

    /**
     * Permet de créer un objet Livres depuis un flux
     * @param dataInputStream Flux d'entrée
     * @return Objet Livres créé depuis le flux d'entrée
     */
    public static Livres fromStream(DataInputStream dataInputStream) {
        try {
            String title = dataInputStream.readUTF();
            String auteur = dataInputStream.readUTF();
            int nbPages = dataInputStream.readInt();
            return new Livres(title, auteur, nbPages);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Permet d'envoyer l'objet via le flux donné selon le protocole établit
     * @param dataOutputStream Flux de sortie des données
     */
    public void toStream(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeInt(Consts.NETWORK_PROTOCOL_LIVRE_CODE);
            dataOutputStream.writeUTF(titre);
            dataOutputStream.writeUTF(auteur);
            dataOutputStream.writeInt(nbPages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de récupérer les valeurs de l'objet et de les mettres en chaîne de caractère lisible par SQL pour l'insertion de valeurs
     * @return La chaîne de caractère à mettre dans la requête SQL
     */
    public String toSQLValues() {
        return "('" + titre + "', '" + auteur + "', " + Integer.toString(nbPages) + ")";
    }
}
