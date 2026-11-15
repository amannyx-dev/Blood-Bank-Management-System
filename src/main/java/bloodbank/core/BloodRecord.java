package bloodbank.core;

import java.time.LocalDate;

public abstract class BloodRecord {
    private final int id;
    private final LocalDate createdOn;
    protected BloodRecord(int id,LocalDate createdOn){this.id=id;this.createdOn=createdOn;}
    public int getId(){return id;} public LocalDate getCreatedOn(){return createdOn;}
    public abstract String getRecordType();
}
