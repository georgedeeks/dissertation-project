/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

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
public class MustCheckForUpdatesTest {
    
    public MustCheckForUpdatesTest() {
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
     * Test of checkTimeStampInput method, of class MustCheckForUpdates.
     */
    @Test
    public void testCheckTimeStampInput() {
        System.out.println("checkTimeStampInput");
        
        String timestampResult = "2015-08-10 16:00:33";
        
        //String timestampResult = "";
        Boolean expResult = true;
        Boolean result = MustCheckForUpdates.checkTimeStampInput(timestampResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }
    
}
