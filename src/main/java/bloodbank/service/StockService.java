package bloodbank.service;

import bloodbank.core.BloodGroup;
import bloodbank.db.StockDAO;
import java.sql.SQLException;
import java.util.Map;

public class StockService {
    private final StockDAO dao = new StockDAO();
    public synchronized void addDonation(BloodGroup group, int units) throws SQLException {
        if (group == null || units < 1 || units > 100) throw new IllegalArgumentException("Select a blood group and enter 1 to 100 units.");
        dao.addUnits(group, units);
    }
    public synchronized void recordDonation(int donorId, BloodGroup group, int units) throws SQLException { dao.recordDonation(donorId, group, units); }
    public Map<BloodGroup,Integer> getStock() throws SQLException { return dao.getCounts(); }
    public synchronized void removeExpired() throws SQLException { dao.removeExpired(); }
}
