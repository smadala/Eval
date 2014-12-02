<%--
  Created by IntelliJ IDEA.
  User: satya
  Date: 29/11/14
  Time: 10:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
  <title>TestCase Page</title>
  <style type="text/css">
    .tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
    .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
    .tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
    .tg .tg-4eph{background-color:#f9f9f9}
  </style>
</head>
<body>
<h1>
  Add/Edit a TestCase
</h1>

<c:url var="addAction" value="/testcase/add" ></c:url>

<form:form action="${addAction}" commandName="testCase">
  <table>
   <%-- <c:if test="${!empty testCase.name}">
      <tr>
        <td>
          <form:label path="id">
            <spring:message text="ID"/>
          </form:label>
        </td>
        <td>
          <form:input path="id" readonly="true" size="8"  disabled="true" />
          <form:hidden path="id" />
        </td>
      </tr>
    </c:if>--%>
    <tr>
      <td>
        <form:label path="name">
          <spring:message text="Name"/>
        </form:label>
      </td>
      <td>
        <form:input path="name" />
      </td>
    </tr>

    <tr>
      <td>
        <form:label path="url">
          <spring:message text="URL"/>
        </form:label>
      </td>
      <td>
        <form:input path="url" />
      </td>
    </tr>


    <tr>
      <td>
        <form:label path="method">
          <spring:message text="Method"/>
        </form:label>
      </td>
      <td>
        <form:input path="method" />
      </td>
    </tr>
    <tr>
      <td>
        <form:label path="numUsers">
          <spring:message text="Num of Users"/>
        </form:label>
      </td>
      <td>
        <form:input path="numUsers" />
      </td>
    </tr>
    <tr>
      <td>
        <form:label path="numRequest">
          <spring:message text="Num of Requests per User"/>
        </form:label>
      </td>
      <td>
        <form:input path="numRequest" />
      </td>
    </tr>
    <tr>
      <td>
        <form:label path="rampUpTime">
          <spring:message text="Ramp Up Time"/>
        </form:label>
      </td>
      <td>
        <form:input path="rampUpTime" />
      </td>
    </tr>
    <tr>
     <tr>
       <td>
         <form:label path="numMachines">
           <spring:message text="Num Of Instances"/>
         </form:label>
       </td>
       <td>
         <form:input path="numMachines" />
       </td>
     </tr>
     <tr>

      <td colspan="2">
        <c:if test="${!empty teseCase.name}">
          <input type="submit"
                 value="<spring:message text="Edit TestCase"/>" />
        </c:if>
        <c:if test="${empty testCase.name}">
          <input type="submit"
                 value="<spring:message text="Add TestCase"/>" />
        </c:if>
      </td>
    </tr>
  </table>
</form:form>
<br>
<h3>TestCases List</h3>
<c:if test="${!empty listTestCases}">
  <table class="tg">
    <tr>
      <th width="50">ID</th>
      <th width="90"> Name </th>
      <th width="120"> URL </th>
      <th width="60">Method</th>
      <th width="80">#Users</th>
      <th width="80">#Request</th>
      <th width="80">RUT</th>
      <th width="80">#Instances</th>

      <th width="50">Edit</th>
      <th width="50">Delete</th>
      <th width="50">Run</th>
      <th width="50">Reports</th>

    </tr>
    <c:forEach items="${listTestCases}" var="testCase">
      <tr>
        <td>${testCase.id}</td>
        <td>${testCase.name}</td>
        <td>${testCase.url}</td>
        <td>${testCase.method}</td>

        <td>${testCase.numUsers}</td>
        <td>${testCase.numRequest}</td>
        <td>${testCase.rampUpTime}</td>
        <td>${testCase.numMachines}</td>

        <td><a href="<c:url value='/edit/${testCase.id}' />" >Edit</a></td>
        <td><a href="<c:url value='/remove/${testCase.id}' />" >Delete</a></td>
        <td><a href="<c:url value='/run/${testCase.id}' />" >Run</a></td>
        <td><a href="<c:url value='/testcase/${testCase.id}/reports' />" > Reports </a></td>

      </tr>
    </c:forEach>
  </table>
</c:if>
</body>
</html>

