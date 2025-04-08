
<%@ page import="model.Prevision" %>
<%@ page import="model.Depense" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<% 
Object from_servlet = request.getAttribute("all_prevision");
List<Prevision>  all_prevision = new ArrayList((List<Prevision>)from_servlet);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emp from</title>
</head>
<body>
    <h1>Formulaire d'insertion Depense</h1>
    <form action="depense_form" method="post">
    <p>
            <label for="idPrevision">Prevision: </label>
            <select name="idPrevision" id="idPrevision"  >
            <% 
                for (Prevision prev :all_prevision){ %>
                    <option value="<%= prev.getId() %>"  ><%= prev.getLibelle() %></option>
            <% }%>
            </select>
      </p>
      <p> 
        <label for="montant">Montant: </label>
        <input type="number" name="montant"  id="montant" placeholder="Ex: 30000"  required>
      </p>
        <input type="submit" name="" id="" value="validez" >
    </form>
    <a href="make_prevision">retour</a>

     
</body>
</html>