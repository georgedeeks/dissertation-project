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


/**
 *
 * @author George
 */
public class AskDBFor {
    
    private static String sql;    
    private static Statement statement;  
    private static String selectedClass; 
    private static String newClass;
        
    public static int currentYear() throws ServletException, SQLException{
             
        statement = DatabaseConnectionSingleton.getStatementInstance();
        
        sql = "SELECT academic_year FROM academic_year WHERE active = TRUE;";
        
        ResultSet resultSet = statement.executeQuery(sql);
        
        resultSet.next();
        
        int result = resultSet.getInt("academic_year");
 
        
        return result;
        
    }

    public static JSONArray generateJSONArray(String strategy) 
            throws ServletException, SQLException, JSONException {

        // unpack strategy (multiple methods here for calculating sql?)
        
        // String sql = [based on strategy] 
            //[nb COLUMNHEADING should be uniform or not??]
                // could return a string result for column heading to use?
        
        if (null != strategy)                
        switch (strategy) {
            
            case "targets":
                
                // TEST THIS
                sql = "SELECT support_plan_target AS result FROM support_plan_target WHERE active = true ORDER BY display_order ASC;";
                
                break;
                
            case "levels":
                
                // TEST THIS
                sql = "SELECT support_plan_level AS result FROM support_plan_level WHERE active = true ORDER BY display_order ASC;";
                
                break;
                
            case "students_in_classes":
                
                sql = "SELECT student.s_name AS student, "
                        + "classroom.class_name AS classroom "
                        + "FROM student, classroom, student_in_class_year, academic_year "
                        + "WHERE student.s_no =  student_in_class_year.s_no "
                        + "AND classroom.class_id = student_in_class_year.class_id "
                        + "AND academic_year.academic_year = student_in_class_year.academic_year "
                        + "AND academic_year.active = TRUE "
                        + "ORDER BY classroom.class_name ASC"
                        + ";"
                        ;
                                
                break;
                
            default:
                //THERE HAS BEEN AN ERROR
                System.out.println("Error matching up strategy string!");
                break;
        }
        else {
            System.out.println("Error as *null* value for strategy String");
        }
        
        JSONArray jsonArrayToBuild = new JSONArray();
        
        statement = DatabaseConnectionSingleton.getStatementInstance();
        
        ResultSet resultSet = statement.executeQuery(sql);
        
        if (strategy.equals("students_in_classes"))
        {
            // handle statement and jsonobject differntly
            
            ArrayList<String> classList = new ArrayList();
            ArrayList<String> studentList = new ArrayList();
            
            while(resultSet.next())     // ie for all rows
            {            
                classList.add(resultSet.getString("classroom"));
                studentList.add(resultSet.getString("student"));
            }
            
            selectedClass = classList.get(0);
            newClass = new String();
            
            JSONObject classesObjectToAddToArray = new JSONObject();

            classesObjectToAddToArray.put("class_name", selectedClass);

            JSONArray studentsArrayToAddToClassesObject = new JSONArray();

            studentsArrayToAddToClassesObject.put(studentList.get(0));

            for (int i = 1; i < classList.size(); i++)
            {
                newClass = classList.get(i);

                if (newClass.equals(selectedClass))
                {
                    studentsArrayToAddToClassesObject.put(studentList.get(i));
                }
                else
                {

                // finish off (array to add to) object to (then) add to final array

                    classesObjectToAddToArray.put("students", studentsArrayToAddToClassesObject);

                    jsonArrayToBuild.put(classesObjectToAddToArray);

                // start creating new object (and its intenral array) so we are in same
                    // position as before the start of this for loop
                    //(then we can go round the loop again, ad infinaturm)

                    selectedClass = newClass;

                    classesObjectToAddToArray = new JSONObject();

                    classesObjectToAddToArray.put("class_name", selectedClass);

                    studentsArrayToAddToClassesObject = new JSONArray();

                    studentsArrayToAddToClassesObject.put(studentList.get(i));

                }

            }

            classesObjectToAddToArray.put("students", studentsArrayToAddToClassesObject);

            jsonArrayToBuild.put(classesObjectToAddToArray);
            
        }
        else
        {
            while(resultSet.next())     // ie for all rows
            {            
                jsonArrayToBuild.put(resultSet.getString("result"));                    
            }
            
        }
                
        return jsonArrayToBuild;
        
    }
    
}

