package persistence;

import POJO.Projet;
import POJO.Seance;
import POJO.Tache;
import POJO.Doc;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.regex.Pattern;

public class ProjetDAO implements DAO<Projet> {
    MongoDatabase database = Connexion.getDatabase();
    MongoCollection<Document> projetCollection = database.getCollection("projets");

    @Override
    public void create(Projet projet) {
        Document document = new Document("categorie", projet.getCategorie())
                .append("type", projet.getType())
                .append("description", projet.getDescription())
                .append("dateDebut", projet.getDateDebut())
                .append("dateFin", projet.getDateFin());
        List<Document> seances = new ArrayList<>();
        if(projet.getSeances()!=null) {
            for (Seance seance : projet.getSeances()) {
                List<Document> docs = new ArrayList<>();
                if (seance.getDoc() != null) {
                    for (Doc doc : seance.getDoc()) {
                        docs.add(new Document("description", doc.getDescription())
                                .append("dateAjout", doc.getDateAjout())
                        );
                    }
                }
                seances.add(new Document("description", seance.getDescription())
                        .append("dateDebut", seance.getDateDebut())
                        .append("dateFin", seance.getDateFin())
                        .append("note", seance.getNote())
                        .append("documents", docs)
                );
            }
        }
        document.append("seances", seances);
        List<Document> docs = new ArrayList<>();
        if(projet.getDoc()!=null){
            for (Doc doc : projet.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        document.append("docs", docs);
        List<Document> taches = new ArrayList<>();
        if(projet.getTaches()!=null) {
            for (Tache tache : projet.getTaches()) {
                List<Document> docus = new ArrayList<>();
                if (tache.getDoc() != null) {
                    for (Doc doc : tache.getDoc()) {
                        docus.add(new Document("description", doc.getDescription())
                                .append("dateAjout", doc.getDateAjout())
                        );
                    }
                }
                taches.add(new Document("categorie", tache.getCategorie())
                        .append("description", tache.getDescription())
                        .append("dateDebut", tache.getDateDebut())
                        .append("dateFin", tache.getDateFin())
                        .append("documents", docus)
                );
            }
        }
        document.append("taches", taches);

        projetCollection.insertOne(document);
    }

    public Projet read(ObjectId projetID) {
        Document filter = new Document("_id", projetID);
        Document result = projetCollection .find(filter).first();

        if (result != null) {
            String categorie = result.getString("categorie");
            String type = result.getString("type");
            String description = result.getString("description");
            Date dateDebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            List<Seance> seances = new ArrayList<>();
            List<Document> seancesDocs = (List<Document>) result.get("seances");
            if(seancesDocs!=null) {
                for (Document seanceDoc : seancesDocs) {
                    ObjectId IdSeance = seanceDoc.getObjectId("_id");
                    String seanceDescription = seanceDoc.getString("description");
                    Date seanceDateDebut = seanceDoc.getDate("dateDebut");
                    Date seanceDateFin = seanceDoc.getDate("dateFin");
                    String note = seanceDoc.getString("note");
                    List<Doc> seanceDocs = new ArrayList<>();
                    List<Document> docDocs = (List<Document>) seanceDoc.get("documents");
                    if(docDocs!=null) {
                        for (Document docDoc : docDocs) {
                            ObjectId Id = docDoc.getObjectId("_id");
                            String docDescription = docDoc.getString("description");
                            Date docDateAjout = docDoc.getDate("dateAjout");
                            seanceDocs.add(new Doc(Id, docDescription, docDateAjout));
                        }
                    }
                    seances.add(new Seance(IdSeance, seanceDescription, seanceDateDebut, seanceDateFin, note, seanceDocs));
                }
            }
            List<Doc> docs = new ArrayList<>();
            List<Document> docDocs = (List<Document>) result.get("docs");
            if(docDocs!=null) {
                for (Document docDoc : docDocs) {
                    ObjectId objectId = docDoc.getObjectId("_id");
                    String docDescription = docDoc.getString("description");
                    Date docDateAjout = docDoc.getDate("dateAjout");
                    docs.add(new Doc(objectId, docDescription, docDateAjout));
                }
            }
            List<Tache> taches = new ArrayList<>();
            List<Document> tachesDocs = (List<Document>) result.get("taches");
            if(tachesDocs!=null) {
                for (Document tacheDoc : tachesDocs) {
                    ObjectId ID = tacheDoc.getObjectId("_id");
                    String tacheCategorie = tacheDoc.getString("categorie");
                    String tacheDescription = tacheDoc.getString("description");
                    Date tacheDateDebut = tacheDoc.getDate("dateDebut");
                    Date tacheDateFin = tacheDoc.getDate("dateFin");
                    List<Doc> tacheDocs = new ArrayList<>();
                    List<Document> docuDocs = (List<Document>) tacheDoc.get("documents");
                    if(docuDocs!=null) {
                        for (Document docDoc : docDocs) {
                            ObjectId objectId = docDoc.getObjectId("_id");
                            String docDescription = docDoc.getString("description");
                            Date docDateAjout = docDoc.getDate("dateAjout");
                            tacheDocs.add(new Doc(objectId, docDescription, docDateAjout));
                        }
                    }
                    taches.add(new Tache(ID, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                }
            }
            return new Projet(projetID,categorie, type, description, dateDebut, dateFin, seances, docs, taches);
        }
        return null;
    }
    public void update(Projet updatedProjet,ObjectId id) {
        Document document = new Document("_id", id)
                .append("categorie", updatedProjet.getCategorie())
                .append("type", updatedProjet.getType())
                .append("description", updatedProjet.getDescription())
                .append("dateDebut", updatedProjet.getDateDebut())
                .append("dateFin", updatedProjet.getDateFin());
        List<Document> seances = new ArrayList<>();
        if(updatedProjet.getSeances()!=null) {
            for (Seance seance : updatedProjet.getSeances()) {
                Document seanceDoc = new Document("_id", seance.getID())
                        .append("description", seance.getDescription())
                        .append("dateDebut", seance.getDateDebut())
                        .append("dateFin", seance.getDateFin())
                        .append("note", seance.getNote());
                List<Document> docs = new ArrayList<>();
                if (seance.getDoc() != null) {
                    for (Doc doc : seance.getDoc()) {
                        docs.add(new Document("description", doc.getDescription())
                                .append("dateAjout", doc.getDateAjout())
                        );
                    }
                }
                seanceDoc.append("documents", docs);
                seances.add(seanceDoc);
            }
        }
        document.append("seances", seances);
        List<Document> docs = new ArrayList<>();
        if(updatedProjet.getDoc()!=null) {
            for (Doc doc : updatedProjet.getDoc()) {
                docs.add(new Document("description", doc.getDescription())
                        .append("dateAjout", doc.getDateAjout())
                );
            }
        }
        document.append("docs", docs);
        List<Document> taches = new ArrayList<>();
        if(updatedProjet.getTaches()!=null) {
            for (Tache tache : updatedProjet.getTaches()) {
                Document tacheDoc = new Document("categorie", tache.getCategorie())
                        .append("description", tache.getDescription())
                        .append("dateDebut", tache.getDateDebut())
                        .append("dateFin", tache.getDateFin());

                List<Document> docus = new ArrayList<>();
                if(tache.getDoc()!=null){
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
        projetCollection .replaceOne(Filters.eq("_id", id), document);
    }
    public void delete(ObjectId id) {
        projetCollection .deleteOne(new Document("_id", id));
    }
    public List<Projet> getAll() {
        List<Projet> projets = new ArrayList<>();
        try (MongoCursor<Document> cursor = projetCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                String type = result.getString("type");
                String categorie = result.getString("categorie");
                Date dateDebut = result.getDate("dateDebut");
                Date dateFin = result.getDate("dateFin");
                List<Document> docs = (List<Document>) result.get("docs");
                List<Doc> documents = new ArrayList<>();
                if (docs != null) {
                    for (Document doc : docs) {
                        ObjectId docId = doc.getObjectId("_id");
                        String docDescription = doc.getString("description");
                        Date docDateAjout = doc.getDate("dateAjout");
                        documents.add(new Doc(docId, docDescription, docDateAjout));
                    }
                }
                List<Seance> seances = new ArrayList<>();
                List<Document> seancesDocs = (List<Document>) result.get("seances");
                if (seancesDocs != null) {
                    for (Document seanceDoc : seancesDocs) {
                        ObjectId seanceId = seanceDoc.getObjectId("_id");
                        String seanceDescription = seanceDoc.getString("description");
                        Date seanceDateDebut = seanceDoc.getDate("dateDebut");
                        Date seanceDateFin = seanceDoc.getDate("dateFin");
                        String note = seanceDoc.getString("note");

                        List<Doc> seanceDocs = new ArrayList<>();
                        List<Document> seanceDocDocs = (List<Document>) seanceDoc.get("documents");
                        if (seanceDocDocs != null) {
                            for (Document docDoc : seanceDocDocs) {
                                ObjectId docId = docDoc.getObjectId("_id");
                                String docDescription = docDoc.getString("description");
                                Date docDateAjout = docDoc.getDate("dateAjout");
                                seanceDocs.add(new Doc(docId, docDescription, docDateAjout));
                            }
                        }

                        seances.add(new Seance(seanceId, seanceDescription, seanceDateDebut, seanceDateFin, note, seanceDocs));
                    }
                }
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

                projets.add(new Projet(id, categorie, type, description, dateDebut, dateFin, seances, documents, taches));
            }
            return projets;
        }
    }
    public List<Projet> chercherProjetsParDescription(String description) {
        List<Projet> projetsFiltres = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Projet projet : getAll()) {
            if (keyword.matcher(projet.getDescription()).find()) {
                projetsFiltres.add(projet);
            }
        }
        return projetsFiltres;
    }

    public  ObjectId getID(Projet projet){
        return projet.getID();
    }
    public Projet getProjetById(ObjectId id) {
        Document filter = new Document("_id", id);
        Document result = projetCollection.find(filter).first();
        if (result != null) {
            String categorie = result.getString("categorie");
            String type = result.getString("type");
            String description = result.getString("description");
            Date dateDebut = result.getDate("dateDebut");
            Date dateFin = result.getDate("dateFin");
            List<Document> docs = (List<Document>) result.get("docs");
            List<Doc> documents = new ArrayList<>();
            if (docs != null) {
                for (Document doc : docs) {
                    ObjectId docId = doc.getObjectId("_id");
                    String docDescription = doc.getString("description");
                    Date docDateAjout = doc.getDate("dateAjout");
                    documents.add(new Doc(docId, docDescription, docDateAjout));
                }
            }
            List<Seance> seances = new ArrayList<>();
            List<Document> seancesDocs = (List<Document>) result.get("seances");
            if (seancesDocs != null) {
                for (Document seanceDoc : seancesDocs) {
                    ObjectId seanceId = seanceDoc.getObjectId("_id");
                    String seanceDescription = seanceDoc.getString("description");
                    Date seanceDateDebut = seanceDoc.getDate("dateDebut");
                    Date seanceDateFin = seanceDoc.getDate("dateFin");
                    String note = seanceDoc.getString("note");
                    List<Doc> seanceDocs = new ArrayList<>();
                    List<Document> seanceDocDocs = (List<Document>) seanceDoc.get("documents");
                    if (seanceDocDocs != null) {
                        for (Document docDoc : seanceDocDocs) {
                            ObjectId docId = docDoc.getObjectId("_id");
                            String docDescription = docDoc.getString("description");
                            Date docDateAjout = docDoc.getDate("dateAjout");
                            seanceDocs.add(new Doc(docId, docDescription, docDateAjout));
                        }
                    }
                    seances.add(new Seance(seanceId, seanceDescription, seanceDateDebut, seanceDateFin, note, seanceDocs));
                }
            }
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
            return new Projet(id, categorie, type, description, dateDebut, dateFin, seances, documents, taches);
        } else {
            System.out.println("Project not found for ID: " + id);
            return null;
        }
    }
    public List<Projet> filtrerParCategorie(String categorie) {
        List<Projet> projets = new ArrayList<>();
        Document filter = new Document("categorie", categorie);
        try (MongoCursor<Document> cursor = projetCollection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                String type = result.getString("type");
                Date dateDebut = result.getDate("dateDebut");
                Date dateFin = result.getDate("dateFin");
                List<Document> docs = (List<Document>) result.get("docs");
                List<Doc> documents = new ArrayList<>();
                if (docs != null) {
                    for (Document doc : docs) {
                        ObjectId docId = doc.getObjectId("_id");
                        String docDescription = doc.getString("description");
                        Date docDateAjout = doc.getDate("dateAjout");
                        documents.add(new Doc(docId, docDescription, docDateAjout));
                    }
                }
                List<Seance> seances = new ArrayList<>();
                List<Document> seancesDocs = (List<Document>) result.get("seances");
                if (seancesDocs != null) {
                    for (Document seanceDoc : seancesDocs) {
                        ObjectId seanceId = seanceDoc.getObjectId("_id");
                        String seanceDescription = seanceDoc.getString("description");
                        Date seanceDateDebut = seanceDoc.getDate("dateDebut");
                        Date seanceDateFin = seanceDoc.getDate("dateFin");
                        String note = seanceDoc.getString("note");

                        List<Doc> seanceDocs = new ArrayList<>();
                        List<Document> seanceDocDocs = (List<Document>) seanceDoc.get("documents");
                        if (seanceDocDocs != null) {
                            for (Document docDoc : seanceDocDocs) {
                                ObjectId docId = docDoc.getObjectId("_id");
                                String docDescription = docDoc.getString("description");
                                Date docDateAjout = docDoc.getDate("dateAjout");
                                seanceDocs.add(new Doc(docId, docDescription, docDateAjout));
                            }
                        }

                        seances.add(new Seance(seanceId, seanceDescription, seanceDateDebut, seanceDateFin, note, seanceDocs));
                    }
                }

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
                projets.add(new Projet(id, categorie, type, description, dateDebut, dateFin, seances, documents, taches));
            }

        return projets;
        }
    }
    public List<Projet> filtrerProjetType(String type) {
        List<Projet> projets = new ArrayList<>();
        Document filter = new Document("type",type);
        try (MongoCursor<Document> cursor = projetCollection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                ObjectId id = result.getObjectId("_id");
                String description = result.getString("description");
                String categorie = result.getString("categorie");
                Date dateDebut = result.getDate("dateDebut");
                Date dateFin = result.getDate("dateFin");
                List<Document> docs = (List<Document>) result.get("docs");
                List<Doc> documents = new ArrayList<>();
                if (docs != null) {
                    for (Document doc : docs) {
                        ObjectId docId = doc.getObjectId("_id");
                        String docDescription = doc.getString("description");
                        Date docDateAjout = doc.getDate("dateAjout");
                        documents.add(new Doc(docId, docDescription, docDateAjout));
                    }
                }
                List<Seance> seances = new ArrayList<>();
                List<Document> seancesDocs = (List<Document>) result.get("seances");
                if (seancesDocs != null) {
                    for (Document seanceDoc : seancesDocs) {
                        ObjectId seanceId = seanceDoc.getObjectId("_id");
                        String seanceDescription = seanceDoc.getString("description");
                        Date seanceDateDebut = seanceDoc.getDate("dateDebut");
                        Date seanceDateFin = seanceDoc.getDate("dateFin");
                        String note = seanceDoc.getString("note");

                        List<Doc> seanceDocs = new ArrayList<>();
                        List<Document> seanceDocDocs = (List<Document>) seanceDoc.get("documents");
                        if (seanceDocDocs != null) {
                            for (Document docDoc : seanceDocDocs) {
                                ObjectId docId = docDoc.getObjectId("_id");
                                String docDescription = docDoc.getString("description");
                                Date docDateAjout = docDoc.getDate("dateAjout");
                                seanceDocs.add(new Doc(docId, docDescription, docDateAjout));
                            }
                        }

                        seances.add(new Seance(seanceId, seanceDescription, seanceDateDebut, seanceDateFin, note, seanceDocs));
                    }
                }

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
                projets.add(new Projet(id, categorie, type, description, dateDebut, dateFin, seances, documents, taches));
            }

            return projets;
        }
    }
    public List<Tache> filtrerTachesParCategorie(String categorieTache) {
        List<Tache> tachesFiltres = new ArrayList<>();
        try (MongoCursor<Document> cursor = projetCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                List<Document> tachesDocs = (List<Document>) result.get("taches");
                if (tachesDocs != null) {
                    for (Document tacheDoc : tachesDocs) {
                        String tacheCategorie = tacheDoc.getString("categorie");
                        if (categorieTache.equals(tacheCategorie)) {
                            ObjectId tacheId = tacheDoc.getObjectId("_id");
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

                            tachesFiltres.add(new Tache(tacheId, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                        }
                    }
                }
            }
        }
        return tachesFiltres;
    }
    public List<Tache> filtrerTachesParStatut(boolean fini) {
        List<Tache> tachesFiltres = new ArrayList<>();
        Date now = new Date();

        try (MongoCursor<Document> cursor = projetCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                List<Document> tachesDocs = (List<Document>) result.get("taches");
                if (tachesDocs != null) {
                    for (Document tacheDoc : tachesDocs) {
                        Date tacheDateFin = tacheDoc.getDate("dateFin");
                        if ((fini && tacheDateFin != null && tacheDateFin.before(now)) ||
                                (!fini && (tacheDateFin == null || tacheDateFin.after(now)))) {
                            ObjectId tacheId = tacheDoc.getObjectId("_id");
                            String tacheCategorie = tacheDoc.getString("categorie");
                            String tacheDescription = tacheDoc.getString("description");
                            Date tacheDateDebut = tacheDoc.getDate("dateDebut");

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
                            tachesFiltres.add(new Tache(tacheId, tacheCategorie, tacheDescription, tacheDateDebut, tacheDateFin, tacheDocs));
                        }
                    }
                }
            }
        }
        return tachesFiltres;
    }
    public List<Tache> searchTacheByDescription(String description) {
        List<Tache> tachesFiltrees = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Projet projet : getAll()) {
            for (Tache tache : projet.getTaches()) {
                if (keyword.matcher(tache.getDescription()).find()) {
                    tachesFiltrees.add(tache);
                }
            }
        }
        return tachesFiltrees;
    }
    public List<Doc> chercherDocParDescription(String description){
        List<Doc> docs = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Projet projet : getAll()) {
            for (Doc doc : projet.getDoc()) {
                if (keyword.matcher(doc.getDescription()).find()) {
                    docs.add(doc);
                }
            }
        }
        return docs;
    }
    public List<Doc> chercherTacheParDescription(String description){
        List<Doc> docs = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Projet projet : getAll()) {
            for (Tache tache :projet.getTaches()) {
                for (Doc doc : tache.getDoc()) {
                    if (keyword.matcher(doc.getDescription()).find()) {
                        docs.add(doc);
                    }
                }
            }
        }
        return docs;
    }
    public List<Doc> chercherSeanceParDescription(String description){
        List<Doc> docs = new ArrayList<>();
        Pattern keyword = Pattern.compile(description, Pattern.CASE_INSENSITIVE);
        for (Projet projet : getAll()) {
            for (Seance sean :projet.getSeances()) {
                for (Doc doc : sean.getDoc()) {
                    if (keyword.matcher(doc.getDescription()).find()) {
                        docs.add(doc);
                    }
                }
            }
        }
        return docs;
    }

}

