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
            The aim of this page is to provide a stub for the 'front-end' of the project: 
            specifically here, requesting particular records. 
            This can be replaced by directly interacting with the native iOS app.
        </div>
                
        <div>
            
            <form action="${pageContext.request.contextPath}/CheckForUpdatesServlet" method="get">
                                                          
                <input type=submit name="check" value="Check against database for lastest updates">
                
                <input type=submit name="launching" value="Initial launching button">
            
            </form>
        
        </div>
        
    </body>
</html>
