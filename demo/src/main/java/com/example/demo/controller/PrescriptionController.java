package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Consultation;
import com.example.demo.model.MedicalInfo;
import com.example.demo.model.Patient;
import com.example.demo.service.ConsultationService;
import com.example.demo.service.MedicalInfoService;

@Controller
public class PrescriptionController {
	
	@Autowired
    private MedicalInfoService medicalInfoService;
    @Autowired
    private ConsultationService consultationService;
	
	@GetMapping("/prescription/{id}")
    public String showPrescription(@PathVariable String id ,Model model) {
        // Fetch the necessary data from saved objects
        Consultation consultation = consultationService.getConsultationById(id);
        Patient patient = consultation.getPatient();
        MedicalInfo medicalInfo = medicalInfoService.getMedicalInfoByPatientId(patient.getId());

        // Add data to the model
        model.addAttribute("patient", patient);
        model.addAttribute("Id", consultation.getId());
        model.addAttribute("dateOfVisit", consultation.getDateOfVisit());
        model.addAttribute("mode", consultation.getMode());
        model.addAttribute("currentComplaints", consultation.getCurrentComplaints());
        model.addAttribute("diagnosis", consultation.getDiagnosis());
        model.addAttribute("treatment", consultation.getTreatment());
        model.addAttribute("investigations", consultation.getInvestigations());
        model.addAttribute("dietAdvice", consultation.getDietAdvice());
        model.addAttribute("exerciseAdvice", consultation.getExerciseAdvice());
        model.addAttribute("specialAdvice", consultation.getSpecialAdvice());
        model.addAttribute("reviewDate", consultation.getReviewDate());

        // Check if the patient has any allergies and mark in red if not NKA
        List<String> allergies = medicalInfo.getAllergies();
        boolean isNKA = allergies != null && allergies.contains("NKA");
        model.addAttribute("allergies", allergies);
        model.addAttribute("isNKA", isNKA);

        // Return the Thymeleaf template for prescription
        return "prescription";
    }
}
