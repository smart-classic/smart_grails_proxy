
oauth {
    smartEmr {
        token = 'grails-proxy'
        secret = 'grails-proxy'
        apiBase = 'http://localhost:7000'
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
        grails.serverURL = "http://recombinant-pgd.chip.org:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://recombinant-pgd.chip.org:7001/proxy_index'
	smart.belongsTo.ResourceURL = "http://recombinant-pgd.chip.org:8080/smartproxy/records/"
    }
    development {
        grails.serverURL = "http://recombinant-pgd.chip.org:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://recombinant-pgd.chip.org:7001/proxy_index'
	smart.belongsTo.ResourceURL = "http://recombinant-pgd.chip.org:8080/smartproxy/records/"
    }
    test {
        grails.serverURL = "http://recombinant-pgd.chip.org:8080/smartproxy"
	grails.moURL = 'http://soatstweb1:8888/CHMObjectsToolkit/servlet/'
	grails.smartURL = 'http://recombinant-pgd.chip.org:7001/proxy_index'
	smart.belongsTo.ResourceURL = "http://recombinant-pgd.chip.org:8080/smartproxy/records/"
    }

}

cerner{
	mo{
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
		encounterResource{
			Outpatient= "http://smartplatforms.org/terms/codes/EncounterType#ambulatory"
			Emergency= "http://smartplatforms.org/terms/codes/EncounterType#emergency"
			Field= "http://smartplatforms.org/terms/codes/EncounterType#field"
			Home= "http://smartplatforms.org/terms/codes/EncounterType#home"
			Inpatient= "http://smartplatforms.org/terms/codes/EncounterType#inpatient"
			Virtual="http://smartplatforms.org/terms/codes/EncounterType#virtual"
		}
		encounterTitle{
			Outpatient="Ambulatory encounter"
			Emergency= "Emergency encounter"
			Field= "Field encounter"
			Home= "Home encounter"
			Inpatient= "Inpatient encounter"
			Virtual= "Virtual encounter"
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
			EVENTCODEHEIGHT= "Height (measured)"
			EVENTCODEWEIGHT= "Body weight (measured)"
			EVENTCODERRATE= "Respiration rate"
			EVENTCODEHEARTRATE= "Heart Rate"
			EVENTCODEOSAT= "Oxygen saturation"
			EVENTCODETEMP= "Body temperature"
			EVENTCODESYS= "Systolic blood pressure"
			EVENTCODEDIA= "Diastolic blood pressure"
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
		
		vitalResource{
			EVENTCODEHEIGHT= "http://purl.bioontology.org/ontology/LNC/8302-2"
			EVENTCODEWEIGHT= "http://purl.bioontology.org/ontology/LNC/3141-9"
			EVENTCODERRATE= "http://purl.bioontology.org/ontology/LNC/9279-1"
			EVENTCODEHEARTRATE= "http://purl.bioontology.org/ontology/LNC/8867-4"
			EVENTCODEOSAT= "http://purl.bioontology.org/ontology/LNC/2710-2"
			EVENTCODETEMP= "http://purl.bioontology.org/ontology/LNC/8310-5"
			EVENTCODESYS= "http://purl.bioontology.org/ontology/LNC/8480-6"
			EVENTCODEDIA= "http://purl.bioontology.org/ontology/LNC/8462-4"
		}
		
		vitalResourceTagMap{
			Auscultation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#auscultation"
			Palpation= "http://smartplatforms.org/terms/codes/BloodPressureMethod#palpation"
			Automated= "http://smartplatforms.org/terms/codes/BloodPressureMethod#machine"
			Invasive= "http://smartplatforms.org/terms/codes/BloodPressureMethod#invasive"
			Sitting= "http://purl.bioontology.org/ontology/SNOMEDCT/33586001" 
			Standing= "http://purl.bioontology.org/ontology/SNOMEDCT/10904000"
			Supine= "http://purl.bioontology.org/ontology/SNOMEDCT/40199007"
			Left_upper="http://purl.bioontology.org/ontology/SNOMEDCT/368208006"
			Right_upper="http://purl.bioontology.org/ontology/SNOMEDCT/368209003"
			Left_lower="http://purl.bioontology.org/ontology/SNOMEDCT/61396006"
			Right_lower="http://purl.bioontology.org/ontology/SNOMEDCT/11207009"
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
        file name:'file', file:'C:\\logs\\smartproxy.log'
    }
    root {
        error 'file'
	}
	
	//Comment the following line if you want to disable logging all MO requests and responses
	debug 	"org.apache.http.wire"
}
