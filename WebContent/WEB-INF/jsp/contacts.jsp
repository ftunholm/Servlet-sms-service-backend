<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

	<script src="https://code.jquery.com/jquery-2.1.4.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css" type="text/css">
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/drawer.js"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1">

<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">


<title>MessMe.se - Kontakter</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<%
String[] contacts = (String[]) request.getAttribute("contactList");
%>

<script type="text/javascript">

var jscontacts = [];

<% for (int j = 0; j < contacts.length; j++) { %>
jscontacts[<%= j %>] = <%= contacts[j] %>; 
<% } %>

$(function() {
	appendContacts();
});

function appendContacts() {
	//Append all the items from the array
	$.each(jscontacts, function() {
		$("#contact-list-contacts").append("<li class='card contact' id='list_" + this.contactId + "'><b>" + this.firstName + " "
				+ this.lastName + "</b><p>" + this.phoneNumber +
				"</p><span class='remove_contact_symbol' id='contact_" + this.contactId + "'></span></li>");
	});

	//If any li-item is pressed this will be called
	$("#contact-list-contacts").delegate("span", "click", function (e) {
		//Get the id with substring 
		var contact_id = $(this).attr("id").substring(8, $(this).attr("id").length);
		var list_id = $(this).attr("id").substring(8, $(this).attr("id").length);
		
		//Remove the contact from the array
		for(var i = 0; i < jscontacts.length; i++) {
			if(jscontacts[i].contactId == contact_id) {
				jscontacts.splice(i, 1);
			}
		}
		
		$("#list_" + list_id).remove();
		$("#contact_" + contact_id).remove();
		
		$("#contact-list-contacts").empty();
		appendContacts();
		
		//Ajax post-request
		$.ajax({
			  type: "POST",
			  url: "${pageContext.request.contextPath}/removecontact",
			  data: {"contact_id" : contact_id},
			  success: function(){
				  //location.reload();
			  }
			});
	});
}


	

</script>

<div id="container">
<div class="row">
<div class="col-md-4">
    <ul id="contact-list-contacts" style="margin-top: 0;"></ul>
</div>
    <div class="col-md-8">

    <div id="add-contact-form" class="card">
			<p><b>LÃ¤gg till ny kontakt</b></p>
			<br/>

			<form method="post" action="${pageContext.request.contextPath}/addcontact">
			
				<label for="firstName">*Förnamn:</label><br/>
				<input name="firstName" required="required" placeholder="Förnamn" title="Förnamn saknas"/><br/><br/>
				
				<label for="lastName">Efternamn:</label><br/>
				<input name="lastName" placeholder="Efternamn"/><br/><br/>
				
				<label for="phoneNumber">*Mobilnummer:</label><br/>
				<input name="phoneNumber" required="required" placeholder="Mobilnummer" title="Mobilnummer saknas"/><br/><br/>
				
				<label for="email">E-mail:</label><br/>
				<input name="email" placeholder="E-postadress"/><br/><br/> 
				
				<input type="Submit" value="Lägg till" name="add_contact"/>
				
			</form>
			
		</div></div>

</div>
    </div>
</body>
</html>