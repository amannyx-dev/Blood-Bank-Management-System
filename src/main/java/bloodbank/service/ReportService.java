package bloodbank.service;

import bloodbank.core.BloodGroup;
import bloodbank.db.StockDAO;
import java.sql.SQLException;
import java.util.*;

public class ReportService {
    private final StockDAO dao=new StockDAO();
    public Map<BloodGroup,Integer> lowStock(int threshold)throws SQLException{Map<BloodGroup,Integer> low=new EnumMap<>(BloodGroup.class);dao.getCounts().forEach((group,count)->{if(count<threshold)low.put(group,count);});return low;}
}
