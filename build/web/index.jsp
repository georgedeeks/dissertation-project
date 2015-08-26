<html>
    <head>
        <title>error</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- 
<link rel="stylesheet" href="style.css">
-->

</head>
<body>         
    
        <h1>Hello there!</h1>
        
        <form action="${pageContext.request.contextPath}/StartSiteServlet">
            
            <p>
               
                <input type="submit" value="Launch main menu" />

            </p>
        
        </form>
        
        
        <form action="${pageContext.request.contextPath}/fakeFrontEnd/launchFakeFrontEnd.jsp">
            
            <p>
               
                <input type="submit" value="Launch fake front end" />

            </p>
        
        </form>
             
</body>
</html>