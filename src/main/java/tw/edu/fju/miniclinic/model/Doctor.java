package tw.edu.fju.miniclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @Column(name = "doctor_id", length = 10)
    private String doctorId;      // 醫師編號
    @Column(name = "name", length = 50, nullable = false)
    private String name;          // 姓名
    @Column(name = "department", length = 20, nullable = false)
    private String department;    // 科別
    @Column(name = "specialty", length = 100)
    private String specialty;     // 專長

    @JsonIgnore
    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    public Doctor() {}

    public Doctor(String doctorId, String name, String department, String specialty) {
        this.doctorId = doctorId;
        this.name = name;
        this.department = department;
        this.specialty = specialty;
    }

    // Getters（Spring 會透過這些方法讀取欄位）
    public String getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getSpecialty() { return specialty; }
    public String getPasswordHash() { return passwordHash; }

    // Setters（之後會用到）
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
