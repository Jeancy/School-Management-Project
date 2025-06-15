package com.jeancy.sms.serviceimpl;

import com.jeancy.sms.entity.Student;
import com.jeancy.sms.repository.StudentRepository;
import com.jeancy.sms.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        student.setAge(computeAge(student));
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found"));
    }

    @Override
    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new EntityNotFoundException("Student with ID " + student.getId() + " not found");
        }
        student.setAge(computeAge(student));
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student with ID " + id + " not found");
        }
        studentRepository.deleteById(id);
    }
    
    private int computeAge(Student student){
      return Period.between(student.getBirthdate(), LocalDate.now()).getYears();
    }
}
