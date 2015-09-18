<%--
  Created by IntelliJ IDEA.
  User: martingabrielsson
  Date: 15-05-27
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="header">
  <a href="#" id="hamburger_trigger">
    <div>
      <span id="hamburger"></span>
    </div>
  </a>

  <img src="${pageContext.request.contextPath}/img/messme.png" alt="logo" id="header_logo"/>
</div>
<div id="navigation_drawer">
  <nav>
    <ul id="nav">
      <li><a href="groups">Grupper</a></li>
      <li><a href="contacts">Kontakter</a></li>
      <li><a href="messages">Meddelanden</a></li>
      <li><a href="logout">Logga ut</a></li>
    </ul>
  </nav>
</div>
