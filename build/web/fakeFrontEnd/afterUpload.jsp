<%-- 
    Document   : afterupload
    Created on : 25-Jun-2015, 14:06:26
    Author     : cut14jhu
--%>

<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>This is the result page for fake front end</h1>
                        
        <%
            
           
                    JSONObject returnJsonToDisplay = new JSONObject();
                    
                    try
                    {
                    
                    
                        returnJsonToDisplay = (JSONObject) request.getAttribute("returnJsonToDisplay");
                    
                                        
                        if (returnJsonToDisplay == null || returnJsonToDisplay.toString().equals(""))
                        {

                        }
                        else
                        {

                            out.print("<div style='color:green'> " + returnJsonToDisplay.toString() + " </div>");          

                        }
                    }
                    catch (Exception e)
                    {
                             out.print("Error occurred. No return JSONObject to display.");
                             
                             String nothingToDisplay = "[Nothing to display]";
                             
                             returnJsonToDisplay.put("file_name", nothingToDisplay);
                             returnJsonToDisplay.put("URL", nothingToDisplay);
                    }
                                    
                
            
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
                         out.print("Error messages will be printed out here. None to display.");
                }   
                
                try
                {
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage == null || successMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:blue'> " + successMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success messages will be printed out here. None to display.");
                }   
            
            
            
        %>
        
         <form action="${pageContext.request.contextPath}/fakeFrontEnd/launchFakeFrontEnd.jsp" method="get">
                                                          
                <input type=submit value='Return to fake front end main menu'>
        
        
    </body>
</html>
