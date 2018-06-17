<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>main</title>
</head>
<body>
	<form action="<%=request.getContextPath()%>/setAttr" >
		<input type="submit" value="setAttr"/>
	</form>
	
	<form action="<%=request.getContextPath()%>/getAttr" >
		<input type="submit" value="getAttr"/>
	</form>
	
	<form action="<%=request.getContextPath()%>/removeAttr" >
		<input type="submit" value="removeAttr"/>
	</form>
	
	<form action="<%=request.getContextPath()%>/invalidate">
		<input type="submit" value="invalidate"/>
	</form>
</body>
</html>