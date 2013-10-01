package chalda

import groovy.util.logging.Log4j;

/**
 * Parsing file somelike
 */
@Log4j
class FileParser {
	def separator

	FileParser() {
		this.separator = "\t"
	}
	FileParser(String separator) {
		this.separator = separator
	}

	def loadFile(String fileName) {
		URL url = this.getClass().getClassLoader().getResource(fileName)
		File f

		if(url == null) {
			f = new File(fileName)
		} else {
			f = new File(url.toURI())
		}

		if(f != null && f.exists()) {
			log.info("Loaded class '$fileName' as '${f.absolutePath}'")
			return f
		} else {
			throw new RuntimeException("We are not able to load file with name '$fileName'")
		}
	}

	def parse(String fileName) {
		File f = loadFile(fileName);
		def returnList = []
		
		f.eachLine{
			it = it.trim()
			if (it.startsWith('#'))  {
				// ignoring comment line starting with #
				log.debug("Ignoring line ${it} as starts with #")
				return;
			}

			List arr = it.tokenize(separator)

			// collect will call the method on the each item of array and this will be propagated to array
			arr.collect{it.trim()}

			// adding parsed array to result array
			returnList.add(arr)
		}
		return returnList
	}
}
