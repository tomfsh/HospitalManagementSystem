package hms.view.tablemodels;

import hms.model
.Prescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionsTableModel extends AbstractTableModel {
    private final String[] cols = {
            "prescription_id","patient_id","clinician_id","appointment_id","prescription_date",
            "medication_name","dosage","frequency","duration_days","quantity","instructions",
            "pharmacy_name","status","issue_date","collection_date"
    };

    private List<Prescription> rows = new ArrayList<>();

    public void setRows(List<Prescription> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public Prescription getAt(int row) { return rows.get(row); }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prescription p = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getPrescriptionId();
            case 1 -> p.getPatientId();
            case 2 -> p.getClinicianId();
            case 3 -> p.getAppointmentId();
            case 4 -> p.getPrescriptionDate();
            case 5 -> p.getMedicationName();
            case 6 -> p.getDosage();
            case 7 -> p.getFrequency();
            case 8 -> p.getDurationDays();
            case 9 -> p.getQuantity();
            case 10 -> p.getInstructions();
            case 11 -> p.getPharmacyName();
            case 12 -> p.getStatus();
            case 13 -> p.getIssueDate();
            case 14 -> p.getCollectionDate();
            default -> "";
        };
    }
}


