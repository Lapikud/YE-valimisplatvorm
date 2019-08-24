package backend.controller;

import backend.model.Applicant;
import backend.repository.ApplicantRepository;
import backend.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    ApplicantRepository repository;

    @GetMapping(value = "/applicant/{id}")
    public ResponseEntity<Applicant> getApplicant(@PathVariable Long id) {
        Optional<Applicant> applicant = repository.findById(id);
        return applicant.isPresent() ? ResponseEntity.ok(applicant.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/applicant/{matrikkel}")
    public ResponseEntity<List<Applicant>> getAllApplicantsByMatrikkel(@PathVariable Long matrikkel) {
        List<Applicant> applicants = repository.findByMatrikkel(matrikkel);
        return applicants.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(applicants);
    }

    @GetMapping(value = "/applicant/all")
    public ResponseEntity<List<Applicant>> getAllApplicants() {
        List<Applicant> applicants = repository.findAll();
        return applicants.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(applicants);
    }

    @PostMapping(value = "/applicant/{id}")
    public ResponseEntity<Applicant> editApplicant(@PathVariable Long id, @Valid @RequestBody Applicant editApplicant) {
        Optional<Applicant> applicantDb = repository.findById(id);
        if (applicantDb.isPresent()) {
            editApplicant.setId(applicantDb.get().getId());
            editApplicant.setCreatedDate(applicantDb.get().getCreatedDate());
            repository.save(editApplicant);
            return ResponseEntity.ok(editApplicant);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/applicant")
    public void addApplicant(@Valid @RequestBody Applicant newApplicant) {
        repository.save(newApplicant);
    }

    @PostMapping(value = "/generate/application")
    public ResponseEntity<InputStreamResource> generatePDF(@Valid @RequestBody Applicant newApplicant) {
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generateApplicantPdf(newApplicant);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(newApplicant.getCreatedDate());
        String filename = String.format("Kandideerimisavaldus_%s_%s_%s.pdf",
                newApplicant.getFirstName(), newApplicant.getLastName(), calendar.get(Calendar.YEAR));

        return createPDFResponseEntity(bis, filename);
    }

    @PostMapping(value = "/generate/motivationletter")
    public ResponseEntity<InputStreamResource> generateMotivationLetterPDF(@Valid @RequestBody Applicant newApplicant) {
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generateApplicantMotivationLetterPdf(newApplicant);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(newApplicant.getCreatedDate());
        String filename = String.format("Motivatsioonikiri_%s_%s_%s.pdf",
                newApplicant.getFirstName(), newApplicant.getLastName(), calendar.get(Calendar.YEAR));

        return createPDFResponseEntity(bis, filename);
    }

    private ResponseEntity<InputStreamResource> createPDFResponseEntity(ByteArrayInputStream bis, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

