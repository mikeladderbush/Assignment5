package LadderbushSourceRoot;


import model.AddressBookEntry;
import model.DataFormatEnum;
import model.FieldEnum;
import static model.FieldEnum.FIRST_NAME;
import static model.FieldEnum.LAST_NAME;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.AddressBookEntry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import model.serviceError;
import java.util.Arrays;

/**
 * This class is a collection to utility methods that are used to manipulate data.
 */
public class DataUtils {
    
    private List<AddressBookEntry> entryList;

    /* all the methods should be static and to enforce this we make the constructor
     * private so no instances can be created.
     * */
    private DataUtils() {}

    /**
     * Given a string of data in a format specified by the DataFormatEnum, parse the data into key value
     * pairs and return a map.
     *
     * @param recordData a String in a particular format.
     * @param dataFormat the format of the data. e.g. XML
     * @return A map the data.
     * @throws edu.uml.smarks.model.serviceError
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws org.json.simple.parser.ParseException
     * 
     * Using a switch statement the mapMake method executes the correct method based on the data format given.
     */
    public static Map<FieldEnum, String> mapMake(String recordData, DataFormatEnum dataFormat) throws serviceError, ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException{
       
       Map<FieldEnum, String> returnValue;
    
       switch (dataFormat) {
            
            case XML:
                
                returnValue = parseXML(recordData);
                break;
                
            case JSON:
                
                returnValue = parseJSOM(recordData);
                break;
                
            case CSV:
                
                returnValue = parseCSV(recordData);
                break;
        }
       
       throw new serviceError("invalid data format enum");
       
    }

    /**
     * 
     * @param recordData
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     * 
     * ParseXML uses documentbuilder to create a new document which is then used to parse the given
     * record data. The for loop iterates over each element of the record data and then gets its contents,
     * and returns them to the entry.
     */
    private static Map<FieldEnum, String> parseXML(String recordData) throws ParserConfigurationException, SAXException, IOException {
        
        Map<FieldEnum, String> XMLMap = new HashMap<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(recordData);
        document.getDocumentElement().normalize();
        NodeList list = document.getElementsByTagName("Addresses");
            
            for (int temp = 0; temp < list.getLength(); temp++) {
                
                Node node = list.item(temp);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element element = (Element) node;                  
                    String first_name = element.getElementsByTagName("first_name").item(0).getTextContent();
                    String last_name = element.getElementsByTagName("last_name").item(0).getTextContent();
                    XMLMap.put(FIRST_NAME, first_name);
                    XMLMap.put(LAST_NAME, last_name);
                    
                    for (Map.Entry<FieldEnum, String> entry : XMLMap.entrySet()) {
                        
                        System.out.println(entry.getKey() + ":" + entry.getValue());
                        
                    }
                }
            }
            
        return XMLMap;
            
  }   

    private static Map<FieldEnum, String> parseJSOM(String recordData) throws FileNotFoundException, ParseException {
        
        JSONParser parser = new JSONParser();
        
        try (FileReader reader = new FileReader("address.json")){
            
            Object obj = parser.parse(reader);
            
            JSONArray addressList = (JSONArray) obj;
            System.out.println(addressList);
            
            addressList.forEach( emp -> parseAddress((JSONObject) emp));
            
        } catch (IOException ex) {
            
            Logger.getLogger(DataUtils.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return null;
    }
    
    private static void parseAddress(JSONObject address){
        
        JSONObject addressObject = (JSONObject) address.get("Addresses");
        
    }

    private static Map<FieldEnum, String> parseCSV(String recordData) throws IOException {
        
        String fileName = "c:\\test\\csv\\country.csv";
        
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
            
        } catch (CsvException ex) {
            
            Logger.getLogger(DataUtils.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return null;
        
    }

}
