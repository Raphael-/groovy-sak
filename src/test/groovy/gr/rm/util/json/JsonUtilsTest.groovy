package gr.rm.util.json

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

class JsonUtilsTest extends Specification{
	
	@Unroll
    def "someLibraryMethod returns true"() {
        expect:
			JsonUtils.toSanitizedJson(objectToConvert, propertiesToExclude) == expectedJson
		where:
			objectToConvert					|			propertiesToExclude					||		expectedJson
			objectWithNestedProperties()	|			nestedPropertiesToExclude()			||		jsonWithExcludedNestedProperties()
			objectWithNestedProperties()	|			nestedNullPropertyToExclude()		||		jsonWithExcludedNullProperty()
			objectWithNestedProperties()	|			nestedObjectToExclude()				||		jsonWithExcludedObject()
			objectWithNestedProperties()	|			nonExistentProperty()				||		jsonWithAllOriginalProperties()
			null							|			nestedPropertiesToExclude()			||		null
			objectWithNestedProperties()	|			null								||		jsonWithAllOriginalPropertiesDifferentKeySort()
			objectWithNestedProperties()	|			[]									||		jsonWithAllOriginalPropertiesDifferentKeySort()
		
    }
	
	def objectWithNestedProperties() {
		def object = new Object() {
			def numericProperty = 1
			def stringProperty = 'test'
			def nestedProperty = new Object() {
				def nestedPropertyA = 7
				def nestedPropertyB = 'nested'
				def nestedPropertyC = 2.0
				def nestedPropertyD = null
				
			}
		}
	}
	
	def nestedPropertiesToExclude() {
		['nestedProperty.nestedPropertyB', 'stringProperty']
	}
	
	def nestedNullPropertyToExclude() {
		['nestedProperty.nestedPropertyD']
	}
	
	def nestedObjectToExclude() {
		['nestedProperty']
	}
	
	def nonExistentProperty() {
		['inexistentProp']
	}
	
	def jsonWithExcludedNestedProperties() {
		'''{"nestedProperty":{"nestedPropertyA":7,"nestedPropertyC":2.0,"nestedPropertyD":null},"numericProperty":1}'''
	}
	
	def jsonWithExcludedNullProperty() {
		'''{"nestedProperty":{"nestedPropertyA":7,"nestedPropertyB":"nested","nestedPropertyC":2.0},"numericProperty":1,"stringProperty":"test"}'''
	}
	
	def jsonWithExcludedObject() {
		'''{"numericProperty":1,"stringProperty":"test"}'''
	}
	
	def jsonWithAllOriginalProperties() {
		'''{"nestedProperty":{"nestedPropertyA":7,"nestedPropertyB":"nested","nestedPropertyC":2.0,"nestedPropertyD":null},"numericProperty":1,"stringProperty":"test"}'''
	}
	
	def jsonWithAllOriginalPropertiesDifferentKeySort() {
		'''{"stringProperty":"test","nestedProperty":{"nestedPropertyC":2.0,"nestedPropertyB":"nested","nestedPropertyA":7,"nestedPropertyD":null},"numericProperty":1}'''
	}
}
