package com.cinchapi.concourse.model;

import java.util.Set;

import com.google.common.primitives.UnsignedLong;

/**
 * Test cases for {@link HeapBasedConcourse}
 * @author jnelson
 *
 */
public class HeapBasedConcourseTest extends AbstractConcourseTest{
	
	@Override
	protected boolean limitScaleTests() {
		return true;
	}
	
	private static int defaultExpectedNumColumnsPerRow = 4;
	
	@Override
	protected Concourse getPopulatedInstance() {
		Concourse concourse = getEmptyInstance();
		int rows = rand.nextInt(10)+1;
		int columns = rand.nextInt(5)+1;
		int values = rand.nextInt(10)+1;
		for(int i = 0; i < rows; i++){
			UnsignedLong row = getNextRow();
			for(int j = 0; j < columns; j++){
				String column = getRandomColumn();
				for(int k = 0; k < values; k++){
					Object value = getRandomValue();
					concourse.add(row, column, value);
				}
			}
		}
		return concourse;
	}

	@Override
	protected Concourse getEmptyInstance() {
		return new HeapBasedConcourse(defaultExpectedNumColumnsPerRow);
	}
	
	@Override
	public void testAdd(){
		super.testAdd();
		Concourse concourse = getEmptyInstance();
		
		//test adding multiple types to a column in a row
		UnsignedLong row = getNextRow();
		String column = getRandomColumn();
		String v1 = getRandomValueString();
		Number v2 = getRandomValueNumber();
		Boolean v3 = getRandomValueBoolean();
		assertTrue(concourse.add(row, column, v1));
		assertTrue(concourse.add(row, column, v2));
		assertTrue(concourse.add(row, column, v3));
		Set<Object> values = concourse.get(row, column);
		assertEquals(3, values.size());
		assertTrue(values.contains(v1));
		assertTrue(values.contains(v2));
		assertTrue(values.contains(v3));
		
		UnsignedLong row2 = getNextRow();
		//can't add a relation to a non-existing row
		try{
			concourse.add(row, column, row2);
			fail("Expecting IllegalArgumentException");
		}
		catch(IllegalArgumentException e){}
		
		//can add relation to existing row
		assertTrue(concourse.add(row2, column, row));
		concourse.add(row, column, row2);
	}

}