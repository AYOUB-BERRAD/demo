package io.reflectoring.demo.entitities;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "commentaires")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texte;

    @Column(nullable = false)
    private LocalDateTime dateCommentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    @JsonIgnoreProperties({"commentaires", "soumissions", "coursEnseigne", "motDePasse"}) // ✅ Éviter récursion
    private Utilisateur auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soumission_id")
    @JsonIgnoreProperties({"commentaires", "etudiant", "projet"}) // ✅ Éviter récursion
    private SoumissionEtudiant soumission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id")
    @JsonIgnoreProperties({"commentaires", "projets", "instructeur"}) // ✅ Éviter récursion
    private Cours cours;

    public Commentaire() {}

    public Commentaire(String texte, LocalDateTime dateCommentaire, Utilisateur auteur) {
        this.texte = texte;
        this.dateCommentaire = dateCommentaire;
        this.auteur = auteur;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public LocalDateTime getDateCommentaire() { return dateCommentaire; }
    public void setDateCommentaire(LocalDateTime dateCommentaire) { this.dateCommentaire = dateCommentaire; }

    public Utilisateur getAuteur() { return auteur; }
    public void setAuteur(Utilisateur auteur) { this.auteur = auteur; }

    public SoumissionEtudiant getSoumission() { return soumission; }
    public void setSoumission(SoumissionEtudiant soumission) { this.soumission = soumission; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }
}