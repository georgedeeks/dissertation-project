/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cut14jhu
 */
public class AskDBForTest {
    
    public AskDBForTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of currentYear method, of class AskDBFor.
     */
    @Test
    public void testCurrentYear() throws Exception {
        System.out.println("currentYear");
        
        
        
        //expect: current Calender year
        
        
        boolean result = false;
        
        int resultInt = AskDBFor.currentYear();
        
        if (resultInt > 2000 && resultInt < 3000)
        {
            result = true;
        }
        
        assertEquals(true, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of generateJSONArray method, 
     * assuming a list of students to add to classes, 
     * of class AskDBFor.
     */
    @Test
    public void testGenerateJSONArray() throws Exception {
        System.out.println("generateJSONArray");
        
        
        String selectedClass;
        String newClass;

        ArrayList<String> classList = new ArrayList();
        ArrayList<String> studentList = new ArrayList();

        classList.add("Yellow");
        studentList.add("Jonjo Shelvey");
        classList.add("Yellow");
        studentList.add("Alan Shearer");
        classList.add("Green");
        studentList.add("Greg Dyke");
        classList.add("Green");
        studentList.add("Ruby Walsh");
        classList.add("Green");
        studentList.add("Dereck Jacobi");
        classList.add("Blue");
        studentList.add("Winston Reid");
        
        selectedClass = classList.get(0);
        newClass = new String();

        JSONArray jsonArrayToBuild = new JSONArray();

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
        
        String result = jsonArrayToBuild.toString();
                
        String expResult = "[{\"students\":[\"Jonjo Shelvey\","
                + "\"Alan Shearer\"],\"class_name\":\"Yellow\"},"
                + "{\"students\":[\"Greg Dyke\",\"Ruby Walsh\","
                + "\"Dereck Jacobi\"],\"class_name\":\"Green\"},"
                + "{\"students\":[\"Winston Reid\"],"
                + "\"class_name\":\"Blue\"}]"
                ;
                        
        System.out.println("result6 = " + result);
        
        System.out.println("result9 = " + expResult);
        
        assertEquals(result, expResult);
    }
    
}
