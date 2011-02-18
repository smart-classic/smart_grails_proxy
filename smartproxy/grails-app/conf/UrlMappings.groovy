class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
		
		"/records/$record_id/medications"{
			controller = "records"
			action = "medications"
		}
		
		"/records/$record_id/demographics"{
			controller = "records"
			action = "demographics"
		}
		
	}
}
