package dbms.activeYear;

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

@WebServlet(name = "ActiveNewEntityServlet", 
        urlPatterns = {"/ActiveNewEntityServlet"})

public class ActiveNewEntityServlet extends HttpServlet 
{
    
    private String objectType;
    private String successMessage;
    private String errorMessage;
    private String className;
    private String studentName;
    private String newEntity;
    private String sql;   
    private Statement statement;
    private int resultInt;
 
    
    private ArrayList<String> classNameList;
    
    
    
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
        
        System.out.println("We've reached the ActiveNewEntityServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
            objectType = request.getParameter("object_type");
            
            successMessage = new String();
            errorMessage = new String();
                        
            newEntity = request.getParameter("new_entity");
            
            if (newEntity.equals("") == false)
            {
                
                statement = DatabaseConnectionSingleton.getStatementInstance();
                                
                if (objectType.equals("Class"))
                {

                    className = newEntity;
                    
                    String sqlToInsertClassroom = "INSERT INTO classroom (class_name) VALUES ('"
                           + className
                           + "');";
                    
                    executeDBInteraction(sqlToInsertClassroom);
                
                    if (resultInt != 0)
                            {
                                successMessage += "Successful insert of " + objectType + " '" + newEntity + "'. ";
                            }
                            else
                            {
                                errorMessage += "Problems with inserting " + objectType + " '" + newEntity + "'. ";
                            }
                            
                    if (resultInt == 1)
                    {
                        
                        if (request.getParameter("new_entity_cbox") != null)
                        {
                            
                            String sqlToInsertIntoClassYear = "INSERT INTO class_year "
                                    + "(class_id, academic_year) "
                                    + "SELECT classroom.class_id, academic_year.academic_year "
                                    + "FROM classroom, academic_year "
                                    + "WHERE '"
                                    + className
                                    + "' = classroom.class_name "
                                    + "AND academic_year.active = true"
                                    + ";"
                                    ;
                            
                            executeDBInteraction(sqlToInsertIntoClassYear);
                            
                            if (resultInt != 0)
                            {
                                successMessage += "And successful insert of " + objectType + " '" + newEntity + "' into current academic year. ";
                            }
                            else
                            {
                                errorMessage += "However, problems with inserting " + objectType + " '" + newEntity + "' into current academic year. ";
                            }
                        
                        }
                        else
                        {
                            //dont proceed as error
                        }
                        
                    }
                    

                }
                else
                {
                    
                    //get all class names
                    
                    String classListSizeString = (String) request.getParameter("classListSize");
                                                            
                    int classListSize = Integer.parseInt(classListSizeString);
                    
                    classNameList = new ArrayList();
                
                    for (int i = 0; i < classListSize; i++)
                    {

                        String key = "class_name_" + i;

                        String classNameToExtract = request.getParameter(key);

                        classNameList.add(classNameToExtract);

                    }
                    // get selectbox option

                    className = request.getParameter("selectbox");                

                    studentName =  newEntity;

                    String sqlToInsertStudent = "INSERT INTO student (s_name) VALUES ('"
                           + studentName
                           + "');";
                   
                    executeDBInteraction(sqlToInsertStudent);
                     
                    if (resultInt != 0)
                            {
                                successMessage += "Successful insert of " + objectType + " '" + newEntity + "'. ";
                            }
                            else
                            {
                                errorMessage += "Problems with inserting " + objectType + " '" + newEntity + "'. ";
                            }
                   
                    if (resultInt == 0)
                    {
                       //do not proceed as already error
                    }
                    else
                    {
                        
                        if (className.equals("Unassigned"))
                        {
                            //don't add
                            
                            successMessage += "Student not added to any class, as was 'Unassigned'. ";
                            
                        }
                        else
                        {
                            // add to student in class year
                            
                            String sqlForStudentInClassYear = "INSERT INTO "
                                + "student_in_class_year (s_no, class_id, academic_year) "
                                + "SELECT student.s_no, class_year.class_id, class_year.academic_year "
                                + "FROM student, class_year, classroom, academic_year "
                                + "WHERE student.s_name = '"
                                    + studentName
                                + "' "
                                + "AND classroom.class_id = class_year.class_id "
                                + "AND classroom.class_name = '"
                                    + className
                                + "' "
                                + "AND class_year.academic_year = academic_year.academic_year "
                                + "AND academic_year.active = true"
                                + ";"
                                ;
                     
                            executeDBInteraction(sqlForStudentInClassYear);
                             
                            if (resultInt != 0)
                            {
                                successMessage += "Successful insert of " + objectType + " '" + newEntity + "' into current academic year. ";
                            }
                            else
                            {
                                errorMessage += "However, problems with inserting " + objectType + " '" + newEntity + "' into current academic year. ";
                            }
                            
                        }
                        
                    }
                   
                }

                
            }
            else
            {
                // empty field so error message
                
                errorMessage = "No new entity entered. Please type something in!";
                
            }
            
        // send back to page
            
            if (errorMessage.equals("") && request.getParameter("normal") != null)
            {
                // no issues with update and user wishes to move to udpate_result
                                
                request.setAttribute("update_result", successMessage);
            
                request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
            
                
            }
            else
            {
                //send back
                
                request.setAttribute("object_type", objectType);
            
                if (objectType.equals("Student"))
                {
                    request.setAttribute("class_list", classNameList);                    
                }
                
                System.out.println("errorMessage to send: " + errorMessage);
                System.out.println("successMessage to send: " + successMessage);
                
                request.setAttribute("error_message", errorMessage);
                request.setAttribute("success_message", successMessage);
                
                request.getRequestDispatcher("dbms/activeYear/activeNewEntity.jsp").forward(request, response);
                
            }            
            
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } catch (SQLException ex) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(ActiveNewEntityServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(ActiveNewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
            
        }        

    }
    
    public void executeDBInteraction(String externSql)
    {
        
        sql = externSql;
        
        resultInt = 0;
        
        try 
                {

                    resultInt = statement.executeUpdate(sql);

                } 
                catch (SQLException ex) 
                {

                    Logger.getLogger(ActiveNewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);

                    resultInt = 0;

                }
       
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
            Logger.getLogger(ActiveNewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ActiveNewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
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
