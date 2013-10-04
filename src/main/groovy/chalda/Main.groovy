package chalda

class Main {
	public static void main(String[] args) {
		def adminEmail = System.getProperty("admin.email", "somebody")
		
		def parser = new FileParser("|")
		List parsedList = parser.parse("christmas-draw.txt")
		
		def dataPreparer = new DataPreparer()
		List toDraw = dataPreparer.prepare(parsedList)
		
		def drawer = new Drawer()
		drawer.matcher(toDraw)
		
		Util.logMatches(toDraw)
		
		String toAdminEmail = "The draw was done with following results:\n"
		toDraw.each { p ->
			def depnames = Util.getNamesByDep(p, toDraw)
			toAdminEmail += "For: " + p.name + " (with deps: ${depnames}) ->  was drawn name: " + p.foundDraw.name + "\n"
		}
		
		println("Sending email to admin\n${toAdminEmail}")
		
		toDraw.each{ p ->
			p.emails.each { email ->
				println ("Sending email to ${p.name} ${email}")
			}
		}
	}
}
