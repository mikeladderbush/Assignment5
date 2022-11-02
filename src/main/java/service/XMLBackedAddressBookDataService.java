package service;

import com.thoughtworks.xstream.XStream;
import model.AddressBookEntry;
import model.FieldEnum;
import model.MatchCriteriaEnum;
import model.serviceError;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;

public class XMLBackedAddressBookDataService implements AddressBookDataService {

    public XMLBackedAddressBookDataService(File file){
           

    }
    

    /**
     * Add or update AddressBookEntry
     *
     * @param entry the record to update
     * @param outFile
     * @throws edu.uml.smarks.model.serviceError
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws throws data service exception (why thrown),(how to fix)
     */
    @Override
    public void writeEntry(AddressBookEntry entry, File outFile) throws serviceError, IOException, ParserConfigurationException {
        
        Path path = outFile.toPath();
        
        Map<FieldEnum, String> xmlEntry = entry.getRecordData();
        XStream stream = new XStream();
        stream.registerConverter(new MapEntryConverter());
        stream.alias("record", Map.class);
        String xml = stream.toXML(xmlEntry);
        Files.write(path, xml.getBytes(), StandardOpenOption.APPEND);
   
    }
    
    @Override
    public void clearEntries(File outFile) throws IOException {
        
        Path path = Paths.get("C:\\Users\\Michael Ladderbush\\Documents\\GitHub\\advanced_java_assignment4_starter\\src\\main\\resources\\address.xml");
        Files.delete(path);
        
    }

    /**
     * Find all records that match the provided search parameters.
     *
     * @param matchCriteria
     * @return 0 or more AddressBookEntry instances that match the provided search parameters
     */
    private final List<AddressBookEntry> addressBookEntries = new ArrayList<>();
    
    public List<AddressBookEntry> getAddressBookEntries() {
        
        return addressBookEntries;
        
    }
    
    //List<AddressBookEntry> data = getAddressBookEntries()
    //List<AddressBookEntry> returnValue = new ArrayList<AddressBookEntry>()
    //For (AddressBookEntry addressBookEntry: data)
	//for (MatchCriteria matchCriteria: matchCriterias)
    //	boolean found = searchValue(addressBookEntry, matchCritia)
	//	if found
	//		returnValue.append(addressBookEntry)

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
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        return listOfMatches;
        
    }   
    
}
