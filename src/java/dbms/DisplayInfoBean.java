/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dbms;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author cut14jhu
 */
public class DisplayInfoBean {
    
    private String objectType;
    private ArrayList<String> listToDisplay;
    private JSONObject previousInfo;
    
    private int activeYear;
    
    private ArrayList<String> associateList;

    private ArrayList<Integer> yearList;
    private int newestYear;
    
    private ArrayList<Boolean> booleanList;
    
    private String errorMessage;
    
    public DisplayInfoBean(String externString, ArrayList<String> externList)
    {
        objectType = externString;
        listToDisplay = externList;
    }
    
    public DisplayInfoBean(String externString, ArrayList<String> externList, ArrayList<String> externList2)
    {
        objectType = externString;
        listToDisplay = externList;
        associateList = externList2;
    }
    
    public DisplayInfoBean(String externString, ArrayList<String> externList, JSONObject externInfoJSON)
    {
        objectType = externString;
        listToDisplay = externList;
        previousInfo = externInfoJSON;
    }
    
    public DisplayInfoBean(String externString, JSONObject externInfoJSON)
    {
        objectType = externString;

        previousInfo = externInfoJSON;
    }
    
     public DisplayInfoBean(ArrayList<String> externList, int externActiveYear)
    {
       
        listToDisplay = externList;
        activeYear = externActiveYear;

    }
     
     // for startnewyearservlet
    public DisplayInfoBean(ArrayList<Integer> externYearList, int externActiveYear, int externNewestYear)
    {
        activeYear = externActiveYear;
        newestYear = externNewestYear;

        yearList = externYearList;
    }

     // for newyearservlet
    public DisplayInfoBean(ArrayList<Integer> externYearList, int externActiveYear, int externNewestYear, String externErrorMessage)
    {
        //just checking
        errorMessage = null;
        
        activeYear = externActiveYear;
        newestYear = externNewestYear;

        yearList = externYearList;
        errorMessage = externErrorMessage;
        
    }

    // for newyear forms from pick term dates onwards
    public DisplayInfoBean(JSONObject externPreviousInfo)
    {
        previousInfo = externPreviousInfo;
    }
    
    // for newyear forms from pick term dates onwards WITH ERROR MESSAGE
    public DisplayInfoBean(JSONObject externPreviousInfo, String externErrorMessage)
    {
        previousInfo = externPreviousInfo;

        errorMessage = externErrorMessage;
    }
    
    public DisplayInfoBean(String externObjectType, int externSelectedDBYear, ArrayList<String> externListToDisplay)
    {
        objectType = externObjectType;
        activeYear = externSelectedDBYear;
        listToDisplay = externListToDisplay;
    }
    
    public DisplayInfoBean(
            String externObjectType, int externSelectedDBYear, 
            ArrayList<String> externListToDisplay, ArrayList<String> externAssociateList)
    {
        objectType = externObjectType;
        activeYear = externSelectedDBYear;
        listToDisplay = externListToDisplay;
        associateList = externAssociateList;
    }
    
    // for entitiesfor newyearservlet 
    public DisplayInfoBean(JSONObject externPreviousInfo, ArrayList<String> externListToDisplay)
    {
        previousInfo = externPreviousInfo;

        listToDisplay = externListToDisplay;
    }
    
    public DisplayInfoBean(ArrayList<String> externList, ArrayList<Boolean> externBooleanList, JSONObject externJSON)
    {
       
        listToDisplay = externList;
        booleanList = externBooleanList;
        previousInfo = externJSON;
    }

    public ArrayList<String> getAssociateList() {
        return associateList;
    }

    public ArrayList<Boolean> getBooleanList() {
        return booleanList;
    }
    
    
    public String getErrorMessage() {
        return errorMessage;
    }
        
    public int getNewestYear() {
        return newestYear;
    }

    public ArrayList<Integer> getYearList() {
        return yearList;
    }
        
    public String getObjectType() {
        return objectType;
    }

    public ArrayList<String> getListToDisplay() {
        return listToDisplay;
    }

    public JSONObject getPreviousInfo() {
        return previousInfo;
    }

    public int getActiveYear() {
        return activeYear;
    }
   
}
