package services;
import io.reflectoring.demo.entitities.Cours;
import io.reflectoring.demo.entitities.Utilisateur;
import repository.CoursRepository;
import repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {
    
    @Autowired
    private CoursRepository coursRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }
    
    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }
    
    public Optional<Cours> getCoursByCode(String code) {
        return coursRepository.findByCode(code);
    }
    
    public List<Cours> getCoursByInstructeur(Long instructeurId) {
        return coursRepository.findByInstructeurId(instructeurId);
    }
    
    public Cours createCours(Cours cours, Long instructeurId) {
        if (coursRepository.existsByCode(cours.getCode())) {
            throw new RuntimeException("Un cours avec ce code existe déjà");
        }
        
        Utilisateur instructeur = utilisateurRepository.findById(instructeurId)
                .orElseThrow(() -> new RuntimeException("Instructeur non trouvé avec l'id: " + instructeurId));
        
        if (instructeur.getRole() != Utilisateur.Role.INSTRUCTEUR) {
            throw new RuntimeException("L'utilisateur n'est pas un instructeur");
        }
        
        cours.setInstructeur(instructeur);
        return coursRepository.save(cours);
    }
    
    public Cours updateCours(Long id, Cours coursDetails) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'id: " + id));
        
        cours.setTitre(coursDetails.getTitre());
        cours.setDescription(coursDetails.getDescription());
        cours.setCode(coursDetails.getCode());
        
        return coursRepository.save(cours);
    }
    
    public void deleteCours(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé avec l'id: " + id));
        coursRepository.delete(cours);
    }
}