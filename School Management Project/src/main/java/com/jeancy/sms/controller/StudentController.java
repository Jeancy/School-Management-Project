package com.jeancy.sms.controller;

import com.jeancy.sms.dto.StudentDto;
import com.jeancy.sms.entity.Student;
import com.jeancy.sms.exception.StudentNotFoundException;
import com.jeancy.sms.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

@Data
@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("studentDto", new StudentDto());
        return "create_student";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("studentDto") StudentDto studentDto,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "create_student";
        }

        try {
            handleImageFile(imageFile, studentDto, redirectAttributes);
        } catch (IOException e) {
            logger.error("Error processing image file", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing image file. Please try again.");
            return "redirect:/students/new";
        }

        studentService.saveStudent(studentDto);
        redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully.");
        return "redirect:/students";
    }

    private void handleImageFile(MultipartFile imageFile, StudentDto studentDto, RedirectAttributes redirectAttributes) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            if (imageFile.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("errorMessage", "File size should not exceed 10MB.");
                throw new IOException("File size too large");
            }

            if (!imageFile.getContentType().startsWith("image/")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Only image files are allowed.");
                throw new IOException("Invalid file type");
            }

            studentDto.setImageBytes(imageFile.getBytes());
        }
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException("Student with ID " + id + " not found");
        }
        // Map Student to StudentDto
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setBirthDate(student.getBirthDate());
        studentDto.setImageBytes(student.getImageBytes());
        model.addAttribute("studentDto", studentDto);
        return "edit_student";// reuse form for update
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("studentDto") StudentDto studentDto,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                BindingResult result,
                                Model mode,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit_student";
        }

        try {
            Student existingStudent = studentService.getStudentById(id);
            if (existingStudent == null) {
                throw new StudentNotFoundException("Student with ID " + id + " not found");
            }

            handleImageFile(imageFile, studentDto, redirectAttributes);

            existingStudent.setFirstName(studentDto.getFirstName());
            existingStudent.setLastName(studentDto.getLastName());
            existingStudent.setEmail(studentDto.getEmail());
            existingStudent.setBirthDate(studentDto.getBirthDate());

            studentService.updateStudent(id, studentDto);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");

        } catch (EntityNotFoundException e) {
            logger.error("Student with ID {} not found", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            return "redirect:/students";
        } catch (IOException e) {
            logger.error("Error processing image file", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing image file.");
            return "redirect:/students";
        }

        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            return "redirect:/error";
        } catch (Exception e) {
            logger.error("Error deleting student", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while trying to delete the student.");
            return "redirect:/error";
        }
        return "redirect:/students";
    }

    @GetMapping("/students/image/{id}")
    public ResponseEntity<byte[]> getStudentImage(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        byte[] imageBytes = student.getImageBytes();

        if (imageBytes != null && imageBytes.length != 0) {
        } else {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.IMAGE_JPEG; // Default fallback
        try {
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
            if (contentType != null) {
                mediaType = MediaType.parseMediaType(contentType);
            }
        } catch (IOException e) {
            logger.warn("Unable to detect image type, defaulting to image/jpeg");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
