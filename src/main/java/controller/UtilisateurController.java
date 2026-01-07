package controller;



import io.reflectoring.demo.entitities.Utilisateur;
import io.reflectoring.demo.entitities.Utilisateur.Role;
import services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*")
public class UtilisateurController {
    
    @Autowired
    private UtilisateurService utilisateurService;
    
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        return utilisateur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getUtilisateurByEmail(@PathVariable String email) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByEmail(email);
        return utilisateur.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Utilisateur>> getUtilisateursByRole(@PathVariable Role role) {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateursByRole(role);
        return ResponseEntity.ok(utilisateurs);
    }
    
    @PostMapping
    public ResponseEntity<?> createUtilisateur(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur nouvelUtilisateur = utilisateurService.createUtilisateur(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelUtilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id, 
                                               @Valid @RequestBody Utilisateur utilisateurDetails) {
        try {
            Utilisateur utilisateurMisAJour = utilisateurService.updateUtilisateur(id, utilisateurDetails);
            return ResponseEntity.ok(utilisateurMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.ok("Utilisateur supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByEmail(loginRequest.getEmail());
        
        if (utilisateur.isPresent()) {
            boolean isPasswordValid = utilisateurService.verifierMotDePasse(
                    loginRequest.getMotDePasse(), 
                    utilisateur.get().getMotDePasse()
            );
            
            if (isPasswordValid) {
                return ResponseEntity.ok(utilisateur.get());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
    }
    
    // Classe interne pour la requête de login
    static class LoginRequest {
        private String email;
        private String motDePasse;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getMotDePasse() {
            return motDePasse;
        }
        
        public void setMotDePasse(String motDePasse) {
            this.motDePasse = motDePasse;
        }
    }
}