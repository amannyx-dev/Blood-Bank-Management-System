package bloodbank.core;

import java.time.LocalDate;

public class Donor extends Person implements Screenable {
    private BloodGroup bloodGroup;
    private double weight;
    private LocalDate lastDonation;
    public Donor(int id,String name,int age,String gender,String phone,BloodGroup bloodGroup,double weight,String city,LocalDate lastDonation){super(id,name,age,gender,phone,city);this.bloodGroup=bloodGroup;this.weight=weight;this.lastDonation=lastDonation;}
    public Donor(String name,int age,String gender,String phone,BloodGroup bloodGroup,double weight,String city){this(0,name,age,gender,phone,bloodGroup,weight,city,null);}
    public BloodGroup getBloodGroup(){return bloodGroup;} public void setBloodGroup(BloodGroup value){bloodGroup=value;}
    public double getWeight(){return weight;} public void setWeight(double value){weight=value;}
    public LocalDate getLastDonation(){return lastDonation;} public void setLastDonation(LocalDate value){lastDonation=value;}
    public String getRole(){return "Donor";}
    public boolean isEligible(){return eligibilityMessage().equals("Eligible");}
    public String eligibilityMessage(){if(getAge()<18||getAge()>65)return "Donor age must be between 18 and 65 years";if(weight<50)return "Weight must be at least 50 kg";if(lastDonation!=null&&lastDonation.plusDays(90).isAfter(LocalDate.now()))return "A 90-day gap is required after the previous donation";return "Eligible";}
}
