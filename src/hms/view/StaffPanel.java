package hms.view;

import hms.controller.StaffController;
import hms.model.Staff;
import hms.view.tablemodels.StaffTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class StaffPanel extends JPanel {

    private final StaffController controller;
    private final StaffTableModel model = new StaffTableModel();
    private final JTable table = new JTable(model);

    public StaffPanel(StaffController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(_ -> onAdd());
        editBtn.addActionListener(_ -> onEdit());
        delBtn.addActionListener(_ -> onDelete());
        refreshBtn.addActionListener(_ -> refresh());

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(delBtn);
        buttons.add(refreshBtn);

        add(buttons, BorderLayout.NORTH);

        refresh();
    }

    private void refresh() {
        model.setRows(controller.all());
    }

    private void onAdd() {
        Map<String, JTextField> f = SwingForms.buildForm(
                "staff_id","first_name","last_name","role","department","facility_id",
                "phone_number","email","employment_status","start_date","line_manager","access_level"
        );

        f.get("staff_id").setText(controller.nextId());
        f.get("start_date").setText(LocalDate.now().toString());

        int res = SwingForms.showForm(this, "Add Staff", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Staff s = new Staff(
                    f.get("staff_id").getText(),
                    f.get("first_name").getText(),
                    f.get("last_name").getText(),
                    f.get("role").getText(),
                    f.get("department").getText(),
                    f.get("facility_id").getText(),
                    f.get("phone_number").getText(),
                    f.get("email").getText(),
                    f.get("employment_status").getText(),
                    f.get("start_date").getText(),
                    f.get("line_manager").getText(),
                    f.get("access_level").getText()
            );
            controller.create(s);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Staff s = model.getAt(row);

        Map<String, JTextField> f = SwingForms.buildForm(
                "staff_id","first_name","last_name","role","department","facility_id",
                "phone_number","email","employment_status","start_date","line_manager","access_level"
        );

        f.get("staff_id").setText(s.getStaffId());
        f.get("staff_id").setEditable(false);

        f.get("first_name").setText(s.getFirstName());
        f.get("last_name").setText(s.getLastName());
        f.get("role").setText(s.getRole());
        f.get("department").setText(s.getDepartment());
        f.get("facility_id").setText(s.getFacilityId());
        f.get("phone_number").setText(s.getPhoneNumber());
        f.get("email").setText(s.getEmail());
        f.get("employment_status").setText(s.getEmploymentStatus());
        f.get("start_date").setText(s.getStartDate());
        f.get("line_manager").setText(s.getLineManager());
        f.get("access_level").setText(s.getAccessLevel());

        int res = SwingForms.showForm(this, "Edit Staff", f);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            s.setFirstName(f.get("first_name").getText());
            s.setLastName(f.get("last_name").getText());
            s.setRole(f.get("role").getText());
            s.setDepartment(f.get("department").getText());
            s.setFacilityId(f.get("facility_id").getText());
            s.setPhoneNumber(f.get("phone_number").getText());
            s.setEmail(f.get("email").getText());
            s.setEmploymentStatus(f.get("employment_status").getText());
            s.setStartDate(f.get("start_date").getText());
            s.setLineManager(f.get("line_manager").getText());
            s.setAccessLevel(f.get("access_level").getText());

            controller.update(s);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Staff s = model.getAt(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete staff " + s.getStaffId() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.delete(s.getStaffId());
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

