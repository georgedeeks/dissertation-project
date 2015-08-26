/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dbms.newYear;

import database.DatabaseConnectionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cut14jhu
 */
public class NextListToDisplayTest {
    
    public NextListToDisplayTest() {
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
     * Test of findAvailableClasses method, of class NextListToDisplay.
     */
    @Test
    public void testFindAvailableClasses() throws ServletException, SQLException {
        System.out.println("findAvailableClasses");
        
        Statement statement = DatabaseConnectionSingleton.getStatementInstance();
        
        String sql = "SELECT academic_year.academic_year AS year, "
                + "EXISTS(SELECT classroom.class_name FROM classroom, "
                + "class_year, academic_year "
                + "WHERE classroom.class_id = "
                + "class_year.class_id AND class_year.academic_year = academic_year.academic_year "
                + "AND academic_year.active = TRUE) AS result "
                + "FROM classroom, "
                + "class_year, academic_year "
                + "WHERE academic_year.active = TRUE;";
        
        
        ResultSet resultSet = statement.executeQuery(sql);
        
        resultSet.next();
        
        int theYearInt = resultSet.getInt("year");
        
        boolean result = false;
        
        if (resultSet.getBoolean("result"))
        {
            //continue
            
            String theYear = Integer.toString(theYearInt);
            
            ArrayList<String> resultList = NextListToDisplay.findAvailableClasses(theYear);
            
            result = resultList.isEmpty();
            
        }
        else
        {
            //result = true;
        }
                      
        assertEquals(false, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    
}
