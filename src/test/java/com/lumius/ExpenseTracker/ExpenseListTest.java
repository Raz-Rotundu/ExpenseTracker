package com.lumius.ExpenseTracker;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExpenseListTest {


	public static ExpenseList listObj;
	
	@BeforeAll
	static void setUp() {
		listObj = ExpenseList.getInstance();
	}
	
	
	@Test
	void testGetters() {
		List<ExpenseRecord> records = new ArrayList<>();
		listObj.setList(records);
		
		assertEquals(0, records.size());
	}
	
	@Test
	void testAdd() {
		List<ExpenseRecord> records = listObj.getList();
		listObj.setList(records);
		
		listObj.add(new ExpenseRecord(0, LocalDateTime.now(), "Test0", 100));
		listObj.add(new ExpenseRecord(1, LocalDateTime.now(), "Test1", 200));
		assertEquals(2, listObj.getList().size());
	}
	
	@Test
	void testRemove(){
		List<ExpenseRecord> records = listObj.getList();
		listObj.setList(records);
		listObj.add(new ExpenseRecord(0, LocalDateTime.now(), "Test0", 100));
		listObj.add(new ExpenseRecord(1, LocalDateTime.now(), "Test1", 200));
		
		listObj.remove(1);
		assertEquals(1, listObj.getList().size());
	}
	
	@Test
	void testSetters() {
		List<ExpenseRecord> test = new ArrayList<>();
		test.add(new ExpenseRecord(2, LocalDateTime.now(), "Test2", 300));
		test.add(new ExpenseRecord(3, LocalDateTime.now(), "Test3", 400));
		test.add(new ExpenseRecord(4, LocalDateTime.now(), "Test4", 500));
		test.add(new ExpenseRecord(4, LocalDateTime.now(), "Test5", 600));
		
		listObj.setList(test);
		assertEquals(4, listObj.getList().size());
		
	}

}
