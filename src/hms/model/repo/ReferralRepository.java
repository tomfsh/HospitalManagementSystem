package hms.model.repo;

import hms.model
.Referral;

public class ReferralRepository extends BaseRepository<Referral> {
    public ReferralRepository() {
        super(Referral::getReferralId);
    }
}
