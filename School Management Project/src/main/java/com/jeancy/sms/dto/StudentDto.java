package com.jeancy.sms.dto;

import jakarta.validation.constraints.*;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // ✅ Replaces Default constructor
@Data // ✅ Replaces getters, setters, toString and hashCode
public class StudentDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
    
    private byte[] imageBytes;

    public StudentDto(String firstName, String lastName, String email, 
                   LocalDate birthDate, MultipartFile imageFile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        try {
            this.imageBytes = imageFile.getBytes();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }    

}
