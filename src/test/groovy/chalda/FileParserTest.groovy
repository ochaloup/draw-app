package chalda

import static org.junit.Assert.*;
import groovy.util.logging.Log4j;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Log4j
class FileParserTest {
	String fileName = "file.parser.txt"
	
	@Before
	void before() {
		File f = new File(fileName)
		log.debug("Creating file ${f.absolutePath}")
		f.withWriter {out ->
			out.writeLine("A|B|C")
			out.writeLine("#A|B|C")
			out.writeLine("D")
		}
	}
	
	@After
	void after() {
		def f = new File(fileName)
		log.debug("Deleting file ${f.absolutePath}")
		f.delete()
	}
	
	@Test
	void loadFile() {
		def fp = new FileParser()
		def file = fp.loadFile(fileName)
		Assert.assertNotNull(file)
	}
	
	@Test 
	void parseFile() {
		def fp = new FileParser("|")
		def arr = fp.parse(fileName)
		log.debug("Got list ${arr}")
		
		Assert.assertEquals("Result size of array is not correct", 2, arr.size());
		Assert.assertEquals("Size of the first index of the array is not correct", 3, arr[0].size());
		Assert.assertEquals("Size of the second index of the array is not correct", 1, arr[1].size());
		Assert.assertEquals("Element of the second index is not correct", "D", arr[1][0]);
	}
}
