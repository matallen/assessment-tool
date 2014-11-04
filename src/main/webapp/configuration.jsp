<%@page session="false" %>
<%@ page import="
com.redhat.wizard.IControl,
com.redhat.wizard.Control,
com.redhat.wizard.*,
java.util.List,
com.redhat.wizard.Main
"%>

<%
HttpSession session=request.getSession(true);
Main bean=new Main();
%>


<HTML>
	<HEAD>
		<TITLE>
			Assessment Tool - Configurations
		</TITLE>

			<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js" type="text/javascript"></script>
			<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript"></script>
			<script src="js/jquery.validate.js" type="text/javascript"></script>
			<script src="js/custom-form-elements.js" type="text/javascript"></script>
			
			<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
			<link href='http://fonts.googleapis.com/css?family=Raleway:400,300' rel='stylesheet' type='text/css'>
			<link href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
			<link href="style.css" rel="stylesheet" type="text/css" media="screen" />
			<link href="table.css" rel="stylesheet" type="text/css" media="screen" />
			
			<script type="text/javascript">
			$(function() {
			  $( "#accordion" ).accordion({
			  heightStyle: "content"
			  });
			});
			</script>
			
			<script src='https://www.google.com/jsapi' type='text/javascript'></script>
	</HEAD>
	<BODY>
		<div id="wrapper">
		    <div id="header-wrapper">
				<div id="header">
					<div id="logo">
						<h1>
							<a href='#'>Configurations</a>
						</h1>
					</div>
				</div>
		    </div>
		    <div id="page">
		        <div id="page-bgtop">
			        <div id="redhatLogo">
                        <img src="images/red-hat-logo.png" height="60px" width="60px">
			        </div>
		        <div id="progressBar"><div></div></div> 
		            <div id="page-bgbtm">
		                <div id="content">
		                    <div class="post">
		                      <ul>
		                        <%
		                        for(String c:bean.listConfigs()){
		                        if ("template.drl".equals(c)) continue;
		                        if (c.startsWith(".")) continue;
		                        %>
		                           <li><a href="handler?download=<%=c%>"><%=c%></a> | <a href="index.jsp?config=<%=c%>">start</a> | <a href="handler?delete=<%=c%>">delete</a></li>
		                        <%
		                        }
		                        %>
		                      </ul>
		                        
								<form action="handler" method="post" enctype="multipart/form-data">
									<input type="file" name="file" /><br />
									<input type="submit" value="Upload Questionnairre" />
								</form>
						   </div>
		                </div>
		            <div style="clear: both;">&nbsp;</div>
		        </div>
		        <!-- end #content -->
		        <div style="clear: both;">&nbsp;</div>
		    </div>
		</div>
        
		<div id="footer">
		    <p>Copyright (c) 2014 Mat Allen. (inspired by Chris Jenkins) </p>
		</div>
	</BODY>
</HTML>
