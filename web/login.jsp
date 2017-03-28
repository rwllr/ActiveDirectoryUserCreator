<%--
  Created by IntelliJ IDEA.
  User: Raphael
  Date: 27/03/2017
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="POST" action="j_security_check">
    <input type="text" name="j_username"> <--Username <br/>
    <input type="password" name="j_password"> <-- Password <br/>
    <input type="submit" name="Submit">
</form>
</body>
</html>
