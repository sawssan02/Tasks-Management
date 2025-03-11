package persistence;

import POJO.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListeDAO implements DAO<Liste> {
    MongoDatabase database = Connexion.getDatabase();
    MongoCollection<Document> listeCollection = database.getCollection("listes");

    @Override
    public void create(Liste liste) {
        Document document = new Document("description", liste.getDescription());
        List<Document> taches = new ArrayList<>();
        if(liste.getTaches()!=null) {
            for (Tache tache : liste.getTaches()) {
                Document tacheDoc = new Document("categorie", tache.getCategorie())
                        .append("description", tache.getDescription())
                        .append("dateDebut", tache.getDateDebut())
                        .append("dateFin", tache.getDateFin());
                List<Document> documents = new ArrayList<>();
                if(tache.getDoc()!=null) {
                    for (Doc doc : tache.getDoc()) {
                        documents.add(new Document("description", doc.getDescription())
                                .append("dateAjout", doc.getDateAjout()));
                    }
                }
                tacheDoc.append("documents", documents);
                taches.add(tacheDoc);
            }
        }
        document.append("taches", taches);
        listeCollection.insertOne(document);
    }


    @Override
    public Liste read(ObjectId listeID) {
        Document filter = new Document("_id", listeID);
        Document result = listeCollection.find(filter).first();

        if (result != null) {
            String description = result.getString("description");
            List<Tache> taches = new ArrayList<>();
            List<Document> tachesDocs = (List<Document>) result.get("taches");
            if(tachesDocs!=null) {
                for (Document tacheDoc : tachesDocs) {
                    ObjectId tacheID = tacheDoc.getObjectId("_id");
                    String tacheCategorie = tacheDoc.getString("categorie");
                    String tacheDescription = tacheDoc.getString("description");
                    Date tacheDateDebut = tacheDoc.getDate("dateDebut");
                    Date tacheDateFin = tacheDoc.getDate("dateFin");

                    List<Doc> tacheDocs = new ArrayList<>();
                    List<Document> docDocs = (List<Document>) tacheDoc.get("documents");
                    if(docDocs!=null) {
                        for (Document docDoc : docDocs) {
                            ObjectId objectId = docDoc.getObjectId("_id");
                            String docDescription = docDoc.getString("description");
                            Date docDateAjout = docDoc.getDate("dateAjout");
                            tacheDocs.add(new Doc(objectId, docDescription, docDateAjout));
                        }
                    }
                    taches.add(new Tache(tacheID, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                }
            }
            return new Liste(listeID,description, taches);
        }
        return null;
    }

    @Override
    public void update(Liste updateListe, ObjectId id) {
        Document document = new Document("description", updateListe.getDescription());
        List<Document> taches = new ArrayList<>();
        if(updateListe.getTaches()!=null) {
            for (Tache tache : updateListe.getTaches()) {
                Document tacheDoc = new Document("categorie", tache.getCategorie())
                        .append("description", tache.getDescription())
                        .append("dateDebut", tache.getDateDebut())
                        .append("dateFin", tache.getDateFin());

                List<Document> docus = new ArrayList<>();
                if (tache.getDoc() != null) {
                    for (Doc doc : tache.getDoc()) {
                        docus.add(new Document("description", doc.getDescription())
                                .append("dateAjout", doc.getDateAjout())
                        );
                    }
                }
                tacheDoc.append("documents", docus);
                taches.add(tacheDoc);
            }
        }
        document.append("taches", taches);
        listeCollection.replaceOne(Filters.eq("_id", id), document);
    }

    @Override
    public void delete(ObjectId id) {
        listeCollection.deleteOne(new Document("_id", id));
    }
    public List<Liste> getAll() {
        List<Liste> listes = new ArrayList<>();
        try (MongoCursor<Document> cursor = listeCollection.find().iterator()) {//récupérer les données depuis la collection listeCollection
            while (cursor.hasNext()) { //parcourir tous les documents
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                List<Tache> taches = new ArrayList<>();
                List<Document> tachesDocs = (List<Document>) result.get("taches");
                if (tachesDocs != null) {
                    for (Document tacheDoc : tachesDocs) {
                        ObjectId tacheId = tacheDoc.getObjectId("_id");
                        String tacheCategorie = tacheDoc.getString("categorie");
                        String tacheDescription = tacheDoc.getString("description");
                        Date tacheDateDebut = tacheDoc.getDate("dateDebut");
                        Date tacheDateFin = tacheDoc.getDate("dateFin");

                        List<Doc> tacheDocs = new ArrayList<>();
                        List<Document> tacheDocDocs = (List<Document>) tacheDoc.get("documents");
                        if (tacheDocDocs != null) {
                            for (Document docDoc : tacheDocDocs) {
                                ObjectId docId = docDoc.getObjectId("_id");
                                String docDescription = docDoc.getString("description");
                                Date docDateAjout = docDoc.getDate("dateAjout");
                                tacheDocs.add(new Doc(docId, docDescription, docDateAjout));
                            }
                        }

                        taches.add(new Tache(tacheId, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                    }
                }

                listes.add(new Liste(id, description,taches));
            }
            return listes;
        }
    }
    public  ObjectId getID(Liste projet){
        return projet.getID();
    }
    public Liste getProjetById(ObjectId id) {
        Document filter = new Document("_id", id);
        Document result = listeCollection.find(filter).first();
        if (result != null) {
            String description = result.getString("description");
            List<Tache> taches = new ArrayList<>();
            List<Document> tachesDocs = (List<Document>) result.get("taches");
            if (tachesDocs != null) {
                for (Document tacheDoc : tachesDocs) {
                    ObjectId tacheId = tacheDoc.getObjectId("_id");
                    String tacheCategorie = tacheDoc.getString("categorie");
                    String tacheDescription = tacheDoc.getString("description");
                    Date tacheDateDebut = tacheDoc.getDate("dateDebut");
                    Date tacheDateFin = tacheDoc.getDate("dateFin");

                    List<Doc> tacheDocs = new ArrayList<>();
                    List<Document> tacheDocDocs = (List<Document>) tacheDoc.get("documents");
                    if (tacheDocDocs != null) {
                        for (Document docDoc : tacheDocDocs) {
                            ObjectId docId = docDoc.getObjectId("_id");
                            String docDescription = docDoc.getString("description");
                            Date docDateAjout = docDoc.getDate("dateAjout");
                            tacheDocs.add(new Doc(docId, docDescription, docDateAjout));
                        }
                    }

                    taches.add(new Tache(tacheId, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                }
            }

            return new Liste(id, description, taches);
        } else {
            System.out.println("Project not found: " + id);
            return null;
        }
    }
}

