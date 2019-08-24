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
import java.util.*;

public class PdfGenerator {

    private static int MARGIN_TOP = 160;
    private static int MARGIN_LEFT = 50;
    private static int MARGIN_RIGHT = 50;
    private static int BEGIN_CONTENT = 330;
    private static int FONT_SIZE_TEXT = 12;

    public ByteArrayInputStream generateApplicantPdf(Applicant applicant) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle mediaBox = page.getMediaBox();
        document.addPage(page);

        try {
            PDPageContentStream contentStream  = new PDPageContentStream(document, page);

            addPdfHeader(contentStream, mediaBox, document);
            createApplicantDataSheet(contentStream, mediaBox, applicant);
            createApplicantContent(contentStream, mediaBox, applicant);
            contentStream.close();

            if (applicant.getMotivationalLetter() != null && applicant.getMotivationalLetter().trim().length() > 0) {
                PDPage secondPage = new PDPage(PDRectangle.A4);
                mediaBox = secondPage.getMediaBox();
                document.addPage(secondPage);

                contentStream  = new PDPageContentStream(document, secondPage);
                addPdfHeader(contentStream, mediaBox, document);
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

    private void addPdfHeader(PDPageContentStream contentStream, PDRectangle mediaBox, PDDocument document) throws IOException {
        PDImageXObject pdfHeaderImage = PDImageXObject.createFromFile("src/main/resources/template/TalTech_YE_logo.png", document);
        PDImageXObject pdfHeaderTextImage = PDImageXObject.createFromFile("src/main/resources/template/Tallinna_Tehnikaylikool.png", document);

        float scale = (MARGIN_TOP / 1.5f) / pdfHeaderImage.getHeight();
        contentStream.drawImage(pdfHeaderImage, MARGIN_LEFT, mediaBox.getHeight() - 30 - pdfHeaderImage.getHeight() * scale,
                pdfHeaderImage.getWidth() * scale, pdfHeaderImage.getHeight() * scale);

        scale = (MARGIN_TOP / 1f) / pdfHeaderTextImage.getHeight();
        contentStream.drawImage(pdfHeaderTextImage, mediaBox.getWidth() - MARGIN_RIGHT + 20, mediaBox.getHeight() - 10 - pdfHeaderTextImage.getHeight() * scale,
                pdfHeaderTextImage.getWidth() * scale, pdfHeaderTextImage.getHeight() * scale);
    }

    private void createApplicantContent(PDPageContentStream contentStream, PDRectangle mediaBox, Applicant applicant) throws IOException {
        PDFont fontText = PDType1Font.TIMES_ROMAN;
        PDFont fontTextBold = PDType1Font.TIMES_BOLD;
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.setLeading(20f);

        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN_RIGHT, mediaBox.getHeight() - BEGIN_CONTENT);
        contentStream.showText("TalTech üliõpilasesindusele");
        for (int i = 0; i < 4; i++) {
            contentStream.newLine();
        }

        contentStream.setFont(fontTextBold, FONT_SIZE_TEXT);
        contentStream.showText("Kandideerimisavaldus");
        for (int i = 0; i < 3; i++) {
            contentStream.newLine();
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(applicant.getCreatedDate());
        String content = String.format("Mina, %s %s, soovin kandideerida %s TalTech üliõpilaste esindajaks %s/%s õppeaastaks.",
                applicant.getFirstName(), applicant.getLastName(), applicant.getFaculty().toString(),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);

        float width = mediaBox.getWidth() - 2 * MARGIN_RIGHT;
        addParagraph(contentStream, width, 0, 0, content, true);
        for (int i = 0; i < 4; i++) {
            contentStream.newLine();
        }

        contentStream.showText("Lugupidamisega,");
        contentStream.newLine();
        String name = String.format("%s %s", applicant.getFirstName(), applicant.getLastName());
        contentStream.showText(name);
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
        contentStream.setLeading(15f);

        contentStream.beginText();
        contentStream.newLineAtOffset(0, mediaBox.getHeight() - MARGIN_TOP);
        createApplicantDataRow(contentStream, mediaBox, "Eesnimi", applicant.getFirstName());
        createApplicantDataRow(contentStream, mediaBox, "Perekonnanimi", applicant.getLastName());
        createApplicantDataRow(contentStream, mediaBox, "Telefon", applicant.getPhoneNumber());
        createApplicantDataRow(contentStream, mediaBox, "E-mail", applicant.getEmail());
        createApplicantDataRow(contentStream, mediaBox, "Matrikklinumber", applicant.getMatrikkel().toString());
        createApplicantDataRow(contentStream, mediaBox, "Teaduskond", applicant.getFaculty().toString());
        createApplicantDataRow(contentStream, mediaBox, "Õppeaste", applicant.getDegree().toString());
        contentStream.endText();
    }

    private void createApplicantDataRow(PDPageContentStream contentStream, PDRectangle mediaBox, String key, String value) throws IOException {
        PDFont fontHeader = PDType1Font.TIMES_BOLD;
        PDFont fontText = PDType1Font.TIMES_ROMAN;

        float text_width = (fontHeader.getStringWidth(key + ": ") / 1000.0f) * FONT_SIZE_TEXT;
        text_width += (fontText.getStringWidth(value) / 1000.0f) * FONT_SIZE_TEXT;

        contentStream.newLineAtOffset(mediaBox.getWidth() - MARGIN_RIGHT - text_width, -15);
        contentStream.setFont(fontHeader, FONT_SIZE_TEXT);
        contentStream.showText(key + ": ");
        contentStream.setFont(fontText, FONT_SIZE_TEXT);
        contentStream.showText(value);
        contentStream.newLineAtOffset(-(mediaBox.getWidth() - MARGIN_RIGHT - text_width), 0);
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
