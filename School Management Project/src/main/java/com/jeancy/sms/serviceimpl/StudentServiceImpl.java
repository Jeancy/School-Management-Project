package com.jeancy.sms.serviceimpl;

import com.jeancy.sms.dto.StudentDto;
import com.jeancy.sms.entity.Student;
import com.jeancy.sms.exception.StudentNotFoundException;
import com.jeancy.sms.repository.StudentRepository;
import com.jeancy.sms.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    @Override
    public Student saveStudent(StudentDto studentDto) {
        Student student = mapToStudent(studentDto);
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + id + " not found"));
    }

    @Transactional
    @Override
    public Student updateStudent(Long id, StudentDto studentDto) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with ID " + id + " not found");
        }
        Student student = mapToStudent(studentDto);
        student.setId(id); // Set the ID manually since DTO has no ID
        return studentRepository.save(student);
    }

    @Transactional
    @Override
    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with ID " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    private int computeAge(StudentDto studentDto) {
        if (studentDto == null || studentDto.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date is required to compute age");
        }
        return Period.between(studentDto.getBirthDate(), LocalDate.now()).getYears();
    }

    private Student mapToStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());
        student.setBirthDate(studentDto.getBirthDate());
        student.setImageBytes(studentDto.getImageBytes());
        student.setAge(computeAge(studentDto));
        return student;
    }
}
