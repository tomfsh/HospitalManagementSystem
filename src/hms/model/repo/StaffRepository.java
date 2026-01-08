package hms.model.repo;

import hms.model.Staff;

public class StaffRepository extends BaseRepository<Staff> {
    public StaffRepository() {
        super(Staff::getStaffId);
    }
}

