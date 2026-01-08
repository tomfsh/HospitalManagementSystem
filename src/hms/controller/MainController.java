package hms.controller;

import hms.service
.DataStore;
import hms.view.MainFrame;

import javax.swing.*;

public class MainController {

    private final DataStore dataStore;
    private MainFrame mainFrame;

    public MainController() {
        this.dataStore = new DataStore("data"); // folder at project root
    }

    public void start() {
        try {
            dataStore.loadAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load CSV files.\n\n" + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        mainFrame = new MainFrame(
                new PatientController(dataStore),
                new ClinicianController(dataStore),
                new AppointmentController(dataStore),
                new PrescriptionController(dataStore),
                new ReferralController(dataStore),
                new StaffController(dataStore)
        );

        mainFrame.setVisible(true);
    }
}


