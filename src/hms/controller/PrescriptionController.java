package hms.controller;

import hms.model
.Prescription;
import hms.service
.DataStore;
import hms.service.PrescriptionOutputService;


import java.io.IOException;
import java.util.List;

public class PrescriptionController {
    private final DataStore store;
    private final PrescriptionOutputService outputService;


    public PrescriptionController(DataStore store) {
        this.store = store;
        this.outputService = new PrescriptionOutputService(store);
    }

    public void generateOutput(Prescription p) throws IOException {
        outputService.generate(p);
    }



    public List<Prescription> all() {
        return store.prescriptions().all();
    }

    public Prescription create(Prescription p) throws IOException {
        store.prescriptions().add(p);
        store.savePrescriptions();
        return p;
    }

    public void update(Prescription p) throws IOException {
        store.prescriptions().update(p);
        store.savePrescriptions();
    }

    public void delete(String id) throws IOException {
        store.prescriptions().delete(id);
        store.savePrescriptions();
    }

    public String nextId() {
        return store.prescriptions().nextId("RX");
    }
}


