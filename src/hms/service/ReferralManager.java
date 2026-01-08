package hms.service;

import hms.model
.Clinician;
import hms.model
.Patient;
import hms.model
.Referral;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class ReferralManager {

    private static ReferralManager instance;

    private final DataStore store;
    private final Deque<Referral> queue = new ArrayDeque<>();
    private final AuditLogger audit;

    private ReferralManager(DataStore store) {
        this.store = store;
        this.audit = new AuditLogger(store.getOutFolder());
    }


    public static synchronized ReferralManager getInstance(DataStore store) {
        if (instance == null) {
            instance = new ReferralManager(store);
        }
        return instance;
    }

    public String previewText(Referral r) {
        return buildReferralEmail(r);
    }


    public void enqueueReferral(Referral r) throws IOException {
        if (isBlank(r.getReferralId())) {
            r.setReferralId(store.referrals().nextId("R"));
        }
        if (isBlank(r.getCreatedDate())) {
            r.setCreatedDate(LocalDate.now().toString());
        }
        r.setLastUpdated(LocalDate.now().toString());

        queue.addLast(r);
        audit("Enqueued referral " + r.getReferralId());
    }

    public void processQueue() throws IOException {
        while (!queue.isEmpty()) {
            Referral r = queue.removeFirst();
            store.referrals().add(r);
            store.saveReferrals();

            String emailText = buildReferralEmail(r);

            Path referralOutDir = store.getOutFolder().resolve("referrals");
            Files.createDirectories(referralOutDir);
            Path outFile = referralOutDir.resolve(r.getReferralId() + "_referral_email.txt");
            Files.writeString(outFile, emailText,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            audit("Processed referral " + r.getReferralId() + " -> wrote " + outFile.getFileName());
        }
    }

    public void audit(String msg) {
        audit.log(msg);
    }

    public String generateOutputForExisting(Referral r) throws IOException {

        String text = buildReferralEmail(r);

        Path outDir = store.getOutFolder().resolve("referrals");
        Files.createDirectories(outDir);

        Path outFile = outDir.resolve(r.getReferralId() + "_referral.txt");
        Files.writeString(outFile,
                text,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        audit("Generated referral output for existing referral " + r.getReferralId());
        return text;
    }


    private String buildReferralEmail(Referral r) {
        Optional<Patient> patient = store.patients().findById(r.getPatientId());
        Optional<Clinician> fromClin = store.clinicians().findById(r.getReferringClinicianId());
        Optional<Clinician> toClin = store.clinicians().findById(r.getReferredToClinicianId());

        String patientName = patient.map(Patient::fullName).orElse(r.getPatientId());
        String fromName = fromClin.map(Clinician::fullName).orElse(r.getReferringClinicianId());
        String toName = toClin.map(Clinician::fullName).orElse(r.getReferredToClinicianId());

        return "SENT"
                + " REFERRAL EMAIL (SIMULATED)\n"
                + "Generated: " + LocalDateTime.now() + "\n\n"
                + "Referral ID: " + r.getReferralId() + "\n"
                + "Patient: " + patientName + " " + r.getPatientId() + "\n"
                + "Referring Clinician: " + fromName + " " + r.getReferringClinicianId() + "\n"
                + "Referred To: " + toName + " " + r.getReferredToClinicianId() + "\n"
                + "Urgency: " + nvl(r.getUrgencyLevel()) + "\n"
                + "Referral Date: " + nvl(r.getReferralDate()) + "\n\n"
                + "Reason:\n" + nvl(r.getReferralReason()) + "\n\n"
                + "Clinical Summary:\n" + nvl(r.getClinicalSummary()) + "\n\n"
                + "Requested Investigations:\n" + nvl(r.getRequestedInvestigations()) + "\n\n"
                + "Notes:\n" + nvl(r.getNotes()) + "\n\n"
                + "Status: " + nvl(r.getStatus()) + "\n";
    }
    

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}


