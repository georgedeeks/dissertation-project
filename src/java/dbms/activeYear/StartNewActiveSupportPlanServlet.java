package dbms.activeYear;

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

@WebServlet(name = "StartNewActiveSupportPlanServlet", 
        urlPatterns = {"/StartNewActiveSupportPlanServlet"})

public class StartNewActiveSupportPlanServlet extends HttpServlet 
{
    
    private ArrayList<String> oldList;
    
    private String errorMessage;
    
     private String successMessage;
    
    private ArrayList<String> displayList;
    
    private String sql;
    
    // 'firedList' i.e. list of classnames (as a result of lokoing at checkbozxes that WERE ticked)
    // OR a list of student names to include
    private ArrayList<String> selectedList; 
    
   // list of class names corresponding to the selectedList
    private ArrayList<String> selectedForThisClassList;
    
    private String formattedObjectTypeForSql;
    
    private String objectType;
    
    
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
        
        System.out.println("We've reached the StartNewActiveSupportPlanServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
        // figure out which link/button was fired to determine this object type

            // Target or Level
            
            if (request.getParameter("target") != null)
            {
                objectType = "Target";
            }
            else
            {
                objectType = "Level";
            }
            
            formattedObjectTypeForSql = objectType.toLowerCase();
                                
        //load variables for newsupportplan.jsp 
            
            JSONObject previousInfo = new JSONObject();
            
            previousInfo.put("object_type", objectType);
                    
            sql = "SELECT support_plan_" + formattedObjectTypeForSql 
                    + " AS result, active AS boolean "
                    + "FROM support_plan_" + formattedObjectTypeForSql 
                    + " ORDER BY active DESC, display_order ASC;";

            ArrayList<Object> containsResults = executeSqlForTwoArrayLists(sql);

            ArrayList<String> resultNameList = (ArrayList<String>) containsResults.get(0);

            ArrayList<Boolean> resultActiveList = (ArrayList<Boolean>) containsResults.get(1);

            DisplayInfoBean displayInfoBean = new DisplayInfoBean(resultNameList, resultActiveList, previousInfo);

            request.setAttribute("displayInfoBean", displayInfoBean);
            
            request.setAttribute("strategy", "active year");
            
            request.getRequestDispatcher("dbms/newYear/newSupportPlan.jsp").forward(request, response);
                
        }
        catch(ServletException | IOException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }        

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
            Logger.getLogger(StartNewActiveSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList<Object> containsResults = new ArrayList<>();
        
        containsResults.add(resultNameList);
        
        containsResults.add(resultActiveList);
        
        return containsResults;
                
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
            Logger.getLogger(StartNewActiveSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartNewActiveSupportPlanServlet.class.getName()).log(Level.SEVERE, null, ex);
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
