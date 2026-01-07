package repository;

import io.reflectoring.demo.entitities.Cours;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findByCode(String code);
    List<Cours> findByInstructeurId(Long instructeurId);
    boolean existsByCode(String code);
}