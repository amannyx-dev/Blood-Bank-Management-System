package bloodbank.service;

import bloodbank.core.BloodGroup;
import bloodbank.core.Request;
import bloodbank.db.RequestDAO;
import bloodbank.exception.BloodNotAvailableException;
import bloodbank.exception.InvalidRequestException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class RequestService {
    private final RequestDAO dao = new RequestDAO();
    private final CompatibilityService compatibility = new CompatibilityService();

    public int create(Request request) throws SQLException, InvalidRequestException {
        if (request == null || request.getHospitalName() == null || request.getHospitalName().isBlank() || request.getPatientName() == null || request.getPatientName().isBlank() || request.getRequiredGroup() == null || request.getPriority() == null || request.getUnits() < 1 || request.getUnits() > 20) throw new InvalidRequestException("Enter hospital, patient, blood group, priority and 1 to 20 units.");
        return dao.save(request);
    }

    public void approve(int id) throws SQLException, BloodNotAvailableException {
        if (id < 1) throw new BloodNotAvailableException("Select a valid pending request.");
        try {
            dao.approveWithStock(id, compatibility);
        } catch (SQLException exception) {
            if (exception.getMessage() != null && (exception.getMessage().toLowerCase().contains("stock") || exception.getMessage().toLowerCase().contains("compatible"))) throw new BloodNotAvailableException(exception.getMessage());
            throw exception;
        }
    }

    public void reject(int id) throws SQLException { dao.updateStatus(id, "REJECTED"); }
    public List<String[]> history() throws SQLException { return dao.history(); }

    public Request nextPriorityRequest() throws SQLException {
        PriorityQueue<Request> queue = new PriorityQueue<>();
        queue.addAll(dao.pendingRequests());
        return queue.poll();
    }

    public List<Request> pendingByPriority() throws SQLException {
        PriorityQueue<Request> queue = new PriorityQueue<>();
        queue.addAll(dao.pendingRequests());
        List<Request> ordered = new ArrayList<>();
        while (!queue.isEmpty()) ordered.add(queue.poll());
        return ordered;
    }
}
