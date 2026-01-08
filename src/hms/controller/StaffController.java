package hms.controller;

import hms.model.Staff;
import hms.service.DataStore;

import java.io.IOException;
import java.util.List;

public class StaffController {
    private final DataStore store;

    public StaffController(DataStore store) {
        this.store = store;
    }

    public List<Staff> all() {
        return store.staff().all();
    }

    public Staff create(Staff s) throws IOException {
        store.staff().add(s);
        store.saveStaff();
        return s;
    }

    public void update(Staff s) throws IOException {
        store.staff().update(s);
        store.saveStaff();
    }

    public void delete(String staffId) throws IOException {
        store.staff().delete(staffId);
        store.saveStaff();
    }

    public String nextId() {
        return store.staff().nextId("ST");
    }
}

