
<%@ page import="model.Etat" %>
<%@ page import="model.Prevision" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<% 
Object from_servlet = request.getAttribute("all_etat");
List<Etat>  all_etat = new ArrayList((List<Etat>)from_servlet);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emp from</title>
</head>
<body>
    <h1>L'etat des previsions</h1>
    <table border="1">
        <tr>
            <th>Prevision</th>
            <th>Depense</th>
            <th>Reste</th>
        </tr>
        <%  for (Etat etat :all_etat){
            Prevision temp_prev = new Prevision();
            temp_prev = (Prevision)temp_prev.getById(etat.getIdPrevision());
             %>
        <tr>
            <td><%=  temp_prev.getLibelle() %></td>
            <td><%=  etat.getDepense() %></td>
            <td><%=  etat.getReste() %></td>
        </tr>
            <% }%>
    </table>
</body>
</html>