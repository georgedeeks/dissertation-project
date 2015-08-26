/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;


import database.DatabaseConnectionSingleton;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.json.*;
//import usefulfortesting.CalculateCurrentTimeForTesting; //obviously comment out when done

/**
 *
 * @author cut14jhu
 */
public class JsonUnparser {
        
    private static JSONObject jsonToReturnToApp;
    
    public static JSONObject unparseandReturnJson(JSONObject receivedJSONObj) 
            throws JSONException, ServletException, SQLException{
  
        jsonToReturnToApp = new JSONObject();
        
        String instructionTag = receivedJSONObj.getString("instruction_tag");

        if (null != instructionTag)                
        switch (instructionTag) 
        {
            
            case "check_updates":
                
                System.out.println("The instruction tag is to check for updates");
                
                String timestampResult = receivedJSONObj.getString("timestamp");
                
                jsonToReturnToApp = MustCheckForUpdates.mustCheckForUpdates(timestampResult);
                    
                break;
                
            case "launching":
                
                System.out.println("instruction tag is a new db");
                
                jsonToReturnToApp = MustCheckForUpdates.putCurrentDBInfoOn(jsonToReturnToApp);
                                
                break;
                
            case "upload_record":
                
                System.out.println("The instruction tag is to upload a record");
                
                //jsonObj.put("testing_log_upload_case", "instruction tag is to upload a record");/////////////////////
            
                jsonToReturnToApp = UploadRecord.uploadRecord(receivedJSONObj);
                
                break;
                
            case "insert_URL":
                
                System.out.println("The instruction tag is to insert a successful URL");
                
                jsonToReturnToApp = InsertURL.insertURL(receivedJSONObj);
                
                break;
            
            case "request_record":
                
                System.out.println("The instruction tag is to request a record");
                
                jsonToReturnToApp = RequestRecord.requestRecord(receivedJSONObj);
                
                break;
                
            default:
                
                //THERE HAS BEEN AN ERROR
                System.out.println("Error unparsing JSON object! Cat aids");
                
                //jsonToReturnToApp = new JSONObject();
                
                jsonToReturnToApp.put("response_tag", "insufficient_info_provided");
                
                break;
                
        }
             
        DatabaseConnectionSingleton.closeConnection();
                
        // final result to return
        return jsonToReturnToApp;
        
    }
    
   
    
    
    // testing
        // has a JSONObject (which it will need to receive somehow), 
        // then goes through processing to return a response JSONObject (which it needs to then send)
    public static void main(String[] args) throws JSONException, ServletException, SQLException {
       
        // TESTING 
        
        /*
        
        String[] checkInputData = {"externChildName", "externSPT", "externYear", "externTerm"};
        
        for (int i = 0; i < 4; i++)
                {
                    //to do
                    System.out.println(checkInputData[i]);
                }
        
        */
        
        // testing that this works for sourcetree/github
        
        // check for updates
        
        /*
        // tested for intiial database 'launching'
        
        JSONObject objLaunching = new JSONObject();
        
        objLaunching.put("instruction_tag", "launching");
        
        
        
    // tested for no updates needed only
        JSONObject objCheckUpdates = new JSONObject();      
        objCheckUpdates.put("instruction_tag", "check_updates"); 
                     
    //    objCheckUpdates.put("timestamp", 
      //          CalculateCurrentTimeForTesting.getCurrentSpecialFormattedTime()); // obviously for back-end testing only
        
        
    
    // tested for updates needed       
        objCheckUpdates.put("timestamp", 
                "2015-07-01 18:48:48");                
                // temp
                //CalculateCurrentTimeForTesting.getOldSpecialFormattedTime());
        
        
    // tested for successful insert
    // tested for unsuccessful insert
        JSONObject objUploadRecord = new JSONObject();      
        objUploadRecord.put("instruction_tag", "upload_record");
        objUploadRecord.put("file_name", "U/2013/1/34/1/3/3945-MSC000040294.mpeg");
        objUploadRecord.put("student_name", "Donatello");
        objUploadRecord.put("spt", "Maths");
        objUploadRecord.put("spt_level", "Initial");
        objUploadRecord.put("observations", 
                "This radical turtle got through Java to be here!");
        objUploadRecord.put("date_stamp", "19-06-2015");
        
    // tested for successful 'any' record
    // tested for unsuccessful record (just gives blank records)
        // TODO
            // figure out how an 'unsuccessful' record can be generated
            // (maybe attach 'unssuccessful' to JSON if catch block is reached)
       
               */ 
                
        JSONObject objRequestRecord = new JSONObject();      
        objRequestRecord.put("instruction_tag", "request_record");
        objRequestRecord.put("student_name", "Any");
        objRequestRecord.put("academic_year", "Any");
        objRequestRecord.put("term", "Any");
        objRequestRecord.put("spt", "Any");      
               
       // System.out.println("This is the JSON to unparse: " + objUploadRecord);    
        
      //  JSONObject returnToDoaaTemp = unparseandReturnJson(objUploadRecord);
    
      //  System.out.println("returning this JSON to Doaa: " + returnToDoaaTemp);
        
        
      
        
        /*
        
      
        
            //TEMPFORTEST
                JSONObject realRequestMessage = new JSONObject();      
        realRequestMessage.put("instruction_tag", "upload_record");
        realRequestMessage.put("file_name", "U/2013/1/34/1/3/3945-MSC000040294.mpeg");
        realRequestMessage.put("student_name", "Donatello");
        realRequestMessage.put("spt", "Maths");
        realRequestMessage.put("spt_level", "Initial");
        realRequestMessage.put("observations", 
                "This radical turtle got through Java to be here!");
        realRequestMessage.put("date_stamp", "19-06-2015");
            /////////////////////////////
         
        
                System.out.println("request message to be processed: \n" + objRequestRecord);
                //System.out.println("Doa'ss message = \n" + "{'instruction_tag':'check_updates','timestamp':'2015-07-01 18:48:48'}");
              */
        // send off JSONObject to be interpreted

            JSONObject realResponseMessage = JsonUnparser.unparseandReturnJson(objRequestRecord);
          
                System.out.println("response message: \n" + realResponseMessage);

        // set attributes for next page
                
               // String realResponseMessageString = realResponseMessage.toString();
                
              //  System.out.println("realresponsemessagestring: \n" + realResponseMessageString);
        
    }
    
}
