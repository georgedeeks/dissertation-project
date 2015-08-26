<%-- 
    Document   : latestservlettest
    Created on : 25-Jun-2015, 14:59:35
    Author     : cut14jhu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Makeshift index page</title>
    </head>
    <body>
            
        <form action="${pageContext.request.contextPath}/StartSiteServlet">
            
            <p>
               
                <input type="submit" value="Enter site" />

            </p>
        
        </form>
                
    </body>
</html>
