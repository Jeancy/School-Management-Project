package com.jeancy.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@Entity
@Table(name="students", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)    
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    //birthDate will be used to compute student age
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;
       
    @Column(name = "age", nullable = false)
    private Integer age; // Will be calculeted using birthDate
    // For storing or saving image file or any type of file
    @Lob    
    @Column(name = "image",columnDefinition = "longblob" , nullable = true)
    private byte[] imageBytes;
    
    public Student(String firstName, String lastName, String email, 
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