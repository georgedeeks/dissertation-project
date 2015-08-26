<%@page import="org.json.JSONObject"%>
<%@page import="java.util.UUID"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Upload record (fake front-end)</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>
            The aim of this page is to provide a stub for the 'front-end' of the project: 
            specifically, this page is for uploading a fake record.
            In time this can be replaced by directly interacting with the native iOS app.            
        </div>
                
        <div>
            
            <form action="${pageContext.request.contextPath}/InsertURLServlet" method="GET">

                
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
                                    
                %>
                              
                1) File name to submit:
                
                <input type="text" name="file_name" 
                       value="<%
                                    
                                    out.print(returnJsonToDisplay.get("file_name"));
                                  
                                    
                                %>" />
                
                <br />
                
                2) URL path
                
                <input type="text" name="url" value="<%
                                    
                                      out.print(returnJsonToDisplay.get("URL"));
                                      
                                %>" />
                
                <br />
                                             
                <input type="submit" />
            
            </form>
        
        </div>      
        
    </body>
</html>
