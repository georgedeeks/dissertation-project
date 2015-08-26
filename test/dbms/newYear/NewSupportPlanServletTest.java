/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dbms.newYear;

import java.util.ArrayList;
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
public class NewSupportPlanServletTest {
    
    public NewSupportPlanServletTest() {
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
     * Test of code within servlet method, of class NewSupportPlanServlet.
     */
    @Test
    public void testProcessChangesToPassiveList() {
        System.out.println("processChanges");
        
        
        ArrayList<Integer> notDeletedOrPassiveChangedOrderList = new ArrayList();
        ArrayList<String> notDeletedOrPassiveNameList = new ArrayList();
        
        notDeletedOrPassiveChangedOrderList.add(5);
        notDeletedOrPassiveChangedOrderList.add(1);
        notDeletedOrPassiveChangedOrderList.add(3);
        notDeletedOrPassiveChangedOrderList.add(6);
        
        notDeletedOrPassiveNameList.add("E");
        notDeletedOrPassiveNameList.add("A");
        notDeletedOrPassiveNameList.add("C");
        notDeletedOrPassiveNameList.add("F");
                        
        System.out.println("before change words: " + notDeletedOrPassiveNameList + notDeletedOrPassiveChangedOrderList);
        
        Boolean needsAnotherChange = true ;
            
            while (needsAnotherChange == true)
            {
                
                needsAnotherChange = false ;
                
                for (int i = 1; i < notDeletedOrPassiveChangedOrderList.size(); i++)
                {
                                        
                    int iMinusOne = i - 1;
                    
                    int formerInt = notDeletedOrPassiveChangedOrderList.get(iMinusOne);
                    
                    String formerName = notDeletedOrPassiveNameList.get(iMinusOne);
                    
                    int latterInt = notDeletedOrPassiveChangedOrderList.get(i);
                    
                    String latterName = notDeletedOrPassiveNameList.get(i);
                    
                    if (latterInt < formerInt)
                    {
                        // switch them
                        
                        
                        
                        notDeletedOrPassiveChangedOrderList.set(iMinusOne, latterInt);
                        notDeletedOrPassiveNameList.set(iMinusOne, latterName);
                                                        
                        notDeletedOrPassiveChangedOrderList.set(i, formerInt);
                        notDeletedOrPassiveNameList.set(i, formerName);
                        
                        // set to true for another while (and for) loop
                        
                        needsAnotherChange = true ;
                                               
                    }        
                    
                    //System.out.println("iteration " + i + ": " + notDeletedOrPassiveNameList + notDeletedOrPassiveChangedOrderList);
                    
                }
                
            } 
            
            ArrayList<String> result = notDeletedOrPassiveNameList;
            
           ArrayList<String> expResult = new ArrayList(); 
           expResult.add("A");
           expResult.add("C");
           expResult.add("E");
           expResult.add("F");
           
           assertEquals(result, expResult);
            
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
     /**
     * Test of code within servlet method, of class NewSupportPlanServlet.
     */
    @Test
    public void testProcessChangesOfDeleteList() {
        System.out.println("processChangesForDeleting");
        
        
        ArrayList<String> oldList = new ArrayList<>();
        ArrayList<String> newList = new ArrayList<>();
        ArrayList<Integer> deleteList = new ArrayList<>();
        
        oldList.add("Adder");
        oldList.add("Mamba");
        oldList.add("Viper");
        oldList.add("Asp");
        oldList.add("Cobra");
        oldList.add("Snake");
        oldList.add("Ekans");
        
        for (int i = 0; i < oldList.size(); i++)
        {
            if (oldList.get(i).contains("e"))
            {
                deleteList.add(i);
            }
        }
        
        System.out.println("new list BEFORE: " + oldList);
        System.out.println("delete list: " + deleteList);
                
        if (deleteList.size() > 0)
        {
                for (int i = 0; i < oldList.size(); i++)
                {                    
                    
                    Boolean deleteThis = false;
                
                    for (int j = 0; j < deleteList.size(); j++)
                    {                        
                        
                        if (i == deleteList.get(j))
                        {
                            
                            deleteThis = true;
                            
                        }

                    }
                    
                    if (deleteThis == false)
                    {
                        newList.add(oldList.get(i));
                    }
                    
                }
        }
        
        ArrayList<String> result = newList;
        
        System.out.println("new list AFTER: " + newList);
        ArrayList<String> expResult = new ArrayList();
        expResult.add("Mamba");
        expResult.add("Asp");
        expResult.add("Cobra");
        
        expResult.add("Ekans");
        
        assertEquals(result, expResult);
            
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
