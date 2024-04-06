package com.jeancy.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name="students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "birthdate")
    private LocalDate birthdate;
    
    @Column(name = "age")
    private String age;
    
    public Student(String firstName, String lastName, String email, 
                   LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
    }

    public Student() {
    }
    
    public int getAge(){
        return this.birthdate.until(LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + '}';
    }

    
    
}
