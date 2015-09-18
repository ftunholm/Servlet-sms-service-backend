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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

<title>MessMe - Sveriges största sms-tjänst på nätet</title>
<script type="application/javascript">
    $(document).ready(function() {

        if(document.cookie.indexOf("register") > -1){
            $("#registerForm").show();
        }else{
            $("#loginForm").show();
        }
        $(".switchForm").click(function(){
            $('.card').slideUp("fast", function(){});
            if ($('#loginForm').is(':visible')){
                $("#loginForm").slideUp("fast", function() {
                    $("#registerForm").slideDown( "fast", function() {});
                    $('.card').slideDown("fast", function(){});
                    document.cookie="form=register";
                });
            }else{
                $("#registerForm").slideUp( "fast", function() {
                    $("#loginForm").slideDown( "fast", function() {});
                    $('.card').slideDown("fast", function(){});
                    document.cookie = "form=login;expires=-1";

                });
            }
        });
    });
</script>
</head>

<body>
<div id="header">
    <img src="${pageContext.request.contextPath}/img/messme.png" alt="logo" id="header_logo"/>
</div>
<div id="container">
<div class="card" style="width: 65em;
                        margin: 0px auto;
                        margin-top: 5em !important;">

    <form method="post" action="${pageContext.request.contextPath}/login" id="loginForm" style="margin: 0px auto">
        <a href="#" class="switchForm"> Registrera konto</a><br>
        <label for="email">E-postadress:</label><br/>

            <input name="email"/><br/><br/>

            <label for="password">Lösenord:</label><br/>
            <input name="password" type="password"/><br/><br/>

            <input type="Submit" value="Logga in" name="loginbtn"/>
    </form>

    <form method="post" action="register" id="registerForm" style="margin: 0px auto; display: none;">
        <a href="#" class="switchForm"> Logga in </a> <br>

        <label for="email">E-post</label><br/>
        <input name="email" pattern="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$" placeholder="E-postadress" required
               title="Du har angett en ogiltig e-postadress"/><br/><br/>

        <label for="password">Lösenord</label><br/>
        <input name="password" type="password" placeholder="Skriv ett lösenord" required="required" id="password"
               pattern="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$" title="Lösenordet matchar inte det angivna formatet"/>

        <font size="3" color="red" id="error"></font><br/><br/>

        <label for="repassword">Lösenord igen</label><br/>
        <input name="repassword" type="password" placeholder="Skriv in lösenord igen" required="required" id="repassword"/><br/><br/>

        <label for="firstName">Förnamn</label><br/>
        <input name="firstName" placeholder="Förnamn" required="required"><br/><br/>

        <label for="lastName">Efternamn</label><br/>
        <input name="lastName" placeholder="Efternamn" required="required"><br/><br/>

        <label for="birthYear">YYYY:</label>
        <input size="4" name="birthYear" pattern="^\d{4}$" placeholder="ÅÅÅÅ" required="required" title="Födelseår skall anges (ÅÅÅÅ)">


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
    <font id="errorMsgFont" color="red"><p name="errormsg">${errorMsg}</p></font>
    <p name="emailAlreadyExistsMessage" id="emailError">${emailExistsError}</p>


</div>
</div>
</body>
</html>