
package com.jeancy.sms.serviceimpl;

import com.jeancy.sms.entity.Student;
import com.jeancy.sms.repository.StudentRepository;
import com.jeancy.sms.service.StudentService;
import java.util.List;
import org.springframework.stereotype.Service;

// Every service implementation class must be annotated with @Service
// StudentService class must implement all the method declared on the Student service
@Service
public class StudentServiceImpl implements StudentService {
    //StudentServiceImpl relies or depends on the StudentRepository interface
    private final StudentRepository studentRepository;
    // Constractor based dependancy injection
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @Override
    public List<Student> getAllStudents() {
         return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }
    
}
