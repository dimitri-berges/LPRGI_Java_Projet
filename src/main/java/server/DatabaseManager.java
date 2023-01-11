package server;

import utils.Lecteurs;
import utils.Livres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/mabd";
    private Connection connection;
    private Statement statement = null;

    public DatabaseManager() {
        connection = null;
    }

    /**
     * Méthodes qui permet d'initialiser la connexion à la base de données
     */
    public void InitializeConnexion() {
        try { // Chargement du driver de connexion à la base de données
            Class.forName(MYSQL_DRIVER);
            System.out.println("Chargement du driver JDBC ok");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try { // Connexion à la base de données
            connection = DriverManager.getConnection(MYSQL_URL, "root", "root");
            System.out.println("Connexion à la BDD ok");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-2);
        }
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-4);
        }
        // Création de la structure de la base de donnée
        ExecuteUpdate(
                "CREATE TABLE IF NOT EXISTS Livres (" +
                        "ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "titre VARCHAR(255) NOT NULL," +
                        "auteur VARCHAR(255) NOT NULL," +
                        "nbPages INT NOT NULL" +
                        ");"
        );
        ExecuteUpdate(
                "CREATE TABLE IF NOT EXISTS Lecteurs (" +
                        "    ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "    nom VARCHAR(255) NOT NULL," +
                        "    prenom VARCHAR(255) NOT NULL," +
                        "    age INT NOT NULL" +
                        ");"
        );
    }

    /**
     * Exécute une requête SQL sans résultat
     * @param sql Requête SQL à exécuter
     */
    private void ExecuteUpdate(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-5);
        }
    }

    /**
     * Insère un livre dans la table Livres de la base de données
     * @param livre Livre à insérer dans la base de données
     */
    public void InsertLivre(Livres livre) {
        try {
            statement.executeUpdate(
                    "INSERT INTO Livres(titre, auteur, nbPages)  VALUES " + livre.toSQLValues() + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insère un lecteur dans la table Lecteurs de la base de données
     * @param lecteur Lecteur à insérer dans la base de données
     */
    public void InsertLecteur(Lecteurs lecteur) {
        try {
            statement.executeUpdate(
                    "INSERT INTO Lecteurs(nom, prenom, age)  VALUES " + lecteur.toSQLValues() + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferme proprement la connexion à la base de données
     */
    public void CloseConnection() {
        try {
            statement.close();
            connection.close();
            System.out.println("Fin de la connexion à la BDD");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }
}
