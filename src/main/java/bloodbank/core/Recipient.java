package bloodbank.core;

public class Recipient extends Person {
    public Recipient(int id,String name,int age,String gender,String phone,String city){super(id,name,age,gender,phone,city);}
    public String getRole(){return "Recipient";}
}
