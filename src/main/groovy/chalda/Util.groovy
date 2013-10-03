package chalda

import groovy.util.logging.Log4j;

@Log4j
class Util {
	
	/**
	 * Just do toStringLog to log over array of persons
	 */
	static void logMatches(processedPersons) {
		// show the results
		log.info("\nFound matches:")
		processedPersons.each{ p ->
			log.info(p.toStringLong())
		}
	}
}
