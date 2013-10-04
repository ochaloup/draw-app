package chalda

import chalda.dto.Person;
import groovy.util.logging.Log4j;

/**
 * Class which interprets parsed data from file.
 * This class knows the logic of the data - how the data in file should be put
 * and transforms this knowledge to DTO object
 */
@Log4j
class DataPreparer {
	def prepare(List parsedList) {
		List persons = [] 
		
		parsedList.each { arr ->
			if(arr.size != 4) {
				log.error("Data from array ${arr} can't be processed. Supposing 4 data items but it was ${arr.size}")
				return
			}
			
			log.debug("Processing array $arr")
			int id = arr[0].toInteger()
			String name = arr[1]
			List<String> emails = arr[2].tokenize(',')
			List<Integer> dependencies = (arr[3] == null) ? [] : arr[3].toString().tokenize(',').collect{it as int}
			def p = new Person(id, name, emails, dependencies)
			
			if(persons.contains(p)) {
				// two persons are the same on basis of their id
				log.warn("Won't add person ${p} to return collection as return array already contains the person with the same ID")
				return
			}
			
			persons.add(p)
		}
		return persons
	}
}
