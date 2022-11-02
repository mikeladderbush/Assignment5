/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import com.opencsv.CSVReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Ladderbush
 */
public class CSVreader {
    
    static public List<String[]> readAllLines(Path filePath) throws Exception {
        
        try (Reader reader = Files.newBufferedReader(filePath)) {
            
            try (CSVReader csvReader = new CSVReader(reader)) {
                
                return csvReader.readAll();
                
            }
        }
    }
    
    public List<String[]> readAll(Path path) throws Exception {
              
        return CSVreader.readAllLines(path);
    
    }
    
    static public List<String[]> readLineByLine(Path filePath) throws Exception {
        
        List<String[]> list = new ArrayList<>();
        
        try (Reader reader = Files.newBufferedReader(filePath)) {
            
            try (CSVReader csvReader = new CSVReader(reader)) {
                
                String[] line;
                
                while ((line = csvReader.readNext()) != null) {
                    
                    list.add(line);
                    
                }
            }
        }
        return list;
    }
    
    public List<String[]> readLineByLineExample() throws Exception {
        
        Path path = Paths.get(ClassLoader.getSystemResource("csv/twoColumn.csv").toURI());
        
        return CSVreader.readLineByLine(path);
        
    }

}

