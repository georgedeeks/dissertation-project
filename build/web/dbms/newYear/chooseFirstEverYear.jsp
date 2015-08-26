<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 

<!DOCTYPE html>

<html>
    <head>
        <title>Enter new year</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <!--    
   <link rel="stylesheet" href="style.css"> 
    --> 
    
    </head>
    <body>
                
        <div>
            
            Enter the year (i.e. 4 digits) that you would like the new academic year to start in (e.g., for 2015-2016, please type in "2015"): 
            
            <form name='myform' action="${pageContext.request.contextPath}/FirstEverYearServlet" method="GET"> 
                
                <input type="number" id="startYear" name="startYear"  max= "3000" min= "2000" value='2015' required/> 
                
                <input type="submit"/>
                
            </form>     
            
                    <%

                try
                {
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage == null || errorMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:red'> " + errorMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. An error was found trying to detect one.");
                }                  

        %>
            
        </div>
        
    </body>
</html>

