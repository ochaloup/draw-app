package chalda

import groovy.util.logging.Log4j;
import chalda.dto.Person

@Log4j
class Drawer {
	
	List chooseAvailablePersons(Person person, List persons) {
		List availablePersons = []
		
		persons.each{ p ->
			log.debug("${person}>dependencies:${person.dependencies} with person: ${p}")
			if(p == person) {
				// person can't be chosen against the him hiMself
				return
			}
			if(person.dependencies.contains(p.id) || p.id == person.dependencies || person.dependencies.contains(p.name)) {
				// person has dependency to the current from the list - the current person is not availabe for the draw
				return
			}
			log.debug("dependencies:${person.dependencies} and pid: ${p.id} - result: ${person.dependencies.contains(p.id)}")
			log.debug(person.dependencies[0].getClass())
			log.debug(p.id.getClass())
			log.debug("Passed ${person}>dep:${person.dependencies} with person:${p} with id:${p.id}")
			availablePersons.add(p)
		}
		
		return availablePersons
	}
	
	def chooseDraw(List<Person> persons) {
		Random rand = new Random()
		def index = rand.nextInt(persons.size())
		return persons[index] 
	}
	
	/**
	 * Just resend the method call :)
	 */
	boolean matcher(List persons) {
		return matcher(persons, persons)
	}
	
	/**
	 * Finding 'a draw' and working over the input list (changing persons inside)
	 * @return true when draw was found, false when draw was not found (we need to do backtrack)
	 */
	boolean matcher(List personsToStart, List personsToMatch) {
		if(personsToStart.size() <= 0) {
			// all persons were matched - yupie!
			return true
		}
		if(personsToMatch.size() <= 0) {
			// we want to get match but we don't have nothing to work with
			return false
		}
		int matcherIndex = personsToStart.size()
		log.debug("matcher: ${matcherIndex}\n starts: ${personsToStart}\n match: ${personsToMatch}")
		
		// to know what persons we already processed
		List notYetStartedPersons = personsToStart.clone()
			
		boolean isOK = false
		while(!isOK){
			// which person we will work with
			Person startedPerson = chooseDraw(notYetStartedPersons)
			notYetStartedPersons.remove(startedPerson)
			// get possible persons to match with the chosen person
			List availPersons = chooseAvailablePersons(startedPerson, personsToMatch)
			if(availPersons.isEmpty()) break
			Person candidatePerson = chooseDraw(availPersons)
			log.debug("Matcher ${matcherIndex}: for person ${startedPerson} found candidate ${candidatePerson}")
			// creating working copy of list which will be put to recursive call
			List passStart = personsToStart.clone()
			passStart.remove(startedPerson)
			List passMatch = personsToMatch.clone()
			passMatch.remove(candidatePerson)

			// recursive call :)
			isOK = matcher(passStart, passMatch)
			// we found correct match - we will get out of the cycle
			if(isOK) startedPerson.foundDraw = candidatePerson
			// we have no more candidates
			if(notYetStartedPersons.isEmpty()) break
		}
		
		return isOK
	}
}
