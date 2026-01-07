package io.reflectoring.demo.entitities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cours")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructeur_id", nullable = false)
    @JsonIgnoreProperties({"coursEnseigne", "soumissions", "commentaires", "motDePasse"}) // ✅ Éviter récursion
    private Utilisateur instructeur;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cours", "soumissions", "instructeur"}) // ✅ Éviter récursion
    private List<Projet> projets = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"cours", "auteur", "soumission"}) // ✅ Éviter récursion
    private List<Commentaire> commentaires = new ArrayList<>();

    public Cours() {}

    public Cours(String titre, String description, String code, Utilisateur instructeur) {
        this.titre = titre;
        this.description = description;
        this.code = code;
        this.instructeur = instructeur;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Utilisateur getInstructeur() { return instructeur; }
    public void setInstructeur(Utilisateur instructeur) { this.instructeur = instructeur; }

    public List<Projet> getProjets() { return projets; }
    public void setProjets(List<Projet> projets) { this.projets = projets; }

    public List<Commentaire> getCommentaires() { return commentaires; }
    public void setCommentaires(List<Commentaire> commentaires) { this.commentaires = commentaires; }
}