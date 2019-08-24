package backend.controller;

import backend.model.Applicant;
import backend.repository.ApplicantRepository;
import backend.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
public class Controller {

    @Autowired
    ApplicantRepository repository;

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

