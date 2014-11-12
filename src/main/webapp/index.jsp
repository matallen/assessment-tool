<%@page session="false" %>
<%@ page import="
com.redhat.wizard.Control,
com.redhat.wizard.*,
java.util.List,
com.redhat.wizard.Main
"%>

<%
HttpSession session=request.getSession(true);
Main bean=null;

if (request.getParameter("config")!=null){
  session.invalidate();
  session=request.getSession(true);
}

if (session.getAttribute("bean")==null){
  session.invalidate();
  session=request.getSession(true);
  bean=new Main();
  System.out.println("loading questionnaire: "+request.getParameter("config"));
  bean.run(request.getParameter("config"));
  session.setAttribute("bean", bean);
}else{
  bean=(Main)session.getAttribute("bean");
}

if (null!=request.getParameter("restart")){
  System.out.println("resetting");
  session.invalidate();
  session=request.getSession(true);
  bean.reset();
  session.setAttribute("bean", bean);
}

Page xpage=null;
Integer pageNumber=null!=request.getParameter("pageNumber")?Integer.parseInt(request.getParameter("pageNumber")):1;
if (request.getParameter("restart")!=null){
  pageNumber=1;
}else if (request.getParameter("next")!=null){
  pageNumber+=1;
}else if (request.getParameter("previous")!=null){
  pageNumber-=1;
}

System.out.println("pn = "+pageNumber);
if (null!=session.getAttribute(String.valueOf(pageNumber))){
  xpage=(Page)session.getAttribute(String.valueOf(pageNumber));
}else{
  xpage=bean.getPage(pageNumber);
}
System.out.println("page object = "+xpage);

if (request.getParameter("next")!=null || request.getParameter("previous")!=null){
  Integer oldPageNumber=request.getParameter("next")!=null?pageNumber-1:pageNumber+1;
  Page oldPage=bean.getPage(oldPageNumber);
  if (null!=oldPage){
    for(Control c:oldPage.getControls()){
      c.setAnswer(request.getParameter(String.valueOf(c.getId())));
      System.out.println("Setting answer for "+c.getQuestion()+" to "+request.getParameter(String.valueOf(c.getId())));
    }
    session.setAttribute(String.valueOf(oldPage.getNumber()), oldPage);
  }
}

if (xpage==null) throw new RuntimeException("Page not found. looking for page "+pageNumber+". Pages known = "+bean.getPages());



%>


<HTML>
	<HEAD>
		<TITLE>
			<%=bean.getTitle()%>
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
							<a href='#'><%=bean.getTitle()%></a>
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
		                    
							   <h2><%=xpage.getName()%></h2>
							   <form method="post" ACTION="index.jsp">
								   <input type="hidden" name="pageName" value="<%=xpage.getName()%>"/>
								   <input type="hidden" name="pageNumber" value="<%=xpage.getNumber()%>"/>
								   <table>
							         <%=xpage.display(request)%>
							       </table>
							       <input type="submit" name="restart" value="Restart"/>
							       &nbsp;&nbsp;&nbsp;&nbsp;
							       &nbsp;&nbsp;&nbsp;&nbsp;
						          &nbsp;&nbsp;&nbsp;&nbsp;
						          <input type="submit" name="previous" <%=xpage.getNumber()==1?"disabled":""%> value="Previous"/>
						          <input type="submit" name="next" <%=xpage.isLast()?"disabled":""%> value="Next"/>
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
		    <p>Copyright (c) 2014 Mat Allen. (inspired by Chris Jenkins)</p>
		</div>
	</BODY>
</HTML>
