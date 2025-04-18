package com.lumius.ExpenseTracker;

import java.nio.file.Path;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
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
	
    public static void main( String[] args )
    {
    	//Definition
    	Options options = new Options();
    	
    	//Primary action
    	String primaryAction = args[0];
    	
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
    			.longOpt("is")
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
        	//TODO
        	//Interrogation
    	}
    	catch(ParseException e) {
    		System.out.printf("Error parsing command line: %s%n", e.getMessage());
    	}

    	

    	
    }
}
