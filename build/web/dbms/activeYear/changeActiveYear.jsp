<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 


<!DOCTYPE html>

<html>
    <head>
        <title>Change the active year</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <!--    
   <link rel="stylesheet" href="style.css"> 
    --> 
    </head>
    <body>
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
		
        Pick a new active year (the current active year is highlighted):
                
        <form name="myform" action="${pageContext.request.contextPath}/ChangeActiveYearServlet" method="GET">
            
            <%
                ArrayList<String> array = displayInfoBean.getListToDisplay();
                  
                int activeYear = displayInfoBean.getActiveYear();
             
                for(int i=0; i<array.size(); i++)
                {

                    int minusOne = Integer.parseInt(array.get(i)) - 1;
                              
                    if 
                    (
                        //it's not the active year
                        activeYear != Integer.parseInt(array.get(i))
                    )
                    {
               
                        //normal print out                           
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
                          
                out.print("<input type='hidden' id='listsize' name='listsize' value='" 
                          + array.size() + "'>");
                               
                %>
                
        </form> 
            
        <p>
                
            <a href="${pageContext.request.contextPath}/dbms/launch/startservers.jsp">Back to main menu</a>
                
        </p>
                
    </body>
    
</html>