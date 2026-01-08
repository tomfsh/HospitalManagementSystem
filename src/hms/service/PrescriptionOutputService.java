package hms.service;

import hms.model.Prescription;
import hms.model.Patient;
import hms.model.Clinician;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class PrescriptionOutputService {

    private final DataStore store;
    private final AuditLogger audit;

    public PrescriptionOutputService(DataStore store) {
        this.store = store;
        this.audit = new AuditLogger(store.getOutFolder());
    }

    public void generate(Prescription p) throws IOException {

        Optional<Patient> patient = store.patients().findById(p.getPatientId());
        Optional<Clinician> clinician = store.clinicians().findById(p.getClinicianId());

        String patientName = patient.map(Patient::fullName).orElse(p.getPatientId());
        String clinicianName = clinician.map(Clinician::fullName).orElse(p.getClinicianId());

        String text =
                "ELECTRONIC PRESCRIPTION (SIMULATED)\n" +
                        "Generated: " + LocalDateTime.now() + "\n\n" +
                        "Prescription ID: " + p.getPrescriptionId() + "\n" +
                        "Patient: " + patientName + " (" + p.getPatientId() + ")\n" +
                        "Prescribing Clinician: " + clinicianName + " (" + p.getClinicianId() + ")\n\n" +
                        "Medication: " + p.getMedicationName() + "\n" +
                        "Dosage: " + p.getDosage() + "\n" +
                        "Frequency: " + p.getFrequency() + "\n" +
                        "Duration (days): " + p.getDurationDays() + "\n" +
                        "Quantity: " + p.getQuantity() + "\n\n" +
                        "Instructions:\n" + safe(p.getInstructions()) + "\n\n" +
                        "Pharmacy: " + safe(p.getPharmacyName()) + "\n" +
                        "Status: " + safe(p.getStatus()) + "\n" +
                        "Issue Date: " + safe(p.getIssueDate()) + "\n" +
                        "Collection Date: " + safe(p.getCollectionDate()) + "\n";

        Path outDir = store.getOutFolder().resolve("prescriptions");
        Files.createDirectories(outDir);

        Path outFile = outDir.resolve(p.getPrescriptionId() + "_prescription.txt");

        Files.write(outFile,
                text.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        audit.log("Generated prescription output for " + p.getPrescriptionId());
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}

