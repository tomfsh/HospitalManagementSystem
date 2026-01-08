package hms.controller;

import hms.model
.Referral;
import hms.service
.DataStore;
import hms.service
.ReferralManager;

import java.io.IOException;
import java.util.List;

public class ReferralController {
    private final DataStore store;
    private final ReferralManager referralManager;

    public ReferralController(DataStore store) {
        this.store = store;
        this.referralManager = ReferralManager.getInstance(store);
    }

    public List<Referral> all() {
        return store.referrals().all();
    }

    public Referral create(Referral r) throws IOException {
        // route through singleton manager for consistency + audit + “email” file
        referralManager.enqueueReferral(r);
        referralManager.processQueue(); // immediate for demo
        return r;
    }

    public void update(Referral r) throws IOException {
        store.referrals().update(r);
        store.saveReferrals();
        referralManager.audit("Updated referral " + r.getReferralId());
    }

    public void delete(String id) throws IOException {
        store.referrals().delete(id);
        store.saveReferrals();
        referralManager.audit("Deleted referral " + id);
    }

    public String nextId() {
        return store.referrals().nextId("R");
    }

    public String generateOutput(Referral r) throws IOException {
        return referralManager.generateOutputForExisting(r);
    }

}


