package backend.model;

import backend.enums.Department;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "applicant")
public class Applicant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "createddate")
    private Date createdDate = new Date();

    @NotBlank(message = "First name should not be empty")
    @Column(name = "firstname")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Column(name = "lastname")
    private String lastName;

    @NotNull(message = "Matrikkel should not be empty")
    @Column(name = "matrikkel")
    private Long matrikkel;

    @NotBlank(message = "Phone number should not be empty")
    @Column(name = "phonenumber")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @NotNull(message = "Department must be chosen")
    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private Department department;

    @Column(name = "picture")
    private Blob picture;

    @Size(max = 500, message = "Maximum motivation letter length is 500 characters")
    @Column(name = "motivationalletter")
    private String motivationalLetter;

    @NotNull
    @AssertTrue(message = "Terms must be accepted")
    @Column(name = "acceptedterms")
    private boolean acceptedTerms;
}
