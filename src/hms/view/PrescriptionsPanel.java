package hms.view;

import hms.controller
.PrescriptionController;
import hms.model
.Prescription;
import hms.view
.tablemodels.PrescriptionsTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class PrescriptionsPanel extends JPanel {
    private final PrescriptionController controller;
    private final PrescriptionsTableModel model = new PrescriptionsTableModel();
    private final JTable table = new JTable(model);

    public PrescriptionsPanel(PrescriptionController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        model.setRows(controller.all());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton viewBtn = new JButton("View");
        JButton outputBtn = new JButton("Generate Output");


        addBtn.addActionListener(_ -> onAdd());
        editBtn.addActionListener(_ -> onEdit());
        delBtn.addActionListener(_ -> onDelete());
        refreshBtn.addActionListener(_ -> refresh());
        outputBtn.addActionListener(_ -> onGenerateOutput());
        viewBtn.addActionListener(_ -> onView());



        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);
        buttons.add(refreshBtn);
        buttons.add(viewBtn);
        buttons.add(outputBtn);


        add(buttons, BorderLayout.NORTH);
    }

    private void refresh() { model.setRows(controller.all()); }

    private void onAdd() {
        Map<String, JTextField> f = SwingForms.buildForm(
                "prescription_id","patient_id","clinician_id","appointment_id","prescription_date",
                "medication_name","dosage","frequency","duration_days","quantity","instructions",
                "pharmacy_name","status","issue_date","collection_date"
        );

        f.get("prescription_id").setText(controller.nextId());
        f.get("prescription_date").setText(LocalDate.now().toString());
        f.get("issue_date").setText(LocalDate.now().toString());

        int res = SwingForms.showForm(this, "Add Prescription", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Prescription p = new Prescription(
                    f.get("prescription_id").getText(),
                    f.get("patient_id").getText(),
                    f.get("clinician_id").getText(),
                    f.get("appointment_id").getText(),
                    f.get("prescription_date").getText(),
                    f.get("medication_name").getText(),
                    f.get("dosage").getText(),
                    f.get("frequency").getText(),
                    f.get("duration_days").getText(),
                    f.get("quantity").getText(),
                    f.get("instructions").getText(),
                    f.get("pharmacy_name").getText(),
                    f.get("status").getText(),
                    f.get("issue_date").getText(),
                    f.get("collection_date").getText()
            );
            controller.create(p);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onView() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a prescription first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prescription p = model.getAt(row);

        String text =
                "PRESCRIPTION DETAILS\n\n" +
                        "Prescription ID: " + p.getPrescriptionId() + "\n" +
                        "Patient ID: " + p.getPatientId() + "\n" +
                        "Clinician ID: " + p.getClinicianId() + "\n\n" +
                        "Medication: " + p.getMedicationName() + "\n" +
                        "Dosage: " + p.getDosage() + "\n" +
                        "Frequency: " + p.getFrequency() + "\n" +
                        "Duration (days): " + p.getDurationDays() + "\n" +
                        "Quantity: " + p.getQuantity() + "\n\n" +
                        "Instructions: " + safe(p.getInstructions()) + "\n\n" +
                        "Pharmacy: " + safe(p.getPharmacyName()) + "\n" +
                        "Status: " + safe(p.getStatus()) + "\n" +
                        "Issue Date: " + safe(p.getIssueDate()) + "\n" +
                        "Collection Date: " + safe(p.getCollectionDate()) + "\n";

        JTextArea area = new JTextArea(text, 22, 70);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "View Prescription: " + p.getPrescriptionId(),
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Prescription p = model.getAt(row);

        Map<String, JTextField> f = SwingForms.buildForm(
                "prescription_id","patient_id","clinician_id","appointment_id","prescription_date",
                "medication_name","dosage","frequency","duration_days","quantity","instructions",
                "pharmacy_name","status","issue_date","collection_date"
        );

        f.get("prescription_id").setText(p.getPrescriptionId());
        f.get("prescription_id").setEditable(false);

        f.get("patient_id").setText(p.getPatientId());
        f.get("clinician_id").setText(p.getClinicianId());
        f.get("appointment_id").setText(p.getAppointmentId());
        f.get("prescription_date").setText(p.getPrescriptionDate());
        f.get("medication_name").setText(p.getMedicationName());
        f.get("dosage").setText(p.getDosage());
        f.get("frequency").setText(p.getFrequency());
        f.get("duration_days").setText(p.getDurationDays());
        f.get("quantity").setText(p.getQuantity());
        f.get("instructions").setText(p.getInstructions());
        f.get("pharmacy_name").setText(p.getPharmacyName());
        f.get("status").setText(p.getStatus());
        f.get("issue_date").setText(p.getIssueDate());
        f.get("collection_date").setText(p.getCollectionDate());

        int res = SwingForms.showForm(this, "Edit Prescription", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            p.setPatientId(f.get("patient_id").getText());
            p.setClinicianId(f.get("clinician_id").getText());
            p.setAppointmentId(f.get("appointment_id").getText());
            p.setPrescriptionDate(f.get("prescription_date").getText());
            p.setMedicationName(f.get("medication_name").getText());
            p.setDosage(f.get("dosage").getText());
            p.setFrequency(f.get("frequency").getText());
            p.setDurationDays(f.get("duration_days").getText());
            p.setQuantity(f.get("quantity").getText());
            p.setInstructions(f.get("instructions").getText());
            p.setPharmacyName(f.get("pharmacy_name").getText());
            p.setStatus(f.get("status").getText());
            p.setIssueDate(f.get("issue_date").getText());
            p.setCollectionDate(f.get("collection_date").getText());

            controller.update(p);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Prescription p = model.getAt(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete prescription " + p.getPrescriptionId() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.delete(p.getPrescriptionId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onGenerateOutput() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a prescription first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Prescription p = model.getAt(row);

        try {
            controller.generateOutput(p);
            JOptionPane.showMessageDialog(this,
                    "Prescription output generated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private String safe(String s) {
        return s == null ? "" : s;
    }


}


