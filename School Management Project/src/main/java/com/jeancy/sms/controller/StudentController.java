package com.jeancy.sms.controller;

import com.jeancy.sms.entity.Student;
import com.jeancy.sms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Every controller class must be annotated with @Controller
//The StudentConttroller class process all the requests and provides answers to all requests
@Controller
public class StudentController {
    //The student controller depends or relies on the StudentService interface for mapping
    @Autowired
    private StudentService studentService;
    
    // Create hanler method to handle or get list of students and return model and view. 
    @GetMapping("/students")
    public String listStudents (Model model){
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }
    
    // Handler method to create a new student.
    @GetMapping("/students/new")
    public String createStudentForm(Model model,Student student ){
        model.addAttribute("student", student);
        return "create_student";
    }
    
    //Handler method to save new added student to the table and display it in the table.
    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student){
        studentService.saveStudent(student);
        return "redirect:/students";
    }
    //Create a handler method edit student with given id details and display it in the table
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model){
        model.addAttribute("student", studentService.getStudentById(id));
        return "edit_student";
    }
    // Create handler method to update student details after editing.
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student student,
                                Model model){
        //get first student from the database
        Student existingStudent = studentService.getStudentById(id);
        // doing the actual update.
        existingStudent.setId(id);
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setAge(student.getAge());
        // Save updated student.
        studentService.updateStudent(existingStudent);
        return "redirect:/students";
    }
    //Create handler method to handle delete student request.
    @GetMapping("/students/{id}") 
    public String deleteStudent(@PathVariable Long id){
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
   
            
}
