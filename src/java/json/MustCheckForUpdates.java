/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import database.DatabaseConnectionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cut14jhu
 */
public class MustCheckForUpdates {
    
    private static JSONObject returnMessageToApp;
    
    public static JSONObject mustCheckForUpdates(String appLastUpdated) throws JSONException, SQLException, ServletException{ //yyyy-MM-dd HH:mm:ss

        //System.out.println("Now executing 'must check for updates module' code...");
        
        //System.out.println("just checking: long to process = " + appLastUpdated);
                
        returnMessageToApp = new JSONObject();
        
        Boolean proceed = checkTimeStampInput(appLastUpdated);
                                
        if (proceed == true)
        {
            
            try 
            {           
            
                String sql = "SELECT date_last_updated > to_timestamp('"
                        + appLastUpdated //i.e. "2014-05-28 15:34:34"
                        + "', 'YYYY-MM-DD hh24:mi:ss')::timestamp without time zone AS shouldAppUpdate "
                        + "FROM last_updated WHERE only_id = 1;";

                Statement statement = DatabaseConnectionSingleton.getStatementInstance();

                ResultSet resultSet = statement.executeQuery(sql);
                
                resultSet.next(); //for next row

                Boolean shouldAppUpdateResult = resultSet.getBoolean("shouldAppUpdate");

                if (shouldAppUpdateResult == false)
                {
                    // app doesn't need updating

                    returnMessageToApp.put("response_tag", "no_update");

                }
                else
                {
                    
                    putCurrentDBInfoOn(returnMessageToApp);

                }

            } 
            catch (SQLException e) 
            {
                
                Logger.getLogger(UploadRecord.class.getName()).log(Level.SEVERE, null, e);
                
                String errorMessage = e.getMessage();

                returnMessageToApp.put("response_tag", "database_error");

                returnMessageToApp.put("error_message", errorMessage);
                
            }

        }
        else
        {
            returnMessageToApp.put("response_tag", "insufficient_info_provided");
        }
        
        return returnMessageToApp;
        
    }
    
    public static JSONObject putCurrentDBInfoOn(JSONObject jsonObj) throws JSONException, ServletException, SQLException{
               
        if (jsonObj == null)                
        {
                    
            jsonObj = new JSONObject();
            
        }     
        
        try
        {
            
            // year
            
            int newYear = AskDBFor.currentYear();
            
            jsonObj.put("academic_year", newYear);
                        
            // targets list
            
            JSONArray targetsList = AskDBFor.generateJSONArray("targets");
            
            jsonObj.put("targets", targetsList);
            
            // levels list

            JSONArray levelsList = AskDBFor.generateJSONArray("levels");
                        
            jsonObj.put("levels", levelsList);
                        
            // classes list
                    
            JSONArray studentsInClassesList = AskDBFor.generateJSONArray("students_in_classes");

            jsonObj.put("classes", studentsInClassesList);
            
            
            // success! so tell JSON this
            
            jsonObj.put("response_tag", "new_database");
                                    
            } 
            catch (ServletException | SQLException | JSONException e)
            {
                //error handling

                String errorMessage = e.getMessage();

                returnMessageToApp.put("response_tag", "database_error");

                returnMessageToApp.put("error_message", errorMessage);

            }
              
        return jsonObj; 
                
    }
    
    
   // 2015/08/10 16:00:33
   // checks the bleow checktimestampinput() 
     public static void main(String[] args) throws JSONException, ServletException, SQLException {
        
         String input = "2015-08-10 16:00:33";
         
         Boolean john = checkTimeStampInput(input);
         
         System.out.println("john = " + john);
         
     }
    
    
    public static Boolean checkTimeStampInput(String timestampResult)
    {
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
       // formatter.setLenient(true);

        try 
        {

            Date date = formatter.parse(timestampResult);

        } 
        catch (ParseException e) 
        {
        
            return false;

        }

        return true;
        
    }
    
}
