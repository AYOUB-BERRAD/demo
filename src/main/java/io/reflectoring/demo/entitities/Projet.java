package io.reflectoring.demo.entitities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id", nullable = false)
    @JsonIgnoreProperties({"projets", "commentaires", "instructeur"}) // ✅ Éviter récursion
    private Cours cours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructeur_id", nullable = false)
    @JsonIgnoreProperties({"coursEnseigne", "soumissions", "commentaires", "motDePasse"}) // ✅ Éviter récursion
    private Utilisateur instructeur;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"projet", "etudiant", "commentaires"}) // ✅ Éviter récursion
    private List<SoumissionEtudiant> soumissions = new ArrayList<>();

    public Projet() {}

    public Projet(String titre, String description, LocalDateTime dateLimite, Cours cours, Utilisateur instructeur) {
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
        this.cours = cours;
        this.instructeur = instructeur;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateLimite() { return dateLimite; }
    public void setDateLimite(LocalDateTime dateLimite) { this.dateLimite = dateLimite; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }

    public Utilisateur getInstructeur() { return instructeur; }
    public void setInstructeur(Utilisateur instructeur) { this.instructeur = instructeur; }

    public List<SoumissionEtudiant> getSoumissions() { return soumissions; }
    public void setSoumissions(List<SoumissionEtudiant> soumissions) { this.soumissions = soumissions; }
}
