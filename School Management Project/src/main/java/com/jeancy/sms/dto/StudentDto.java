package com.jeancy.sms.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // ✅ Replaces Default constructor
@AllArgsConstructor // ✅ Replaces all args constructor
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

    private MultipartFile image;

}
