package backend.repository;

import backend.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByMatrikkel(Long matrikkel);
    List<Applicant> findAll();
}
