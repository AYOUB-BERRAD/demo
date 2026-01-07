package services;

import io.reflectoring.demo.entitities.Cours;
import io.reflectoring.demo.entitities.Projet;
import io.reflectoring.demo.entitities.Utilisateur;
import repository.CoursRepository;
import repository.ProjetRepository;
import repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjetService {
    
    @Autowired
    private ProjetRepository projetRepository;
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }
    
    public Optional<Projet> getProjetById(Long id) {
        return projetRepository.findById(id);
    }
    
    public List<Projet> getProjetsByCours(Long coursId) {
        return projetRepository.findByCoursId(coursId);
    }
    
    public List<Projet> getProjetsByInstructeur(Long instructeurId) {
        return projetRepository.findByInstructeurId(instructeurId);
    }
    
    public Projet createProjet(Projet projet, Long coursId, Long instructeurId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'id: " + coursId));
        
        Utilisateur instructeur = utilisateurRepository.findById(instructeurId)
                .orElseThrow(() -> new RuntimeException("Instructeur non trouvé avec l'id: " + instructeurId));
        
        if (instructeur.getRole() != Utilisateur.Role.INSTRUCTEUR) {
            throw new RuntimeException("L'utilisateur n'est pas un instructeur");
        }
        
        projet.setCours(cours);
        projet.setInstructeur(instructeur);
        
        return projetRepository.save(projet);
    }
    
    public Projet updateProjet(Long id, Projet projetDetails) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id: " + id));
        
        projet.setTitre(projetDetails.getTitre());
        projet.setDescription(projetDetails.getDescription());
        projet.setDateLimite(projetDetails.getDateLimite());
        
        return projetRepository.save(projet);
    }
    
    public void deleteProjet(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id: " + id));
        projetRepository.delete(projet);
    }
}