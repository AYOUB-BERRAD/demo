package io.reflectoring.demo.entitities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ✅ Ignorer les propriétés Hibernate
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "instructeur", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"instructeur", "projets", "commentaires"}) // ✅ Éviter récursion
    private List<Cours> coursEnseigne = new ArrayList<>();

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"etudiant", "projet", "commentaires"}) // ✅ Éviter récursion
    private List<SoumissionEtudiant> soumissions = new ArrayList<>();

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"auteur", "soumission", "cours"}) // ✅ Éviter récursion
    private List<Commentaire> commentaires = new ArrayList<>();

    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email, String motDePasse, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<Cours> getCoursEnseigne() { return coursEnseigne; }
    public void setCoursEnseigne(List<Cours> coursEnseigne) { this.coursEnseigne = coursEnseigne; }

    public List<SoumissionEtudiant> getSoumissions() { return soumissions; }
    public void setSoumissions(List<SoumissionEtudiant> soumissions) { this.soumissions = soumissions; }

    public List<Commentaire> getCommentaires() { return commentaires; }
    public void setCommentaires(List<Commentaire> commentaires) { this.commentaires = commentaires; }

    public enum Role {
        ETUDIANT,
        INSTRUCTEUR,
        ADMINISTRATEUR
    }
}