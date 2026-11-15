package bloodbank.core;

import java.time.LocalDate;

public class BloodUnit extends BloodRecord {
    private final BloodGroup bloodGroup;
    private final LocalDate expiryDate;
    private boolean available;
    public BloodUnit(int id,BloodGroup group,LocalDate collectedOn){super(id,collectedOn);this.bloodGroup=group;this.expiryDate=collectedOn.plusDays(42);this.available=true;}
    public BloodGroup getBloodGroup(){return bloodGroup;} public LocalDate getExpiryDate(){return expiryDate;}
    public boolean isAvailable(){return available;} public void setAvailable(boolean value){available=value;}
    public String getRecordType(){return "Blood unit";}
}
