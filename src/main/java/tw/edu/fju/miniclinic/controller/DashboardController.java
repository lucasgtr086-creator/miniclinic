package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PasswordForm;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");
        
        if (loggedInDoctorId == null) {
            return "redirect:/login";
        }

        Doctor doctor = doctorRepo.findById(loggedInDoctorId).orElse(null);
        
        if (doctor == null) {
            session.invalidate();
            return "redirect:/login";
        }

        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepo.findByDoctorAndApptDate(doctor, today);

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        model.addAttribute("today", today);
        return "dashboard";
    }

    @GetMapping("/password")
    public String showPasswordForm(HttpSession session, Model model) {
        model.addAttribute("loggedInDoctorName", session.getAttribute("loggedInDoctorName"));
        model.addAttribute("passwordForm", new PasswordForm());
        return "password";
    }

    @PostMapping("/password")
    public String processPasswordChange(@Valid @ModelAttribute("passwordForm") PasswordForm form, BindingResult result, HttpSession session, Model model) {
        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");
        
        if (loggedInDoctorId == null) {
            return "redirect:/login";
        }

        Doctor doctor = doctorRepo.findById(loggedInDoctorId).orElse(null);
        model.addAttribute("loggedInDoctorName", session.getAttribute("loggedInDoctorName"));

        if (doctor == null) { return "redirect:/login"; }
        if (result.hasErrors()) {
            return "password";
        }
        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            model.addAttribute("errorMessage", "舊密碼錯誤"); return "password";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errorMessage", "兩次密碼不相符"); return "password";
        }
        if (form.getNewPassword().length() < 8) {
            model.addAttribute("errorMessage", "密碼至少需要 8 個字元"); return "password";
        }

        doctor.setPasswordHash(BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt()));
        doctorRepo.save(doctor);
        model.addAttribute("successMessage", "密碼修改成功");
        return "password";
    }
}