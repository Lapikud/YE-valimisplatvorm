package backend.bean;

import javax.validation.constraints.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class ApplicantForm {

    private Date date = new Date();
    @NotBlank(message = "First name should not be empty")
    private String firstName;
    @NotBlank(message = "Last name should not be empty")
    private String lastName;
    @NotBlank(message = "Matrikkel should not be empty")
    private String matrikkel;
    @NotBlank(message = "Faculty should not be empty")
    private String faculty;
    @NotBlank(message = "Major should not be empty")
    private String major;
    @NotBlank(message = "Phone number should not be empty")
    private String phoneNumber;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotNull
    @AssertTrue(message = "Terms must be accepted")
    private boolean acceptedTerms;
    @Size(max = 500, message = "Maximum motivation letter length is 500 characters")
    private String motivationLetter;
    private BufferedImage image;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMatrikkel() {
        return matrikkel;
    }

    public void setMatrikkel(String matrikkel) {
        this.matrikkel = matrikkel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMotivationLetter() {
        return motivationLetter;
    }

    public void setMotivationLetter(String motivationLetter) {
        this.motivationLetter = motivationLetter;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
