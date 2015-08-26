<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 


<!DOCTYPE html>

<html>
    <head>
        <title>Please select for upcoming year</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <!--    
   <link rel="stylesheet" href="style.css"> 
    --> 
    </head>
    <body>
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
	
        Pick a <jsp:getProperty name="displayInfoBean" property="objectType" />
        
                
                <% if (displayInfoBean.getObjectType().equals("Year"))
                 {
                     out.print("(the current active year is highlighted)");
                 }
                out.print(":");
                %>
        
            
            <form name="myform" action="${pageContext.request.contextPath}/DisplayListServlet" method="GET">
                
                
                <%
                    
                 
                    
                  ArrayList<String> array = displayInfoBean.getListToDisplay();
                  
                  // to do get active year in bold to display here!
                  // to do get 'change active year' as a possibility
                  
                  
                  if (displayInfoBean.getObjectType().equals("Year"))
                 {
                            
                            int activeYear = displayInfoBean.getPreviousInfo().getInt("active_academic_year");
                        
                            for(int i=0; i<array.size(); i++)
                            {

                              int minusOne = Integer.parseInt(array.get(i)) - 1;
                              
                              if 
                                      (
                                        //it's not the active year
                                        activeYear != Integer.parseInt(array.get(i))
               
                                      )
                                {
                                    //normal print out    ]
                                    //TODO
                                        //put this in CSS file
                                    out.print("<input type='submit' name='btn" + i + "' value='" + minusOne + "-" + array.get(i) + "'>");
                                        
                                }
                                else
                                {
                                    //special print out
                                    out.print("<input type='submit' name='btn" + i + "' style='color:blue;font-weight:bold' value='" + minusOne + "-" + array.get(i) + "'>");
                                        
                                }
                                        
                                        
                              out.print("<input type='hidden' id='list" 
                                      + i + "' name='list" + i 
                                      + "' value='" + displayInfoBean.getListToDisplay().get(i) +  "'>");

                            }
                 }
                  else if (displayInfoBean.getObjectType().equals("Term"))
                 {
                     
                     String[] termNames = new String[]{"Winter", "Spring", "Summer"};
                     
                     for(int i=0; i<array.size(); i++)
                            {
                                
                                if (array.get(i).equals("true"))
                                        {
                                            
                                            out.print("<input type='submit' name='btn" + i + "' value='" + termNames[i] + "'>");

                                            out.print("<input type='hidden' id='list" 
                                                    + i + "' name='list" + i 
                                                    + "' value='" + termNames[i] +  "'>");
                                        }

                            }
                 }
                  else
                  {
                      for(int i=0; i<array.size(); i++)
                            {

                              out.print("<p><input type='submit' name='btn" 
                                      + i + "' value='" 
                                      + displayInfoBean.getListToDisplay().get(i) + "'></p>");

                              out.print("<input type='hidden' id='list" 
                                      + i + "' name='list" + i 
                                      + "' value='" + displayInfoBean.getListToDisplay().get(i) +  "'>");

                            }
                  }
                  
                  out.print("<input type='hidden' id='objecttype' name='objecttype' value='" 
                          + displayInfoBean.getObjectType() + "'>");
                  
                  
                  
                  out.print("<input type='hidden' id='listsize' name='listsize' value='" 
                          + array.size() + "'>");
                  
                  out.print("<input type='hidden' id='previousinfo' name='previousinfo' value='" 
                          + displayInfoBean.getPreviousInfo().toString() + "'>");
                  
                  
                %>
            </form> 
            
            <p>
                
            <a href="${pageContext.request.contextPath}/dbms/launch/startMainMenu.jsp">Back to main menu</a>
                
        </p>
            
            
    </body>
</html>

