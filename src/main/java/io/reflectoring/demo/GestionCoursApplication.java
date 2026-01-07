package io.reflectoring.demo;

import io.reflectoring.demo.entitities.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import services.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@ComponentScan({
    "controller",
    "services",
    "repository",
    "io.reflectoring.demo"
})
@EnableJpaRepositories(basePackages = "repository")
public class GestionCoursApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionCoursApplication.class, args);
	}
    @Bean
    CommandLineRunner initData(
            UtilisateurService utilisateurService,
            CoursService coursService,
            ProjetService projetService,
            SoumissionService soumissionService,
            CommentaireService commentaireService
    ) {
        return args -> {

            if (!utilisateurService.getAllUtilisateurs().isEmpty()) {
                System.out.println("🔥 Données déjà présentes — seed ignoré");
                return;
            }

            System.out.println("🚀 Initialisation base (multi-données)…");

            // ==== CREATE INSTRUCTEURS ====
            var instructeurs = List.of(
                    new Utilisateur("Smith","John","john@mail.com","123456", Utilisateur.Role.INSTRUCTEUR),
                    new Utilisateur("Taylor","Emma","emma@mail.com","123456", Utilisateur.Role.INSTRUCTEUR),
                    new Utilisateur("Brown","David","david@mail.com","123456", Utilisateur.Role.INSTRUCTEUR)
            ).stream().map(utilisateurService::createUtilisateur).toList();

            // ==== CREATE ETUDIANTS ====
            var etudiants = List.of(
                    new Utilisateur("Ali","Karim","ali@mail.com","123456", Utilisateur.Role.ETUDIANT),
                    new Utilisateur("Sara","Mehdi","sara@mail.com","123456", Utilisateur.Role.ETUDIANT),
                    new Utilisateur("Lina","Khalid","lina@mail.com","123456", Utilisateur.Role.ETUDIANT),
                    new Utilisateur("Adam","Nouman","adam@mail.com","123456", Utilisateur.Role.ETUDIANT),
                    new Utilisateur("Yassine","Omar","yassine@mail.com","123456", Utilisateur.Role.ETUDIANT)
            ).stream().map(utilisateurService::createUtilisateur).toList();

            // ==== CREATE COURS ====
            var coursList = List.of(
                    new Cours("Angular", "Frontend avancé", "ANG-101", instructeurs.get(0)),
                    new Cours("Spring Boot", "Backend REST", "SPR-201", instructeurs.get(1)),
                    new Cours("DevOps", "CI/CD & Docker", "DEV-301", instructeurs.get(2))
            ).stream().map(c -> coursService.createCours(c, c.getInstructeur().getId())).toList();

            // ==== CREATE PROJECTS FOR EACH COURSE ====
            var projets = new java.util.ArrayList<Projet>();

            for (var c : coursList) {
                for (int i = 1; i <= 2; i++) {
                    Projet p = new Projet(
                            c.getTitre() + " – Projet " + i,
                            "Description du projet " + i,
                            LocalDateTime.now().plusDays(7 + i),
                            c,
                            c.getInstructeur()
                    );
                    projets.add(projetService.createProjet(p, c.getId(), c.getInstructeur().getId()));
                }
            }

            // ==== ONE SUBMISSION PER STUDENT PER PROJECT ====
            for (var p : projets) {
                for (var etu : etudiants) {
                    SoumissionEtudiant s = new SoumissionEtudiant(
                            etu,
                            p,
                            LocalDateTime.now(),
                            "https://files.example.com/" + etu.getPrenom() + "-" + p.getId() + ".pdf"
                    );
                    var soum = soumissionService.createSoumission(s, etu.getId(), p.getId());

                    // Commentaire instructeur
                    Commentaire com = new Commentaire(
                            "Bon travail 👍",
                            LocalDateTime.now(),
                            p.getInstructeur()
                    );
                    commentaireService.createCommentaireSurSoumission(com, p.getInstructeur().getId(), soum.getId());
                }
            }

            System.out.println("✅ Base remplie avec plusieurs éléments !");
        };
    }


}
