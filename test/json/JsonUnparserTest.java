/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
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
public class JsonUnparserTest {
    
    public JsonUnparserTest() {
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
     * Test of unparseandReturnJson method, of class JsonUnparser, for a request record instruction.
     * @throws java.lang.Exception
     */
    @Test
    public void testUnparseandReturnJsonForRequestRecord() throws Exception {
        System.out.println("unparseandReturnJsonRequestRecord");
        
        
        JSONObject objRequestRecord = new JSONObject();      
        objRequestRecord.put("instruction_tag", "request_record");
        objRequestRecord.put("student_name", "Any");
        objRequestRecord.put("academic_year", "Any");
        objRequestRecord.put("term", "Any");
        objRequestRecord.put("target", "Any"); 
        
        
        JSONObject jsonObj = objRequestRecord;
                
        String expResult = "successful_request";
        JSONObject result = JsonUnparser.unparseandReturnJson(jsonObj);
               
        System.out.println(result.toString());
        
        assertEquals(expResult, result.get("response_tag"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }
    
    /**
     * Test of unparseandReturnJson method, of class JsonUnparser, for an upload record instruction.
     * @throws java.lang.Exception
     */
    @Test
    public void testUnparseandReturnJsonForUploadRecord() throws Exception {
        System.out.println("unparseandReturnJsonUploadRecord");
                
        JSONObject uploadRecordJSON = new JSONObject();      
        uploadRecordJSON.put("instruction_tag", "upload_record");
        String uuid = UUID.randomUUID().toString();
        uploadRecordJSON.put("file_name", uuid);
        uploadRecordJSON.put("student_name", "Donatello");
        uploadRecordJSON.put("target", "Maths");
        uploadRecordJSON.put("level", "Initial");
        uploadRecordJSON.put("observations", 
                "This radical turtle got through Java to be here!");
        uploadRecordJSON.put("date_stamp", "19-06-2015");
        uploadRecordJSON.put("is_image", 1); 
        
        JSONObject jsonObj = uploadRecordJSON;
                
        String expResult = "uploaded_record_success";
        JSONObject result = JsonUnparser.unparseandReturnJson(jsonObj);
               
        System.out.println(result.toString());
        
        assertEquals(expResult, result.get("response_tag"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }
    
    /**
     * Test of unparseandReturnJson method, of class JsonUnparser, for an upload record instruction.
     * @throws java.lang.Exception
     */
    @Test
    public void testUnparseandReturnJsonForCheckUpdates() throws Exception {
        System.out.println("unparseandReturnJsonCheckUpdates");
                
        JSONObject objCheckUpdates = new JSONObject();      
     
        objCheckUpdates.put("instruction_tag", "check_updates"); 
           
        Calendar today = Calendar.getInstance();
       
        Date todayDate = today.getTime();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String formattedDate = formatter.format(todayDate);        
        
        objCheckUpdates.put("timestamp", formattedDate);
        
        JSONObject jsonObj = objCheckUpdates;
                
        String expResult = "no_update";
        JSONObject result = JsonUnparser.unparseandReturnJson(jsonObj);
               
        System.out.println(result.toString());
        
        assertEquals(expResult, result.get("response_tag"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }
    
    public void testUnparseandReturnJsonForLaunching() throws Exception {
        System.out.println("unparseandReturnJsonCheckUpdates");
                
        JSONObject jsonObj = new JSONObject();      
     
        jsonObj.put("instruction_tag", "launching"); 
           
        String expResult = "new_database";
        JSONObject result = JsonUnparser.unparseandReturnJson(jsonObj);
               
        System.out.println(result.toString());
        
        assertEquals(expResult, result.get("response_tag"));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }
    
     
  
    
}
