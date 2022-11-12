package model;


import LadderbushSourceRoot.DataUtils;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * This class holds a single address book entry e.g. a specific person or business.
 */


public class AddressBookEntry {



        private Map<FieldEnum, String> recordData;


        private final String uniqueId;



    private AddressBookEntry() {
        
        UUID uuid = UUID.randomUUID();
        this.uniqueId = uuid.toString();
        
    }

    /**
     * Creates a new AddressBook record using a map
     *
     * @param recordData the data as a Map
     */
    public AddressBookEntry(Map<FieldEnum, String> recordData) {
        
        this();
        this.recordData = recordData;

    }
    
    /**
     * Creates a new AddressBook record using a JSON String
     *
     * @param recordData as a String. The data format is specified in the dataFormat enum
     * @param dataFormat an enumeration the specifics the format the data is in.
     * @throws serviceError
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws org.json.simple.parser.ParseException
     */
    public AddressBookEntry(String recordData, DataFormatEnum dataFormat) throws serviceError, ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException{
        
        this();
        this.recordData = DataUtils.mapMake(recordData,dataFormat);
        
    }
    
    public Map<FieldEnum, String> getRecordData() {
         
        return recordData;
        
    }

}
