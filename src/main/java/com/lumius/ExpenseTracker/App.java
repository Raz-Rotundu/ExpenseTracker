package com.lumius.ExpenseTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * ExpenseTracker application -- track expenses at the command line, save the values, import and export to CSV
 * @author Razvan Rotundu
 */
public class App 
{
	public static Path csvPath = Path.of("./expenses.csv");
	
	public static final String ERRORMSG = String.format("Usage: expensetracker + one of:%n"
			+ "\tadd%n"
			+ "\tdelete%n"
			+ "\tlist%n"
			+ "\tsummary%n"
			+ "\texport");
	
    public static void main( String[] args )
    {
    	// The event list
    	ExpenseList list = ExpenseList.getInstance();
    	
    	//Definition
    	Options options = new Options();
    	
    	//Primary action
    	String primaryAction = "";
    	if(args.length != 0) {
        	primaryAction += args[0];
    	} else {
    		System.out.println(ERRORMSG);
    		return;
    	}

    	
    	//Additional options
    	Option description = Option.builder("d")
    			.longOpt("description")
    			.hasArg()
                .argName("description")
                .desc("Description of the expense")
                .build();
    	options.addOption(description);
    	
    	Option amount = Option.builder("a")
    			.longOpt("amount")
    			.hasArg()
                .argName("amount")
                .desc("Amount of money spent")
                .build();
    	options.addOption(amount);
    	
    	Option id = Option.builder("i")
    			.longOpt("id")
    			.hasArg()
                .argName("ID")
                .desc("ID of existing expense record in the system")
                .build();
    	options.addOption(id);
    	
    	Option month = Option.builder("m")
    			.longOpt("month")
    			.hasArg()
                .argName("Month")
                .desc("Month number (Eg. Aug = 8)")
                .build();
    	options.addOption(month);
    	
    	//Parsing
    	DefaultParser parser = new DefaultParser();
    	
    	try {
        	CommandLine cmd = parser.parse(options, args);
        	//Interrogation
        	switch(primaryAction) {
        	case("add"):
        		System.out.println("add mode");
        	
        		if(cmd.hasOption("d") && (cmd.hasOption("a"))) {
        			// Amount and description
        			String newDesc = cmd.getOptionValue("d");
        			Integer newAmount = Integer.valueOf(cmd.getOptionValue("a") );
        			
        			//Make a unique-ish ID
        			Number newOffset = Float.valueOf(String.format("%.2f", Math.random())) * 100 ;
        			Integer newId = list.getSize() + newOffset.intValue();

        			//Date
        			LocalDateTime dateCreated = LocalDateTime.now();
        			
        			//Create and add object
        			ExpenseRecord record = new ExpenseRecord(newId, dateCreated, newDesc, newAmount);
        			list.add(record);
        			System.out.printf("Expense added successfully (ID: %d)%n", newId);
        			
        			// Save list to json
        			list.saveList();
        			
        		} else {
        			System.out.println("Usage: expensetracker add --description --amount");
        		}
        		break;
        	case("delete"):
        		if(cmd.hasOption("i")) {
        			int remid = Integer.valueOf(cmd.getOptionValue("i"));
        			list.remove(remid);
        			list.saveList();
        			System.out.println("Expense deleted successfully");
        			
        		}else {
        			System.out.println("Usage: expensetracker delete --id");
        		}
        		System.out.println("delete mode");
        		break;
        	case("list"):
        		System.out.println(list.getAll());
        		break;
        	case("summary"):
        		if(cmd.hasOption("m")) {
        			int m = Integer.valueOf(cmd.getOptionValue("m"));
        			System.out.println(list.getSummary(m));
        		} else {
        			System.out.println(list.getSummary());
        		}
        		System.out.println("summary mode");
        		break;
        	case("export"):
        		try {
            		list.exportCSV(csvPath);
            		System.out.printf("Data exported to %s", csvPath.toString());
        		}
            	catch(IOException e ){
            			e.printStackTrace();
            		}

        		break;
        	default:
        		System.out.println(ERRORMSG);
        		break;
        	}
    	}
    	catch(ParseException e) {
    		System.out.printf("Error parsing command line: %s%n", e.getMessage());
    	}

    	

    	
    }
}
