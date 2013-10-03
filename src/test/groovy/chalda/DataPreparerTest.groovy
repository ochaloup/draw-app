package chalda

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import groovy.util.logging.Log4j;

@Log4j
class DataPreparerTest {
	@Test
	void prepare() {
		def email1 = "a@a.com"
		def email2 = "b@b.com"
		
		def testArr = [[1,"A",email1,null], ["2","B",email2,2],[],[111,222], ["1","EE","",2],[],[111,222], ,[3,"C","$email1,$email2","1,2"]]
		def preparer = new DataPreparer()
		def result = preparer.prepare(testArr)	
		
		Assert.assertEquals("Prepared size of array does not match", 3, result.size())
		Assert.assertEquals("Person1 email count does not match", 1, result[0].emails.size())
		Assert.assertEquals("Person1 email does not match", email1, result[0].emails[0])
		Assert.assertEquals("Person2 email count does not match", 1, result[1].emails.size())
		Assert.assertEquals("Person2 email does not match", email2, result[1].emails[0])
		Assert.assertEquals("Person3 email count does not match", 2, result[2].emails.size())
	}
}
