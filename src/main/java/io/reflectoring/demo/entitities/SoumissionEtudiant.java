package io.reflectoring.demo.entitities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "soumissions_etudiants")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SoumissionEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"soumissions", "coursEnseigne", "commentaires", "motDePasse"}) // ✅ Éviter récursion
    private Utilisateur etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    @JsonIgnoreProperties({"soumissions", "cours", "instructeur"}) // ✅ Éviter récursion
    private Projet projet;

    @Column(nullable = false)
    private LocalDateTime dateSoumission;

    @Column(nullable = false)
    private String fichierUrl;

    private Double note;

    @Column(columnDefinition = "TEXT")
    private String commentairesInstructeur;

    @OneToMany(mappedBy = "soumission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"soumission", "auteur", "cours"}) // ✅ Éviter récursion
    private List<Commentaire> commentaires = new ArrayList<>();

    public SoumissionEtudiant() {}

    public SoumissionEtudiant(Utilisateur etudiant, Projet projet, LocalDateTime dateSoumission, String fichierUrl) {
        this.etudiant = etudiant;
        this.projet = projet;
        this.dateSoumission = dateSoumission;
        this.fichierUrl = fichierUrl;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getEtudiant() { return etudiant; }
    public void setEtudiant(Utilisateur etudiant) { this.etudiant = etudiant; }

    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public String getFichierUrl() { return fichierUrl; }
    public void setFichierUrl(String fichierUrl) { this.fichierUrl = fichierUrl; }

    public Double getNote() { return note; }
    public void setNote(Double note) { this.note = note; }

    public String getCommentairesInstructeur() { return commentairesInstructeur; }
    public void setCommentairesInstructeur(String commentairesInstructeur) { this.commentairesInstructeur = commentairesInstructeur; }

    public List<Commentaire> getCommentaires() { return commentaires; }
    public void setCommentaires(List<Commentaire> commentaires) { this.commentaires = commentaires; }
}