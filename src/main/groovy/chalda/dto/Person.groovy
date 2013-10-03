package chalda.dto

import groovy.util.logging.Log4j;

@Log4j
class Person {
	private int id
	
	private String name
	private List emails
	private List dependencies
	
	private Person foundDraw
	private boolean isMailSent = false;
	
	Person(){}
	Person(id, name, emails, dependencies) {
		this.id = id
		this.name = name
		this.emails = emails
		this.dependencies = dependencies
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Person)) {
			return false
		}
		return ((Person)obj).id == this.id
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		return 31 * hash + id
	}

	@Override
	public String toString() {
		return "Person[id:${id}|name:${name}]"
	}
		
	@Override
	public String toStringLong() {
		return "${this.getClass()} - id:${id}, name:${name}, found: ${foundDraw}, emails:${emails}, dependencies:${dependencies}, isMailSent:${isMailSent}"
	}
}
