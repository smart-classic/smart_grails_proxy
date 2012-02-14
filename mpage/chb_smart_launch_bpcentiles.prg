/*
==============================================================================================
					Copyright to Children's Hospital Boston
==============================================================================================
-| Object Name		:	chb_smart_launch_bpcentiles
-| Source File Name	:	chb_smart_launch_bpcentiles.prg
-| Program Purpose	:	Launch BP Centiles app, setting cas token and Power Chart context variables.
-| Author			:	Manish Kapoor, Josh Mandel
-| SME/Sponsor		:	Josh Mandel, SMART Platforms
-| Usage			:	Automatically launuches the BP Centiles App in an external window.
-| Notes			:	Relies on CHB CAS Server + SMART Stack (both hosted externally)
 
-| Version History	:	See Below
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-|	Version Initial Date		Description of Change
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	v1		01/23/2012		+ Initial Program with variable names, identation and layout
							updated as per CHIP CCL Guidelines
							Change Control: mpage-v1@
							https://github.com/chb/smart_grails_proxy/tags

 	v2		02/14/2012		+ Updated MPage code per CHB CCL Guidelines
							Change Control: mpage-v2@
							https://github.com/chb/smart_grails_proxy/tags

==============================================================================================
-| Curent Version	: v2
==============================================================================================
*/
 
drop program chb_smart_launch_bpcentiles:dba go
create program chb_smart_launch_bpcentiles:dba
prompt
	"Output to File/Printer/MINE"	= "MINE"
	, "Person ID"					= ""
	, "Encounter ID"				= ""
	, "User ID"						= ""
	, "Location"					= ""
	, "APP"							= ""

with OUTDEV, PID, EID, UID, LOC, APP

/**************************************************************
; INCLUDES AND EXTERNAL LIBRARIES
**************************************************************/
;None


/**************************************************************
; RECORD STRUCTURES
**************************************************************/
;None


/**************************************************************
; DECLARED SUBROUTINES
**************************************************************/
;None


/**************************************************************
; CONSTANTS
**************************************************************/
;None


/**************************************************************
; VARIABLES
**************************************************************/
;None


/**************************************************************
; VARIABLE INITIALIZATION
**************************************************************/
;None


/**************************************************************
; MAIN PROGRAM AND QUERIES
**************************************************************/


; Get the current patient's name and patient ID
select into $OUTDEV
	  p.person_id
	, p.name_full_formatted

from
	person p

plan p
	where p.person_id = cnvtreal($PID)

head report
	row +1 "<html>"
	row +1 "<head>"
	row +1 "    <title>BP Centiles Fronting MPage</title>" 
	row +1 "    <meta content='CCLLINK,APPLINK,XMLCCLREQUEST,CCLNEWSESSIONWINDOW' name='DISCERN'>"
	row +1 "</head>"
 
	row +1 "<body>"
 
	row +1 "<script>"
 
	; Initialize JS variables we'll need to launch the app in an external browser window 
	row +1 "    var grails_proxy_base = 'http://10.36.142.250:8980/smartproxy/smart/readContext?',"
	row +1 "	initial_app = 'pedi_bpc@apps.smartplatforms.org',"
	row +1 "	cas_secret = 'SECRET_HERE',"
	row +1 "	cas_url = 'http://chssotest.tch.harvard.edu/cas/wsProxy?clientId=smartmpage&sharedSecret='+cas_secret,"
	row +1 "	httpRequest = new XMLHttpRequest(),"
	row +1 "	cas_token;"
 
	; Include the patient ID and encounter ID among JS variables
	call print(build("var record_id = '", $PID, "';" ))
	call print(build("var encounter_id = '", $EID, "';" ))
 
	row +1 "</script>"
 
 
detail
	row +1 "Launching BP Centiles in external window for: ", p.name_full_formatted
 
 
foot report
	row +1 "<script>"
 
	; CHB CAS server authentication: parse proxyTicket when request is successful
	row +1 "httpRequest.onreadystatechange = function() {"
	row +1 "    if (httpRequest.readyState==4 && httpRequest.status==200) {"
	row +1 "    cas_token = parseXml() "
	row +1 "            .getElementsByTagName('cas:proxyTicket')[0] "
	row +1 "            .childNodes[0] "
	row +1 "            .nodeValue; "
	row +1 "    launchApp();"
	row +1 "    }"
	row +1 "};"
 
	; CHB CAS server authentication: issue request
	row +1 "httpRequest.open('GET', cas_url);"
	row +1 "httpRequest.send();"
 
	; W3C DOM XML Parsing wrapper function
	row +1 "function parseXml() {"
	row +1 "    var responseText = httpRequest.responseText;"
	row +1 "    if (window.DOMParser){ "
	row +1 "        xmlDoc=new DOMParser().parseFromString(responseText ,'text/xml');"
	row +1 "    } else { "
	row +1 "        xmlDoc=new ActiveXObject('Microsoft.XMLDOM');"
	row +1 "        xmlDoc.async=false;"
	row +1 "        xmlDoc.loadXML(responseText);"
	row +1 "    }"
	row +1 "    return xmlDoc;"
	row +1 "};"

	; Once we have a proxyTicket, launch the app in an external window 
	row +1 "function launchApp() {"
	row +1 "    APPLINK(100, "
	row +1 "        grails_proxy_base + "
	row +1 "        'initial_app='+initial_app + "
	row +1 "        '&record_id=' + record_id + "
	row +1 "        '&encounter_id=' + encounter_id + "
	row +1 "        '&cas_token=' + cas_token,"
	row +1 "        ''"
	row +1 "    );"
	row +1 "};"
 
	row +1 "</script>"
	row +1 "</body>"
	row +1 "</html>"
 
with
	  nocounter
	, format = variable
	, maxcol = 200
 
end
go
