package service;

import model.serviceError;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

/**
 * This factory provides a AddressBookDataService  implementation.
 */
public class AddressBookDataServiceFactory {

    /**
     * @param file
     * @return AddressBookDataService implementation
     * @throws edu.uml.smarks.model.serviceError
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     */
    public static AddressBookDataService getInstance(File file) throws Exception {
        
        AddressBookDataService service;
        String fileName = file.toString();
        
        String extension = FilenameUtils.getExtension(fileName);
        
        switch (extension) {
            case "xml":
                service = new XMLBackedAddressBookDataService(file);
                break;
            case "csv":
                service = new CSVBackedAddressBookDataService(file);
                break;
            case "json":
                service = new JSONBackedAddressBookDataService(file);
                break;
            default:
                throw new serviceError("This extension is invalid");
        }
        //add else for invalid argument exception***
        return service;
    }
}


