package chalda


class Main {
	public static void main(String[] args) {
		def parser = new FileParser("|")
		List parsedList = parser.parse("christmas-draw.txt")
		
		def dataPreparer = new DataPreparer()
		List toDraw = dataPreparer.prepare(parsedList)
		
		def drawer = new Drawer()
		drawer.matcher(toDraw, toDraw)
		
		Util.logMatches(toDraw)
	}
}
