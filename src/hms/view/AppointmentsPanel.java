package hms.view;

import hms.controller
.AppointmentController;
import hms.model
.Appointment;
import hms.view
.tablemodels.AppointmentsTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class AppointmentsPanel extends JPanel {
    private final AppointmentController controller;
    private final AppointmentsTableModel model = new AppointmentsTableModel();
    private final JTable table = new JTable(model);

    public AppointmentsPanel(AppointmentController controller) {
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


        addBtn.addActionListener(_ -> onAdd());
        editBtn.addActionListener(_ -> onEdit());
        delBtn.addActionListener(_ -> onDelete());
        refreshBtn.addActionListener(_ -> refresh());
        viewBtn.addActionListener(_ -> onView());


        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);
        buttons.add(refreshBtn);
        buttons.add(viewBtn);


        add(buttons, BorderLayout.NORTH);
    }

    private void refresh() { model.setRows(controller.all()); }

    private void onAdd() {
        Map<String, JTextField> f = SwingForms.buildForm(
                "appointment_id","patient_id","clinician_id","facility_id","appointment_date",
                "appointment_time","duration_minutes","appointment_type","status",
                "reason_for_visit","notes","created_date","last_modified"
        );
        f.get("appointment_id").setText(controller.nextId());
        f.get("created_date").setText(LocalDate.now().toString());
        f.get("last_modified").setText(LocalDate.now().toString());

        int res = SwingForms.showForm(this, "Add Appointment", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Appointment a = new Appointment(
                    f.get("appointment_id").getText(),
                    f.get("patient_id").getText(),
                    f.get("clinician_id").getText(),
                    f.get("facility_id").getText(),
                    f.get("appointment_date").getText(),
                    f.get("appointment_time").getText(),
                    f.get("duration_minutes").getText(),
                    f.get("appointment_type").getText(),
                    f.get("status").getText(),
                    f.get("reason_for_visit").getText(),
                    f.get("notes").getText(),
                    f.get("created_date").getText(),
                    f.get("last_modified").getText()
            );
            controller.create(a);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onView() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an appointment first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("APPOINTMENT DETAILS\n\n");

        for (int col = 0; col < model.getColumnCount(); col++) {
            String colName = model.getColumnName(col);
            Object value = model.getValueAt(row, col);

            sb.append(colName).append(": ").append(value == null ? "" : value.toString()).append("\n");
        }

        JTextArea area = new JTextArea(sb.toString(), 22, 70);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(this,
                new JScrollPane(area),
                "View Appointment",
                JOptionPane.INFORMATION_MESSAGE);
    }




    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Appointment a = model.getAt(row);

        Map<String, JTextField> f = SwingForms.buildForm(
                "appointment_id","patient_id","clinician_id","facility_id","appointment_date",
                "appointment_time","duration_minutes","appointment_type","status",
                "reason_for_visit","notes","created_date","last_modified"
        );

        f.get("appointment_id").setText(a.getAppointmentId());
        f.get("appointment_id").setEditable(false);

        f.get("patient_id").setText(a.getPatientId());
        f.get("clinician_id").setText(a.getClinicianId());
        f.get("facility_id").setText(a.getFacilityId());
        f.get("appointment_date").setText(a.getAppointmentDate());
        f.get("appointment_time").setText(a.getAppointmentTime());
        f.get("duration_minutes").setText(a.getDurationMinutes());
        f.get("appointment_type").setText(a.getAppointmentType());
        f.get("status").setText(a.getStatus());
        f.get("reason_for_visit").setText(a.getReasonForVisit());
        f.get("notes").setText(a.getNotes());
        f.get("created_date").setText(a.getCreatedDate());
        f.get("last_modified").setText(a.getLastModified());

        int res = SwingForms.showForm(this, "Edit Appointment", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            a.setPatientId(f.get("patient_id").getText());
            a.setClinicianId(f.get("clinician_id").getText());
            a.setFacilityId(f.get("facility_id").getText());
            a.setAppointmentDate(f.get("appointment_date").getText());
            a.setAppointmentTime(f.get("appointment_time").getText());
            a.setDurationMinutes(f.get("duration_minutes").getText());
            a.setAppointmentType(f.get("appointment_type").getText());
            a.setStatus(f.get("status").getText());
            a.setReasonForVisit(f.get("reason_for_visit").getText());
            a.setNotes(f.get("notes").getText());
            a.setCreatedDate(f.get("created_date").getText());
            a.setLastModified(f.get("last_modified").getText());

            controller.update(a);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Appointment a = model.getAt(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete appointment " + a.getAppointmentId() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.delete(a.getAppointmentId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private String safe(String s) {
        return s == null ? "" : s;
    }

}


