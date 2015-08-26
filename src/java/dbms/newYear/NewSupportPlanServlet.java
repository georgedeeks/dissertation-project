package dbms.newYear;

import dbms.DisplayInfoBean;
import database.DatabaseConnectionSingleton;
import java.io.IOException;
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

@WebServlet(name = "NewSupportPlanServlet", 
        urlPatterns = {"/NewSupportPlanServlet"})

public class NewSupportPlanServlet extends HttpServlet 
{
    
    private ArrayList<String> externNameList;
    private ArrayList<Integer> externChangedOrderList;
    private ArrayList<Boolean> externActiveList;
    private ArrayList<Boolean> externDeleteList;
    
    private String newEntityName;
    
    private ArrayList<String> deletedNameList;
    private ArrayList<String> passiveNameList;
    private ArrayList<String> notDeletedOrPassiveNameList;    //
    private ArrayList<Integer> notDeletedOrPassiveChangedOrderList;
    
    private ArrayList<Integer> finalOrderList;
    
    private ArrayList<String> revisedWithNewOrderFinalNameList;
    
    private String errorMessage;
    private String successMessage;
    
    private String sql;
    private Statement statement;
    
    private ArrayList<String> displayNameList;
    private ArrayList<Boolean> displayBooleanList;
    
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
        
        System.out.println("We've reached the NewSupportPlanServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
        // initialise varaibles
            externActiveList = new ArrayList<>();
            externDeleteList = new ArrayList<>();
            deletedNameList = new ArrayList<>();
            passiveNameList = new ArrayList<>();
            notDeletedOrPassiveNameList = new ArrayList<>();
            notDeletedOrPassiveChangedOrderList = new ArrayList<>();
            externNameList = new ArrayList<>();
            externChangedOrderList = new ArrayList<>();
            displayBooleanList = new ArrayList<>();
            errorMessage = new String();
            
        // get info from webpage
            
            // strategy
            
            String strategy = request.getParameter("strategy");
            
            //System.out.println("strategy = " + strategy);
                /*
                do later - figure out strategy string based on whether new or active entity
                if new entity, get JSON info
                if not, different way to process
                atm, just new entity processing
                */
            
            String objectType;
            
            //if (strategy.equals("new year"))
            //{
                
                // JSONObject

                String jsonString = request.getParameter("json_object");

                JSONObject previousInfo = new JSONObject(jsonString);

                //System.out.println("JSON print out: \n" + previousInfo.toString());
            
            
            
            
            // get object type                
             objectType = previousInfo.getString("object_type");
            //}
            //else
            //{
                //objectType = request.getParameter("object_type");
            //}
            
                //ensure that target and level have lowercase option for database entry
                String support_plan_objectType = "support_plan_" + objectType.toLowerCase();
                                           
            // get initial lists
            
            String listSizeString = request.getParameter("list_size");
            
            System.out.println("listsizestring: " + listSizeString);
            
            int listSize = Integer.parseInt(listSizeString);
            
            // NB if exisitng list of targets/levels are empty, then we sip right over this! of course...
            
            for (int i = 0; i < listSize; i++)
            {
                // for externName
                
                String externNameParameterKey = "item" + i;
                
                String externName = request.getParameter(externNameParameterKey);
                
                externNameList.add(externName);
                
                // for externOrder
                
                String externOrderParameterKey = "order" + i;
                
                String externOrderString = request.getParameter(externOrderParameterKey);
                int externOrder = Integer.parseInt(externOrderString);
                
                externChangedOrderList.add(externOrder);
                
                // for externDeleteList
                
                String externDeleteParameterCBoxKey = "cbox_delete_" + i;
                
                if (request.getParameter(externDeleteParameterCBoxKey) != null)
                {
                    // then checkbox has been ticked
                    // so we must add this name into the delete list
                    
                    externDeleteList.add(true);
                    
                }
                else
                {
                    externDeleteList.add(false);
                }
                                   
                // for externActiveList
                
                String externActiveParameterCBoxKey = "cbox_active_" + i;
                
                if (request.getParameter(externActiveParameterCBoxKey) != null)
                {
                    // then checkbox has been ticked
                    // so we must add this name into the delete list
                    
                    externActiveList.add(true);
                    
                }
                else
                {
                    externActiveList.add(false);
                }
                
            }
            
        // step 2 - see if new field can be inserted
            
            newEntityName = request.getParameter("new_entity");
            
            if (newEntityName == null || newEntityName.equals(""))
            {
                // error?
                
                // no need to update errorMessage as probably intended to leave blank
                
            }
            else
            {
                // check there is not a stupid character
                
                    // remove \\s - ' from string to test                
                String newEntityNameMinusAcceptableSpecialCharacters = 
                        newEntityName.replaceAll(" ", "").replaceAll("-", "").replaceAll("'", "");
                    
                        //test it                
                if (newEntityNameMinusAcceptableSpecialCharacters.matches("[a-zA-Z]+") == false)
                {
                    errorMessage = "name contains special character. please try again";
                }
                else
                {
                    // continue - all is good pre-db
                    
                    //try to add to database
                    
                    try 
                    {
   
                    // get fields
                        
                        // got name
                        
                        // get active/passive
                        
                            Boolean activeStatusForNewEntity;
                        
                            if (request.getParameter("new_cbox_active") != null)
                            {
                               // active
                                
                                activeStatusForNewEntity = true;
                                
                            }
                            else
                            {
                                //passive
                                
                                 activeStatusForNewEntity = false;
                            }
                        
                        // get assigned order number
                            
                            // the NEW element is the 'Nth + 1' element
                                // (where n = listSize.size, ie the previuos elements)
                            String keyForNewEntityOrder = "order" + listSize;
                        
                            String newEntityOrderString = request.getParameter(keyForNewEntityOrder);
                        
                            int newEntityOrder = Integer.parseInt(newEntityOrderString);                           
                            
                            
                    // add to database
           
                               

                        sql =  "INSERT INTO " + support_plan_objectType + " "
                                        + "(" + support_plan_objectType + ", active) VALUES ('"
                                        + newEntityName
                                        + "', true);";
                        
                        statement = DatabaseConnectionSingleton.getStatementInstance();
                        
                        int resultInt = statement.executeUpdate(sql);
                        
                        if (resultInt != 1)
                        {
                            databaseError("inserting");
                        }
                        else
                        {
                           // successful insert into database!
                            
                            DatabaseConnectionSingleton.closeConnection();
                        }                        
                        
                    // add to rest of list
                        
                        // set name
                        
                            externNameList.add(newEntityName);
                        
                        // set active/passive
                        
                            externActiveList.add(activeStatusForNewEntity);
                        
                        // set order
                        
                            externChangedOrderList.add(newEntityOrder);
                        
                        // set delete
                        
                            externDeleteList.add(false);

                        // set new list size int
                        
                            listSize++;
                            
                            // award one + for getting this far successsfully!
                            successMessage = "+";
                        
                    }
                    catch(SQLException | ServletException e)
                    {
                      
                        databaseError("inserting");
                        
                        // don't add to rest of list
                    }
                    
                    
                }
            }
            
            
            
            
            
                    
                    
        // step 3 - separate names list into 'deleted', 'passive' and 'other' (ie 3) lists
            
            //if (listSize < 2) //ie was empty before we added in the new element
           // {
                // we must initialise here
                
                //externDeleteList = new ArrayList<>();
                //externNameList = new ArrayList<>();
                //externChangedOrderList = new ArrayList<>();
          //  }
                        
                    
            for (int i = 0; i < listSize; i++)
            {
                String currentName = externNameList.get(i);
                int currentChangedPosition = externChangedOrderList.get(i);
                
                if (externDeleteList.get(i) == true)
                {
                    deletedNameList.add(currentName);
                }
                else if (externActiveList.get(i) == false)
                {
                    passiveNameList.add(currentName);
                } 
                else
                {
                    notDeletedOrPassiveNameList.add(currentName);
                    notDeletedOrPassiveChangedOrderList.add(currentChangedPosition);
                }
            }
            
            
             
        // step 4 - if deletedlist has any entries, then remove them from database
            
            if (deletedNameList.isEmpty() == false)
            {
                
                //then we need to delete some entries

                statement = DatabaseConnectionSingleton.getStatementInstance();
                                
                for (int i = 0; i < deletedNameList.size(); i++)
                {
                    String nameToDelete = deletedNameList.get(i);
                    
                    sql = "DELETE FROM support_plan_" + objectType.toLowerCase() 
                            + " WHERE support_plan_" + objectType.toLowerCase() 
                            + " = '"
                            + nameToDelete
                            + "';";
                    
                    int resultInt = statement.executeUpdate(sql);
                    
                    if (resultInt != 1)
                    {
                         databaseError("deleting");
                    }
                     
                }
                
                
                DatabaseConnectionSingleton.closeConnection();
                
                
            }
            
        // i've tested this 
        // step 5 - now process notDeletedOrPassiveChangedOrderList to get revisedWithNewOrderFinalNameList
            
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
                        
            // System.out.println("after change words: " + notDeletedOrPassiveNameList + notDeletedOrPassiveChangedOrderList);
            
            // effectively rename the re-ordered list for clarity
            revisedWithNewOrderFinalNameList = notDeletedOrPassiveNameList ;
            
        // step 6 - update database for active and passive sp names
            try {
                
                statement = DatabaseConnectionSingleton.getStatementInstance();
            
            // active
                
                for (int i = 0; i < revisedWithNewOrderFinalNameList.size(); i++)            
                {
                    
                    int displayOrder = i + 1;
                    
                // remove previous order
                    
                    String sqlPrecursor = "update " + support_plan_objectType 
                            + " set display_order = NULL where display_order = 1;";
                    
                    int resultIntPrecursor; 
                    resultIntPrecursor = statement.executeUpdate(sqlPrecursor);
                    
                // update the new order
                    
                    sql = "update " + support_plan_objectType 
                            + " set display_order = " + displayOrder 
                            + ", active = TRUE where " + support_plan_objectType + " = '"
                            + revisedWithNewOrderFinalNameList.get(i)
                            + "';";                
               
                    int resultInt; 
                    resultInt = statement.executeUpdate(sql);
                    
                    if (resultInt == 0)
                    {
                        databaseError("inserting");
                        
                        errorMessage += " Problem with record " + revisedWithNewOrderFinalNameList.get(i) + ".";
                    }
                    
                }
                
            // passive
                
                for (int i = 0; i < passiveNameList.size(); i++)            
                {
                    
                    sql = "update " + support_plan_objectType 
                            + " set display_order = NULL, active = FALSE where " + support_plan_objectType + " = '"
                            + passiveNameList.get(i)
                            + "';";                
               
                    int resultInt; 
                    resultInt = statement.executeUpdate(sql);
                    
                    if (resultInt == 0)
                    {
                        databaseError("inserting");
                        
                        errorMessage += " Problem with record " + passiveNameList.get(i) + ".";
                    }
                    
                }
                
                DatabaseConnectionSingleton.closeConnection();
                
                // success message should now be '++'
                successMessage += "+";
                
            } catch (SQLException ex) {
            
                    Logger.getLogger(NewSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
                    
                    System.out.println("error with db: " + ex);
                    
                    databaseError("inserting");
                    
            }
            
        // decision tree for where to send user next
            
            // attributes regardless of decision tree
            
                request.setAttribute("strategy", strategy);
            
            // option 3a: same page as error
            // option 3b: same page as user wishes to add another
            
                // only difference is success v error message
            
            // option 2a: same page but with levels instead
            // option 1a: confirmation page
            
            // get option 3a/b
            
            Boolean sendToSamePage = true;
            Boolean errorMessageBoolean = true;
            
            if (successMessage.equals("++") && request.getParameter("new_cbox_another") != null)
            {
                sendToSamePage = true;
                errorMessageBoolean = false;
            }
            else if (errorMessage.equals("") || errorMessage == null)
            {
                
                sendToSamePage = false;
                
            }
            else
            {                
                sendToSamePage = true;
                errorMessageBoolean = true;
            }
           
            
            //////////////////////////////
            
            if (sendToSamePage == true)
            {
                
                String errorOrSuccessKey = "error_message";
                String errorOrSuccessMessage = "";
                
                if (errorMessageBoolean == true)
                {
                    //error page - 3a
                
                    errorOrSuccessKey = "error_message";
                    errorOrSuccessMessage = errorMessage;
                
                }
                else
                {
                    // anoter page - 3b
                
                    errorOrSuccessKey = "success_message";
                    
                    if (successMessage.equals("++"))
                    {
                        successMessage = "Updates successfully applied!";
                    }
                    else
                    {
                        errorOrSuccessMessage = successMessage;
                    }
                }
                
            // load up all variables for 3a or 3b
                
                request.setAttribute(errorOrSuccessKey, errorOrSuccessMessage);
                
            // load up other variables (regardless of error or success-another page)
                
                //object type is the same
                
            // load up name and boolean lists
                
                // add active first
            
                displayNameList = revisedWithNewOrderFinalNameList;

                for (int i = 0; i < displayNameList.size(); i++)
                {
                    displayBooleanList.add(true);
                }

                // now add passive

                for (int i = 0; i < passiveNameList.size(); i++)            
                {

                    displayNameList.add(passiveNameList.get(i));
                    displayBooleanList.add(false);

                } 
                
            // make dib
                
                DisplayInfoBean displayInfoBean = new DisplayInfoBean(displayNameList, displayBooleanList, previousInfo);
                
                request.setAttribute("displayInfoBean", displayInfoBean);
                
                // i'm explicitly putting it here for error control
                request.setAttribute("strategy", strategy);
                //request.setAttribute("strategy", "new year");
                
                request.getRequestDispatcher("dbms/newYear/newSupportPlan.jsp").forward(request, response);
                                
            }
            else // instruction is to move onto a different page
            {
                
                if (strategy.equals("new year") != true)
                {
                    
                    
                    // active year - so send to confrim page
                    
                    request.setAttribute("update_result", successMessage);
                    
                    request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
                 
                }
                else
                {

                    if (objectType.equals("Target"))
                    {
                    //switch to level - option 2

                        //change object type

                            previousInfo.put("object_type", "Level");

                        // strategy done

                        // list of names and booleans to display

                            sql = "SELECT support_plan_level AS result, active AS boolean "
                                        + "FROM support_plan_level ORDER BY active DESC, display_order ASC;"; 

                            ArrayList<Object> containsResults = NewEntityServlet.executeSqlForTwoArrayLists(sql);

                            ArrayList<String> resultNameList = (ArrayList<String>) containsResults.get(0);

                            ArrayList<Boolean> resultActiveList = (ArrayList<Boolean>) containsResults.get(1);

                            DisplayInfoBean displayInfoBean = new DisplayInfoBean(resultNameList, resultActiveList, previousInfo);

                            request.setAttribute("displayInfoBean", displayInfoBean);

                        // no error or success message - but we'll put a success message in anyway ( //todo remove??)

                            request.setAttribute("success_message", "Support Plan Targets successfully confirmed in database");

                            request.getRequestDispatcher("dbms/newYear/newSupportPlan.jsp").forward(request, response);

                    }
                    else
                    {
                    // confirm details - option 1

                        // insert all remaining stuff (i.e. students in class) into database and send user to confirmation page

                        try{
                            
                            //ensure errorMessage is blank
                            
                            errorMessage = "";

                            statement = DatabaseConnectionSingleton.getStatementInstance();
                            
                    // ENSURE ALL TABLES ARE DONE FOR NEW YEAR NOW
                            // not fully completed earlier to ensure data redundancy issues are minimised
                            
                            
                            
                        // classroom insert - already done
                        // academic year insert - already done
                        
                        
                            
                        // update active year (i.e. tis notw active)
                            
                            int dbYear = (int) previousInfo.get("selected_db_year");
                            
                            String sqlForActive = "update academic_year set active = TRUE where academic_year = "
                                    + dbYear
                                    + ";"
                                    ;
                            
                            int resultIntForActive = statement.executeUpdate(sqlForActive);

                                //for testing
                                if (resultIntForActive == 1)
                                {
                                    System.out.println("successful update of acdemic eyar");
                                }
                                else
                                {
                                    //errorMessage = "";
                                    System.out.println("FAILED insert of new academic year as active");
                                }
                            
                        // term dates - already done
                        // insert classroom - laready done
                                
                        // insert class year        
                                
                            int classSizelist = (int) previousInfo.get("classname_size");

                            for (int i = 0; i < classSizelist; i++)
                            {
                                String key = "classname_" + i;

                                String className = previousInfo.getString(key);
                                
                                sql = "INSERT INTO class_year (class_id, academic_year) "
                                        + "SELECT classroom.class_id, "
                                        + dbYear //dbYear
                                        + " FROM classroom WHERE '"
                                        + className //className
                                        + "' = classroom.class_name;";

                                int resultInt = statement.executeUpdate(sql);

                                //for testing
                                if (resultInt == 1)
                                {
                                    System.out.println("successful insert of " + className + " into class_year " + dbYear);
                                }
                                else
                                {
                                    System.out.println("FAILED insert of " + className + " into class_year " + dbYear);
                                }

                            }
                                
                        // isnert student =- already done        
                                
                        // update student_in_class_year (i.e. insert)
                                
                            int studentInClassListSize = (int) previousInfo.get("student_in_class_size");

                            for (int i = 0; i < studentInClassListSize; i++)
                            {
                                String key = "student_in_class_" + i;

                                String value = previousInfo.getString(key);

                                String[] splittedStringArray = value.split("#");

                                String studentName = splittedStringArray[0];

                                String className = splittedStringArray[1];

                                sql = "INSERT INTO student_in_class_year (s_no, class_id, academic_year) "
                                        + "SELECT student.s_no, class_year.class_id, class_year.academic_year "
                                        + "FROM student, class_year, classroom "
                                        + "WHERE student.s_name = '"
                                        + studentName //student name
                                        + "' AND classroom.class_id = class_year.class_id "
                                        + "AND classroom.class_name = '"
                                        + className // classname
                                        + "' AND class_year.academic_year = "
                                        + dbYear // dbYear
                                        + ";";

                                int resultInt = statement.executeUpdate(sql);

                                //for testing
                                if (resultInt == 1)
                                {
                                    System.out.println("successful insert of " + value);
                                }
                                else
                                {
                                    System.out.println("FAILED insert of " + value);
                                }

                            }
                            
                        // spt - arleady done
                        // sp level - already done
                        // evidence = to be done later
                        // last updated - autogenerated
                            
                        // finally - update active year
                            
                            sql = "SELECT change_active_year("
                                    + dbYear
                                    + ");"
                                    ;
                            
                            statement.executeQuery(sql);                            

                            DatabaseConnectionSingleton.closeConnection();                        

                            successMessage = "Congratulations! Entities have been added to the database!";
                            
                            request.setAttribute("update_result", successMessage);

                        // all we need to do is send a success message!

                        request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);


                        } catch (SQLException e)
                        {

                            System.out.println(e);
                            databaseError("inserting");

                            errorMessage += " Problem entering students into new classes for the year; "
                                    + "please review or contact your system administrator.";

                            request.setAttribute("error_message", errorMessage);


                            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);

                        }

                    }
                
                }
                
            }
            
        } catch (NumberFormatException | ServletException | JSONException e)
        {
            
            System.out.println("error G : " + e);
            
            errorMessage = "Error with server trying to understand request. Please try again or contact your system administrator.";
            
            request.setAttribute("error_message", errorMessage);
            
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(NewSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }   
    

    public void databaseError(String insertOrRetrieve)
    {
          
        try {
            DatabaseConnectionSingleton.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(NewSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
        errorMessage = "Error " + insertOrRetrieve + " entry with database.";
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
            Logger.getLogger(NewSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(NewSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
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
