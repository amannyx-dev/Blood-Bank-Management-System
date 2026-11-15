package bloodbank.core;

import java.time.LocalDate;

public class Request extends BloodRecord implements Comparable<Request> {
    public enum Priority { NORMAL, URGENT, EMERGENCY }
    private final String hospitalName,patientName;
    private final BloodGroup requiredGroup;
    private final int units;
    private final Priority priority;
    private String status;
    public Request(int id,String hospital,String patient,BloodGroup group,int units,Priority priority){super(id,LocalDate.now());this.hospitalName=hospital;this.patientName=patient;this.requiredGroup=group;this.units=units;this.priority=priority;this.status="PENDING";}
    public String getHospitalName(){return hospitalName;} public String getPatientName(){return patientName;} public BloodGroup getRequiredGroup(){return requiredGroup;} public int getUnits(){return units;} public Priority getPriority(){return priority;} public String getStatus(){return status;} public void setStatus(String value){status=value;}
    public String getRecordType(){return "Blood request";}
    public int compareTo(Request other){return Integer.compare(other.priority.ordinal(),priority.ordinal());}
    public String createReceipt(){StringBuffer receipt=new StringBuffer();receipt.append("BLOOD BANK RECEIPT\n").append("Request #").append(getId()).append("\n").append(patientName).append(" - ").append(requiredGroup).append(" x ").append(units);return receipt.toString();}
}
