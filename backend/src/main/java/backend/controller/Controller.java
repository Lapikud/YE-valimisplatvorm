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

@RestController
public class Controller {

    @Autowired
    ApplicantRepository repository;

    @PostMapping(value = "/generate")
    public ResponseEntity<InputStreamResource> generatePDF(@Valid @RequestBody Applicant newApplicant) {
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generateApplicantPdf(newApplicant);

        String filename = String.format("kandideerimisavaldus_%s_%s.pdf",
                newApplicant.getFirstName(), newApplicant.getLastName());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

