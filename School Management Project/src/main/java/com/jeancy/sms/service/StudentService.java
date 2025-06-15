
package com.jeancy.sms.service;

import com.jeancy.sms.dto.StudentDto;
import com.jeancy.sms.entity.Student;
import java.util.List;

// The StudentService defines all the business logic so all needed methods must be declared here.
public interface StudentService {
    // to get the students list
    List<Student> getAllStudents();
    //to save a particular student
    Student saveStudent(StudentDto studentDto);
    //getting a student based on provided id
    Student getStudentById(Long id);
    //updating a student from the list
    Student updateStudent(Long id, StudentDto studentDto);
    //deleting a student based on given id.
    void deleteStudentById(Long id);
}
