package controller;

import io.reflectoring.demo.entitities.Projet;
import services.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "*")
public class ProjetController {
    
    @Autowired
    private ProjetService projetService;
    
    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        Optional<Projet> projet = projetService.getProjetById(id);
        return projet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Projet>> getProjetsByCours(@PathVariable Long coursId) {
        List<Projet> projets = projetService.getProjetsByCours(coursId);
        return ResponseEntity.ok(projets);
    }
    
    @GetMapping("/instructeur/{instructeurId}")
    public ResponseEntity<List<Projet>> getProjetsByInstructeur(@PathVariable Long instructeurId) {
        List<Projet> projets = projetService.getProjetsByInstructeur(instructeurId);
        return ResponseEntity.ok(projets);
    }
    
    @PostMapping
    public ResponseEntity<?> createProjet(@Valid @RequestBody ProjetRequest projetRequest) {
        try {
            Projet projet = new Projet();
            projet.setTitre(projetRequest.getTitre());
            projet.setDescription(projetRequest.getDescription());
            projet.setDateLimite(projetRequest.getDateLimite());
            
            Projet nouveauProjet = projetService.createProjet(
                    projet, 
                    projetRequest.getCoursId(), 
                    projetRequest.getInstructeurId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauProjet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProjet(@PathVariable Long id, 
                                         @Valid @RequestBody Projet projetDetails) {
        try {
            Projet projetMisAJour = projetService.updateProjet(id, projetDetails);
            return ResponseEntity.ok(projetMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjet(@PathVariable Long id) {
        try {
            projetService.deleteProjet(id);
            return ResponseEntity.ok("Projet supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Classe interne pour la requête de création de projet
    static class ProjetRequest {
        private String titre;
        private String description;
        
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime dateLimite;
        
        private Long coursId;
        private Long instructeurId;
        
        public String getTitre() {
            return titre;
        }
        
        public void setTitre(String titre) {
            this.titre = titre;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public LocalDateTime getDateLimite() {
            return dateLimite;
        }
        
        public void setDateLimite(LocalDateTime dateLimite) {
            this.dateLimite = dateLimite;
        }
        
        public Long getCoursId() {
            return coursId;
        }
        
        public void setCoursId(Long coursId) {
            this.coursId = coursId;
        }
        
        public Long getInstructeurId() {
            return instructeurId;
        }
        
        public void setInstructeurId(Long instructeurId) {
            this.instructeurId = instructeurId;
        }
    }
}