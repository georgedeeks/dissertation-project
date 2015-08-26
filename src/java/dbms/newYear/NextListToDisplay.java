/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms.newYear;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import database.DatabaseConnectionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;

/**
 *
 * @author George
 */
public class NextListToDisplay {
    
    // variables to send back
    private String toDisplayObjectType;
    private ArrayList<String> toDisplayList;
    private JSONObject adjustedPreviousInfo;
    
    // different 'strategies' or object types to decipher
    private final String yearName = "Year";
    private final String className = "Class";
    private final String studentName = "Student";
    private final String termName = "Term";
    
    // different values to extract from JSON
    private static String specifiedYear;
    private static String specifiedClass;
    private static String specifiedStudent;
    private static String specifiedTerm;
    
    private final static String[] allTerms = {"Winter", "Spring", "Summer"};
    
    private static Boolean[] termResults = new Boolean[3];
   
    
    public NextListToDisplay(String objectType, String objectValue, JSONObject previousInfo){
        
        try {
            // check the requested object type in order to process it
            
            if (null != objectType)
                switch (objectType) {
                    
                    case yearName: //NB capital letters for all of these! //ie "Year"
                        
                        System.out.println("The objectType is YEAR, "
                                + "therefore searching for a display list of "
                                + "available CLASSes");
                        
                        // Assign 3 variables that other classes will access
                        
                            toDisplayObjectType = className;
                        
                            toDisplayList = findAvailableClasses(objectValue);
                            
                            adjustedPreviousInfo = previousInfo;
                                // reset academic year to selected > 'active' one
                                adjustedPreviousInfo.put("academic_year", objectValue);
                            
                        break;
                        
                    case className: //NB capital letters for all of these!
                        
                        System.out.println("The objectType is CLASS, "
                                + "therefore searching for a display list of "
                                + "available STUDENTs");
                        
                        // Assign 3 variables that other classes will access
                            
                            toDisplayObjectType = className;
                        
                                specifiedClass = objectValue;
                                specifiedYear = previousInfo.getString("academic_year");
                            toDisplayList = findAvailableStudents(specifiedClass, specifiedYear);
                            
                                adjustedPreviousInfo = previousInfo;
                            adjustedPreviousInfo.put("class_name", objectValue);
                            
                        break;
                        
                    case studentName:
                        
                        System.out.println("The objectType is STUDENT, "
                                + "therefore searching for a display list of "
                                + "available TERMs");
                        
                        // Assign 3 variables that other classes will access
                        
                            toDisplayObjectType = studentName;
                        
                                specifiedStudent = objectValue;
                                specifiedYear = previousInfo.getString("academic_year");
                            toDisplayList = findAvailableTerms(specifiedYear, specifiedStudent);
                            
                                adjustedPreviousInfo = previousInfo;
                            adjustedPreviousInfo.put("student_name", objectValue);
                            adjustedPreviousInfo.put("winter_term_has_records", termResults[0]);
                            adjustedPreviousInfo.put("spring_term_has_records", termResults[1]);
                            adjustedPreviousInfo.put("summer_term_has_records", termResults[2]);
                            
                        break;
                        
                    case termName:
                        
                        System.out.println("The objectType is TERM, "
                                + "therefore searching for a display list of "
                                + "available TARGETs");
                        
                        // Assign 3 variables that other classes will access
                        
                            toDisplayObjectType = termName;
                        
                                specifiedTerm = objectValue;
                                specifiedStudent = previousInfo.getString("student_name");
                                specifiedClass = previousInfo.getString("class_name");
                                specifiedYear = previousInfo.getString("academic_year");
                            toDisplayList = findAvailableTargets(specifiedYear, specifiedClass, specifiedStudent, specifiedTerm);
                            
                                adjustedPreviousInfo = previousInfo;
                            adjustedPreviousInfo.put("selected_term", objectValue);
                            
                        break;
                        
                    default:
                        
                        //THERE HAS BEEN AN ERROR
                        
                        System.out.println("Error unparsing objectType String, "
                                + "though not because it is null");
                        
                        break;
                } //this ends the switch
            
            // we've assigned local variables, so can now return out of this constructor method
            
            else {
                
                //THERE HAS BEEN A NULL ERROR
                
                System.out.println("Error unparsing objectType String as it is null");
                
            }
            
        } 
        catch (JSONException ex) {
            Logger.getLogger(NextListToDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
        
    public static ArrayList<String> executeSqlForList(String sqlQuery){
        
        ArrayList<String> temp = new ArrayList();
            
        try {
    
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            
            while (resultSet.next())
            {
                temp.add(resultSet.getString("result"));
            }
            
            DatabaseConnectionSingleton.closeConnection();
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NextListToDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return temp;
    
    }
    
    //method for availabe classes
    public static ArrayList<String> findAvailableClasses(String theYear){
        
        String sql = "SELECT classroom.class_name AS result "
                + "FROM classroom, class_year, academic_year "
                + "WHERE classroom.class_id = class_year.class_id "
                + "AND class_year.academic_year = "
                + theYear
                + ";";
            
        ArrayList<String> temp = executeSqlForList(sql);
        
        return temp;
        
    }

    //method for availabe students
    public static ArrayList<String> findAvailableStudents(String theClass, String theYear){
        
        String sql = "SELECT student.s_name AS result "
                + "FROM student, student_in_class_year, classroom, class_year, academic_year "
                + "WHERE student.s_no = student_in_class_year.s_no "
                + "AND student_in_class_year.active = TRUE "
                + "AND student_in_class_year.class_id = classroom.class_id "
                + "AND student_in_class_year.academic_year = "
                + theYear
                + " AND classroom.class_name = '"
                + theClass
                + "';";
            
        ArrayList<String> temp = executeSqlForList(sql);
        
        return temp;
        
    }
    
    //method for availabe terms
    public static ArrayList<String> findAvailableTerms(String theYear, String theStudent){
        
        ArrayList<String> temp = new ArrayList();
        
        try {
       
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();

            for (int i = 0; i < allTerms.length; i++)
            {
                String termtoCheck = allTerms[i];

                String sql = "SELECT EXISTS (SELECT TRUE "
                        + "FROM evidence, student "
                        + "WHERE student_in_class_year.s_no = student.s_no "
                        + "AND student.s_name = '"
                        + theStudent
                        + "' AND evidence.media_date =< "
                        + termtoCheck
                        + "."
                        + termtoCheck
                        + "_term_start AND evidence.media_date =< "
                        + termtoCheck
                        + "."
                        + termtoCheck
                        + "_term_end "
                        + "AND academic_year.academic_year = "
                        + theYear
                        + ") AS result"
                        + ";";
                    // NB resultSet.getInt("result") -- have I sorted this?
                
                ResultSet resultSet = statement.executeQuery(sql);
                
                termResults[i] = resultSet.getBoolean("result");
                
                if (termResults[i] == true)
                {
                    temp.add(allTerms[i]);
                }
                
            }

        /* NOTE
            termResults corresponds to termtoCheck, 
            and we will return (as a separate array 'temp')
            only those terms which have a record in them 
            (i.e. to then display on the website page),
            though we will also need to save this information
            about whether EACH term has a record or not 
            into the storedInfo JSONObject (for future sql queries).
        */
                
        } catch (SQLException | ServletException ex) {
               
            Logger.getLogger(NextListToDisplay.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return temp;
        
    }
    
    //method for availabe targets
    public static ArrayList<String> findAvailableTargets(String theClass, String theYear, String theStudent, String theTerm){
        
        String sql = "SELECT DISTINCT evidence.support_plan_target AS result "
                + "FROM student, evidence, student_in_class_year "
                + "WHERE student.s_no = evidence.s_no "
                + "AND evidence.s_no = student_in_class_year.s_no "
                + "AND student_in_class_year.academic_year = "
                + theYear
                + " AND evidence.media_date =< "
                + theTerm
                + "."
                + theTerm
                + "_term_start AND evidence.media_date =< "
                + theTerm
                + "."
                + theTerm
                + "_term_end AND student.s_name = '"
                + theStudent
                + "' AND classroom.class_no = student_in_class_year.class_no "
                + "AND classroom.class_name = '"
                + theClass
                + "';";
            
        ArrayList<String> temp = executeSqlForList(sql);
        
        return temp;
        
    }
        
    public String getToDisplayObjectType() {
        return toDisplayObjectType;
    }

    public ArrayList<String> getToDisplayList() {
        return toDisplayList;
    }

    public JSONObject getAdjustedPreviousInfo() {
        return adjustedPreviousInfo;
    }

}
