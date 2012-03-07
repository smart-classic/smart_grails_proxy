package org.chip.mo.mappers

import java.util.Map;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class SmartMapper {
	
	static final Map mappingTypeMap
	
	static final Map vitalTypeMap
	static final Map vitalTitleMap
	static final Map vitalResourceMap
	static final Map vitalUnitMap
	
	static{
		
		def config = ConfigurationHolder.config
		
		vitalTypeMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsType)
		
		vitalTitleMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsTitle)
		vitalTitleMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalsTitleTagMap))
		
		vitalResourceMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalResource)
		vitalResourceMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalResourceTagMap))
		
		vitalUnitMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalUnits)
		
		mappingTypeMap = new HashMap()
		mappingTypeMap.put('Type', vitalTypeMap)
		mappingTypeMap.put('Title', vitalTitleMap)
		mappingTypeMap.put('Resource', vitalResourceMap)
		mappingTypeMap.put('Unit', vitalUnitMap)
	}
	
	static def readEventCodesConfigMap(eventCodesMap, propertiesMap){
		def hashMap = new HashMap()
		propertiesMap.each{propertiesMapEntry->
			String key = eventCodesMap.get(propertiesMapEntry.getKey())
			hashMap.put(key,  propertiesMapEntry.getValue())
		}
		return hashMap
	}
	
	static def readEventTagConfigMap(propertiesMap){
		def hashMap = new HashMap()
		propertiesMap.each{propertiesMapEntry->
			String key = propertiesMapEntry.getKey()
			if (key.indexOf("_")>0){
				key = key.replaceAll("_", " ")
			}
			hashMap.put(key, propertiesMapEntry.getValue())
		}
			return hashMap
	}
	
	public static map(sourceCode, mappingType){
		def map = mappingTypeMap.get(mappingType)
		map.get(sourceCode)
	}
}
