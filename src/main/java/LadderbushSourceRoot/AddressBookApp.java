package LadderbushSourceRoot;

import LadderbushSourceRoot.storage.StorageProperties;
import LadderbushSourceRoot.storage.StorageService;
import jakarta.transaction.Transactional;
import model.Address;
import model.AddressBookEntry;
import model.FieldEnum;
import model.MatchCriteriaEnum;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import service.AddressBookDataService;
import service.AddressBookDataServiceFactory;
import service.MatchCriteria;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.FieldEnum.*;
import static model.MatchCriteriaEnum.EXACT;

/**
 * A simple address book application
 */

@SpringBootApplication
@Transactional
@EnableConfigurationProperties(StorageProperties.class)
public class AddressBookApp {

    @Bean
    CommandLineRunner commandLineRunner(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    public static void main(String[] args) throws Exception, HibernateException {

        SpringApplication.run(AddressBookApp.class, args);

        // This is to assign the given argument as the file to be written to and read.
        // The created file object is then converted to a string and its path object is
        // taken using the path.of method.

        String fileName = ("C:\\Users\\Michael Ladderbush\\buttonboy123\\Advanced Java 2022\\Assignment 5\\Assignment 5\\src\\main\\resources\\address.xml");
        File file = new File(fileName);
        String filePath = file.toString();
        Path path = Paths.get(filePath);
        
        //An addressbookdataservice object is created by calling the getinstance method on the AddressBookDataServiceFactory,
        // with the file object from line 45 used as an argument.

        AddressBookDataService addressBookDataService = AddressBookDataServiceFactory.getInstance(file);
        
        //A new hashmap is created here with the specific identifiers "FieldEnum" and "String"
        // the first of these is a ENUM from another class. In the case of our first recordData
        // object we put the three key value pairs into the hashmap.

        Map<FieldEnum, String> recordData = new HashMap<>();
        recordData.put(FIRST_NAME, "Mike");
        recordData.put(CITY, "Boston");
        recordData.put(LAST_NAME, "Ladderbush");
        recordData.put(FieldEnum.STREET_ADDRESS, "384 Washington Street");
        recordData.put(FieldEnum.ZIP, "02135");
        recordData.put(FieldEnum.STATE, "Massachusetts");
        recordData.put(FieldEnum.PHONE, "9787708430");
        
        //An addressBookEntry object is created by passing our recordData object to a new entry.
        // Once this object "entry" is created we can use our writeEntry method on our previously
        // created addressBookDataService. Passing the entry and file to this service will write
        // the recordData to the file. The entry is then added to a list.

        AddressBookEntry entry = new AddressBookEntry(recordData);
        addressBookDataService.writeEntry(entry, file);
        List<AddressBookEntry> entryList = new ArrayList<>();
        entryList.add(entry);
        
        //We create new instances of the three variables below so that we can add them to the
        // matchCriteria variable. Because these variables are in lists we can have multiple criteria
        // in one search. The criteria are then used with the findEntries method to return any entries that match.

        List<MatchCriteriaEnum> howToMatch = new ArrayList<>();
        List<FieldEnum> whereToLook = new ArrayList<>();
        String valueToMatch = "Mike";
        howToMatch.add(EXACT);
        whereToLook.add(FIRST_NAME);
        MatchCriteria matchCriteria = new MatchCriteria(valueToMatch, howToMatch, whereToLook);
        List<MatchCriteria> matchCriteriaList = new ArrayList<>();
        matchCriteriaList.add(matchCriteria);
       
        List<AddressBookEntry> foundEntries = addressBookDataService.findEntries(entryList, matchCriteriaList);
        
        for (AddressBookEntry i : foundEntries) {
 
            // Print all elements of ArrayList

            Map<FieldEnum, String>iteratedRecordData = i.getRecordData();
            String finalData = iteratedRecordData.toString();
            System.out.println(finalData);
            
        }

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(System.getenv("url"), System.getenv("username"), System.getenv("password"))) {

            System.out.println("Database connected!");

        } catch (SQLException e) {

            throw new IllegalStateException("Cannot connect the database!", e);

        }

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Address addressToDB = new Address(123123L, "Mike", "Ladderbush", " 384 Washington Street", "Brighton", "Massachusetts", "02135", "9787708430");

        session.persist(addressToDB);
        session.getTransaction().commit();
        session.close();

    }

}

