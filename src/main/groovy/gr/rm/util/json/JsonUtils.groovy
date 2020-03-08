package gr.rm.util.json
import groovy.json.JsonOutput
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

@CompileStatic
class JsonUtils {

	/**
	 * Converts an object to JSON and removes any properties that shouldn't be present in JSON string
	 * @param objectToConvert the object to convert to JSON
	 * @param propertiesToExclude properties of the object to exclude from JSON. Dot notation can be used in order to remove nested properties
	 * @return sanitized JSON string
	 */
	static String toSanitizedJson(def objectToConvert, def propertiesToExclude) {
		if(objectToConvert == null) {
			return null
		}
		if(!propertiesToExclude) {
			return JsonOutput.toJson(objectToConvert)
		}
		def objectAsMap = ((Map) new JsonSlurper().parseText(JsonOutput.toJson(objectToConvert))).sort();
		propertiesToExclude.each { String propertyToExclude ->
			removePropertyFromMap(objectAsMap, propertyToExclude)
		}
		JsonOutput.toJson(objectAsMap)
	}

	private static removePropertyFromMap(Map objectAsMap, String propertyToExclude) {
		def propertySegments = propertyToExclude.split("\\.")
		propertySegments.eachWithIndex{ propertyToExcludeSegment, index ->
			if(objectAsMap.containsKey(propertyToExcludeSegment)) {
				boolean isLastSegment = index == propertySegments.size() - 1
				if(isLastSegment) {
					objectAsMap.remove(propertyToExcludeSegment)
				}
				else if(Map.isInstance(objectAsMap.get(propertyToExcludeSegment))) {
					objectAsMap = objectAsMap.get(propertyToExcludeSegment)
				}
			}
		}
	}
}
