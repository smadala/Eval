<%--
  Created by IntelliJ IDEA.
  User: satya
  Date: 29/11/14
  Time: 10:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
  <title>Report Page</title>
  <style type="text/css">
    .tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
    .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
    .tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
    .tg .tg-4eph{background-color:#f9f9f9}
  </style>
</head>
<body>
<h1>
  Reports
</h1>


<c:if test="${!empty listReports}">
  <table class="tg">
    <tr>
      <th width="80">ID</th>

      <th width="100">Status</th>
      <th width="100">Samples</th>
      <th width="100">Min</th>
      <th width="100">Max</th>
      <th width="100">Average</th>



      <th width="60">Delete</th>
    </tr>
    <c:forEach items="${listReports}" var="report">
      <tr>
        <td>${report.id}</td>
        <td>${report.status}</td>
        <td>${report.samples}</td>
        <td>${report.min}</td>
        <td>${report.max}</td>
        <td>${report.average}</td>

        <td><a href="<c:url value='/testcase/${testCaseId}/report/remove/${report.id}' />" >Delete</a></td>
      </tr>
    </c:forEach>
  </table>
</c:if>
</body>
</html>

