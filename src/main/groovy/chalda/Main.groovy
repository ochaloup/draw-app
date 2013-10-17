package chalda

class Main {
	def static INPUT_FILE = System.getProperty("DRAW_INPUT", "draw.txt")
	def static ADMIN_MAIL = System.getProperty("DRAW_ADMIN_EMAIL", "gandalf@istari.home")
	def static SUBJECT = System.getProperty("DRAW_SUBJECT", "A christmas draw")  
  
  def static IS_SEND = true;
	
	public static void main(String[] args) {
		if (args.length > 0) {
      INPUT_FILE = args[0].toString()
    }
    if (args.length > 1) {
      ADMIN_MAIL = args[1].toString()
    }
        
		def parser = new FileParser("|")
		List parsedList = parser.parse(INPUT_FILE)
		
		def dataPreparer = new DataPreparer()
		List toDraw = dataPreparer.prepare(parsedList)
		
		def drawer = new Drawer()
		drawer.matcher(toDraw)
		
		Util.logMatches(toDraw)
		
		def mailer = new SMTPMailer("", "", "smtp.gmail.com")
		
		
		def adminMail = "Draw results\n\n" << "Persons in the draw:\n"
		adminMail << "ID".padLeft(3) << "|" << "NAME".padLeft(10) << "|" << "E-MAILS".padLeft(30) << "|" << "DEPENDENCIES" << "\n"
		toDraw.each { p ->
			def depnames = Util.getNamesByDep(p, toDraw)
			adminMail << p.id.toString().padLeft(3) << "|" << p.name.padLeft(10) << "|" << p.emails.toString().padLeft(30) << "|" << depnames << "(" << p.dependencies << ")\n" 
		}
		adminMail << "\n\nResults:\n"
		toDraw.each { p ->
			adminMail << "Name: " << p.name << " got person in draw: " << p.foundDraw.name + "\n"
		}
		sendEmail(mailer, ADMIN_MAIL, SUBJECT << " [REPORT]", adminMail)
		
		def participantMail = "You've got person: "
		println("Sending emails to all participants of the draw")
		toDraw.each{ p ->
      def emailText = participantMail << p.foundDraw.name
			p.emails.each { email ->
				sendEmail(mailer, email, SUBJECT, emailText)
			}
		}
	}
  
  private static void sendEmail(SMTPMailer mailer, email, subject, text) {
    if(IS_SEND) {
      mailer.sendMail(email as String, subject as String, text as String)
      println("Sent mail via mailer '$mailer' to '$email' with subject '$subject' and text\n---\n$text\n\n")
    } else {
      println("Mail would be sent but IS_SEND property is false (to '$email' with subject '$subject' and text\n---\n$text)\n\n")
    }
  }
}
