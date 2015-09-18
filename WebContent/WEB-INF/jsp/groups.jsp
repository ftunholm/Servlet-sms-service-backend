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

<title>MessMe.se - Grupper</title>

</head>
<body>
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


function showContacts(group_name) {
	var group_id;
	var showDiv = false;
	var $middlecontactlist = $("#middlecontactlist");
	var contacts_no_group = [];
	
	//Reset the click listener so it does not stack up
	$middlecontactlist.undelegate("span", "click");

	$("#list").text(" ");
	$("#middlecontactlist").text(" ");
	for (var i = 0; i < jsgroups.length; i++) {
		if (jsgroups[i].groupName == group_name.value) {
			group_id = jsgroups[i].groupId;
			showDiv = true;
		}
	}

	if(!showDiv) {
		$(".contactcontainer").hide();
		$(".middlecontactcontainer").hide();
	}
	else {
		$(".contactcontainer").show();
		$(".middlecontactcontainer").show();
	}


	for (var j = 0; j < jscontacts.length; j++) {
		if (group_id == jscontacts[j].groupId) {
			$("#list").append("<li class='contact card'><b>" + jscontacts[j].firstName + " "
					+ jscontacts[j].lastName + "</b><p>" + jscontacts[j].phoneNumber + "</p></li>");
		}
		else {
			//Double check this for null or 0
			if(jscontacts[j].groupId == 0) {
				contacts_no_group.push(jscontacts[j]);
			}
			
		}
	}
	
	if(jQuery.isEmptyObject(contacts_no_group)) {
		$middlecontactlist.append("Du har inga kontakter som saknar grupp");
	}

	//Append all the items from the array
	$.each(contacts_no_group, function() {
		$middlecontactlist.append("<li class='contact card' id='list_" + this.contactId + "'><b>" + this.firstName + " "
				+ this.lastName + "</b><p>" + this.phoneNumber +
				"</p><span class='add_contact_symbol' id='contact_" + this.contactId + "'></span></li>");
	});
	//If any li-item is pressed this will be called
	$middlecontactlist.delegate("span" +
            "", "click", function (e) {
		//Get the id with substring 
		var contact_id = $(this).attr("id").substring(8, $(this).attr("id").length);
		var list_id = $(this).attr("id").substring(8, $(this).attr("id").length);
		
		//Append the contact that was added to group to the contact list
		for(var i = 0; i < contacts_no_group.length; i++) {
			if(contacts_no_group[i].contactId == contact_id) {
				
				$("#list").append("<li class='contact card'><b>" + contacts_no_group[i].firstName + " "
						+ contacts_no_group[i].lastName + "</b><p>" + contacts_no_group[i].phoneNumber + "</p></li>")
						
			}
		}
		
		$("#list_" + list_id).remove();
		$("#contact_" + contact_id).remove();
		
		//Ajax post-request
		$.ajax({
			  type: "POST",
			  url: "${pageContext.request.contextPath}/add-contact-to-group",
			  data: {"contactId" : contact_id, "groupId" : group_id},
			  success: function(){
				  //location.reload();
			  }
			});
	});
	
}

$(function() {
	$(".contactcontainer").hide();


	for (var x = 0; x < jsgroups.length; x++) {
		$("#dropdown").append("<option><b>" + jsgroups[x].groupName + "</b></option>");
	}
});



</script>
	<jsp:include page="header.jsp"/>
<div id="container">
	<div class="row">

	<div class="col-md-4">
	<div class="middlecontactcontainer">

		<p><b>Kontakter som inte tillhör någon grupp</b></p>
		<ul id="middlecontactlist">
			
		</ul>	
	</div>
	</div>

	<div class="col-md-4">
		<select id="dropdown" onchange="showContacts(this);">
			<option>Välj en grupp</option>
		</select>
        <br>
        <div class="contactcontainer">
            <ul id="list">

            </ul>
        </div>
	</div>
		<div class="col-md-4">
			<div class="addcontainer">

				<h3><b>Lägg till ny grupp</b></h3>
				<div class="card">
					<form method="post" action="${pageContext.request.contextPath}/addgroup">

						<label for="groupName">Gruppnamn:</label><br/>
						<input id="groupName" name="groupName" required="required" title="Gruppnamn saknas" placeholder="Gruppnamn"/><br/><br/>

						<input type="Submit" value="Skapa grupp" name="add_group"/>

					</form>
				</div>
			</div>
		</div>
</div>
</div>
</body>
</html>