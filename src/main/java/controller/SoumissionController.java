package controller;

import io.reflectoring.demo.entitities.SoumissionEtudiant;
import services.SoumissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/soumissions")
@CrossOrigin(origins = "*")
public class SoumissionController {
    
    @Autowired
    private SoumissionService soumissionService;
    
    @GetMapping
    public ResponseEntity<List<SoumissionEtudiant>> getAllSoumissions() {
        List<SoumissionEtudiant> soumissions = soumissionService.getAllSoumissions();
        return ResponseEntity.ok(soumissions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SoumissionEtudiant> getSoumissionById(@PathVariable Long id) {
        Optional<SoumissionEtudiant> soumission = soumissionService.getSoumissionById(id);
        return soumission.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<SoumissionEtudiant>> getSoumissionsByEtudiant(@PathVariable Long etudiantId) {
        List<SoumissionEtudiant> soumissions = soumissionService.getSoumissionsByEtudiant(etudiantId);
        return ResponseEntity.ok(soumissions);
    }
    
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<SoumissionEtudiant>> getSoumissionsByProjet(@PathVariable Long projetId) {
        List<SoumissionEtudiant> soumissions = soumissionService.getSoumissionsByProjet(projetId);
        return ResponseEntity.ok(soumissions);
    }
    
    @PostMapping
    public ResponseEntity<?> createSoumission(@Valid @RequestBody SoumissionRequest soumissionRequest) {
        try {
            SoumissionEtudiant soumission = new SoumissionEtudiant();
            soumission.setFichierUrl(soumissionRequest.getFichierUrl());
            
            SoumissionEtudiant nouvelleSoumission = soumissionService.createSoumission(
                    soumission,
                    soumissionRequest.getEtudiantId(),
                    soumissionRequest.getProjetId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleSoumission);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSoumission(@PathVariable Long id,
                                             @Valid @RequestBody SoumissionEtudiant soumissionDetails) {
        try {
            SoumissionEtudiant soumissionMiseAJour = soumissionService.updateSoumission(id, soumissionDetails);
            return ResponseEntity.ok(soumissionMiseAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/noter")
    public ResponseEntity<?> noterSoumission(@PathVariable Long id,
                                            @RequestBody NotationRequest notationRequest) {
        try {
            SoumissionEtudiant soumissionNotee = soumissionService.noterSoumission(
                    id,
                    notationRequest.getNote(),
                    notationRequest.getCommentaires()
            );
            return ResponseEntity.ok(soumissionNotee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSoumission(@PathVariable Long id) {
        try {
            soumissionService.deleteSoumission(id);
            return ResponseEntity.ok("Soumission supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Classe interne pour la requête de création de soumission
    static class SoumissionRequest {
        private Long etudiantId;
        private Long projetId;
        private String fichierUrl;
        
        public Long getEtudiantId() {
            return etudiantId;
        }
        
        public void setEtudiantId(Long etudiantId) {
            this.etudiantId = etudiantId;
        }
        
        public Long getProjetId() {
            return projetId;
        }
        
        public void setProjetId(Long projetId) {
            this.projetId = projetId;
        }
        
        public String getFichierUrl() {
            return fichierUrl;
        }
        
        public void setFichierUrl(String fichierUrl) {
            this.fichierUrl = fichierUrl;
        }
    }
    
    // Classe interne pour la requête de notation
    static class NotationRequest {
        private Double note;
        private String commentaires;
        
        public Double getNote() {
            return note;
        }
        
        public void setNote(Double note) {
            this.note = note;
        }
        
        public String getCommentaires() {
            return commentaires;
        }
        
        public void setCommentaires(String commentaires) {
            this.commentaires = commentaires;
        }
    }
}