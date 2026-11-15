package bloodbank.db;

import bloodbank.core.BloodGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

public class StockDAO {
    public synchronized void addUnits(BloodGroup group, int count) throws SQLException {
        String sql = "INSERT INTO stock(blood_group,collection_date,expiry_date,status) VALUES(?,CURRENT_DATE,DATE_ADD(CURRENT_DATE,INTERVAL 42 DAY),'AVAILABLE')";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int index = 0; index < count; index++) { statement.setString(1, group.toString()); statement.addBatch(); }
            statement.executeBatch();
        }
    }

    public synchronized void recordDonation(int donorId, BloodGroup group, int units) throws SQLException {
        if (donorId < 1 || group == null || units < 1 || units > 3) throw new SQLException("Enter a valid donor ID and 1 to 3 units.");
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                java.sql.Date lastDonation;
                String donorGroup;
                try (PreparedStatement donor = connection.prepareStatement("SELECT blood_group,last_donation FROM donors WHERE id=? FOR UPDATE")) {
                    donor.setInt(1, donorId);
                    try (ResultSet result = donor.executeQuery()) {
                        if (!result.next()) throw new SQLException("Donor ID was not found.");
                        donorGroup = result.getString("blood_group");
                        lastDonation = result.getDate("last_donation");
                    }
                }
                if (!group.toString().equals(donorGroup)) throw new SQLException("Selected blood group does not match the donor record.");
                if (lastDonation != null && lastDonation.toLocalDate().plusDays(90).isAfter(java.time.LocalDate.now())) throw new SQLException("A 90-day gap is required after the donor's previous donation.");
                try (PreparedStatement stock = connection.prepareStatement("INSERT INTO stock(blood_group,collection_date,expiry_date,status) VALUES(?,CURRENT_DATE,DATE_ADD(CURRENT_DATE,INTERVAL 42 DAY),'AVAILABLE')")) {
                    for (int index = 0; index < units; index++) { stock.setString(1, group.toString()); stock.addBatch(); }
                    stock.executeBatch();
                }
                try (PreparedStatement donation = connection.prepareStatement("INSERT INTO donations(donor_id,units,donation_date) VALUES(?,?,CURRENT_DATE)")) {
                    donation.setInt(1, donorId);
                    donation.setInt(2, units);
                    donation.executeUpdate();
                }
                try (PreparedStatement updateDonor = connection.prepareStatement("UPDATE donors SET last_donation=CURRENT_DATE WHERE id=?")) {
                    updateDonor.setInt(1, donorId);
                    updateDonor.executeUpdate();
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public Map<BloodGroup,Integer> getCounts() throws SQLException {
        Map<BloodGroup,Integer> counts = new EnumMap<>(BloodGroup.class);
        for (BloodGroup group : BloodGroup.values()) counts.put(group, 0);
        String sql = "SELECT blood_group,COUNT(*) total FROM stock WHERE status='AVAILABLE' AND expiry_date>=CURRENT_DATE AND collection_date>DATE_SUB(CURRENT_DATE,INTERVAL 42 DAY) GROUP BY blood_group";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
            while (result.next()) counts.put(BloodGroup.fromLabel(result.getString("blood_group")), result.getInt("total"));
        }
        return counts;
    }

    public synchronized void removeExpired() throws SQLException {
        String sql = "UPDATE stock SET status='EXPIRED' WHERE status='AVAILABLE' AND (expiry_date<CURRENT_DATE OR collection_date<=DATE_SUB(CURRENT_DATE,INTERVAL 42 DAY))";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) { statement.executeUpdate(); }
    }
}
