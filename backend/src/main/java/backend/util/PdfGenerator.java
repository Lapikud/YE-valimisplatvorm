package backend.util;

import backend.model.Applicant;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdfGenerator {

    private static int MARGIN_TOP = 160;
    private static int MARGIN_BOTTOM = 160;
    private static int MARGIN_LEFT = 50;
    private static int MARGIN_RIGHT = 50;
    private static int BEGIN_DATA = 380;
    private static int BEGIN_CONTENT = 260;
    private static int FONT_SIZE_TEXT = 12;

    public ByteArrayInputStream generateApplicantPdf(Applicant applicant) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        document.addPage(page);

        try {
            PDImageXObject pdfHeaderImage = PDImageXObject.createFromFile("src/main/resources/template/application_header_old.png", document);

            PDPageContentStream contentStream  = new PDPageContentStream(document, page);

            addPdfHeader(contentStream, mediaBox, pdfHeaderImage);
            createPdfHeaderText(contentStream, mediaBox, new Date());
            createApplicantContent(contentStream, mediaBox, applicant);
            createApplicantDataSheet(contentStream, mediaBox, applicant);
            contentStream.close();

            if (applicant.getMotivationalLetter() != null && applicant.getMotivationalLetter().trim().length() > 0) {
                PDPage secondPage = new PDPage(PDRectangle.A4);
                mediaBox = secondPage.getMediaBox();
                document.addPage(secondPage);

                contentStream  = new PDPageContentStream(document, secondPage);
                addPdfHeader(contentStream, mediaBox, pdfHeaderImage);
                createApplicatnMotivationLetter(contentStream, mediaBox, applicant);

                contentStream.close();
            }
            document.save(byteArrayOutputStream);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void addPdfHeader(PDPageContentStream contentStream, PDRectangle mediaBox, PDImageXObject pdfHeaderImage) throws IOException {
        float scale = mediaBox.getWidth() / pdfHeaderImage.getWidth();
        contentStream.drawImage(pdfHeaderImage, 0, mediaBox.getHeight() - pdfHeaderImage.getHeight() * scale,
                mediaBox.getWidth(), pdfHeaderImage.getHeight() * scale);
    }

    private void createPdfHeaderText(PDPageContentStream contentStream, PDRectangle mediaBox, Date date) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.setLeading(20f);
        contentStream.beginText();
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

    private void createApplicantContent(PDPageContentStream contentStream, PDRectangle mediaBox, Applicant applicant) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.setLeading(20f);

        contentStream.beginText();
        String pattern = "dd-MM-YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(applicant.getCreatedDate());
        float text_width = (fontText.getStringWidth(dateString) / 1000.0f) * FONT_SIZE_TEXT;
        contentStream.newLineAtOffset(mediaBox.getWidth() - MARGIN_RIGHT - text_width, mediaBox.getHeight() - BEGIN_CONTENT);
        contentStream.showText(dateString);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN_RIGHT, mediaBox.getHeight() - BEGIN_CONTENT);
        contentStream.newLine();
        contentStream.showText("AVALDUS");
        contentStream.newLine();
        contentStream.newLine();
        String content = String.format("Mina, %s %s, soovin kandideerida Üliõpilaskogu volikogusse.",
                applicant.getFirstName(), applicant.getLastName());
        contentStream.showText(content);
        contentStream.endText();
    }

    private void createApplicatnMotivationLetter(PDPageContentStream contentStream, PDRectangle mediaBox, Applicant applicant) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        contentStream.setFont(fontText, FONT_SIZE_TEXT);

        float width = mediaBox.getWidth() - 2 * MARGIN_RIGHT;
        float startX = mediaBox.getLowerLeftX() + MARGIN_RIGHT;
        float startY = mediaBox.getUpperRightY() - MARGIN_TOP;
        contentStream.beginText();
        addParagraph(contentStream, width, startX, startY, applicant.getMotivationalLetter(), true);
        contentStream.endText();
    }

    private void createApplicantDataSheet(PDPageContentStream contentStream, PDRectangle mediaBox, Applicant applicant) throws IOException {
        contentStream.setLeading(23f);

        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN_LEFT, mediaBox.getHeight() - BEGIN_DATA);
        createApplicantDataRow(contentStream, "Eesnimi", applicant.getFirstName());
        createApplicantDataRow(contentStream, "Perekonnanimi", applicant.getLastName());
        createApplicantDataRow(contentStream, "Matrikklinumber", applicant.getMatrikkel().toString());
        createApplicantDataRow(contentStream, "Department", applicant.getDepartment().toString());
        //createApplicantDataRow(contentStream, "Eriala", applicant.getMajor());
        createApplicantDataRow(contentStream, "Telefon", applicant.getPhoneNumber());
        createApplicantDataRow(contentStream, "E-mail", applicant.getEmail());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN_LEFT, MARGIN_BOTTOM);
        contentStream.showText("(allkirjastatud digitaalselt)");
        contentStream.endText();
    }

    private void createApplicantDataRow(PDPageContentStream contentStream, String key, String value) throws IOException {
        PDFont fontHeader = PDType1Font.TIMES_BOLD;
        PDFont fontText = PDType1Font.TIMES_ROMAN;

        contentStream.setFont(fontHeader, FONT_SIZE_TEXT);
        contentStream.showText(key + ": ");
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.showText(value);
        contentStream.newLine();
    }

    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                     float sy, String text) throws IOException {
        addParagraph(contentStream, width, sx, sy, text, false);
    }

    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                     float sy, String text, boolean justify) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        List<String> lines = parseLines(text, width);
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.newLineAtOffset(sx, sy);
        for (String line: lines) {
            float charSpacing = 0;
            if (justify){
                if (line.length() > 1) {
                    float size = FONT_SIZE_TEXT * fontText.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            contentStream.setCharacterSpacing(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -20f);
        }
    }

    private static List<String> parseLines(String text, float width) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            String subString = text.substring(0, spaceIndex);
            float size = FONT_SIZE_TEXT * fontText.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0){
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }
}
