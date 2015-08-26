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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "StartNewActiveEntityServlet", 
        urlPatterns = {"/StartNewActiveEntityServlet"})

public class StartNewActiveEntityServlet extends HttpServlet 
{
    
    private String objectType;
    
    private String errorMessage;
    private String successMessage;
    
    private ArrayList<String> classList;    
    private ArrayList<String> studentList;
        
    private Statement statement;
    private ResultSet resultSet;    
    private String sql;
    
    
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
        
        System.out.println("We've reached the StartNewActiveEntityServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
            errorMessage = new String();
            
            // look at what was clicked off off from the main menu
            if (request.getParameter("Class") != null)
            {                
                objectType = "Class";                
            }
            else
            {                
                objectType = "Student";                
            }  
            
            sql = "SELECT distinct classroom.class_name AS result "
                    + "FROM classroom;";
                        
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            resultSet = statement.executeQuery(sql);
            
            classList = new ArrayList<>();
            
            while (resultSet.next())
            {
                
                String classNameToAdd = resultSet.getString("result");
                
                classList.add(classNameToAdd);
                
            }
            
            if (objectType.equals("Student"))
            {
                
                String sqlForStudent = "SELECT DISTINCT student.s_name AS result FROM student;";
                
                resultSet = statement.executeQuery(sqlForStudent);
            
                studentList = new ArrayList<>();

                while (resultSet.next())
                {

                    String studentNameToAdd = resultSet.getString("result");

                    studentList.add(studentNameToAdd);

                }
                
            }
            
            DatabaseConnectionSingleton.closeConnection();
            
            if (classList.size() < 1)
            {
                
                errorMessage += "No Classes found in database. ";
                
                if (objectType.equals("Student"))
                {
                    errorMessage += "Please use new year form. ";
                     
                }
                
            }
            
            if (objectType.equals("Student"))
            {
                if (studentList.size() < 1)
                {
                errorMessage += "No Students found to display. ";
                }
            }

            //load variables for newentity.jsp    
            
            request.setAttribute("object_type", objectType);
            request.setAttribute("error_message", errorMessage);
            request.setAttribute("error_message", successMessage);

            if (objectType.equals("Student"))
            {
                request.setAttribute("class_list", classList);                   
            }    
            
            request.getRequestDispatcher("dbms/activeYear/activeNewEntity.jsp").forward(request, response);
                                        
        }
        catch(ServletException | IOException e) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(StartNewActiveEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);            
            
            request.setAttribute("error_message", "Servlet/IO Exception encountered");
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } 
        catch (SQLException ex) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(StartNewActiveEntityServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(StartNewActiveEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
                                    
            request.setAttribute("error_message", "Error connecting to database for entries relating to " + objectType + ".");
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
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
            Logger.getLogger(StartNewActiveEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartNewActiveEntityServlet.class.getName()).log(Level.SEVERE, null, ex);
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
