package com.lumius.ExpenseTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ExpenseTracker application -- track expenses at the command line, save the values, import and export to CSV
 * @author Razvan Rotundu
 */
public class App 
{
	public static Path csvPath = Path.of("./expenses.csv");
	
    public static void main( String[] args )
    {
    	ExpenseList list = ExpenseList.getInstance();
    	
    	list.add(new ExpenseRecord(0, LocalDateTime.now(), "Test0", 000));
    	list.add(new ExpenseRecord(1, LocalDateTime.now(), "Test1", 100));
    	list.add(new ExpenseRecord(2, LocalDateTime.now(), "Test2", 200));
    	
    	try {
        	list.exportCSV(csvPath);
        	System.out.println("CSV Exported?");
        	
        	List<ExpenseRecord> records = ExpenseList.importCSV(csvPath).get();
        	records.stream()
        	.forEach(System.out::println);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }
}
