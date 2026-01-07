package controller;



import io.reflectoring.demo.entitities.Commentaire;
import services.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commentaires")
@CrossOrigin(origins = "*")
public class CommentaireController {
    
    @Autowired
    private CommentaireService commentaireService;
    
    @GetMapping
    public ResponseEntity<List<Commentaire>> getAllCommentaires() {
        List<Commentaire> commentaires = commentaireService.getAllCommentaires();
        return ResponseEntity.ok(commentaires);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Commentaire> getCommentaireById(@PathVariable Long id) {
        Optional<Commentaire> commentaire = commentaireService.getCommentaireById(id);
        return commentaire.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/soumission/{soumissionId}")
    public ResponseEntity<List<Commentaire>> getCommentairesBySoumission(@PathVariable Long soumissionId) {
        List<Commentaire> commentaires = commentaireService.getCommentairesBySoumission(soumissionId);
        return ResponseEntity.ok(commentaires);
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Commentaire>> getCommentairesByCours(@PathVariable Long coursId) {
        List<Commentaire> commentaires = commentaireService.getCommentairesByCours(coursId);
        return ResponseEntity.ok(commentaires);
    }
    
    @GetMapping("/auteur/{auteurId}")
    public ResponseEntity<List<Commentaire>> getCommentairesByAuteur(@PathVariable Long auteurId) {
        List<Commentaire> commentaires = commentaireService.getCommentairesByAuteur(auteurId);
        return ResponseEntity.ok(commentaires);
    }
    
    @PostMapping("/soumission")
    public ResponseEntity<?> createCommentaireSurSoumission(
            @Valid @RequestBody CommentaireSoumissionRequest request) {
        try {
            Commentaire commentaire = new Commentaire();
            commentaire.setTexte(request.getTexte());
            
            Commentaire nouveauCommentaire = commentaireService.createCommentaireSurSoumission(
                    commentaire,
                    request.getAuteurId(),
                    request.getSoumissionId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCommentaire);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/cours")
    public ResponseEntity<?> createCommentaireSurCours(
            @Valid @RequestBody CommentaireCoursRequest request) {
        try {
            Commentaire commentaire = new Commentaire();
            commentaire.setTexte(request.getTexte());
            
            Commentaire nouveauCommentaire = commentaireService.createCommentaireSurCours(
                    commentaire,
                    request.getAuteurId(),
                    request.getCoursId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCommentaire);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommentaire(@PathVariable Long id,
                                              @Valid @RequestBody Commentaire commentaireDetails) {
        try {
            Commentaire commentaireMisAJour = commentaireService.updateCommentaire(id, commentaireDetails);
            return ResponseEntity.ok(commentaireMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentaire(@PathVariable Long id) {
        try {
            commentaireService.deleteCommentaire(id);
            return ResponseEntity.ok("Commentaire supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Classes internes pour les requêtes
    static class CommentaireSoumissionRequest {
        private String texte;
        private Long auteurId;
        private Long soumissionId;
        
        public String getTexte() {
            return texte;
        }
        
        public void setTexte(String texte) {
            this.texte = texte;
        }
        
        public Long getAuteurId() {
            return auteurId;
        }
        
        public void setAuteurId(Long auteurId) {
            this.auteurId = auteurId;
        }
        
        public Long getSoumissionId() {
            return soumissionId;
        }
        
        public void setSoumissionId(Long soumissionId) {
            this.soumissionId = soumissionId;
        }
    }
    
    static class CommentaireCoursRequest {
        private String texte;
        private Long auteurId;
        private Long coursId;
        
        public String getTexte() {
            return texte;
        }
        
        public void setTexte(String texte) {
            this.texte = texte;
        }
        
        public Long getAuteurId() {
            return auteurId;
        }
        
        public void setAuteurId(Long auteurId) {
            this.auteurId = auteurId;
        }
        
        public Long getCoursId() {
            return coursId;
        }
        
        public void setCoursId(Long coursId) {
            this.coursId = coursId;
        }
    }
}