package bloodbank.core;

public interface Screenable {
    boolean isEligible();
    String eligibilityMessage();
}
