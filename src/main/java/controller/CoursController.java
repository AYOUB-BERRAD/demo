package controller;


import io.reflectoring.demo.entitities.Cours;
import services.CoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cours")
@CrossOrigin(origins = "*")
public class CoursController {
    
    @Autowired
    private CoursService coursService;
    
    @GetMapping
    public ResponseEntity<List<Cours>> getAllCours() {
        List<Cours> cours = coursService.getAllCours();
        return ResponseEntity.ok(cours);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cours> getCoursById(@PathVariable Long id) {
        Optional<Cours> cours = coursService.getCoursById(id);
        return cours.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Cours> getCoursByCode(@PathVariable String code) {
        Optional<Cours> cours = coursService.getCoursByCode(code);
        return cours.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/instructeur/{instructeurId}")
    public ResponseEntity<List<Cours>> getCoursByInstructeur(@PathVariable Long instructeurId) {
        List<Cours> cours = coursService.getCoursByInstructeur(instructeurId);
        return ResponseEntity.ok(cours);
    }
    
    @PostMapping
    public ResponseEntity<?> createCours(@Valid @RequestBody CoursRequest coursRequest) {
        try {
            Cours cours = new Cours();
            cours.setTitre(coursRequest.getTitre());
            cours.setDescription(coursRequest.getDescription());
            cours.setCode(coursRequest.getCode());
            
            Cours nouveauCours = coursService.createCours(cours, coursRequest.getInstructeurId());
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauCours);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCours(@PathVariable Long id, 
                                        @Valid @RequestBody Cours coursDetails) {
        try {
            Cours coursMisAJour = coursService.updateCours(id, coursDetails);
            return ResponseEntity.ok(coursMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCours(@PathVariable Long id) {
        try {
            coursService.deleteCours(id);
            return ResponseEntity.ok("Cours supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // Classe interne pour la requête de création de cours
    static class CoursRequest {
        private String titre;
        private String description;
        private String code;
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
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public Long getInstructeurId() {
            return instructeurId;
        }
        
        public void setInstructeurId(Long instructeurId) {
            this.instructeurId = instructeurId;
        }
    }
}