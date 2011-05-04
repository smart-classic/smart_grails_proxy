class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
		
		"/records/$record_id/$transaction"{
			controller = "records"
			action = "makeCall"
		}
		
	}
}
