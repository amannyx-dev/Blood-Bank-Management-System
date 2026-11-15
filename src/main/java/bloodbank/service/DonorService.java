package bloodbank.service;

import bloodbank.core.Donor;
import bloodbank.db.DonorDAO;
import bloodbank.exception.InvalidDonorException;
import bloodbank.util.Validator;
import java.sql.SQLException;
import java.util.List;

public class DonorService {
    private final DonorDAO dao=new DonorDAO();
    public int register(Donor donor)throws SQLException,InvalidDonorException{Validator.validateDonor(donor);return dao.save(donor);}
    public void update(Donor donor)throws SQLException,InvalidDonorException{Validator.validateDonor(donor);dao.update(donor);}
    public void delete(int id)throws SQLException{dao.delete(id);}
    public List<Donor> search(String term)throws SQLException{return dao.search(term);}
}
