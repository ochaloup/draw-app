package chalda

import groovy.util.logging.Log4j;
import chalda.dto.Person

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Log4j
class DrawerTest {
	
	List persons
	List personsABC
	List personsABCFail
	List personMix
	
	def person1 = new Person(11, "Name#1", ["a@a.com"], [22])
	def person2 = new Person(22, "Name#2", ["b@b.com"], [])
	def person3 = new Person(33, "Name#3", ["c@c.com"], [])
	def person4 = new Person(44, "Name#4", ["d@d.com"], [])
	// --
	def personA = new Person(1, "A", ["mail"], [1,2,3,4,5,7,8,9])
	def personB = new Person(2, "B", ["mail"], [3,4])
	def personC = new Person(3, "C", ["mail"], [7,8,9])
	def personD = new Person(4, "D", ["mail"], [5,6])
	def personE = new Person(5, "E", ["mail"], [2,3,4])
	def personF = new Person(6, "F", ["mail"], [2,3,4,5,6,7,8,9])
	def personG = new Person(7, "G", ["mail"], [3,4,8])
	def personH = new Person(8, "H", ["mail"], [5,7])
	def personI = new Person(9, "I", ["mail"], [2,3,8])
	// --
	def personZ = new Person(26, "Z", ["mail"], [1,2,3,4,5,6,7,8,9])
	
	
	def Frodo = new Person(1, "Frodo", ["mail"], [22])
	def Gandalf = new Person(2, "Gandalf", ["mail"], [])
	def Bifur = new Person(3, "Bifur", ["mail"], [])
	def Bombur = new Person(4, "Bombur", ["mail"], [])
	def Galadriel = new Person(5, "Galadriel", ["mail"], [])
	def Elrond = new Person(6, "Elrond", ["mail"], [])
	
	@Before
	void before() {
		persons = []
		persons << person1
		persons << person2
		persons << person3
		persons << person4
		
		personsABC = []
		personsABC << personA << personB << personC << personD << personE << personF << personG << personH << personI
		
		personsABCFail = personsABC.clone()
		personsABCFail << personZ
	}
	
	@Test
	void testChoose() {
		def drawer = new Drawer();
		def availPersons = drawer.chooseAvailablePersons(person1, persons)
		
		log.debug("Available persons for ${person1} are ${availPersons}")
		Assert.assertEquals("Size of list of the available persons do not match", 2, availPersons.size())
		Assert.assertTrue("Available person list should contains person #3", availPersons.contains(person3))
		Assert.assertTrue("Available person list should contains person #4", availPersons.contains(person4))
		
		// just for info that no exception is thrown
		log.info("chosen person: " + drawer.chooseDraw(availPersons))
	}
	
	@Test
	void testDraw1() {
		def drawer = new Drawer();
		def retCode = drawer.matcher(persons, persons)
		
		Assert.assertTrue("Match not found", retCode)
		Assert.assertTrue("Person1 is dependent on person2 - can't be matched together", person1.foundDraw != person2)
		
		Util.logMatches(persons)
	}
	
	@Test
	void testDraw2() {
		def drawer = new Drawer();
		def retCode = drawer.matcher(personsABC, personsABC)
				
		Util.logMatches(personsABC)
		
		Assert.assertTrue("Match not found", retCode)
		Assert.assertTrue("PersonA(#1) has no other chance than to be bound to PersonF(#6)", personA.foundDraw == personF)
		Assert.assertTrue("PersonF(#6) has no other chance than to be bound to PersonA(#1)", personF.foundDraw == personA)
		Assert.assertTrue("PersonB(#2) can't depent on PersonC(#3)", personB.foundDraw != personC)
		Assert.assertTrue("PersonB(#2) can't depent on PersonD(#4)", personB.foundDraw != personD)
		Assert.assertTrue("PersonC(#3) can't depent on PersonG(#7)", personC.foundDraw != personG)
		Assert.assertTrue("PersonC(#3) can't depent on PersonH(#8)", personC.foundDraw != personH)
		Assert.assertTrue("PersonC(#3) can't depent on PersonI(#9)", personC.foundDraw != personI)
		Assert.assertTrue("PersonD(#4) can't depent on PersonE(#5)", personD.foundDraw != personE)
		Assert.assertTrue("PersonD(#4) can't depent on PersonF(#6)", personD.foundDraw != personF)
		Assert.assertTrue("PersonE(#5) can't depent on PersonB(#2)", personE.foundDraw != personB)
		Assert.assertTrue("PersonE(#5) can't depent on PersonC(#3)", personE.foundDraw != personC)
		Assert.assertTrue("PersonE(#5) can't depent on PersonD(#4)", personE.foundDraw != personD)
		Assert.assertTrue("PersonG(#7) can't depent on PersonC(#3)", personG.foundDraw != personC)
		Assert.assertTrue("PersonG(#7) can't depent on PersonD(#4)", personG.foundDraw != personD)
		Assert.assertTrue("PersonG(#7) can't depent on PersonH(#8)", personG.foundDraw != personH)
		Assert.assertTrue("PersonH(#8) can't depent on PersonE(#5)", personH.foundDraw != personE)
		Assert.assertTrue("PersonH(#8) can't depent on PersonG(#7)", personH.foundDraw != personG)
		Assert.assertTrue("PersonI(#9) can't depent on PersonB(#2)", personI.foundDraw != personB)
		Assert.assertTrue("PersonI(#9) can't depent on PersonC(#3)", personI.foundDraw != personC)
		Assert.assertTrue("PersonI(#9) can't depent on PersonH(#8)", personI.foundDraw != personH)
	}
	
	@Test
	void testDraw3() {
		def drawer = new Drawer();
		def retCode = drawer.matcher(personsABCFail, personsABCFail)
				
		Assert.assertTrue("Supposing that personZ is unmachable", !retCode)
		Assert.assertTrue("No match is possible", personA.foundDraw == null)
		
		Util.logMatches(personsABCFail)
	}
}
