package hms.view.tablemodels;

import hms.model
.Referral;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReferralsTableModel extends AbstractTableModel {
    private final String[] cols = {
            "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id",
            "referring_facility_id","referred_to_facility_id","referral_date","urgency_level",
            "referral_reason","clinical_summary","requested_investigations","status",
            "appointment_id","notes","created_date","last_updated"
    };

    private List<Referral> rows = new ArrayList<>();

    public void setRows(List<Referral> rows) {
        this.rows = new ArrayList<>(rows);
        fireTableDataChanged();
    }

    public Referral getAt(int row) { return rows.get(row); }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Referral r = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.getReferralId();
            case 1 -> r.getPatientId();
            case 2 -> r.getReferringClinicianId();
            case 3 -> r.getReferredToClinicianId();
            case 4 -> r.getReferringFacilityId();
            case 5 -> r.getReferredToFacilityId();
            case 6 -> r.getReferralDate();
            case 7 -> r.getUrgencyLevel();
            case 8 -> r.getReferralReason();
            case 9 -> r.getClinicalSummary();
            case 10 -> r.getRequestedInvestigations();
            case 11 -> r.getStatus();
            case 12 -> r.getAppointmentId();
            case 13 -> r.getNotes();
            case 14 -> r.getCreatedDate();
            case 15 -> r.getLastUpdated();
            default -> "";
        };
    }
}

