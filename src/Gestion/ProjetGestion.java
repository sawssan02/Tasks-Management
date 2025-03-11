package Gestion;
import POJO.Doc;
import POJO.Projet;

import POJO.Seance;
import POJO.Tache;
import org.bson.types.ObjectId;
import persistence.ProjetDAO;

import java.util.ArrayList;
import java.util.List;

public class ProjetGestion {
    private ProjetDAO projetDAO;

    public ProjetGestion() {
        this.projetDAO = new ProjetDAO();
    }
    public void createProjet(Projet projet) {
        projetDAO.create(projet);
    }
    public Projet readProjet(ObjectId id) {
        return projetDAO.read(id);
    }
    public void updateProjet(Projet updatedProjet, ObjectId id) {
        projetDAO.update(updatedProjet,id);
    }
    public void deleteProjet(ObjectId id) {
        projetDAO.delete(id);
    }
    public List<Projet> searchByDescription(String description){
        return projetDAO.chercherProjetsParDescription(description);
    }

    public List<Projet> filtrerProjetType(String type){
        return projetDAO.filtrerProjetType(type);
    }
    public List<Projet> filtrerProjetCategorie(String categorie){
        return projetDAO.filtrerParCategorie(categorie);
    }
    public List<Tache> filtrerTachesParCategorie(String categorie){
        return projetDAO.filtrerTachesParCategorie(categorie);
    }
    public List<Tache> filtrerTachesParStatut(boolean statut){
        return projetDAO.filtrerTachesParStatut(statut);
    }
    public List<Tache> searchTacheByDescription(String description){
        return projetDAO.searchTacheByDescription(description);
    }
    public ObjectId getID(Projet projet){
        return projetDAO.getID(projet);

    }
    public Projet getProjetByID(ObjectId id){
        return projetDAO.getProjetById(id);
    }
    public List<Projet> getAllProjet(){
        return projetDAO.getAll();
    }
    public void ajouterDocumentAProjet(ObjectId projetId, Doc newDoc) {
        Projet projet = projetDAO.read(projetId);
        List<Doc> docs = projet.getDoc();
        if (docs == null) {
            docs = new ArrayList<>();
        }
        docs.add(newDoc);
        projet.setDoc(docs);
        projetDAO.update(projet, projetId);
    }
    public void ajouterSeanceAProjet(ObjectId projetId, Seance newSeance) {
        Projet projet = projetDAO.read(projetId);
        if (projet != null) {
            List<Seance> seances = projet.getSeances();
            if (seances == null) {
                seances = new ArrayList<>();
            }
            seances.add(newSeance);
            projetDAO.update(projet, projetId);
        }
    }
    public void ajouterTacheAProjet(ObjectId projetId, Tache newTache) {
        Projet projet = projetDAO.read(projetId);
        if (projet != null) {
            List<Tache> taches = projet.getTaches();
            if (taches == null) {
                taches = new ArrayList<>();
            }
            taches.add(newTache);
            projetDAO.update(projet, projetId);
        }
    }
    public List<Doc> chercherDocParDescription(String description){
        return projetDAO.chercherDocParDescription(description);
    }
    public List<Doc> chercherTacheParDescription(String description){
        return projetDAO.chercherTacheParDescription(description);
    }
    public List<Doc> chercherSeanceParDescription(String description){
        return projetDAO.chercherSeanceParDescription(description);
    }
    public ObjectId clonerProjet(ObjectId projetId) {
        Projet projetOriginal = projetDAO.read(projetId);
        if (projetOriginal != null) {
            Projet projetClone = new Projet();
            projetClone.setDescription(projetOriginal.getDescription());
            projetClone.setCategorie(projetOriginal.getCategorie());
            projetClone.setType(projetOriginal.getType());
            projetClone.setDateDebut(projetOriginal.getDateDebut());
            projetClone.setDateFin(projetOriginal.getDateFin());
            projetClone.setDoc(new ArrayList<>(projetOriginal.getDoc()));
            projetClone.setSeances(new ArrayList<>(projetOriginal.getSeances()));
            projetClone.setTaches(new ArrayList<>(projetOriginal.getTaches()));

            projetDAO.create(projetClone);
            return projetDAO.getID(projetClone);
        }
        return null;
    }
}
