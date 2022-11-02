package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import model.AddressBookEntry;
import model.FieldEnum;
import model.MatchCriteriaEnum;
import model.serviceError;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JSONBackedAddressBookDataService  implements AddressBookDataService{

    public JSONBackedAddressBookDataService(File file) throws IOException, ParseException {
        
    }

    /**
     * Add or update AddressBookEntry
     *
     * @param entry the record to update
     * @throws java.io.IOException
     */
    @Override
    public void writeEntry(AddressBookEntry entry, File outFile) throws serviceError, IOException {
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outFile, entry);

    }

    @Override
    public void clearEntries(File outFile) throws IOException {

        Path path = outFile.toPath();
        Files.delete(path);

    }
    
    /**
     * Find all records that match the provided search parameters.
     *
     * @param matchCriteria the list of Match MatchCriteria
     * @return 0 or more AddressBookEntry instances that match the provided search parameters
     */
    @Override
    public List<AddressBookEntry> findEntries(List<AddressBookEntry> entries, List<MatchCriteria> matchCriteria) {
        
        List<AddressBookEntry> listOfMatches = new ArrayList<>();
        
        for(MatchCriteria criteria : matchCriteria){
            
            for(MatchCriteriaEnum howToMatch : criteria.getHowToMatch()){

                for(AddressBookEntry searchEntry : entries){
                
                    for(FieldEnum fieldsToCheck: criteria.getWhereToLook()){
                        
                        if(AddressBookDataService.searchValue(howToMatch, searchEntry, fieldsToCheck, criteria.getValueToMatch())){
                            
                            if(!listOfMatches.contains(searchEntry)){
                                
                                listOfMatches.add(searchEntry);
                                
                                for (int i = 0; i < listOfMatches.size(); i++) {
                                    
                                    System.out.println(listOfMatches.get(i).getRecordData());
                 
                                }
                                                                
                            }
                        }
                    }
                }
            }
        }
        return listOfMatches;
    }  
}
