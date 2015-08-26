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
            This can be replaced by directly interacting with Doaa's native iOS app.
        </div>
                
        <div>
            
            <form action="${pageContext.request.contextPath}/RequestRecordServlet" method="get">
            
                1) Enter child's name (or type in 'any' for all):
                
                <input type="text" name="child_name" />
                
                <br />

                
               
                                
                
                2) Enter an academic year (or type in 'any' for all):
                
                <input type="text" name="academic_year" />
                
                <br />
                
                
                3) Select a term (or 'any' for all):
                                
                <select name="selectbox">
                    
                    <option value='any'>Any</option>
                    <option value='winter'>Winter</option>
                    <option value='spring'>Spring</option>
                    <option value='summer'>Summer</option>                    
                
                </select>
                    
                <br />              
                              
                <input type="submit" />
            
            </form>
        
        </div>
        
    </body>
</html>
