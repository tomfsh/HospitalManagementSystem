package hms.view;

import hms.controller.*;
import hms.view
.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(PatientController patientController,
                     ClinicianController clinicianController,
                     AppointmentController appointmentController,
                     PrescriptionController prescriptionController,
                     ReferralController referralController,
                     StaffController staffController) {


        super("Healthcare Management System");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Patients", new PatientsPanel(patientController));
        tabs.addTab("Clinicians", new CliniciansPanel(clinicianController));
        tabs.addTab("Staff", new StaffPanel(staffController));
        tabs.addTab("Appointments", new AppointmentsPanel(appointmentController));
        tabs.addTab("Prescriptions", new PrescriptionsPanel(prescriptionController));
        tabs.addTab("Referrals", new ReferralsPanel(referralController));


        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}

