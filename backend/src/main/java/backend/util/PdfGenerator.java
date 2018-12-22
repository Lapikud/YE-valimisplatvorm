package backend.util;

import backend.bean.ApplicantForm;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfGenerator {

    private static int MARGIN_TOP = 160;
    private static int MARGIN_LEFT = 50;
    private static int BEGIN_CONTENT = 300;
    private static int FONT_SIZE_TEXT = 12;

    public ByteArrayInputStream generateApplicantPdf(ApplicantForm applicant) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        document.addPage(page);

        try {
            PDImageXObject pdfHeaderImage = PDImageXObject.createFromFile("src/main/resources/template/application_header_old.png", document);

            PDPageContentStream contentStream  = new PDPageContentStream(document, page);
            float scale = mediaBox.getWidth() / pdfHeaderImage.getWidth();
            contentStream.drawImage(pdfHeaderImage, 0, mediaBox.getHeight() - pdfHeaderImage.getHeight() * scale,
                    mediaBox.getWidth(), pdfHeaderImage.getHeight() * scale);

            createPdfHeader(contentStream, mediaBox);
            createApplicantContent(contentStream, mediaBox, applicant);

            contentStream.close();
            document.save(byteArrayOutputStream);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void createPdfHeader(PDPageContentStream contentStream, PDRectangle mediaBox) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        contentStream.beginText();
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.setLeading(20f);
        contentStream.newLineAtOffset(MARGIN_LEFT, mediaBox.getHeight() - MARGIN_TOP);

        contentStream.showText("TalTech Üliõpilasesinduse valimiskomisjonile");
        contentStream.newLine();
        contentStream.showText("Tallinna Tehnikaülikool");
        contentStream.newLine();
        contentStream.showText("Ehitajate tee 5");
        contentStream.newLine();
        contentStream.showText("19086 TALLINN");
        contentStream.newLine();

        contentStream.endText();
    }

    private void createApplicantContent(PDPageContentStream contentStream, PDRectangle mediaBox, ApplicantForm applicant) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN_LEFT, mediaBox.getHeight() - BEGIN_CONTENT);
        contentStream.setLeading(23f);

        createApplicantContentRow(contentStream, "Eesnimi", applicant.getFirstName());
        createApplicantContentRow(contentStream, "Perekonnanimi", applicant.getLastName());
        createApplicantContentRow(contentStream, "Matrikklinumber", applicant.getMatrikkel());
        createApplicantContentRow(contentStream, "Teaduskond", applicant.getFaculty());
        createApplicantContentRow(contentStream, "Eriala", applicant.getMajor());
        createApplicantContentRow(contentStream, "Telefon", applicant.getPhoneNumber());
        createApplicantContentRow(contentStream, "E-mail", applicant.getEmail());

        contentStream.endText();
    }

    private void createApplicantContentRow(PDPageContentStream contentStream, String key, String value) throws IOException {
        PDFont fontHeader = PDType1Font.TIMES_BOLD;
        PDFont fontText = PDType1Font.TIMES_ROMAN;

        contentStream.setFont(fontHeader, FONT_SIZE_TEXT);
        contentStream.showText(key + ": ");
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.showText(value);
        contentStream.newLine();
    }
}
