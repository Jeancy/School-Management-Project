
package com.jeancy.sms.serviceimpl;

import com.jeancy.sms.entity.Student;
import com.jeancy.sms.repository.StudentRepository;
import com.jeancy.sms.service.StudentService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.stereotype.Service;

// Every service implementation class must be annotated with @Service
@Service
public class StudentServiceImpl implements StudentService {
    //StudentServiceImpl relies or depends on the StudentRepository interface
    private final StudentRepository studentRepository;
    // Constractor based dependancy injection
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    // Getting list of students.
    @Override
    public List<Student> getAllStudents() {
         return studentRepository.findAll();
    }
    // Calculating the age automaticaly
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // Saving a newly created student
    @Override
    public Student saveStudent(Student student) {       
        student.setAge(calculateAge(student.getBirthdate()));
        return studentRepository.save(student);
    }
    // Retreaving a student based on given id
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    //Updating an existing student
    @Override
    public Student updateStudent(Student student) {
        student.setAge(calculateAge(student.getBirthdate()));
        return studentRepository.save(student);
    }
    // Deleting a student based on given id.
    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }
    
}
