package service;


import model.MatchCriteriaEnum;
import model.FieldEnum;
import java.util.List;


/**
 * Instances of this class contain all the information required to perform
 * a match.  Use instances of this class in  AddressBookDataService requests.
 */
public class MatchCriteria {

    private final String valueToMatch;
    private final List<MatchCriteriaEnum> howToMatch;
    private final List<FieldEnum> whereToLook;
    /**
     * Specify what to search for, how to match, and where to look
     *
     * @param valueToMatch what to look for e.g. "Spencer"
     * @param howToMatch   how to match e.g. EXACT
     * @param whereToLook  which fields to look in e.g FIRST_NAME
     */
    public MatchCriteria(String valueToMatch, List<MatchCriteriaEnum> howToMatch, List<FieldEnum> whereToLook) {
        
        this.valueToMatch = valueToMatch;
        this.howToMatch = howToMatch;
        this.whereToLook = whereToLook;
        
    }
    
    public String getValueToMatch() {
        
        return valueToMatch;
        
    }

    public List<FieldEnum> getWhereToLook() {
        
        return whereToLook;
        
    }

    public List<MatchCriteriaEnum> getHowToMatch() {
        
        return howToMatch;
        
    }
}
