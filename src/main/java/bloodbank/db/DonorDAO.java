package bloodbank.db;

import bloodbank.core.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DonorDAO {
    public class DonationEntry {
        private final int donorId;
        private final int units;
        private final java.time.LocalDate donationDate;
        public DonationEntry(int donorId,int units,java.time.LocalDate donationDate){this.donorId=donorId;this.units=units;this.donationDate=donationDate;}
        public int getDonorId(){return donorId;}
        public int getUnits(){return units;}
        public java.time.LocalDate getDonationDate(){return donationDate;}
    }
    public int save(Donor donor) throws SQLException {
        String sql="INSERT INTO donors(name,age,gender,phone,blood_group,weight,city,last_donation) VALUES(?,?,?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){bind(s,donor);s.executeUpdate();try(ResultSet r=s.getGeneratedKeys()){if(r.next())return r.getInt(1);}}return 0;
    }
    public void update(Donor donor) throws SQLException {try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("UPDATE donors SET name=?,age=?,gender=?,phone=?,blood_group=?,weight=?,city=?,last_donation=? WHERE id=?")){bind(s,donor);s.setInt(9,donor.getId());s.executeUpdate();}}
    private void bind(PreparedStatement s,Donor d)throws SQLException{s.setString(1,d.getName());s.setInt(2,d.getAge());s.setString(3,d.getGender());s.setString(4,d.getPhone());s.setString(5,d.getBloodGroup().toString());s.setDouble(6,d.getWeight());s.setString(7,d.getCity());if(d.getLastDonation()==null)s.setNull(8,Types.DATE);else s.setDate(8,java.sql.Date.valueOf(d.getLastDonation()));}
    public void delete(int id)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("DELETE FROM donors WHERE id=?")){s.setInt(1,id);s.executeUpdate();}}
    public List<Donor> search(String term)throws SQLException{List<Donor> donors=new ArrayList<>();String sql="SELECT * FROM donors WHERE name LIKE ? OR phone LIKE ? OR blood_group LIKE ? ORDER BY name";try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(sql)){String pattern="%"+term+"%";s.setString(1,pattern);s.setString(2,pattern);s.setString(3,pattern);try(ResultSet r=s.executeQuery()){while(r.next())donors.add(map(r));}}return donors;}
    public Donor findById(int id) throws SQLException {
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM donors WHERE id=?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? map(result) : null;
            }
        }
    }
    public List<Donor> findAll()throws SQLException{return search("");}
    private Donor map(ResultSet r)throws SQLException{java.sql.Date date=r.getDate("last_donation");return new Donor(r.getInt("id"),r.getString("name"),r.getInt("age"),r.getString("gender"),r.getString("phone"),BloodGroup.fromLabel(r.getString("blood_group")),r.getDouble("weight"),r.getString("city"),date==null?null:date.toLocalDate());}
}
