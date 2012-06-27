package org.chip.mo.mappers

import java.util.Map;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
* SmartMapper.groovy
* Purpose: Provides mapping between various MO supplied entities and corresponding SMART values.
* The mappings are defined in the external config file which is accessed through the ConfigurationHolder.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class SmartMapper {
	static final Map mappingTypeMap
	
	static final Map vitalTypeMap
	static final Map vitalTitleMap
	static final Map vitalResourceMap
	static final Map vitalUnitMap
	static final Map vitalsCodingSystemsMap
	static final Map vitalsCodeTypeMap
	
	static{
		
		def config = ConfigurationHolder.config
		
		vitalTypeMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsType)
		
		vitalTitleMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsTitle)
		vitalTitleMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalsTitleTagMap))
		
		vitalResourceMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalResource)
		vitalResourceMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalResourceTagMap))
		
		vitalUnitMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalUnits)
		
		vitalsCodingSystemsMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsCodingSystemMap)
		vitalsCodingSystemsMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalsCodingSystemTagMap))
		
		vitalsCodeTypeMap = readEventCodesConfigMap(config.cerner.mo.eventCode, config.cerner.mo.vitalsCodeTypeMap)
		vitalsCodeTypeMap.putAll(readEventTagConfigMap(config.cerner.mo.vitalsCodeTypeTagMap))
		
		mappingTypeMap = new HashMap()
		mappingTypeMap.put('Type', vitalTypeMap)
		mappingTypeMap.put('Title', vitalTitleMap)
		mappingTypeMap.put('Resource', vitalResourceMap)
		mappingTypeMap.put('Unit', vitalUnitMap)
		mappingTypeMap.put('codingSystem', vitalsCodingSystemsMap)
		mappingTypeMap.put('codeType', vitalsCodeTypeMap)
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
