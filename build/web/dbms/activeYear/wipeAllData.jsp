<%-- 
    Document   : wipealldata
    Created on : 21-Jul-2015, 14:19:28
    Author     : cut14jhu

    COULD DO: pop up window to ultra confirm?

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
        
        Are you really sure you want to wipe all data? 
        
        This will remove all elements from the database and the entire system.
        
        You will have undermined my entire dissertation project.
        
        <form name="myform" action="${pageContext.request.contextPath}/WipeAllDataServlet" method="GET">
        
            <input type="submit" value="All delete" />
        
        </form>
        
        <p>
                
            <a href="${pageContext.request.contextPath}/dbms/launchSite/startMainMenu.jsp">Back to main menu</a>
                
        </p>
        
    </body>
</html>
