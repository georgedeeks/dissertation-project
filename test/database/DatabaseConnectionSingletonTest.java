/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.sql.Statement;
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
public class DatabaseConnectionSingletonTest {
    
    public DatabaseConnectionSingletonTest() {
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
     * Test of getStatementInstance method, of class DatabaseConnectionSingleton.
     * @throws java.lang.Exception
     */
    @Test
    public void testConnectionMethods() throws Exception {
        System.out.println("testConnectionMethods");
        
        Statement statement = DatabaseConnectionSingleton.getStatementInstance();
        
        boolean expResult = statement != null;
        
        DatabaseConnectionSingleton.closeConnection();
                               
        assertEquals(expResult, true);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
                        
        
    }

}
