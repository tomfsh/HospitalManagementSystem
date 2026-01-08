package hms.view.tablemodels;

import hms.model.Staff;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StaffTableModel extends AbstractTableModel {

    private final String[] cols = {
            "staff_id","first_name","last_name","role","department","facility_id",
            "phone_number","email","employment_status","start_date","line_manager","access_level"
    };

    private List<Staff> rows = new ArrayList<>();

    public void setRows(List<Staff> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public Staff getAt(int row) { return rows.get(row); }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Staff s = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> s.getStaffId();
            case 1 -> s.getFirstName();
            case 2 -> s.getLastName();
            case 3 -> s.getRole();
            case 4 -> s.getDepartment();
            case 5 -> s.getFacilityId();
            case 6 -> s.getPhoneNumber();
            case 7 -> s.getEmail();
            case 8 -> s.getEmploymentStatus();
            case 9 -> s.getStartDate();
            case 10 -> s.getLineManager();
            case 11 -> s.getAccessLevel();
            default -> "";
        };
    }
}

