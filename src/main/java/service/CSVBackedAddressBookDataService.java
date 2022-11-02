package service;

import com.opencsv.CSVWriter;
import model.AddressBookEntry;
import model.FieldEnum;
import static model.FieldEnum.CITY;
import static model.FieldEnum.FIRST_NAME;
import static model.FieldEnum.LAST_NAME;
import model.MatchCriteriaEnum;
import model.serviceError;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class CSVBackedAddressBookDataService implements AddressBookDataService {

    public CSVBackedAddressBookDataService(File file) throws Exception {

        // read the CSV data  - this should be a list of addaresses
        String fileString = file.toString();
        Path path = Paths.get(fileString);
        CSVreader reader = new CSVreader();
        List<String[]> allLines = reader.readAll(path);

    }

    /**
     * Add or update AddressBookEntry
     *
     * @param entry the record to update
     * @throws java.io.IOException
     */
    @Override
    public void writeEntry(AddressBookEntry entry, File outFile) throws serviceError, IOException {

        FileWriter writer = new FileWriter(outFile);
        Map<FieldEnum, String> csvEntry = entry.getRecordData();
        String csvString = csvEntry.toString();
       
        CSVWriter csvWriter = new CSVWriter(writer);
  
        // adding header to csv
        String[] header = { "FIRST_NAME", "LAST_NAME", "CITY" };
        csvWriter.writeNext(header);
  
        Map<FieldEnum, String> entryMap = entry.getRecordData();
        String firstName = entryMap.get(FIRST_NAME);
        String lastName = entryMap.get(LAST_NAME);
        String city = entryMap.get(CITY);
        
        // add data to csv
        String[] data1 = { firstName, lastName, city};
        csvWriter.writeNext(data1);
        
        // closing writer connection
        writer.close();
        
    }
    
    @Override
    public void clearEntries(File outFile) throws IOException {
        
        Path path = Paths.get("C:\\Users\\Michael Ladderbush\\Documents\\GitHub\\advanced_java_assignment4_starter\\src\\main\\resources\\address.xml");
        Files.delete(path);
        
    }

    /**
     * Find all records that match the provided search parameters.
     *
     * @param matchCriterias the list of Match MatchCriteria
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
