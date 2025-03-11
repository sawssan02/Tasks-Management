package persistence;

import POJO.Doc;
import POJO.Tache;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
public class TacheDAO  implements DAO<Tache>{
    MongoDatabase database = Connexion.getDatabase();
    MongoCollection<Document> tacheCollection = database.getCollection("taches");

    @Override
    public void create(Tache tache) {
        List<Document> docs = new ArrayList<>();
        if (tache.getDoc() != null) {
            for (Doc doc : tache.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        Document document = new Document("description", tache.getDescription())
                .append("categorie", tache.getCategorie())
                .append("dateDebut", tache.getDateDebut())
                .append("dateFin", tache.getDateFin())
                .append("docs", docs);

        tacheCollection.insertOne(document);
    }
    @Override
    public Tache read(ObjectId tacheID) {
        Document filter = new Document("_id", tacheID);
        Document result = tacheCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            String categorie = result.getString("categorie");
            Date dateDebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            List<Doc> docs = new ArrayList<>();
            List<Document> docDocs = (List<Document>) result.get("docs");
            if (docDocs != null) {
                for (Document docDoc : docDocs) {
                    String docDescription = docDoc.getString("description");
                    Date docDateAjout = docDoc.getDate("dateAjout");
                    docs.add(new Doc(tacheID, docDescription, docDateAjout));
                }
            }
            return new Tache( tacheID,description,categorie ,dateDebut, dateFin, docs);
        }
        return null;
    }

    @Override
    public void update(Tache updateTache, ObjectId id) {
        Document document = new Document("description", updateTache.getDescription())
                .append("categorie",updateTache.getCategorie())
                .append("dateDebut", updateTache.getDateDebut())
                .append("dateFin", updateTache.getDateFin());
        List<Document> docs = new ArrayList<>();
        if (updateTache.getDoc() != null) {
            for (Doc doc : updateTache.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        document.append("docs", docs);
        tacheCollection.replaceOne(Filters.eq("_id", id), document);
    }

    @Override
    public void delete(ObjectId id) {
        tacheCollection.deleteOne(new Document("_id", id));
    }
    public  List<Tache> getAll() {
        List<Tache> taches = new ArrayList<>();
        try (MongoCursor<Document> cursor = tacheCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                String categorie = result.getString("categorie");
                Date datedebut = result.getDate("dateDebut");
                Date dateFin = result.getDate("dateFin");
                List<Document> docs = (List<Document>) result.get("docs");
                List<Doc> documents = new ArrayList<>();
                if (docs != null) {
                    for (Document doc : docs) {
                        ObjectId docId = doc.getObjectId("_id");
                        String docDescription = doc.getString("description");
                        Date date = doc.getDate("dateAjout");
                        documents.add(new Doc(docId, docDescription, date));
                    }
                }
                Tache tache = new Tache(id, description, categorie, datedebut, dateFin, documents);
                taches.add(tache);
            }
            return taches;
        }
    }

    public  ObjectId getID(Tache tache){
        return tache.getID();
    }

    public Tache getTacheById(ObjectId id) {
        Document filter = new Document("_id", id);
        Document result = tacheCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            String categorie = result.getString("categorie");
            Date datedebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            List<Document> docs = (List<Document>) result.get("docs");
            List<Doc> documents = new ArrayList<>();
            if (docs != null) {
                for (Document doc : docs) {
                    ObjectId docId = doc.getObjectId("_id");
                    String docDescription = doc.getString("description");
                    Date date = doc.getDate("dateAjout");
                    documents.add(new Doc(docId, docDescription, date));
                }
            }
            return new Tache(id, description, categorie, datedebut, dateFin, documents);
        } else {
            System.out.println("Tâche non trouvée pour l'ID: " + id);
            return null;
        }
    }

}

