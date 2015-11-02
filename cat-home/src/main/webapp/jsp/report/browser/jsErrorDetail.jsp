<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>

<jsp:useBean id="model" type="com.dianping.cat.report.page.browser.Model" scope="request"/>
<a:web_body>
<table class="table table-striped table-condensed table-bordered table-hover">
	<tr>
		<td>Error Time : </td>
		<td>${model.errorTime}</td>
	</tr>
	<tr>
		<td>Level : </td>
		<td>${model.level}</td>
	</tr>
	<tr>
		<td>Module : </td>
		<td>${model.module}</td>
	</tr>
	<tr>
		<td>Detail: </td>
		<td>${model.detail}</td>
	</tr>
</table>
</a:web_body>