<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="https://code.jquery.com/jquery-2.1.4.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css" type="text/css">
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/drawer.js"></script>

	<%
		String[] groups = (String[]) request.getAttribute("groups");
		String[] contacts = (String[]) request.getAttribute("contacts");
	%>
	<script type="text/javascript">

		var jsgroups = [];
		var jscontacts = [];

		<% for (int i = 0; i < groups.length; i++) { %>
		jsgroups[<%= i %>] = <%= groups[i] %>;
		<% } %>


		<% for (int j = 0; j < contacts.length; j++) { %>
		jscontacts[<%= j %>] = <%= contacts[j] %>;
		<% } %>

		$(document).ready(function() {

			//Append all the contacts from the array
			$.each(jscontacts, function() {
				$("#contact-list").append("<li class='contact card'><b>" + this.firstName + " "
						+ this.lastName + "</b><p>" + this.phoneNumber +
						"</p><input type='checkbox' id='contact_" + this.contactId + "'/></li>");
			});


			$("#sendBtn").click(function () {
				preparePost();
			});

			$("#group-dropdown-message").change(function () {
				if($("#group-dropdown-message option:selected").attr("id") != "default") {
					$("#contact-list").hide();
					appendGroupContacts();
				}
				else {
					$("#contact-list").show();
					$("#group-contacts-list").hide();
				}
			});
		});

		function appendGroupContacts() {
			var group_name_selected = $("#group-dropdown-message option:selected").val();
			var group_id;

			$("#group-contacts-list").empty();

			for(var i = 0; i < jsgroups.length; i++) {
				if(jsgroups[i].groupName == group_name_selected) {
					group_id = jsgroups[i].groupId;
				}
			}
			for(var i = 0; i < jscontacts.length; i++) {
				if(jscontacts[i].groupId == group_id) {
					$("#group-contacts-list").append("<li class='contact card'><div><b>" + jscontacts[i].firstName + " "
							+ jscontacts[i].lastName + "</b><p>" + jscontacts[i].phoneNumber +
							"</p></li>");
				}
			}
			$("#group-contacts-list").show();
		}

		function preparePost() {

			var selectedGroupName = $("#group-dropdown-message option:selected").val();
			var selected_group_id;
			var message = $("#message-textarea").val();
			var phone_numbers = [];

			//If no group is selected
			if($("#group-dropdown-message option:selected").attr("id") == "default") {
				$("#contact-list input:checked").each(function() {
					var id = $(this).attr("id").substring(8, $(this).attr("id").length);
					for(var x = 0; x < jscontacts.length; x++) {
						if(jscontacts[x].contactId == id) {
							phone_numbers.push(jscontacts[x].phoneNumber);
						}
					}
				});
				
				if($.isEmptyObject(phone_numbers)) {
					alert("Du måste välja en grupp eller telefonnummer att skicka meddelande till");
				}
				else if(message == "") {
					alert("Du har inte skrivit något meddelande");
				}
				else {
					postMessage(JSON.stringify(phone_numbers), message);
				}
			}
			else {

				for(var i = 0; i < jsgroups.length; i++) {
					if(jsgroups[i].groupName == selectedGroupName) {
						selected_group_id = jsgroups[i].groupId;
					}
				}
				
				if(message == "") {
					alert("Du har inte skrivit något meddelande");
				}
				else {
					postMessage(selected_group_id, message);
				}				
			}
		}

		function postMessage(group_or_phonenumbers, message) {
			$.ajax({
				type: "POST",
				url: "${pageContext.request.contextPath}/sendmessage",
				data: {"group_or_phonenumbers" : group_or_phonenumbers, "message" : message},
				success: function() {
					alert("Meddelandet är skickat");
					$("#message-textarea").text("");
					
				}
			});
		}


		$(function() {
			for (var x = 0; x < jsgroups.length; x++) {
				$("#group-dropdown-message").append("<option><b>" + jsgroups[x].groupName + "</b></option>");
			}
		});

	</script>


<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">

<title>MessMe.se - Skicka SMS</title>
</head>
<body>



<jsp:include page="header.jsp"/>

<div id="container">
	<div class="row">
	<div class="col-md-4">
		<select id="group-dropdown-message">
			<option id="default">Välj en grupp</option>
		</select>
		<ul id="contact-list">

		</ul>
		
		<ul id="group-contacts-list">
		
		</ul>
	
	</div>

	<div class="col-md-6">

	<div id="message-container">
		<p>
		<b>Meddelande:</b><br/><textarea cols="50" rows="10" id="message-textarea" title="Du har inte skrivit något"></textarea>
		</p>
		<p>
			<br/><button id="sendBtn">Skicka</button>
		</p>
	</div>
	</div>
</div>
</div>
</body>
</html>