<%--
  Created by IntelliJ IDEA.
  User: Raphael
  Date: 16/02/2017
  Time: 22:21
  To change this template use File | Settings | File Templates.
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fn" %>
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <title>Servlet Hello World</title>
  <style>.error { color: red; } .success { color: green; }</style>
</head>
<body>
<h1>${authstatus}</h1>
<form action="/inputDetails.do" method="post">
    <h1>Hello</h1>
    <p>
        <label for="number">What's your number?</label>
        <input id="number" name="number" value="${param.number}">
        <span class="error">${messages.authstatus}</span>
    </p>
    <p>
        <label for="mail">What's your email?</label>
        <input id="mail" name="mailaddress" value="${param.mail}">
        <span class="error">${messages.authstatus}</span>
    </p>
        <input type="submit">
        <span class="success">${messages.success}</span>
    </p>
</form>


<%--
<form action="hello" method="post">
  <h1>Hello</h1>
  <p>
    <label for="fieldname">What's your fieldname?</label>
    <input id="fieldname" fieldname="fieldname" value="${fn:escapeXml(param.fieldname)}">
    <span class="error">${messages.fieldname}</span>
  </p>
  <p>
    <label for="age">What's your age?</label>
    <input id="age" fieldname="age" value="${fn:escapeXml(param.age)}">
    <span class="error">${messages.age}</span>
  </p>
  <p>
    <input type="submit">
    <span class="success">${messages.success}</span>
  </p>
</form>
--%>
</body>
</html>