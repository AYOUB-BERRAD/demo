package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.reflectoring.demo.entitities.SoumissionEtudiant;

@Repository
public interface SoumissionEtudiantRepository extends JpaRepository<SoumissionEtudiant, Long> {
    List<SoumissionEtudiant> findByEtudiantId(Long etudiantId);
    List<SoumissionEtudiant> findByProjetId(Long projetId);
    Optional<SoumissionEtudiant> findByEtudiantIdAndProjetId(Long etudiantId, Long projetId);
}
