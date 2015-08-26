/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms.records;

import java.util.ArrayList;

/**
 *
 * @author George
 */
public class RecordsToDisplay {
    
    private ArrayList<Integer> mediaIdArray;
    private ArrayList<String> fileNameArray;
    private ArrayList<String> levelArray;
    private ArrayList<String> observationArray;
    private ArrayList<String> mediaDateArray; 
    
    //for specific records
    private String specificSpt;
    private String specificTerm;
    private String specificStudent; 
    private String specificClass; 
    private int specificYear;
    //private ArrayList<String> URLNameArray //TODO 
    
    //for all records
    private ArrayList<String> specificSptArray;
    private ArrayList<String> selectedTermCapitalisedArray;
    private ArrayList<String> specificStudentArray;
    private ArrayList<String> specificClassArray;
    private ArrayList<Integer> specificYearArray;
    
    
    public RecordsToDisplay
        (
            ArrayList<Integer> externMediaIdArray,
            ArrayList<String> externFileNameArray, 
            ArrayList<String> externLevelArray, 
            ArrayList<String> externObservationArray, 
            ArrayList<String> externMediaDateArray, 
            ArrayList<String> externSpecificSptArray, 
            ArrayList<String> externSelectedTermCapitalisedArray, 
            ArrayList<String> externSpecificStudentArray, 
            ArrayList<String> externSpecificClassArray, 
            ArrayList<Integer> externSpecificYearArray 
        )
    {                   
        mediaIdArray = externMediaIdArray;
        fileNameArray = externFileNameArray;
        levelArray = externLevelArray;
        observationArray = externObservationArray;
        mediaDateArray = externMediaDateArray;
        specificSptArray = externSpecificSptArray;
        selectedTermCapitalisedArray = externSelectedTermCapitalisedArray;
        specificStudentArray = externSpecificStudentArray;
        specificClassArray = externSpecificClassArray;
        specificYearArray = externSpecificYearArray;        
    }
    
    public RecordsToDisplay(ArrayList<Integer> externMediaIdArray, ArrayList<String> externFileNameArray, 
            ArrayList<String> externLevelArray, ArrayList<String> externObservationArray, 
            ArrayList<String> externMediaDateArray, String externSpecificSpt, 
            String externSpecificTerm, String externSpecificStudent, String externSpecificClass, int externSpecificYear){
            // ArrayList<String> URLNameArray, TODO
        
        
        // URLNameArray //TODO   
        
        mediaIdArray = externMediaIdArray;
        fileNameArray = externFileNameArray;
        levelArray = externLevelArray;        
        observationArray = externObservationArray; 
        mediaDateArray = externMediaDateArray;     
        specificSpt = externSpecificSpt;                           
        specificTerm = externSpecificTerm;                            
        specificStudent = externSpecificStudent;                            
        specificClass = externSpecificClass;                  
        specificYear = externSpecificYear;            
                           
    }  
    
    public ArrayList<String> getSpecificSptArray() {
        return specificSptArray;
    }

    public ArrayList<String> getSelectedTermCapitalisedArray() {
        return selectedTermCapitalisedArray;
    }

    public ArrayList<String> getSpecificStudentArray() {
        return specificStudentArray;
    }

    public ArrayList<String> getSpecificClassArray() {
        return specificClassArray;
    }

    public ArrayList<Integer> getSpecificYearArray() {
        return specificYearArray;
    }

    public ArrayList<Integer> getMediaIdArray() {
        return mediaIdArray;
    }

    public ArrayList<String> getFileNameArray() {
        return fileNameArray;
    }

    public ArrayList<String> getLevelArray() {
        return levelArray;
    }

    public ArrayList<String> getObservationArray() {
        return observationArray;
    }

    public ArrayList<String> getMediaDateArray() {
        return mediaDateArray;
    }

    public String getSpecificSpt() {
        return specificSpt;
    }

    public String getSpecificTerm() {
        return specificTerm;
    }

    public String getSpecificStudent() {
        return specificStudent;
    }

    public String getSpecificClass() {
        return specificClass;
    }

    public int getSpecificYear() {
        return specificYear;
    }
    
    
    
}
