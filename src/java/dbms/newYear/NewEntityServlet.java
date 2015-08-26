package dbms.newYear;

import dbms.DisplayInfoBean;
import database.DatabaseConnectionSingleton;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "NewEntityServlet", 
        urlPatterns = {"/NewEntityServlet"})

public class NewEntityServlet extends HttpServlet 
{
    
    private ArrayList<String> oldList;
    
    private String errorMessage;
    
    private String classToAssignTo;
    
     private String successMessage;
    
     private int arraySize;
     
    private ArrayList<String> displayList;
    
    
    // 'firedList' i.e. list of classnames (as a result of lokoing at checkbozxes that WERE ticked)
    // OR a list of student names to include
    private ArrayList<String> selectedList; 
    
   // list of class names corresponding to the selectedList
    private ArrayList<String> selectedForThisClassList;
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException 
    {
        
        System.out.println("We've reached the EntitiesForNewYearServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
            /*
            do later - figure out strategy string based on whether new or active entity
            if new entity, get JSON info
            if not, different way to process
            atm, just new entity processing
            */
            
            successMessage = new String();
            
            String strategy = request.getParameter("strategy");
       
        // get info from webpage
            
            // JSONObject
            
            String jsonString = request.getParameter("json_object");
            
            JSONObject previousInfo = new JSONObject(jsonString);
            
            System.out.println("previnfo = " + previousInfo);
            
            String objectType = previousInfo.getString("object_type");
            
            String newEntity = "";
            String sql = "";
            
            
            
            if (request.getParameter("none") != null)
            {
                
                // determine wehther we have at least one enetitiy
                
                    // what is our object type list size key?
                
                String entitySizeStringKey = "";
               
                boolean atLeastOne = true;
                
                try
                {

                    if (objectType.equals("Class"))
                         {
                             entitySizeStringKey = "classname_size";
                         }
                         else // studnet
                         {
                             entitySizeStringKey = "student_in_class_size";
                         }
                         arraySize = (Integer) previousInfo.get(entitySizeStringKey);
                         
                         if (arraySize < 1)
                         {
                             atLeastOne = false;
                         }
                         else
                         {
                             atLeastOne = true;
                         }
                             

                }
                catch (JSONException e)
                {
                    atLeastOne = false;
                }
                         
                if (atLeastOne)
                {
                    //load success message
                    
                    successMessage = "Success! No new objects of type '" + objectType + "' were entered.";
                                        
                }
                else
                {
                    errorMessage = "Warning! At least one '" + objectType + "' must be entered.";
                }
                
            }
            else if 
                    
                    // here we are checking, that if the normal button was fired, and its 
                    
                (
                    request.getParameter("normal") != null 
                    && 
                    objectType.equals("Student") 
                    && 
                    //one of:
                        request.getParameter("new_entity").equals("")
                        |
                        request.getParameter("selectbox").equals("Unassigned")
                )
            {
                
                int checkThisIsNotBelowOne = -1;
                
                try
                {
                    checkThisIsNotBelowOne = previousInfo.getInt("student_in_class_size");
                }
                catch (JSONException e)
                {
                    checkThisIsNotBelowOne = -1;
                }
                
                if (checkThisIsNotBelowOne < 1)
                {
                    //error
                    
                    errorMessage = "Warning! At least one student must be entered "
                            + "into the new academic year.";
                    
                }
                
                
            }
            else
            {
                
            
                          
                newEntity = request.getParameter("new_entity");  

                ////////////////////////////////////

                // if no previous classes, don't delete

                //declare upcmoing variables

                 String arraySizeString = new String();
                String keyMinusIndex = new String();
                String entitySizeStringKey = new String();
              

                Boolean nullArray = false;

                //check
                try
                {

                    if (objectType.equals("Class"))
                    {
                        entitySizeStringKey = "classname_size";
                    }
                    else // studnet
                    {
                        entitySizeStringKey = "student_in_class_size";
                    }
                    arraySize = (Integer) previousInfo.get(entitySizeStringKey);

                    //arraySizeString = (String) previousInfo.get(entitySizeStringKey);

                    if (arraySize < 1)
                    {
                        nullArray = true;
                    }
                }
                catch (Exception e)
                {
                    nullArray = true;
                }

                // proceed to delete cbox check if is NOT null


                if (nullArray == false)
                {
                        /////////////////////////////////////////////////////////////////////////////////////

                        // next section step is to extract tickboxes that have been ticked to delete from the JSONArray

                        // populating arraySizeString and keyMinusIndex depends on objectType           

                    arraySize = 0; //reset

                        if (objectType.equals("Class"))
                                    {

                                        entitySizeStringKey = "classname_size";

                                         arraySize = (Integer) previousInfo.get(entitySizeStringKey);

                                         keyMinusIndex = "classname_";

                                         sql = "INSERT INTO classroom (class_name) VALUES ('"
                                            + newEntity
                                            + "');";
                                    }

                        else //(objectType.equals("Student") )
                                    {
                                        entitySizeStringKey = "student_in_class_size";

                                        arraySize = (Integer) previousInfo.get(entitySizeStringKey);

                                        keyMinusIndex = "student_in_class_";

                                        sql = "INSERT INTO student (s_name) VALUES ('"
                                                + newEntity
                                                + "');";
                                    }


                    // STEP #1 of 3 (only step 1 is mandatory)
                        // for loop over the array of checkboxes, 
                            //oldList containing all list of names, 
                            //selectedList containing those that have NOT been checked

                        selectedList = new ArrayList<>();
                        oldList = new ArrayList<>();

                        for (int i = 0; i < arraySize; i++)
                        {

                            // add ALL classnames to oldList

                                String key = keyMinusIndex + i;

                                String nameToAdd = previousInfo.getString(key);

                                oldList.add(nameToAdd); //add regardless

                            // if checkboxed, then add to list to delete

                                String buttonName = "item" + i;


                                if (request.getParameter(buttonName) != null)  
                                {

                                    selectedList.add(nameToAdd); //ie this is the delete list, so 
                                    // empty if none are to be delete

                                }

                        } // end of for loop #1

                    // STEP #2 of 3
                        // IF any checkboxes have been ticked (step 1), THEN we need to edit JSONArray (in step 3)
                        // (step 2 first though:)
                        // go through each name in the selected list, and 
                            //check against every name in the oldlist
                            // if there's NOT a match, add that element of the oldlist to edited list
                        // end up with edited list i.e. of non-ticked entities



                        if (selectedList.size() > 0) // i.e., have any checkboxes been ticked?
                        {
                            //step 2a - create editedList

                            ArrayList editedList = new ArrayList();

                            for (int i = 0; i < selectedList.size(); i++)
                                    {

                                        String match = selectedList.get(i);

                                            for (int j = 0; j < arraySize; j++)
                                            {
                                                if (oldList.get(j).equals(match))
                                                {
                                                    //do nothing
                                                }
                                                else // not a match - i.e., has not been deleted
                                                {
                                                    editedList.add(oldList.get(j));
                                                }
                                            }

                                    }

                            // step 2b - rewrite JSONArray (with selectedlist)

                            for (int i = 0; i < arraySize; i++)              
                                    {

                                        String key = keyMinusIndex + i;

                                        previousInfo.remove(key);

                                    }                                

                                // JSON array size REPLACE


                                    int newArraySize = editedList.size();


                                    previousInfo.put(entitySizeStringKey, newArraySize);

                                // JSON array values ADD


                                    for (int i = 0; i < newArraySize; i++)
                                    {

                                        String key = keyMinusIndex + i;

                                        previousInfo.put(key, editedList.get(i));

                                    }


                        } // end of 'if any checkboxes have been ticked' statement
                        else
                        {
                            // do nothing
                        }


                }
                else
                {
                    System.out.println("no deleting required as empty array of previous enetities of type " + objectType);
                } 
                //nothing - we do not need to carry out delete process if empty classname list

                ///END OF SECTION


                // we still have  a nullArray Boolean based on wehther there were previous entrants or not!

                ///////////////////////////////////////////////////////////////////////

                //next step (NB oldList is the current(/new) list of classnames)

                    //next step is to get new name entry entry

                errorMessage = ""; // firstly, ensure errorMessage is blank

               // got newEntitty out earlier for determining sql

                if (newEntity.equals("")) // if new entity is blank
                {
                    errorMessage = "Field entry is blank";
                }
                else
                {
                    // everything is fine, so continue

                    //try to insert name into database

                    // sql determined ni earlier 'objectType' if statements

                    if (objectType.equals("Class"))
                        {

                            sql = "INSERT INTO classroom (class_name) VALUES ('"
                                + newEntity
                                + "');";
                        }
                    else // student
                    {
                        sql = "INSERT INTO student (s_name) VALUES ('"
                                + newEntity
                                + "');";
                    }

                    Boolean success = tryToAddNameToDB(sql);

                    if (success == false)
                    {
                        //error handling

                        errorMessage = "Unable to add name '" + newEntity + "' into database. "
                                + "Please check spelling, and whether that entry already exists.";

                    }
                    else //(success == true)
                    {


                                 ArrayList<String> classNamesArray = new ArrayList();
                        // see if they want the new entity added to the new/active year JOSN String

                        Boolean addToCurrent;


                        if (objectType.equals("Class"))
                        {
                            if (request.getParameter("new_entity_cbox") == null)
                            {
                                addToCurrent = false;
                            }
                            else
                            {
                                addToCurrent = true;
                            }
                        } else //if (objectType.equals("Student"))
                        {

                            classToAssignTo = request.getParameter("selectbox");

                            if (classToAssignTo.equals("Unassigned"))
                            {
                                addToCurrent = false;
                            }
                            else
                            {
                                addToCurrent = true;
                            }
                        }


                        if (addToCurrent == false)
                        {
                            // then it was NOT fired so does NOT want to be included

                            //so do nothing but update result message

                            successMessage = "Entity '" + newEntity 
                                    + "' added to database, but not included in" + strategy 
                                    + " list.";

                        }
                        else // addToCurrent == true
                        {
                            // include new entry into stored entitylist

                            // add class name to new/active list, and update list size also


                            String newEntityEdited;
                            int entityListSize = 0;
                            String key;


                            if (nullArray == true)
                             {

                                if (objectType.equals("Class"))
                                {

                                    // handle empty list
                                    // object type here MUST be a "class" (cannot have empty class list for pupils)

                                    //first key
                                     key = "classname_0";
                                    // first size key
                                     entitySizeStringKey = "classname_size";
                                     entityListSize = 0; //already established but i'll repeat for clarity here 

                                }
                                else
                                {
                                    //student

                                    //first key
                                     key = "student_in_class_0";
                                    // first size key
                                     entitySizeStringKey = "student_in_class_size";
                                     entityListSize = 0; //already established but i'll repeat for clarity here 

                                }


                            }
                            else
                            {
                                //populate already existing list

                                // this should work regardsless of object type

                                entityListSize = (Integer) previousInfo.get(entitySizeStringKey);

                                 key = keyMinusIndex + entityListSize;
                            }





                            // determine String newEntityEdited to store in key


                            if (objectType.equals("Student"))
                                {

                                    // newEntityEdited as must also include class name

                                    //String selectedClassName = request.getParameter("selectbox");

                                    newEntityEdited = newEntity + "#" + classToAssignTo;

                            }
                            else
                                {
                                    newEntityEdited = newEntity;
                            }


                            int TEMPnewEntityListSize = entityListSize + 1;
                        // checking upcmoing super important thign
                            System.out.println("key = " + key);
                            System.out.println("newEntityEdited = " + newEntityEdited);
                            System.out.println("entitySizeStringKey = " + entitySizeStringKey);
                            System.out.println("newEntityListSize = " + TEMPnewEntityListSize);


                        // super important! - update entity list and entity list size

                            previousInfo.put(key, newEntityEdited);

                            int newEntityListSize = entityListSize + 1;

                            previousInfo.put(entitySizeStringKey, newEntityListSize);

                        }

                    }

                }        
            
            }    
            
            

       // updated all info ready to send to next apge             
       // determine which page we are sending to

            if (errorMessage == null || errorMessage.equals(""))
            {

                // everything is cool so DONT send to error page

                // in fact, load a success message if one is not already there


                    if (successMessage == null || successMessage.equals("")) // 
                    {
                        successMessage = "Successful insert of entity '" + newEntity + "'!";
                    }

                // now redirect user based on which submit button was fired

                    if (request.getParameter("another") != null)
                    {
                        // then the user wants to return to new entity page, with same details as before, except a success message loaded in

                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(objectType, previousInfo);

                        request.setAttribute("displayInfoBean", displayInfoBean);

                        request.setAttribute("strategy", strategy); 

                        request.setAttribute("success_message", successMessage);

                        request.getRequestDispatcher("dbms/newYear/newEntity.jsp").forward(request, response);

                    }
                    else if (strategy.equals("new year") == false)
                    {
                        // active year/normal use

                        request.setAttribute("update_result", successMessage);

                        request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);

                    } 
                    else ///other submit button, to move onto another page, was fired
                            // AND it's a new year form
                    {

                        // send to either
                        // active entity, archived eneity, new entity

                        displayList = new ArrayList<>();


                        if (objectType.equals("Class"))
                        {

                            // change to student
                                previousInfo.put("object_type", "Student");


                            // find sqlForActive and sqlForArchived based on objectType

                                String sqlForActive;
                                String sqlForArchived;

                                sqlForActive = "SELECT student.s_name AS result "
                                    + "FROM student, student_in_class_year, academic_year "
                                    + "WHERE student.s_no = student_in_class_year.s_no "
                                    + "AND student_in_class_year.academic_year = academic_year.academic_year "
                                    + "AND academic_year.active = TRUE;";

                                sqlForArchived = "SELECT student.s_name AS result "
                                    + "FROM student "
                                    + "EXCEPT "
                                    + "SELECT student.s_name "
                                    + "FROM student, student_in_class_year, academic_year "
                                    + "WHERE student.s_no = student_in_class_year.s_no "
                                    + "AND student_in_class_year.academic_year = academic_year.academic_year "
                                    + "AND academic_year.active = TRUE;";

                            // see if there are active student entitites
                                displayList = executeSqlForArrayList(sqlForActive);

                            // decision tree based on shwether there were any results

                                if (displayList.size() > 0)  
                                {

                                // successful listToDisplay for active classes!           

                                // store everything in a JSONArray



                                         previousInfo.put("active_status", "currently active");

                                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo, displayList);

                                        request.setAttribute("displayInfoBean", displayInfoBean);

                                        request.getRequestDispatcher("dbms/newYear/pickArray.jsp").forward(request, response);




                                } else
                                {

                                //error finding any active entities

                                // move straight to finding all except active (ie archived) entities

                                    displayList = executeSqlForArrayList(sqlForArchived);

                                    if (displayList.size() > 0)
                                    {

                                    // successful listToDisplay for NONactive classes!           

                                    // store everything in a JSONArray

                                        previousInfo.put("active_status", "archived");

                                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo, displayList);

                                        request.setAttribute("displayInfoBean", displayInfoBean);     

                                        request.getRequestDispatcher("dbms/newYear/pickArray.jsp").forward(request, response);

                                    } else 
                                    {
                                    //gotta send to new entity list

                                        // after new entity list page, will need active status to be currently active
                                        previousInfo.put("active_status", "currently active");

                                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo);

                                        request.setAttribute("displayInfoBean", displayInfoBean); 

                                        successMessage += " No current or archived students found. Please enter entirely new students.";

                                        request.setAttribute("success_message", successMessage);

                                        request.setAttribute("strategy", "new year");

                                        request.getRequestDispatcher("dbms/newYear/newEntity.jsp").forward(request, response);

                                    } //end of SECOND listdispaly is null logic

                                } // end of INITIAL listdisplay is null logic

                        } // end of objectType = Class
                        else // if (objectType.equals("Student") )
                        {

                            previousInfo.put("object_type", "Target");

                            // determine target list

                            sql = "SELECT support_plan_target AS result, active AS boolean "
                                    + "FROM support_plan_target ORDER BY active DESC, display_order ASC;"; 

                            ArrayList<Object> containsResults = executeSqlForTwoArrayLists(sql);

                            ArrayList<String> resultNameList = (ArrayList<String>) containsResults.get(0);

                            ArrayList<Boolean> resultActiveList = (ArrayList<Boolean>) containsResults.get(1);

                            DisplayInfoBean displayInfoBean = new DisplayInfoBean(resultNameList, resultActiveList, previousInfo);

                            request.setAttribute("displayInfoBean", displayInfoBean);

                            request.setAttribute("strategy", "new year");

                            request.setAttribute("success_message", successMessage);

                            Boolean test = resultActiveList.get(0);

                            System.out.println("test = " + test);

                            System.out.println("");

                            request.getRequestDispatcher("dbms/newYear/newSupportPlan.jsp").forward(request, response);

                        } // end of object type = class                       

                    } // end of 'normal move on' submit button logic

            } // end of no error logic when updaing new entitiy information
            else
            {
                // handle error messages as erro when updating new entity info

                DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo);

                request.setAttribute("displayInfoBean", displayInfoBean);

                request.setAttribute("strategy", "new year");

                request.setAttribute("error_message", errorMessage);

                request.getRequestDispatcher("dbms/newYear/newEntity.jsp").forward(request, response);

            }           
                
        // end of whole try block now
                
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }        

    }

    
    public Boolean tryToAddNameToDB(String sql)
    {
    
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            int resultInt = statement.executeUpdate(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            if (resultInt == 0)
            { 
                //error
                return false;
            }
            
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }

            return false;
        }
                
        return true;
    }
    
    public static ArrayList<Object> executeSqlForTwoArrayLists(String sql)
    {
       
        // dweclare 2 arraylists
        
        ArrayList<String> resultNameList = new ArrayList<>();
        
        ArrayList<Boolean> resultActiveList = new ArrayList<>();
        
        /// populate these 2 arraylists
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                resultNameList.add(resultSet.getString("result"));
                
                resultActiveList.add(resultSet.getBoolean("boolean"));
                
                // no need to do display_order, as this will be calculated by 'ORDER BY'
                
            }
            
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList<Object> containsResults = new ArrayList<>();
        
        containsResults.add(resultNameList);
        
        containsResults.add(resultActiveList);
        
        return containsResults;
                
    }
    
    public ArrayList<String> executeSqlForArrayList(String sql)
    {
        ArrayList<String> resultList = new ArrayList<>();
        
        
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                resultList.add(resultSet.getString("result"));
            }
            
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return resultList;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
