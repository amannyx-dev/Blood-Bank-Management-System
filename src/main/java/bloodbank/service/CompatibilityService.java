package bloodbank.service;

import bloodbank.core.BloodGroup;
import java.util.*;

public class CompatibilityService {
    private static final Map<BloodGroup,List<BloodGroup>> DONORS=new EnumMap<>(BloodGroup.class);
    static {
        DONORS.put(BloodGroup.O_NEGATIVE,List.of(BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.O_POSITIVE,List.of(BloodGroup.O_POSITIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.A_NEGATIVE,List.of(BloodGroup.A_NEGATIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.A_POSITIVE,List.of(BloodGroup.A_POSITIVE,BloodGroup.A_NEGATIVE,BloodGroup.O_POSITIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.B_NEGATIVE,List.of(BloodGroup.B_NEGATIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.B_POSITIVE,List.of(BloodGroup.B_POSITIVE,BloodGroup.B_NEGATIVE,BloodGroup.O_POSITIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.AB_NEGATIVE,List.of(BloodGroup.AB_NEGATIVE,BloodGroup.A_NEGATIVE,BloodGroup.B_NEGATIVE,BloodGroup.O_NEGATIVE));
        DONORS.put(BloodGroup.AB_POSITIVE,List.of(BloodGroup.values()));
    }
    public List<BloodGroup> compatibleDonors(BloodGroup recipient){return DONORS.getOrDefault(recipient,List.of());}
}
