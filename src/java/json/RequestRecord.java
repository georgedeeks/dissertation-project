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
import java.util.ArrayList;
import javax.servlet.ServletException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 *
 * @author cut14jh
 */
public class RequestRecord {
    
    private static String sql;
    
    private static JSONObject returnMessageToApp;
    
    private static String errorMessage;
    
    public static JSONObject requestRecord(JSONObject jsonObj) throws JSONException {

        System.out.println("Now executing 'requesting a record' code...");
        
        returnMessageToApp = new JSONObject();
        
        returnMessageToApp.put("requested_query", jsonObj);
        
        errorMessage = new String();

        String studentName = new String();
        String academicYearString = new String();
        String term = new String();
             
// attempt to extract JSON values       

    try
    {        
        // extract values from JSONObject

        studentName = jsonObj.getString("student_name"); // e.g. "John Smith"

        academicYearString = jsonObj.getString("academic_year"); // e.g. "1999"

        term = jsonObj.getString("term"); // e.g. "winter"        

    } catch (JSONException e)
    {
        errorMessage = "input";
    }

    if 
    (
        studentName.equals("")
        ||             
        academicYearString.equals("")            
    )
    {
        errorMessage = "input";
    }

    //check that term is one of four specific String values

    if (
            term.equals("any") || term.equals("spring") 
            || term.equals("summer") || term.equals("winter") 
        )
    {
        //then acceptable!
    }
    else
    {
        errorMessage = "input";
        System.out.println("error wtih term name input");
    }

    if (errorMessage.equals("input") == false)
    {

        //continue
        
        // create sql query based on parameters    

            // basic sql query

            sql = "SELECT DISTINCT evidence_data.file_url, student.s_name, "
                    + "evidence.support_plan_target, evidence.support_plan_level, "
                    + "evidence.observation, evidence.media_date, evidence.file_name, "
                    + "evidence.is_image, "
                    + "whichTerm(EXTRACT(year FROM evidence.media_date)::int,evidence.media_date) AS term, "
                    + "whichYear(evidence.media_date) AS latterYear, "
                    + "classroom.class_name "
                    + "FROM evidence, student, student_in_class_year, classroom, evidence_data, academic_year "
                    + "WHERE student.s_no = evidence.s_no "
                    + "AND student_in_class_year.s_no = evidence.s_no "
                    + "AND student_in_class_year.academic_year = whichYear(evidence.media_date) "
                    + "AND student_in_class_year.class_id = classroom.class_id "
                    + "AND evidence_data.media_id = evidence.media_id";

            // COULD DO:
            // go through JSONObject values to append to search query
                    // currently only supports singular values for queries
                    // change to multiple in agile development
                    // for multiple, change sql so it's
                    // IN ('value1', 'value2', value3')
                    // achieve this by creating an array and for loop, based on array size


            // student name

            if (studentName.equals("any"))
            {
                // do nothing
            }
            else
            {
                // add student name as a search possibility
                sql += " AND student.s_name = '"
                        + studentName
                        + "'";
            }

            // academic year

            if (academicYearString.equals("any"))
            {
                // do nothing
            }
            else
            {

                // add student name as a search possibility

                // NB don't need to convert 'academic_year' string to int as will become string anyway

                sql += " AND whichYear(evidence.media_date) = "
                        + academicYearString
                        + "";
            }

            // term name

            if (term.equals("any"))
            {
                // do nothing
            }
            else
            {
                /*
                
                String yearToSelect = "";
                
                if (academicYearString.equals("any"))
                {
                    //pick active year 
                    yearToSelect = "(SELECT academic_year FROM academic_year WHERE academic_year.active = TRUE)";
                }
                else
                {
                    yearToSelect = academicYearString;
                }                
                
                // add student name as a search possibility
                sql += " AND whichTerm("
                        + yearToSelect
                        + ", evidence.media_date) = '"
                        + term
                        + "'";                */
                
                sql += " AND whichTerm(EXTRACT(year FROM evidence.media_date)::int, evidence.media_date) = '" + term + "'";

            }

            // end the sql query statement
            sql += ";";

            System.out.println("sql query to send for request records: " + sql);

        // execute statement and collect results

            ArrayList<String> fileList = new ArrayList();
            ArrayList<String> urlList = new ArrayList();
            ArrayList<String> studentList = new ArrayList();
            ArrayList<String> targetList = new ArrayList();
            ArrayList<String> levelList = new ArrayList();
            ArrayList<String> observationList = new ArrayList();
            ArrayList<String> dateList = new ArrayList();
            ArrayList<String> termList = new ArrayList();
            ArrayList<String> classNameList = new ArrayList();
            ArrayList<String> academicYearStringList = new ArrayList();
            ArrayList<String> isImageStringList = new ArrayList();
            
            try
            {

                Statement statement = DatabaseConnectionSingleton.getStatementInstance();

                ResultSet resultSet = statement.executeQuery(sql);
                
                while (resultSet.next())
                {                
                    /*
                    
                    column headings:
                    
                    file_url
                    s_name
                    support_plan_target
                    support_plan_level
                    observation
                    media_date
                    term
                    latterYear
                    class_name;
                    
                    */
                    
                    String urlListStringToAdd = resultSet.getString("file_url") ;
                    String fileListStringToAdd = resultSet.getString("file_name");
                    String studentListStringToAdd = resultSet.getString("s_name");
                    String targetListStringToAdd = resultSet.getString("support_plan_target");
                    String levelListStringToAdd = resultSet.getString("support_plan_level");
                    String observationListStringToAdd = resultSet.getString("observation");
                    String dateListStringToAdd = resultSet.getString("media_date");
                    String termStringToAdd = resultSet.getString("term");                    
                    String classNameStringToAdd = resultSet.getString("class_name");
                        int academicYearIntToConvert = resultSet.getInt("latteryear");
                        int academicYearMinusOne = academicYearIntToConvert - 1;
                    String academicYearStringToAdd = academicYearMinusOne + "-" + academicYearIntToConvert;
                         int isImage = resultSet.getInt("is_image");
                    String isImageString = Integer.toString(isImage);
                    
                    
                    urlList.add(urlListStringToAdd);
                    fileList.add(fileListStringToAdd);
                    studentList.add(studentListStringToAdd);
                    targetList.add(targetListStringToAdd);
                    levelList.add(levelListStringToAdd);
                    observationList.add(observationListStringToAdd);
                    dateList.add(dateListStringToAdd);
                    termList.add(termStringToAdd);
                    classNameList.add(classNameStringToAdd);
                    academicYearStringList.add(academicYearStringToAdd);
                    isImageStringList.add(isImageString);

                }

            }
            catch (SQLException e)
            {

                errorMessage = e.toString();

            }        
            catch (ServletException e)
            {

                errorMessage = "Servlet exception.";

            }
            
            //System.out.println("I found a mole last Wednesday.");
               
            if (studentList.size() > 0 && errorMessage.equals(""))
            {
                
            //continue
                 
            // process results into JSONObject

                JSONArray recordsList = new JSONArray();

                for (int i = 0; i < studentList.size(); i++)
                {

                    JSONObject tempRecord = new JSONObject();

                    tempRecord.put("URL", urlList.get(i));
                    tempRecord.put("file_name", fileList.get(i));
                    tempRecord.put("student_name", studentList.get(i));
                    tempRecord.put("target", targetList.get(i));
                    tempRecord.put("level", levelList.get(i));
                    tempRecord.put("observation", observationList.get(i));
                    tempRecord.put("date_stamp", dateList.get(i));
                    tempRecord.put("term", termList.get(i));
                    tempRecord.put("class_name", classNameList.get(i));
                    tempRecord.put("academic_year", academicYearStringList.get(i));
                    tempRecord.put("is_image", isImageStringList.get(i));
                    
                    recordsList.put(tempRecord);

                }

                returnMessageToApp.put("response_tag", "successful_request");

                returnMessageToApp.put("records", recordsList);
        
            }
            else // if (studentList.size() == 0)
            {
                //error
                
                if (errorMessage.equals(""))
                {
                    errorMessage = "No records to display! Please contact your system "
                            + "administrator if you are not expecting this result.";
                }

                returnMessageToApp.put("response_tag", "unsuccessful_request");
                
                returnMessageToApp.put("error_message", errorMessage); 
                
            }
            
            //DatabaseConnectionSingleton.closeConnection();
            
           // System.out.println(returnMessageToApp.toString());

        }
        else        
        {
            //error with input
            
            returnMessageToApp.put("response_tag", "insufficient_info_provided");
            
        }    
        
    // return JSONObject
    
        //System.out.println(returnMessageToApp);
    
        return returnMessageToApp;
        
    }
    
    
    // test area    
    public static void main(String[] args) throws JSONException, ServletException, SQLException {
    
        JSONObject test1 = new JSONObject();
            test1.put("cat", "miaow");
            JSONObject test2 = new JSONObject();
            test2.put("dog", "woof");

            JSONArray test3 = new JSONArray();

            JSONObject test4 = new JSONObject();


            test3.put(test1);
            test3.put(test2);

            test4.put("evidence_records", test3);

            System.out.println("test4 = " + test4);
        
    }
    
}