package backend.controller;

import backend.bean.ApplicantForm;
import backend.util.PdfGenerator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class Controller {

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePDF(@RequestBody ApplicantForm newApplicant) {
        PdfGenerator pdfGenerator = new PdfGenerator();
        ByteArrayInputStream bis = pdfGenerator.generateApplicantPdf(newApplicant);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=coooooooolllllll.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

