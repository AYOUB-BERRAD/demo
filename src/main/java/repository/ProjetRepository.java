package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.reflectoring.demo.entitities.Projet;

import java.util.List;
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    List<Projet> findByCoursId(Long coursId);
    List<Projet> findByInstructeurId(Long instructeurId);
}