package com.jeancy.sms.controller;


import com.jeancy.sms.entity.Student;
import com.jeancy.sms.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for managing student entities.
 * Handles CRUD operations and image file processing for students.
 */
@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    /**
     * Handles requests to list all students.
     *
     * @param model the model to be used by the view
     * @return the view name to be rendered
     */
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    /**
     * Displays the form for creating a new student.
     *
     * @param model the model to be used by the view
     * @return the view name to be rendered
     */
    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student";
    }

    /**
     * Handles the submission of a new student form and saves the student.
     *
     * @param student the student entity to be saved
     * @param imageFile the image file associated with the student
     * @param result the binding result for form validation
     * @param redirectAttributes the redirect attributes for flash messages
     * @return the view name to redirect to
     */
    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") Student student,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "create_student";
        }

        try {
            handleImageFile(imageFile, student, redirectAttributes);
        } catch (IOException e) {
            logger.error("Error processing image file", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing image file. Please try again.");
            return "redirect:/students/new";
        }

        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully.");
        return "redirect:/students";
    }

    /**
     * Validates and processes the uploaded image file for a student.
     *
     * @param imageFile the image file to be processed
     * @param student the student entity to which the image will be added
     * @param redirectAttributes the redirect attributes for flash messages
     * @throws IOException if an error occurs during file processing
     */
   /**
 * Processes the uploaded image file and sets it to the student entity.
 *
 * @param imageFile the image file uploaded by the user
 * @param student the student entity to which the image will be set
 * @param redirectAttributes the redirect attributes to add flash messages
 * @throws IOException if there is an issue with processing the image file
 */
    private void handleImageFile(MultipartFile imageFile, Student student, RedirectAttributes redirectAttributes) throws IOException {
        // Check if the uploaded file is not empty
        if (!imageFile.isEmpty()) {
            // Validate the file size (limit: 10MB)
            if (imageFile.getSize() > 10485760) {
                redirectAttributes.addFlashAttribute("errorMessage", "File size should not exceed 10MB.");
                throw new IOException("File size too large");
            }

            // Validate the file type (must be an image)
            if (!imageFile.getContentType().startsWith("image/")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Only image files are allowed.");
                throw new IOException("Invalid file type");
            }

            // Convert the image file to a byte array
            byte[] imageBytes = imageFile.getBytes();
            // Set the image bytes to the student entity
            student.setImageBytes(imageBytes);
        }
    }

    /**
     * Displays the form for editing an existing student.
     *
     * @param id the ID of the student to be edited
     * @param model the model to be used by the view
     * @return the view name to be rendered
     * @throws EntityNotFoundException if the student is not found
     */
    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new EntityNotFoundException("Student with ID " + id + " not found");
        }
        model.addAttribute("student", student);
        return "edit_student";
    }

     /**
     * Handles the submission of an edited student form and updates the student.
     *
     * @param id the ID of the student to be updated
     * @param student the student entity with updated information
     * @param imageFile the image file associated with the student
     * @param result the binding result for form validation
     * @param redirectAttributes the redirect attributes for flash messages
     * @return the view name to redirect to
     */
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student student,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit_student";
        }

        try {
            Student existingStudent = studentService.getStudentById(id);
            if (existingStudent == null) {
                throw new EntityNotFoundException("Student with ID " + id + " not found");
            }

            // Process and handle the image file
            handleImageFile(imageFile, existingStudent, redirectAttributes);

            // Update the existing student's details
            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setBirthdate(student.getBirthdate());
            existingStudent.setAge(student.getAge());

            // Save the updated student
            studentService.updateStudent(existingStudent);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
        } catch (EntityNotFoundException e) {
            logger.error("Student with ID {} not found", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            return "redirect:/students";
        } catch (IOException io) {
            logger.error("Error processing image file", io);
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing image file.");
            return "redirect:/students";
        }
        return "redirect:/students";
    }
    /**
     * Handles the deletion of a student.
     *
     * @param id the ID of the student to be deleted
     * @param redirectAttributes the redirect attributes for flash messages
     * @return the view name to redirect to
     */
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            return "redirect:/error";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while trying to delete the student.");
            return "redirect:/error";
        }
        return "redirect:/students";
    }
}
