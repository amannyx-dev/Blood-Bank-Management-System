package bloodbank.db;

import bloodbank.core.BloodGroup;
import bloodbank.core.Request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    public int save(Request request) throws SQLException {
        String sql = "INSERT INTO requests(hospital_name,patient_name,blood_group,units,priority,status) VALUES(?,?,?,?,?,'PENDING')";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, request.getHospitalName());
            statement.setString(2, request.getPatientName());
            statement.setString(3, request.getRequiredGroup().toString());
            statement.setInt(4, request.getUnits());
            statement.setString(5, request.getPriority().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Request could not be saved.");
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE requests SET status=? WHERE id=? AND status='PENDING'";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            if (statement.executeUpdate() != 1) throw new SQLException("Request not found or already processed.");
        }
    }

    public List<String[]> history() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT id,hospital_name,patient_name,blood_group,units,priority,status,created_at FROM requests ORDER BY FIELD(priority,'EMERGENCY','URGENT','NORMAL'),created_at DESC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
            while (result.next()) rows.add(new String[]{result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8)});
        }
        return rows;
    }

    public List<Request> pendingRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT id,hospital_name,patient_name,blood_group,units,priority FROM requests WHERE status='PENDING' ORDER BY FIELD(priority,'EMERGENCY','URGENT','NORMAL'),created_at";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
            while (result.next()) requests.add(new Request(result.getInt(1),result.getString(2),result.getString(3),BloodGroup.fromLabel(result.getString(4)),result.getInt(5),Request.Priority.valueOf(result.getString(6))));
        }
        return requests;
    }

    public void approveWithStock(int requestId, bloodbank.service.CompatibilityService compatibility) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                BloodGroup requiredGroup;
                int units;
                try (PreparedStatement request = connection.prepareStatement("SELECT blood_group,units FROM requests WHERE id=? AND status='PENDING' FOR UPDATE")) {
                    request.setInt(1, requestId);
                    try (ResultSet result = request.executeQuery()) {
                        if (!result.next()) throw new SQLException("Pending request was not found.");
                        requiredGroup = BloodGroup.fromLabel(result.getString("blood_group"));
                        units = result.getInt("units");
                    }
                }
                List<BloodGroup> compatibleGroups = compatibility.compatibleDonors(requiredGroup);
                String placeholders = String.join(",", java.util.Collections.nCopies(compatibleGroups.size(), "?"));
                String selectSql = "SELECT id FROM stock WHERE blood_group IN (" + placeholders + ") AND status='AVAILABLE' AND expiry_date>=CURRENT_DATE AND collection_date>DATE_SUB(CURRENT_DATE,INTERVAL 42 DAY) ORDER BY FIELD(blood_group," + placeholders + "),expiry_date,id LIMIT ? FOR UPDATE";
                List<Long> stockIds = new ArrayList<>();
                try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                    int index = 1;
                    for (BloodGroup group : compatibleGroups) select.setString(index++, group.toString());
                    for (BloodGroup group : compatibleGroups) select.setString(index++, group.toString());
                    select.setInt(index, units);
                    try (ResultSet result = select.executeQuery()) { while (result.next()) stockIds.add(result.getLong("id")); }
                }
                if (stockIds.size() != units) throw new SQLException("Insufficient compatible stock for this request.");
                try (PreparedStatement updateStock = connection.prepareStatement("UPDATE stock SET status='ISSUED' WHERE id=? AND status='AVAILABLE'")) {
                    for (Long stockId : stockIds) { updateStock.setLong(1, stockId); updateStock.addBatch(); }
                    updateStock.executeBatch();
                }
                try (PreparedStatement updateRequest = connection.prepareStatement("UPDATE requests SET status='APPROVED' WHERE id=? AND status='PENDING'")) {
                    updateRequest.setInt(1, requestId);
                    if (updateRequest.executeUpdate() != 1) throw new SQLException("Request was processed by another administrator.");
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
}
