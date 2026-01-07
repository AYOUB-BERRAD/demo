package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.reflectoring.demo.entitities.Commentaire;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findBySoumissionId(Long soumissionId);
    List<Commentaire> findByCoursId(Long coursId);
    List<Commentaire> findByAuteurId(Long auteurId);
}