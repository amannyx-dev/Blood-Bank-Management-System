package bloodbank.service;

import bloodbank.db.HistoryDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class HistoryService {
    private final HistoryDAO dao = new HistoryDAO();
    public List<String[]> search(String filter, LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) throw new IllegalArgumentException("Start date cannot be after end date.");
        return dao.donations(filter, fromDate, toDate);
    }
}
