package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Lecteurs {
    private String nom;
    private String prenom;
    private int age;

    public Lecteurs(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    /**
     * Permet de créer un objet Lecteurs depuis un flux
     * @param dataInputStream Flux d'entrée
     * @return Objet Lecteurs créé depuis le flux d'entrée
     */
    public static Lecteurs fromStream(DataInputStream dataInputStream) {
        try {
            String nom = dataInputStream.readUTF();
            String prenom = dataInputStream.readUTF();
            int age = dataInputStream.readInt();
            return new Lecteurs(nom, prenom, age);
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
            dataOutputStream.writeInt(Consts.NETWORK_PROTOCOL_LECTEUR_CODE);
            dataOutputStream.writeUTF(nom);
            dataOutputStream.writeUTF(prenom);
            dataOutputStream.writeInt(age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de récupérer les valeurs de l'objet et de les mettres en chaîne de caractère lisible par SQL pour l'insertion de valeurs
     * @return La chaîne de caractère à mettre dans la requête SQL
     */
    public String toSQLValues() {
        return "('" + nom + "', '" + prenom + "', " + Integer.toString(age) + ")";
    }
}
