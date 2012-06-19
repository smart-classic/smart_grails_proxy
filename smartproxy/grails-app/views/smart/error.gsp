<html>
<head>
	<title>SMART App Viewer</title>
	<link rel="stylesheet" type="text/css" href="../css/bpc-screen.css" />
</head>
<body>
        <div id="main">
        
            <!-- Page header -->
            <div id="headings">
                <table width="786">
                    <tbody><tr>
                        <td valign="bottom" align="left"><div id="title">Blood Pressure Centiles</div></td>
                        <td valign="bottom" align="center"><div id="patient-info"></div></td>
                        <td align="right">
                            <div id="logo"><a target="_blank" href="http://www.smartplatforms.org/"><img width="65" height="40" border="0" alt="SMART Logo" src="../images/smart-logo.png"></a></div>
                        </td>
                    </tr>
                </tbody></table>
            </div>
            <div id="info"><%="${failureReason}" %>
							<br/>
							<g:if test="${rootCause!=''}">
								<%="Root Cause: ${rootCause}"%></div>
							</g:if>
	        </div>
</body>
</html>