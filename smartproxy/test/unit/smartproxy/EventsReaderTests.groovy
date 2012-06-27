package smartproxy

import org.chip.readers.EventsReader;
import org.chip.mo.model.Event;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import grails.test.*

class EventsReaderTests extends GrailsUnitTestCase {
	private Map ecm
    protected void setUp() {
        super.setUp()
		//mock the config
		mockConfig('''
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
					}
				}
		''')
		
		def config = ConfigurationHolder.config
		ecm = config.cerner.mo.eventCode
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSplitComplexEvents() {
		//Incoming complex events.
		EventsReader eventsReader = new EventsReader()
		eventsReader.eventsByParentEventId = new HashMap()
		eventsReader.eventsByParentEventId.put("99999999", [new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641203", eventCode:"1164539", eventValue:"60", eventTag:null]),
															new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641201", eventCode:"1164536", eventValue:"90", eventTag:null]),
															new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641207", eventCode:"1164542", eventValue:"59", eventTag:null]),
															new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641205", eventCode:"1164545", eventValue:"89", eventTag:null]),
															new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641209", eventCode:"1164548", eventValue:"88", eventTag:null]),
															new Event([encounterId:"34853623", parentEventId:"1174641199", eventEndDateTime:"2011-08-08T13:54:00.000-04:00", updateDateTime:"2011-08-08T18:55:09.000+01:00", eventId:"1174641211", eventCode:"1164551", eventValue:"58", eventTag:null])])

		//Call method to split complex events
		eventsReader.splitComplexEvents()
		
		//The incoming 6 complex events are all mapped to the same parent event id. 
		//We should now have 3 mappings in the processed map.
		assert eventsReader.eventsByParentEventId.size()==3
		
		//The value for each mapping should be a list consisting of 3 events.
		eventsReader.eventsByParentEventId.each{key, value->
			assert value.size()==3
		}
		
		//For each mapping, the list of 3 events should consist of a bodyPosition event, a systolic event, a diastolic event.
		eventsReader.eventsByParentEventId.each{key, events->
			boolean diastolicFound=false
			boolean systolicFound=false
			boolean bodyPositionFound=false
			
			events.each{event->
				if (event.eventCode==ecm.EVENTCODESYS){
					systolicFound=true
				}else if (event.eventCode == ecm.EVENTCODEDIA){
					diastolicFound=true
				}else if (event.eventCode == ecm.EVENTCODEPOSITION){
					bodyPositionFound=true
				}
			}
			assert diastolicFound
			assert systolicFound
			assert bodyPositionFound
		}
		
		//Verify that the values of the systolic and diastolic events are as expected in each mapping
		//For each mapping, the list of 3 events should consist of a bodyPosition event, a systolic event, a diastolic event.
		eventsReader.eventsByParentEventId.each{key, events->
			def diastolicValue
			def systolicValue=false
			def bodyPositionValue=false
			
			events.each{event->
				if (event.eventCode==ecm.EVENTCODESYS){
					systolicValue=event.value
				}else if (event.eventCode == ecm.EVENTCODEDIA){
					diastolicValue=event.value
				}else if (event.eventCode == ecm.EVENTCODEPOSITION){
					bodyPositionValue=event.eventTag
				}
			}
			
			switch(bodyPositionValue){
				case "Sitting":
					assert systolicValue=="89"
					assert diastolicValue=="59"
					break
				
				case "Standing":
					assert systolicValue=="88"
					assert diastolicValue=="58"
					break
					
				case "Supine":
					assert systolicValue=="90"
					assert diastolicValue=="60"
					break
			}
		}
    }
}