package dbms.newYear;

import java.io.IOException;
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

@WebServlet(name = "NewYearServlet", 
        urlPatterns = {"/NewYearServlet"})

public class NewYearServlet extends HttpServlet 
{
    
    private ArrayList<Integer> yearList;
    private int activeYear;
    private int newestYear;
    private int selectedYear;
    private int oldListSize;
    
    private String textToDisplay;
    private Boolean proceed = false;
    private Boolean match = false;
      
    
    
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
        
        System.out.println("We've reached the NewYearServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
            
        // get elements from previous page
            
            textToDisplay = null;
                        
            //about to try to parse the user input int
            try {
                
                // must see if selectedYear will transform into int
                
                    String startYearString = request.getParameter("startYear"); 
                    selectedYear = Integer.parseInt(startYearString);
                    
                    System.out.println("selected year = " + selectedYear);
                    
            
            String newestYearString = request.getParameter("newest_year");            
                        newestYear = Integer.parseInt(newestYearString);

                        String activeYearString = request.getParameter("active_year");          
                        activeYear = Integer.parseInt(activeYearString);

                        String oldListSizeString = request.getParameter("listsize");
                        oldListSize = Integer.parseInt(oldListSizeString);
            
            yearList = new ArrayList<>();

                        for (int i = 0; i < oldListSize; i++)
                        {

                        // get each array list attributes from jsp page

                            String listName = "list" + i;

                            String listValueString = request.getParameter(listName);
                            int listValue = Integer.parseInt(listValueString);

                            yearList.add(listValue);

                            // we must measure user's earlier year against db's year plus 1 
                            // (i.e. as for '2013-2014' academic eyar, user inputs '2013' and db has '2014' stored)
                            int listValueMinusOne = listValue - 1;
                            
                            if (listValueMinusOne == selectedYear) 
                            {
                                //there was A match
                                match = true; //helps stop us proceeding later
                                
                                textToDisplay = "Error: academic year " 
                                    + listValueMinusOne + "-" + listValue 
                                    + " already exists in the database. Please try again or select that academic year to edit.";

                                System.out.println("match! " + textToDisplay);
                                
                            }
                            
                            System.out.println("listValue element (" + i + ") = " + listValue + "<<");

                        }
                        
                        System.out.println("been through the for loop and match status is: " + match.toString());
                    
                    
                    // penultimate check    
                        
                        int anIntIsTooBigForSelectedYear = newestYear + 2;
                        
                    if (selectedYear > 2000 && selectedYear < anIntIsTooBigForSelectedYear)
                    {
                        //final check
                        
                        System.out.println("selected yaer is in good range 2000-3000!");

                            if (match == false)
                            {
                            //all criteria satisfied

                            proceed = true;

                            }
                            else
                            {
                               
                                proceed = false;
                                
                            }

                    }
                    else
                    {
                        // selected value is not in suitbale range
                        
                        if (selectedYear < 2000)
                        {
                        textToDisplay = "Error: selected number is not in a suitable range. Try picking a year in this century!";
                        }
                        else
                        {
                        textToDisplay = "Error: selected number is not in a suitable range. "
                                + "Please bear in mind that " + anIntIsTooBigForSelectedYear 
                                + " or higher will not be accepted!";
                            
                        }
                        proceed = false;
                    }
                
            } catch (NumberFormatException e)
            {
                // was not an int                
                textToDisplay = "Error: user input was not recognised as a number. Please try again!";
                                
            }
                        
            //////////////////////
                        
            if (textToDisplay != null)
            {
            System.out.println("texttodisplay = " + textToDisplay);
            }
            else
            {
            System.out.println("status now of proceed is: " + proceed.toString());
            }
                        
            ///////////////////////
                                   
            
            if (proceed == true)
            {
                // take to the term dates page
                          
                // attributes set for next page
            
                JSONObject infoForNewYear = new JSONObject();
                
                int selectedYearPlusOne = selectedYear + 1;
                
                String selectedYearDisplay = selectedYear + "-" + selectedYearPlusOne;
                
                infoForNewYear.put("display_year", selectedYearDisplay);
                
                // ie db year
                infoForNewYear.put("selected_db_year", selectedYearPlusOne);
                
                DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoForNewYear);
                
                request.setAttribute("displayInfoBean", displayInfoBean);                
                
                System.out.println("success!" + "\n year list = " + yearList.toString() 
                        + "\n selected year display = " + selectedYearDisplay);

            // Move to next page

                request.getRequestDispatcher("dbms/newYear/chooseTermDates.jsp").forward(request, response); //temp change should be displayArray
                
            }
            else
            {
                // take back to choosenewyear.jsp
               
                // attributes set for next page

                DisplayInfoBean displayInfoBean = new DisplayInfoBean(yearList, activeYear, newestYear, textToDisplay);

                request.setAttribute("displayInfoBean", displayInfoBean);
                
                System.out.println("failure!" + "\n" + yearList.toString() + "\n" + activeYear + "\n" + newestYear + "\n" + textToDisplay);

                // Move to next page

                request.getRequestDispatcher("dbms/newYear/chooseNewYear.jsp").forward(request, response); //temp change should be displayArray
                
            }   
            
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
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
            Logger.getLogger(NewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(NewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
