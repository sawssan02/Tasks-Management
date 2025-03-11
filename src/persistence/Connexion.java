package persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

class Connexion {
    private static MongoClient mongoClient;//connexion avec Mongodb
    private static MongoDatabase database;// pour stock la base de donnee
    public static MongoDatabase getDatabase() {
        if (database == null) {
            initDatabase();
        }
        return database;
    }
    private static void initDatabase() {// pour  la connexion à initialise la base de donnees
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("projetJAVA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
