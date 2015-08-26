<%-- 
    Document   : welcome
    Created on : 26-Jun-2015, 11:18:24
    Author     : cut14jhu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <div>
           
            <form name="myform" action="${pageContext.request.contextPath}/StartDisplayListServlet" method="GET">
              
                <p>
                    <input type="submit" name="btn1" value="activeyear">
                </p>
                
                <p>
                    <input type="submit" name="btn2" value="chooseanotheryear">
                </p>
                
                <p>
                    <input type="submit" name="btn3" value="select all records for all years">
                </p>
                
              
            </form>
            
        </div>
                       
    </body>
</html>
