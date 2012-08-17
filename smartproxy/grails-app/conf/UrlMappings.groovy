/**
 * UrlMappings.groovy
 * Purpose: Maps incoming requests to specific controllers and actions.
 * @author mkapoor
 * @version Jun 19, 2012 12:53:03 PM
 */
class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
		
		/*
		 * maps incoming calls like http://smart/records/15649511/vital_signs
		 * to the "makeCall" action in "RecordsController".
		 * -record_id is the patient id
		 * -transaction is one of they keys in MilleniumService.transactionStepsMap
		 */ 
		"/records/$record_id/$transaction"{
			controller = "records"
			action = "makeCall"
		}
		
	}
}
