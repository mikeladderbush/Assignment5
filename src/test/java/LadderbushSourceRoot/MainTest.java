package LadderbushSourceRoot;

import LadderbushSourceRoot.storage.StorageFileNotFoundException;
import LadderbushSourceRoot.storage.StorageService;
import model.AddressBookEntry;
import model.FieldEnum;
import model.MatchCriteriaEnum;
import model.serviceError;
import service.AddressBookDataService;
import service.AddressBookDataServiceFactory;
import service.MatchCriteria;
import org.apache.commons.io.FilenameUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static model.FieldEnum.FIRST_NAME;
import static model.MatchCriteriaEnum.EXACT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MainTest {
    
    File file = new File("C:\\Users\\Michael Ladderbush\\Documents\\GitHub\\advanced_java_assignment4_starter\\src\\main\\resources\\address.xml");
    String fileName = file.toString();
    String extension = FilenameUtils.getExtension(fileName);
    List<MatchCriteriaEnum> howToMatch = new ArrayList<>();
    List<FieldEnum> whereToLook = new ArrayList<>();
    String valueToMatch = "Mike";
    Map<FieldEnum, String> recordData = new HashMap<>(); 
    
    AddressBookEntry entry = new AddressBookEntry(recordData); 
    
    MatchCriteria matchCriteria = new MatchCriteria(valueToMatch, howToMatch, whereToLook);
    List<MatchCriteria> matchCriteriaList = new ArrayList<>();
               
    @Test
    public void testCorrectServiceCreation() throws Exception {
        
        switch (extension) {
            
            case "xml":
                
                System.out.println("This is an XML file");
                break;
                
            case "csv":
                
                System.out.println("This is a CSV file");
                break;
                
            case "json":
                
                System.out.println("This is a JSON file");
                break;
                
            default:
                
                throw new serviceError("This extension is invalid");
                
        }
    }

    
    @Test
    public void serviceCreationTester() throws Exception {
        
        AddressBookDataService service = AddressBookDataServiceFactory.getInstance(file);
        Class serviceClass = service.getClass();
        String serviceString = serviceClass.toString();
        String XMLClass = ("class service.XMLBackedAddressBookDataService");
        assertEquals(serviceString, XMLClass);
        
    }
    
    @Test
    public void testRecordData() throws Exception {
         
        recordData.put(FieldEnum.FIRST_NAME, "Mike");
        recordData.put(FieldEnum.CITY, "Boston");
        recordData.put(FieldEnum.LAST_NAME, "Ladderbush");
        
        Map<FieldEnum, String> testData = entry.getRecordData();

        assertEquals(testData, recordData);
        
    }
    
    @Test
    public void testMatchValuesLists() {
        
        howToMatch.add(EXACT);
        whereToLook.add(FIRST_NAME);
        matchCriteriaList.add(matchCriteria);
        
        assertTrue(matchCriteriaList.contains(matchCriteria));
        assertEquals((matchCriteria.getHowToMatch()), howToMatch);
        assertEquals((matchCriteria.getWhereToLook()), whereToLook);
        assertEquals((matchCriteria.getValueToMatch()), valueToMatch);
        
    }
    
    @Test
    public void testWriteEntry() throws Exception {
        
        File newFile = new File("C:\\Users\\Michael Ladderbush\\Documents\\GitHub\\advanced_java_assignment4_starter\\src\\main\\resources\\addressTest.xml");
        newFile.delete();
        newFile.createNewFile();
        String fileString = newFile.toString();
        Path filePath = Paths.get(fileString);

        AddressBookDataService service = AddressBookDataServiceFactory.getInstance(newFile);
        Map<FieldEnum, String> recordDataTest = new HashMap<>();
        
        recordDataTest.put(FieldEnum.FIRST_NAME, "Mike");
        recordDataTest.put(FieldEnum.CITY, "Boston");
        recordDataTest.put(FieldEnum.LAST_NAME, "Ladderbush");
        
        AddressBookEntry writeTest = new AddressBookEntry(recordDataTest);
        service.writeEntry(writeTest, newFile);
        
        File dummyFile = new File("C:\\Users\\Michael Ladderbush\\Documents\\GitHub\\advanced_java_assignment4_starter\\src\\main\\resources\\addressTestTwo.xml");
        String testFileString = dummyFile.toString();
        Path testFilePath = Paths.get(testFileString);
        
        long mismatchValue = Files.mismatch(filePath, testFilePath);
        
        if (mismatchValue == 8){
            
            System.out.println("No mismatch found in the files");
            
        }else
            
            System.out.println("mismatch found");
        
        }
    
    @Test
    public void testFindEntries() throws Exception {
        
        howToMatch.add(EXACT);
        whereToLook.add(FIRST_NAME);
        matchCriteriaList.add(matchCriteria);
        
        recordData.put(FieldEnum.FIRST_NAME, "Mike");
        recordData.put(FieldEnum.CITY, "Boston");
        recordData.put(FieldEnum.LAST_NAME, "Ladderbush");
               
        List<AddressBookEntry> entryList = new ArrayList<>();
        entryList.add(entry);
        
        AddressBookDataService addressBookDataService = AddressBookDataServiceFactory.getInstance(file);
        addressBookDataService.findEntries(entryList, matchCriteriaList);
        
    }

    @AutoConfigureMockMvc
    @SpringBootTest
    public class FileUploadTests {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private StorageService storageService;

        @Test
        public void shouldListAllFiles() throws Exception {
            given(this.storageService.loadAll())
                    .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

            this.mvc.perform(get("/")).andExpect(status().isOk())
                    .andExpect(model().attribute("files",
                            Matchers.contains("http://localhost/files/first.txt",
                                    "http://localhost/files/second.txt")));
        }

        @Test
        public void shouldSaveUploadedFile() throws Exception {
            MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                    "text/plain", "Spring Framework".getBytes());
            this.mvc.perform(multipart("/").file(multipartFile))
                    .andExpect(status().isFound())
                    .andExpect(header().string("Location", "/"));

            then(this.storageService).should().store(multipartFile);
        }

        @SuppressWarnings("unchecked")
        @Test
        public void should404WhenMissingFile() throws Exception {
            given(this.storageService.loadAsResource("test.txt"))
                    .willThrow(StorageFileNotFoundException.class);

            this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
        }

    }
    
}
    


    
