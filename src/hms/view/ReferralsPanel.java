package hms.view;

import hms.controller
.ReferralController;
import hms.model
.Referral;
import hms.view
.tablemodels.ReferralsTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class ReferralsPanel extends JPanel {
    private final ReferralController controller;
    private final ReferralsTableModel model = new ReferralsTableModel();
    private final JTable table = new JTable(model);

    public ReferralsPanel(ReferralController controller) {
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
        JButton genBtn = new JButton("Generate Output");



        addBtn.addActionListener(_ -> onAdd());
        editBtn.addActionListener(_ -> onEdit());
        delBtn.addActionListener(_ -> onDelete());
        refreshBtn.addActionListener(_ -> refresh());
        viewBtn.addActionListener(_ -> onView());
        genBtn.addActionListener(_ -> onGenerateOutput());



        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);
        buttons.add(refreshBtn);
        buttons.add(viewBtn);
        buttons.add(genBtn);



        add(buttons, BorderLayout.NORTH);
    }

    private void refresh() { model.setRows(controller.all()); }

    private void onAdd() {
        Map<String, JTextField> f = SwingForms.buildForm(
                "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id",
                "referring_facility_id","referred_to_facility_id","referral_date","urgency_level",
                "referral_reason","clinical_summary","requested_investigations","status",
                "appointment_id","notes","created_date","last_updated"
        );

        f.get("referral_id").setText(controller.nextId());
        f.get("referral_date").setText(LocalDate.now().toString());
        f.get("created_date").setText(LocalDate.now().toString());
        f.get("last_updated").setText(LocalDate.now().toString());
        f.get("status").setText("Pending");

        int res = SwingForms.showForm(this, "Add Referral", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Referral r = new Referral(
                    f.get("referral_id").getText(),
                    f.get("patient_id").getText(),
                    f.get("referring_clinician_id").getText(),
                    f.get("referred_to_clinician_id").getText(),
                    f.get("referring_facility_id").getText(),
                    f.get("referred_to_facility_id").getText(),
                    f.get("referral_date").getText(),
                    f.get("urgency_level").getText(),
                    f.get("referral_reason").getText(),
                    f.get("clinical_summary").getText(),
                    f.get("requested_investigations").getText(),
                    f.get("status").getText(),
                    f.get("appointment_id").getText(),
                    f.get("notes").getText(),
                    f.get("created_date").getText(),
                    f.get("last_updated").getText()
            );

            controller.create(r);

            JOptionPane.showMessageDialog(this,
                    "Referral created.\n\nCheck: data/out/referrals/\n(and audit log in data/out/audit.log)",
                    "Referral Created",
                    JOptionPane.INFORMATION_MESSAGE);

            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Referral r = model.getAt(row);

        Map<String, JTextField> f = SwingForms.buildForm(
                "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id",
                "referring_facility_id","referred_to_facility_id","referral_date","urgency_level",
                "referral_reason","clinical_summary","requested_investigations","status",
                "appointment_id","notes","created_date","last_updated"
        );

        f.get("referral_id").setText(r.getReferralId());
        f.get("referral_id").setEditable(false);

        f.get("patient_id").setText(r.getPatientId());
        f.get("referring_clinician_id").setText(r.getReferringClinicianId());
        f.get("referred_to_clinician_id").setText(r.getReferredToClinicianId());
        f.get("referring_facility_id").setText(r.getReferringFacilityId());
        f.get("referred_to_facility_id").setText(r.getReferredToFacilityId());
        f.get("referral_date").setText(r.getReferralDate());
        f.get("urgency_level").setText(r.getUrgencyLevel());
        f.get("referral_reason").setText(r.getReferralReason());
        f.get("clinical_summary").setText(r.getClinicalSummary());
        f.get("requested_investigations").setText(r.getRequestedInvestigations());
        f.get("status").setText(r.getStatus());
        f.get("appointment_id").setText(r.getAppointmentId());
        f.get("notes").setText(r.getNotes());
        f.get("created_date").setText(r.getCreatedDate());
        f.get("last_updated").setText(LocalDate.now().toString());

        int res = SwingForms.showForm(this, "Edit Referral", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            r.setPatientId(f.get("patient_id").getText());
            r.setReferringClinicianId(f.get("referring_clinician_id").getText());
            r.setReferredToClinicianId(f.get("referred_to_clinician_id").getText());
            r.setReferringFacilityId(f.get("referring_facility_id").getText());
            r.setReferredToFacilityId(f.get("referred_to_facility_id").getText());
            r.setReferralDate(f.get("referral_date").getText());
            r.setUrgencyLevel(f.get("urgency_level").getText());
            r.setReferralReason(f.get("referral_reason").getText());
            r.setClinicalSummary(f.get("clinical_summary").getText());
            r.setRequestedInvestigations(f.get("requested_investigations").getText());
            r.setStatus(f.get("status").getText());
            r.setAppointmentId(f.get("appointment_id").getText());
            r.setNotes(f.get("notes").getText());
            r.setCreatedDate(f.get("created_date").getText());
            r.setLastUpdated(f.get("last_updated").getText());

            controller.update(r);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onGenerateOutput() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a referral first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Referral r = model.getAt(row);

        try {
            controller.generateOutput(r); // writes file + audit
            JOptionPane.showMessageDialog(this,
                    "Referral output generated (data/out/referrals/).",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onView() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a referral first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Referral r = model.getAt(row);

        String text = controller.previewText(r); // preview only

        JTextArea area = new JTextArea(text, 25, 70);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "Referral: " + r.getReferralId(),
                JOptionPane.INFORMATION_MESSAGE);
    }




    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Referral r = model.getAt(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete referral " + r.getReferralId() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.delete(r.getReferralId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
