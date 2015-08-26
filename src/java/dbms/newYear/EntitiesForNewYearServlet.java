package dbms.newYear;

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
import dbms.DisplayInfoBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "EntitiesForNewYearServlet", 
        urlPatterns = {"/EntitiesForNewYearServlet"})

public class EntitiesForNewYearServlet extends HttpServlet 
{
    
    private ArrayList<String> oldList;
    
    // 'firedList' i.e. list of classnames (as a result of lokoing at checkbozxes that WERE ticked)
    // OR a list of student names to include
    private ArrayList<String> selectedList; 
    
   // list of class names corresponding to the selectedList
    private ArrayList<String> selectedForThisClassList;
    
    private ArrayList<String> newListToDisplay;
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.json.JSONException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException 
    {
        
        System.out.println("We've reached the EntitiesForNewYearServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            oldList = new ArrayList<>();
            selectedList = new ArrayList<>();
            selectedForThisClassList = new ArrayList<>();
            newListToDisplay = new ArrayList<>();
            
       
    // get info from webpage
            
        // uinpack JSON to get at values
            
            String previousInfoString = request.getParameter("json_object");
            
            JSONObject previousInfo = new JSONObject(previousInfoString);
            
        // get old list values
            
            String oldListSizeString = request.getParameter("listsize");
            int oldListSize = Integer.parseInt(oldListSizeString);
            
            
            //this upcoming line might fall over as int is converted to String
            int selectedDBYear = (int) previousInfo.get("selected_db_year");
            
            for (int i = 0; i < oldListSize; i++)
            {
           
            // get each array list attributes from (initial sent to) jsp page
                
                String listParameterName = "list" + i;
                
                String listParameterValue = request.getParameter(listParameterName);
                                
                oldList.add(listParameterValue);
               
            }
            
        // we need to see what object type is now, to see how to process the information and which page to next send it to
            // 6 possibilites:
            // student or class
                // active or archived or new (for each)
            
            
        // get object type
            
            String objectType = request.getParameter("object_type");
            
        // get active status
            
            String activeStatus = previousInfo.getString("active_status");
        
        /////////////////////////////  
        // if object type = class
            if (objectType.equals("Class")) // i.e., just checkboxes
                    {
                        for (int i = 0; i < oldListSize; i++)
                        {

                            String buttonName = "item" + i;

                            if (request.getParameter(buttonName) != null)
                            {
                                selectedList.add(oldList.get(i));
                            }
                        }
                
                    // send to classes archived or classes new
                        
                        if (activeStatus.equals("archived") == false)
                        {
                            // i.e., 'currently active' == true

                            // send to pickArray with 'archived status' as strategy
                            
                                previousInfo.put("active_status", "archived");

                            // maintain the same objectType
                                
                                //
                                
                            // store selected list and selected list size in JSON
                                
                                // (NB no previous info to add to classNameSize / classNameList)
                                int classNameSize = selectedList.size();

                                previousInfo.put("classname_size", classNameSize);

                                for (int i = 0; i < selectedList.size(); i++)
                                {

                                    String key = "classname_" + i;

                                    previousInfo.put(key, selectedList.get(i));

                                }

                                System.out.println("JSONObject to send is: \n" + previousInfo);       

                            // generate new display list (i.e., archived class names)
                                
                                String sql = "SELECT classroom.class_name AS result "
                                    + "FROM classroom "
                                    + "EXCEPT SELECT classroom.class_name "
                                    + "FROM classroom, class_year, academic_year "
                                    + "WHERE classroom.class_id = class_year.class_id "
                                    + "AND class_year.academic_year = academic_year.academic_year "
                                    + "AND academic_year.active = TRUE;";
                                
                                newListToDisplay = findArchivedNames(sql);
                        }
                        else
                        {
                            // if archived strategy, then a bit more complex to add recently selected list
                            
                                // store selected list and selected list size in JSON

                                    int oldClassNameListSize = previousInfo.getInt("classname_size");

                                    for (int i = 0; i < selectedList.size(); i++)
                                    {

                                        int currentIPlusPrevious = i + oldClassNameListSize;

                                        String key = "classname_" + currentIPlusPrevious;

                                        previousInfo.put(key, selectedList.get(i));

                                    }

                                    int newCombinedClassNameListSize = oldClassNameListSize + selectedList.size();

                                    previousInfo.put("classname_size", newCombinedClassNameListSize);
                            
                            // ensure that newList is null for upcmoign if statment
                                    
                               newListToDisplay = new ArrayList<>();
                        
                        }
                        
                        // send to pickarray or newentity logic is after 'else if objecttype- student' clause                       
                        
            /////////////////////////////  
        // if object type = student
                        
            }
            else // object type = student
            {
                
            // get students and their classes
                
                // unpack select options
                    // IF NOT defuault option,  THEN associate with corresponding oldList and add (to create0 selectedList
                
                // NOTE before unpacking
                            // we must determine where to start our studentinclass key from
                            // either from 0 or from the size of the existing list
                            // we determine this if there already records (ie strategy is archived)
                
                int nextKeyIntAndStudentInClassArraySize;
                
                if (activeStatus.equals("archived") == false)
                {
                       // start from 0 / arraysize is 0
                               
                        nextKeyIntAndStudentInClassArraySize = 0;
                                
                }
                else
                {
                        // key int will start from size of studentinclass array
                            
                       try
                       {
                           nextKeyIntAndStudentInClassArraySize = previousInfo.getInt("student_in_class_size");
                       }
                       catch (JSONException e)
                       {
                           previousInfo.put("student_in_class_size", 0);
                           nextKeyIntAndStudentInClassArraySize = 0;
                       }
                                
                }
                      
                for (int i = 0; i < oldListSize; i++)
                {
                    
                    String selectBoxName = "item" + i;
                    
                    String selectedClassName = (String)request.getParameter(selectBoxName);
                    
                    if (selectedClassName.equals("Unassigned"))
                    {
                        //do nothing
                    }
                    else
                    {
                        //new strategy means we can add values in without worrying about previous values
                            
                            // 1 create string 'key' that has studentinclass_i
                            // 2 create string 'value' that has studentName + # + className  
                            // 3 store this key-value pair in a JSONArray

                            String key = "student_in_class_" + nextKeyIntAndStudentInClassArraySize;
                            
                            // increase key int for next time
                            nextKeyIntAndStudentInClassArraySize++;

                            String studentName = oldList.get(i);
                            String value = studentName + "#"+ selectedClassName;

                            previousInfo.put(key, value);
                    }
                                             
                } // end of for loop
                
                previousInfo.put("student_in_class_size", nextKeyIntAndStudentInClassArraySize) ;
                
                // see where to send based on strategy                
                // send to classes archived or classes new
                  
                                  ////////////////////////////////////////////////////////start of edite
                        
                if (activeStatus.equals("archived") == false)
                        
                {
                            // i.e., 'currently active' == true

                            // send to pickArray with 'archived status' as strategy
                            
                                
                    previousInfo.put("active_status", "archived");

                            // maintain the same objectType
                               
                    System.out.println("JSONObject to send is: \n" + previousInfo);       

                            // generate new display list (i.e., archived class names)
                            // if empty, will have to go to new entity
                                
                                String sql = "SELECT student.s_name AS result "
                                        + "FROM student "
                                        + "EXCEPT "
                                        + "SELECT student.s_name "
                                        + "FROM student, student_in_class_year, academic_year "
                                        + "WHERE student.s_no = student_in_class_year.s_no "
                                        + "AND student_in_class_year.academic_year = academic_year.academic_year "
                                        + "AND academic_year.active = TRUE;";
                    
                                newListToDisplay = findArchivedNames(sql);
                       
                }                        
                else                       
                {
                    // cannot populate new List as need to go to new entity
                     
                    newListToDisplay = new ArrayList<>();
                        
                }                        
                    
            } // end of if objectType = student logic           
            
        // 2 options now:
        // #1: send to pick array with populated archived students
                        // (if archived strategy and a populated list of archived students)
        // #2: send to new class entity                      
                        
            if (newListToDisplay.size() > 0)                       
            {
               
            //option #1    send to pick array with archived student names       

                            // load attributes for next page (pick array)

                DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo, newListToDisplay);

                request.setAttribute("displayInfoBean", displayInfoBean);

                            // send off to next page (pick array)

                request.getRequestDispatcher("dbms/newYear/pickArray.jsp").forward(request, response);

            }                      
            else                       
            {                           

            // option #2 send to new class entity
                // strategy is effectively 'archived', 
                //but next time we have to unpack strategy it should be currently active
                // so let's change it now

                previousInfo.put("active_status", "currently active");

            // send to new attribute page

                DisplayInfoBean displayInfoBean = new DisplayInfoBean(previousInfo);

                request.setAttribute("displayInfoBean", displayInfoBean);

                request.setAttribute("strategy", "new year");

                request.getRequestDispatcher("dbms/newYear/newEntity.jsp").forward(request, response);

            } 

        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            request.setAttribute("error_message", "Problem with interacting with database: \n" + e 
                    + "\n If unable to resolve, please contact your system adminstrator.");
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }        

    }
    
    
    public ArrayList<String> findArchivedNames(String sql)
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
            Logger.getLogger(EntitiesForNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EntitiesForNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EntitiesForNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
