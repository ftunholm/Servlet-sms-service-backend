<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<script src="https://code.jquery.com/jquery-2.1.4.min.js" type="text/javascript"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MessMe.se - Registrera dig</title>
</head>
<body>

	<script type="text/javascript">
	$(document).ready(function() {
		$("#error").css('color', 'gray');
		$("#emailError").css('color', 'red');
		$("#error").text("(Lösenordet måste vara minst 6 tecken och innehålla en siffra, versal och specialtecken)");
		$("#submitRegister").submit(function(event) {
			if($("#password").val() != $("#repassword").val()) {	
				$("#error").css('color', 'red');
				$("#error").text("Lösenorden stämmer inte överens");
				event.preventDefault();
			}
			if($("#termsCheckbox").is(':checked') == false) {
				$("#termsError").css('color', 'red');
				$("#termsError").text("Du måste acceptera användarvillkoren för att fortsätta");
				event.preventDefault();
			}
		});
	});

	</script>
	<center><h1><b>Registering</b></h1></center>
	<br/><br/>
	<a href="${pageContext.request.contextPath}/index">Tillbaka till startsidan</a>
	<br/><br/><br/><br/>
	
	<form method="post" action="${pageContext.request.contextPath}/register" id="submitRegister">
		<label for="email">E-post</label><br/>
		<input name="email" pattern="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$" placeholder="E-postadress" required="required"
		title="Du har anget en ogiltig e-postadress"/><br/><br/>
		
		<label for="password">Lösenord</label><br/>
		<input name="password" type="password" placeholder="Skriv ett lösenord" required="required" id="password" 
		pattern="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,24}$" title="Lösenordet matchar inte det angivna formatet"/>
		<font size="3" color="red" id="error"></font><br/><br/>
		
		<label for="repassword">Lösenord igen</label><br/>
		<input name="repassword" type="password" placeholder="Skriv in lösenord igen" required="required" id="repassword"/><br/><br/>
		
		<label for="firstName">Förnamn</label><br/>
		<input name="firstName" placeholder="Förnamn" required="required"><br/><br/>
		
		<label for="lastName">Efternamn</label><br/>
		<input name="lastName" placeholder="Efternamn" required="required"><br/><br/>
		
		<label for="birthYear">YYYY:</label>
		<input size="4" name="birthYear" pattern="^\d{4}$" placeholder="ÅÅÅÅ" required="required" title="Födelseår skall anges (ÅÅÅÅ)">	
			
		<label for="birthMonth">MM:</label>
		<input size="2" name="birthMonth" pattern="^\d{2}$" placeholder="MM" required="required" title="Födelsemånad skall anges (MM)">
		
		<label for="birthDay">DD:</label>
		<input size="2" name="birthDay" pattern="^\d{2}$" placeholder="DD" required="required" title="Födelsedag skall anges (DD)"><br/><br/>
		
		Jag accepterar och är införstådd med <a href="terms.html"
			onclick="javascript:void window.open('terms.html','1432716049191','width=700,height=500,toolbar=0,menubar=0,location=0,status=1,scrollbars=1,resizable=1,left=0,top=0');return false;">
			 användarvillkoren</a>:
			
		<input name="checkbox_terms" type="checkbox" required="required" title="Du måste acceptera användarvillkoren för att fortsätta" id="termsCheckbox"/>
		<br/>
		<font size="2" color="red" id="termsError"></font>
		<br/>
		<input type="Submit" value="Fortsätt" name="registermebtn"/>
		
	</form>
	<br/><br/><br/>
	<p name="emailAlreadyExistsMessage" id="emailError">${emailExistsError}</p>

</body>
</html>