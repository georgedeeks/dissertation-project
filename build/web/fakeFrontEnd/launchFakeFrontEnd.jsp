<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Request record (fake front end)</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>
            The aim of this page is to launch one of the three webpages that act as stubs for
            what we can expect the front end of this application to provide.
        </div>
                
        <div>
            
            <a href="${pageContext.request.contextPath}/fakeFrontEnd/uploadRecordForm.jsp">1. Upload record form</a>
            <a href="${pageContext.request.contextPath}/fakeFrontEnd/checkForUpdatesOrLaunchingForm.jsp">2. Check for updates/Launching button</a>
            <a href="${pageContext.request.contextPath}/fakeFrontEnd/requestRecordForm.jsp">3. Search/Request Records form</a>
                   
        </div>
            
        <%
            
            try
                {
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage == null || errorMessage.equals(""))
                    {
                        //do nothing
                    }
                    else
                    {

                        out.print("<div style='color:red'> " + errorMessage + " </div>");                    
                        
                    }
                }
            catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. "
                                 + "Error in determining what to display.");
                } 
        
       
       %>
       
    </body>
</html>
