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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
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
    
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
       
    @Column(name = "age", nullable = false)
    private Integer age;
   
    @Lob    
    @Column(name = "image",columnDefinition = "longblob" , nullable = true)
    private byte[] imageBytes;
    
    public Student(String firstName, String lastName, String email, 
                   LocalDate birthdate, MultipartFile imageFile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        try {
            this.imageBytes = imageFile.getBytes();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    
}