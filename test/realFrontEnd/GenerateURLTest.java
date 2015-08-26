/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package realFrontEnd;

import java.util.UUID;
import json.GenerateURL;
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
public class GenerateURLTest {
    
    public GenerateURLTest() {
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
     * Test of createPath method, of class GenerateURL.
     */
    @Test
    public void testCreatePath() {
        System.out.println("createPath");
        
        String studentName = "Donatello";
        int studentNumber = 2;
        int latterYear = 2015;
        String termString = "winter";
        String targetString = "Maths";
        String fileString = UUID.randomUUID().toString();
        
        String expResult = "/home/ftpuser\\Donatello_2\\2014-2015\\winter\\Maths\\" + fileString;
        
        String result = GenerateURL.createPath(studentName, studentNumber, latterYear, termString, targetString, fileString);
                
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /*
     * can't test without file insertion capability; 
     * ultimately, unrequried for requirement specifications
     * 
     * Test of insertFile method, of class GenerateURL.
     /**
    @Test
    public void testInsertFile() {
        System.out.println("insertFile");
        
        String fileString = UUID.randomUUID().toString();        
        String path = "C:\\Donatello_2\\2014-2015\\winter\\Maths\\" + fileString;
        
        Boolean expResult = true;
        Boolean result = GenerateURL.insertFile(path);
        
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    */
    
    /**
     * Test of createURL method, of class GenerateURL.
     */
    @Test
    public void testCreateURL() {
          System.out.println("createURL");
        String studentName = "Donatello";
        int studentNumber = 2;
        int latterYear = 2015;
        String termString = "winter";
        String targetString = "Maths";
        String fileString = UUID.randomUUID().toString();
                
        String expResultCombine = "/home/ftpuser\\Donatello_2\\2014-2015\\winter\\Maths\\" + fileString;
        String expResult = expResultCombine;
        String beforeResult = GenerateURL.createURL(studentName, studentNumber, latterYear, termString, targetString, fileString);
        
        String[] parts = beforeResult.split("#");
        
        String result = parts[0];
        
        System.out.println(result);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

 
    
}
