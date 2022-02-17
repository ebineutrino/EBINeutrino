<%@ page import="org.sdk.EBISystem" %>

<html>
  <head>
    <title>Meine erste JSP</title>
  </head>
  <body>
    <h1>Meine erste JSP</h1>
    <%
      out.println(EBISystem.getInstance().builder().textField("companyText","Summary").getText());
      
      // beliebiger Java-Code
      out.println( "Hallo" );
    %>
    <%= request.getRemoteHost() %>
    ...
  </body>
</html>
