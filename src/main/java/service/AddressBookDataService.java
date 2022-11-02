package service;


import model.AddressBookEntry;
import model.FieldEnum;
import model.MatchCriteriaEnum;
import model.serviceError;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This interface describes a simple API for handling Address Book Data
 */
public interface AddressBookDataService {

    /**
     * Add or update AddressBookEntry
     *
     * @param entry the record to update
     * @param outFile
     * @throws edu.uml.smarks.model.serviceError
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    void writeEntry(AddressBookEntry entry, File outFile) throws serviceError, IOException, ParserConfigurationException;
    void clearEntries(File outFile) throws IOException; 
    /**
     * Find all records that match the provided search parameters.
     *
     * @param entries
     * @param matchCriterias the list of Match MatchCriteria
     * @return 0 or more AddressBookEntry instances that match the provided search parameters
     */
    
    
    List<AddressBookEntry> findEntries(List<AddressBookEntry> entries, List<MatchCriteria>  matchCriterias);
    
    static boolean searchValue(MatchCriteriaEnum howToMatch, AddressBookEntry addressBookEntry, FieldEnum fieldsToCheck, String whatToFind){
        
        switch (howToMatch) {
            case EXACT:
            return addressBookEntry.getRecordData().get(fieldsToCheck).equals(whatToFind);

            case CONTAINS:
            return addressBookEntry.getRecordData().get(fieldsToCheck).contains(whatToFind);

            case ENDS_WITH:
            return addressBookEntry.getRecordData().get(fieldsToCheck).endsWith(whatToFind);

            default:
            return addressBookEntry.getRecordData().get(fieldsToCheck).startsWith(whatToFind);

        }
    }
}
