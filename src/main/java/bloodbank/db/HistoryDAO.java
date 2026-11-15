package bloodbank.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {
    public List<String[]> donations(String filter) throws SQLException { return donations(filter, null, null); }

    public List<String[]> donations(String filter, LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT d.id,do.name,do.phone,do.blood_group,d.units,d.donation_date FROM donations d JOIN donors do ON d.donor_id=do.id WHERE (do.name LIKE ? OR do.phone LIKE ? OR do.blood_group LIKE ?)");
        if (fromDate != null) sql.append(" AND d.donation_date>=?");
        if (toDate != null) sql.append(" AND d.donation_date<=?");
        sql.append(" ORDER BY d.donation_date DESC,d.id DESC");
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            String pattern = "%" + (filter == null ? "" : filter.trim()) + "%";
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            int index = 4;
            if (fromDate != null) statement.setDate(index++, Date.valueOf(fromDate));
            if (toDate != null) statement.setDate(index, Date.valueOf(toDate));
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) rows.add(new String[]{result.getString("id"),result.getString("name"),result.getString("phone"),result.getString("blood_group"),result.getString("units"),result.getString("donation_date")});
            }
        }
        return rows;
    }
}
