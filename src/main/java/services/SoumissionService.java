package services;


import io.reflectoring.demo.entitities.Projet;
import io.reflectoring.demo.entitities.SoumissionEtudiant;
import io.reflectoring.demo.entitities.Utilisateur;
import repository.ProjetRepository;
import repository.SoumissionEtudiantRepository;
import repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SoumissionService {
    
    @Autowired
    private SoumissionEtudiantRepository soumissionRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private ProjetRepository projetRepository;
    
    public List<SoumissionEtudiant> getAllSoumissions() {
        return soumissionRepository.findAll();
    }
    
    public Optional<SoumissionEtudiant> getSoumissionById(Long id) {
        return soumissionRepository.findById(id);
    }
    
    public List<SoumissionEtudiant> getSoumissionsByEtudiant(Long etudiantId) {
        return soumissionRepository.findByEtudiantId(etudiantId);
    }
    
    public List<SoumissionEtudiant> getSoumissionsByProjet(Long projetId) {
        return soumissionRepository.findByProjetId(projetId);
    }
    
    public SoumissionEtudiant createSoumission(SoumissionEtudiant soumission, Long etudiantId, Long projetId) {
        Utilisateur etudiant = utilisateurRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'id: " + etudiantId));
        
        if (etudiant.getRole() != Utilisateur.Role.ETUDIANT) {
            throw new RuntimeException("L'utilisateur n'est pas un étudiant");
        }
        
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id: " + projetId));
        
        // Vérifier si l'étudiant a déjà soumis pour ce projet
        Optional<SoumissionEtudiant> existingSoumission = 
                soumissionRepository.findByEtudiantIdAndProjetId(etudiantId, projetId);
        
        if (existingSoumission.isPresent()) {
            throw new RuntimeException("L'étudiant a déjà soumis un travail pour ce projet");
        }
        
        soumission.setEtudiant(etudiant);
        soumission.setProjet(projet);
        soumission.setDateSoumission(LocalDateTime.now());
        
        return soumissionRepository.save(soumission);
    }
    
    public SoumissionEtudiant updateSoumission(Long id, SoumissionEtudiant soumissionDetails) {
        SoumissionEtudiant soumission = soumissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soumission non trouvée avec l'id: " + id));
        
        soumission.setFichierUrl(soumissionDetails.getFichierUrl());
        
        return soumissionRepository.save(soumission);
    }
    
    public SoumissionEtudiant noterSoumission(Long id, Double note, String commentaires) {
        SoumissionEtudiant soumission = soumissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soumission non trouvée avec l'id: " + id));
        
        soumission.setNote(note);
        soumission.setCommentairesInstructeur(commentaires);
        
        return soumissionRepository.save(soumission);
    }
    
    public void deleteSoumission(Long id) {
        SoumissionEtudiant soumission = soumissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soumission non trouvée avec l'id: " + id));
        soumissionRepository.delete(soumission);
    }
}