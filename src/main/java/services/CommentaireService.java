package services;



import io.reflectoring.demo.entitities.Commentaire;
import io.reflectoring.demo.entitities.Utilisateur;
import io.reflectoring.demo.entitities.Cours;

import io.reflectoring.demo.entitities.SoumissionEtudiant;
import repository.CommentaireRepository;
import repository.CoursRepository;
import repository.SoumissionEtudiantRepository;
import repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentaireService {
    
    @Autowired
    private CommentaireRepository commentaireRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private SoumissionEtudiantRepository soumissionRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }
    
    public Optional<Commentaire> getCommentaireById(Long id) {
        return commentaireRepository.findById(id);
    }
    
    public List<Commentaire> getCommentairesBySoumission(Long soumissionId) {
        return commentaireRepository.findBySoumissionId(soumissionId);
    }
    
    public List<Commentaire> getCommentairesByCours(Long coursId) {
        return commentaireRepository.findByCoursId(coursId);
    }
    
    public List<Commentaire> getCommentairesByAuteur(Long auteurId) {
        return commentaireRepository.findByAuteurId(auteurId);
    }
    
    public Commentaire createCommentaireSurSoumission(Commentaire commentaire, Long auteurId, Long soumissionId) {
        Utilisateur auteur = utilisateurRepository.findById(auteurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + auteurId));
        
        SoumissionEtudiant soumission = soumissionRepository.findById(soumissionId)
                .orElseThrow(() -> new RuntimeException("Soumission non trouvée avec l'id: " + soumissionId));
        
        commentaire.setAuteur(auteur);
        commentaire.setSoumission(soumission);
        commentaire.setDateCommentaire(LocalDateTime.now());
        
        return commentaireRepository.save(commentaire);
    }
    
    public Commentaire createCommentaireSurCours(Commentaire commentaire, Long auteurId, Long coursId) {
        Utilisateur auteur = utilisateurRepository.findById(auteurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + auteurId));
        
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'id: " + coursId));
        
        commentaire.setAuteur(auteur);
        commentaire.setCours(cours);
        commentaire.setDateCommentaire(LocalDateTime.now());
        
        return commentaireRepository.save(commentaire);
    }
    
    public Commentaire updateCommentaire(Long id, Commentaire commentaireDetails) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'id: " + id));
        
        commentaire.setTexte(commentaireDetails.getTexte());
        
        return commentaireRepository.save(commentaire);
    }
    
    public void deleteCommentaire(Long id) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé avec l'id: " + id));
        commentaireRepository.delete(commentaire);
    }
}