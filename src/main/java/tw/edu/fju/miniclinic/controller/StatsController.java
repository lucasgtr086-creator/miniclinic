package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatsController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String getStats(Model model) {
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());
        
        List<Object[]> deptStatsRaw = appointmentRepo.countByDepartment();
        List<Map<String, Object>> deptStats = new ArrayList<>();
        for (Object[] row : deptStatsRaw) {
            Map<String, Object> map = new HashMap<>();
            map.put("department", row[0]);
            map.put("count", row[1]);
            deptStats.add(map);
        }
        model.addAttribute("deptStats", deptStats);
        return "stats";
    }
}