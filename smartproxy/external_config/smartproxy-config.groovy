
oauth {
    smartEmr {
        token = 'grails-proxy'
        secret = 'grails-proxy'
        apiBase = 'http://localhost:7001'
    }
}

cas{
    chb {
        skipValidation = false
        casValidationUrl =  'http://chssotest.tch.harvard.edu/cas/wsProxyValidate'
        casClientId = 'smartmpage'
        casServiceId = 'SmartWebApp'
    }
}

environments {
    production {
        grails.serverURL = "http://10.36.141.252:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://10.36.141.252/proxy_index'
    }
    development {
        grails.serverURL = "http://10.36.141.252:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://10.36.141.252/proxy_index'
    }
    test {
        grails.serverURL = "http://10.36.141.252:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://10.36.141.252/proxy_index'
    }

}

cerner{
	mo{
		encounterResource{
			Outpatient= "ambulatory"
			Emergency= "emergency"
			Field= "field"
			Home= "home"
			Inpatient= "inpatient"
			Virtual="virtual"
		}
		encounterTitle{
			Outpatient="Ambulatory encounter"
			Emergency= "Emergency encounter"
			Field= "Field encounter"
			Home= "Home encounter"
			Inpatient= "Inpatient encounter"
			Virtual= "Virtual encounter"
		}
		
		eventCode{
			EVENTCODEHEIGHT="2700653"
			EVENTCODEWEIGHT="2700654"
			EVENTCODERRATE="703540"
			EVENTCODEHEARTRATE="7935038"
			EVENTCODEOSAT="8238766"
			EVENTCODETEMP="8713424"
			EVENTCODESYS="703501"
			EVENTCODEDIA="703516"
			EVENTCODELOCATION="4099993"
			EVENTCODEPOSITION="13488852"
			EVENTCODEBPMETHOD="4100005"
			EVENTCODESYSSUPINE="1164536"
			EVENTCODESYSSITTING="1164545"
			EVENTCODESYSSTANDING="1164548"
			EVENTCODEDIASUPINE="1164539"
			EVENTCODEDIASITTING="1164542"
			EVENTCODEDIASTANDING="1164551"
		}
		
		vitalsType{
			EVENTCODEHEIGHT= "height"
			EVENTCODEWEIGHT= "weight"
			EVENTCODERRATE= "respiratoryRate"
			EVENTCODEHEARTRATE= "heartRate"
			EVENTCODEOSAT= "oxygenSaturation"
			EVENTCODETEMP= "temperature"
			EVENTCODESYS= "systolic"
			EVENTCODEDIA= "diastolic"
			EVENTCODEBPMETHOD= "method"
			EVENTCODELOCATION= "bodySite"
			EVENTCODEPOSITION= "bodyPosition"
		}
		
		vitalsTitle{		
			EVENTCODEHEIGHT= "Body height"
			EVENTCODEWEIGHT= "Body weight"
			EVENTCODERRATE= "Respiration rate"
			EVENTCODEHEARTRATE= "Heart Rate"
			EVENTCODEOSAT= "Oxygen saturation"
			EVENTCODETEMP= "Body temperature"
			EVENTCODESYS= "Intravascular systolic"
			EVENTCODEDIA= "Intravascular diastolic"
		}

		vitalsTitleTagMap{
			Auscultation= "Auscultation"
			Palpation= "Palpation"
			Automated= "Machine"
			Invasive= "Invasive"
			Sitting= "Sitting"
			Standing= "Standing"
			Supine= "Supine"
			Left_upper= "Left arm"
			Right_upper="Right arm"
			Left_lower= "Left thigh"
			Right_lower= "Right thigh"
		}
		
		codingSystemMap{
			encounterType="http://smartplatforms.org/terms/codes/EncounterType#"
		}
		
		vitalsCodingSystemMap{
			EVENTCODEHEIGHT="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODEWEIGHT="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODERRATE="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODEHEARTRATE="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODEOSAT="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODETEMP="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODESYS="http://purl.bioontology.org/ontology/LNC/"
			EVENTCODEDIA="http://purl.bioontology.org/ontology/LNC/"
		}
		
		vitalsCodingSystemTagMap{
			Auscultation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#"
			Palpation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#"
			Automated= "http://smartplatforms.org/terms/codes/BloodPressureMethod#"
			Invasive= "http://smartplatforms.org/terms/codes/BloodPressureMethod#"
			Sitting= "http://purl.bioontology.org/ontology/SNOMEDCT/"
			Standing= "http://purl.bioontology.org/ontology/SNOMEDCT/"
			Supine= "http://purl.bioontology.org/ontology/SNOMEDCT/"
			Left_upper= "http://purl.bioontology.org/ontology/SNOMEDCT/"
			Right_upper="http://purl.bioontology.org/ontology/SNOMEDCT/"
			Left_lower= "http://purl.bioontology.org/ontology/SNOMEDCT/"
			Right_lower= "http://purl.bioontology.org/ontology/SNOMEDCT/"
		}
		
		vitalsCodeTypeMap{
			EVENTCODEHEIGHT="VitalSign"
			EVENTCODEWEIGHT="VitalSign"
			EVENTCODERRATE="VitalSign"
			EVENTCODEHEARTRATE="VitalSign"
			EVENTCODEOSAT="VitalSign"
			EVENTCODETEMP="VitalSign"
			EVENTCODESYS="VitalSign"
			EVENTCODEDIA="VitalSign"
		}
		
		vitalsCodeTypeTagMap{
			Auscultation= "BloodPressureMethod"
			Palpation= "BloodPressureMethod"
			Automated= "BloodPressureMethod"
			Invasive= "BloodPressureMethod"
			Sitting= "BloodPressureBodyPosition"
			Standing= "BloodPressureBodyPosition"
			Supine= "BloodPressureBodyPosition"
			Left_upper= "BloodPressureBodySite"
			Right_upper="BloodPressureBodySite"
			Left_lower= "BloodPressureBodySite"
			Right_lower= "BloodPressureBodySite"
		}
		
		vitalResource{
			EVENTCODEHEIGHT= "8302-2"
			EVENTCODEWEIGHT= "3141-9"
			EVENTCODERRATE= "9279-1"
			EVENTCODEHEARTRATE= "8867-4"
			EVENTCODEOSAT= "2710-2"
			EVENTCODETEMP= "8310-5"
			EVENTCODESYS= "8480-6"
			EVENTCODEDIA= "8462-4"
		}
		
		vitalResourceTagMap{
			Auscultation= "auscultation"
			Palpation= "palpation"
			Automated= "machine"
			Invasive= "invasive"
			Sitting= "33586001" 
			Standing= "10904000"
			Supine= "40199007"
			Left_upper="368208006"
			Right_upper="368209003"
			Left_lower="61396006"
			Right_lower="11207009"
		}
		
		vitalUnits{
			EVENTCODEHEIGHT= "m"
			EVENTCODEWEIGHT= "kg"
			EVENTCODERRATE= "{breaths}/min"
			EVENTCODEHEARTRATE= "{beats}/min"
			EVENTCODEOSAT= "%{HemoglobinSaturation}"
			EVENTCODETEMP= "Cel"
			EVENTCODESYS= "mm[Hg]"
			EVENTCODEDIA= "mm[Hg]"
		}
		//values are of no importance to us in the bpEvents map. We are only interested in the keyset.
		//The keyset contains all the events which are a part of blood pressure
		bpEvents{
			EVENTCODESYS=""
			EVENTCODEDIA=""
			EVENTCODELOCATION=""
			EVENTCODEPOSITION=""
			EVENTCODEBPMETHOD=""
		}
	}
}
	
	
log4j = {
    appenders {
        file name:'file', file:(System.getProperty('catalina.base') ?: 'target') + '/logs/smartproxy.log'
	'null' name: 'stacktrace'
    }
    root {
        error 'file'
	}
	
	//Comment the following line if you want to disable logging all MO requests and responses
	//debug 	"org.apache.http.wire"
	debug		"org.apache.http.headers"
}
