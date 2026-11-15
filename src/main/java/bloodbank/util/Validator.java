package bloodbank.util;

import bloodbank.core.Donor;
import bloodbank.exception.InvalidDonorException;

public final class Validator {
    private Validator() {}
    public static void validateDonor(Donor donor) throws InvalidDonorException {
        if(donor.getName()==null||donor.getName().trim().length()<2)throw new InvalidDonorException("Enter a valid full name");
        for(char character:donor.getName().toCharArray())if(!Character.isLetter(character)&&character!=' '&&character!='.'&&character!='-')throw new InvalidDonorException("Name can contain letters, spaces, dots and hyphens only");
        String phone=donor.getPhone()==null?"":donor.getPhone().trim();
        if(phone.length()!=10)throw new InvalidDonorException("Phone number must contain 10 digits");
        for(char digit:phone.toCharArray())if(!Character.isDigit(digit))throw new InvalidDonorException("Phone number must contain digits only");
        if(!donor.isEligible())throw new InvalidDonorException(donor.eligibilityMessage());
    }
    public static boolean isValidPhone(String phone){return phone!=null&&phone.matches("[0-9]{10}");}
}
