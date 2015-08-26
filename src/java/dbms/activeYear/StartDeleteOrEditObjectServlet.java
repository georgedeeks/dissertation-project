package dbms.activeYear;

import database.DatabaseConnectionSingleton;
import dbms.newYear.NewEntityServlet;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "StartDeleteOrEditObjectServlet", 
        urlPatterns = {"/StartDeleteOrEditObjectServlet"})

public class StartDeleteOrEditObjectServlet extends HttpServlet 
{
    
    private String objectType;
    
    private ArrayList<String> newListToDisplay;
    
   private ArrayList<String> classNamesArray;
   private ArrayList<String> whichClassforStudent;
   private ArrayList<Boolean> activeList;
    
    private String sql;
    private Statement statement;
    private ResultSet resultSet;
    
    private String errorMessage;
        
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
            throws ServletException, IOException             
    {
        
        System.out.println("We've reached the StartDeleteOrEditObjectServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
                 
            // get object type from which button clicked
            
            if (request.getParameter("active_students") != null)
            {
                objectType = "Current Students";
            }
            else if (request.getParameter("archived_students") != null)
            {
                objectType = "Archived Students";
            }
            else if (request.getParameter("active_classes") != null)
            {
                objectType = "Current Classes";
            }
            else if (request.getParameter("archived_classes") != null)
            {
                objectType = "Archived Classes";
            }
            else
            {
                System.out.println("error with objectType");
            }
            
            
             
            switch (objectType) {

                
                // case Levels = send to newsupportplan
                // case Targets = send to newsupportplan
                // case Records = send to view records
                    
                case "Current Students":

                    // we will deal with this later
                                        
                    break;

                case "Archived Students":

                    sql = "SELECT student.s_name AS result "
                            + "FROM student "
                            + "EXCEPT SELECT student.s_name "
                            + "FROM student, student_in_class_year, academic_year "
                            + "WHERE student.s_no = student_in_class_year.s_no "
                            + "AND student_in_class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE;";
                    

                    break;

                case "Current Classes":

                    sql = "SELECT classroom.class_name AS result "
                            + "FROM classroom, class_year, academic_year "
                            + "WHERE classroom.class_id = class_year.class_id "
                            + "AND class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE;";
                    

                    break;

                case "Archived Classes":

                    sql = "SELECT classroom.class_name AS result "
                            + "FROM classroom "
                            + "EXCEPT SELECT classroom.class_name "
                            + "FROM classroom, class_year, academic_year "
                            + "WHERE classroom.class_id = class_year.class_id "
                            + "AND class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE;";
                                        
                    break;

                default:
                    
                    //THERE HAS BEEN AN ERROR
                    
                    errorMessage = "Error matching up object type string!";
                    
                    System.out.println(errorMessage);
                    
                    break;
          
            }
            
            if (objectType.equals("Current Students"))
            {
                                
            // also, before formulating sql for display list, get classNamesArray & whichClassPosition;
                
                String sqlForStudentsPlusClassesPlusActive = "SELECT student.s_name AS student, "
                        + "classroom.class_name AS classroom, student_in_class_year.active AS active "
                        + "FROM student, classroom, student_in_class_year, academic_year "
                        + "WHERE student.s_no =  student_in_class_year.s_no "
                        + "AND classroom.class_id = student_in_class_year.class_id "
                        + "AND academic_year.academic_year = student_in_class_year.academic_year "
                        + "AND academic_year.active = TRUE"
                        + ";"
                        ;
                
                // this method loads up:
                    //newListToDisplay
                    //whichClassforStudent
                executeSqlForStudentPlusClassesPlusActiveList(sqlForStudentsPlusClassesPlusActive);
                
                // get separate list of distinct class names 'classNamesArray'
                
                classNamesArray = new ArrayList();
                
                for (int i = 0; i < whichClassforStudent.size(); i++)
                {
                    String toTest = whichClassforStudent.get(i);
                    
                    if (classNamesArray.isEmpty())
                    {
                        // add the first name as it's bound to be distinct!!
                        
                        classNamesArray.add(toTest);
                    }
                    else
                    {
                        
                        Boolean match = false;
                        
                        for (int j = 0; j < classNamesArray.size(); j++)
                        {
                            
                            if (whichClassforStudent.get(i).equals(classNamesArray.get(j)))
                            {
                                match = true;
                            }
                                                                            
                        }
                        
                        if (match == false)
                        {
                            classNamesArray.add(toTest);
                        }
                        
                    }
                                        
                }

                // load attributes unique to 
                
                System.out.println("classNamesArray = " + classNamesArray.toString());
                
                 request.setAttribute("currentClassesList", classNamesArray);
                 
                 request.setAttribute("activeList", activeList);
  
                 request.setAttribute("whichClassForStudent", whichClassforStudent);
                
                
            }
            else
            {
                 
                newListToDisplay = findList(sql);
                 
            }
            
            // load geeneral attributes for next page
            
            System.out.println("objectType = " + objectType);
            request.setAttribute("object_type", objectType);
            
                        
            request.setAttribute("newListToDisplay", newListToDisplay);
            
            // send to next page
            
            
            request.getRequestDispatcher("/dbms/activeYear/deleteEditObject.jsp").forward(request, response);
            
        } catch (ServletException e)
        {
            System.out.println("error: " + e);
            
            if (errorMessage == null)
            {
                errorMessage = "Unable to delete selected database entries. "
                    + "Please try again or contact your system administrator.";
            }
            
            request.setAttribute("error_message", errorMessage);
            
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        }
    
    }
    
     public ArrayList<String> findList(String sql)
    {
        
        ArrayList<String> resultList = new ArrayList<>();
        
        try {
            
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                resultList.add(resultSet.getString("result"));
            }
                        
            
        } catch (SQLException | ServletException ex) {
            Logger.getLogger(StartDeleteOrEditObjectServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultList;
    }
     
     
    public void executeSqlForStudentPlusClassesPlusActiveList(String sql)
    {
       
        // reset 2 arraylists
        
        newListToDisplay = new ArrayList<>();
        
        whichClassforStudent = new ArrayList<>();
        
        activeList = new ArrayList<>();
        
        /// populate these 2 arraylists
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                newListToDisplay.add(resultSet.getString("student"));
                
                whichClassforStudent.add(resultSet.getString("classroom"));
                
                activeList.add(resultSet.getBoolean("active"));
                
                // no need to do display_order, as this will be calculated by 'ORDER BY'
                
            }            
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NewEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            errorMessage += "Problem locating students and their associated classes. "
                    + "Please try again or contact your system administrator.";
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
            processRequest(request, response);
        
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
            processRequest(request, response);
        
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
