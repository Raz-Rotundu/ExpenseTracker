package com.lumius.ExpenseTracker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * ExpenseList -- A singleton class containing a list of expense items, as well as defining operations to modify them
 * @author Razvan Rotundu
 */
public class ExpenseList {
	private static final Headers[] HEADERS = {Headers.id, Headers.timeCreated, Headers.description, Headers.amount};
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
	private static final CSVFormat WRITERCSVFORMAT = CSVFormat.DEFAULT.builder()
			.setHeader(Headers.class)
//			.setSkipHeaderRecord(true)
			.get();
	
	private static final CSVFormat READERCSVFORMAT = CSVFormat.DEFAULT.builder()
			.setHeader(Headers.class)
			.setSkipHeaderRecord(true)
			.get();
	public static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormatter.ofPattern("yy-MM-dd");
	
	private static ExpenseList instance;
	
	private List<ExpenseRecord> list;
	
	private ExpenseList() {
		list = new ArrayList<ExpenseRecord>();
	}
	
	private ExpenseList(List<ExpenseRecord> inlist) {
		list = inlist;
	}
	
	/**
	 * Method to return instance of singleton if it exists, or create a new one if it does not
	 * @return instance of ExpenseList
	 */
	public static ExpenseList getInstance() {
		if(instance == null) {
			instance = new ExpenseList();
		}
		return instance;
	}
	
	/**
	 * Method to add a new expense to the expense list
	 * @param rec an ExpenseRecord object
	 */
	public void add(ExpenseRecord rec) {
		list.add(rec);
	}
	
	
	/**
	 * Method to delete the expense record with the given ID out of the list
	 * @param id the ID value of the expense to be deleted
	 */
	public void remove(int id) {
		List<ExpenseRecord> newList = list.stream()
		.filter(r -> r.id() == id)
		.collect(Collectors.toCollection(ArrayList<ExpenseRecord>::new));
		
		if(newList.size() == list.size()) {
			System.out.println("ID not in list!");
		} else {
			list = newList;
		}
	}
	
	/**
	 * Generates a string representation of all expense entries in the list
	 * @return a string representation of the whole list
	 */
	public String getAll() {
		if(list.size() == 0) {
			return "No expenses!";
		}
		else {
			StringBuilder out = new StringBuilder();
			out.append(String.format("ID  Date        Description  Amount%n"));
			out.append(String.format("-----------------------------------%n"));
			
			list.stream()
			.forEach(e -> {
				out.append(e.toString());
			});
			
			return out.toString();
		}

	}
	
	/**
	 * Generates a short string summarizing the total expenses from all list items
	 * @return String describing total expenses so far
	 */
	public String getSummary() {
		return String.format("Total expenses: $%s", String.valueOf(getSum(list)));
		
	}
	
	/**
	 * Returns an integer sum of all expenses in the given list
	 * @param list a list of ExpenseRecord objects
	 * @return integer sum of the amount fields of all objects in the list
	 */
	private int getSum(List<ExpenseRecord> list) {
		Optional<Integer> sum = list.stream()
		.map(ExpenseRecord::amount)
		.reduce((t, a) -> t + a);
		
		if(sum.isEmpty()) {
			return 0;
		} else {
			return sum.get();
		}

	}
	
	/**
	 * Returns a single sentence summary of all expenses within given month
	 * @param month numerical value of a month (Eg 8 = August)
	 * @return A string representing total expenses for given month
	 */
	public String getSummary(int month){
		String monthName = capitalize(Month.of(month));
		Optional<Integer> sum = list.stream()
				.filter(k -> k.timeCreated().getMonthValue() == month)
				.map(ExpenseRecord::amount)
				.reduce((t, a) -> t + a);
		if(sum.isEmpty()) {
			return(String.format("There are no entries for the month of %s", monthName));
		} else {
			return(String.format("Total expenses for %s: $%d", monthName, sum.get()));
		}
		
	}
	/**
	 * Helper method to capitalize the month names
	 * @param in a Month object
	 * @return A string representation of the month's name capitalized
	 */
	private String capitalize(Month in) {
		String s = in.toString().toLowerCase();
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	/**
	 * Reads in a CSV representation of expenses, and parses it into a list of expense records
	 * @param location the path to the csv file
	 * @return A list of ExpenseRecords, or null if exception
	 */
	public static Optional<List<ExpenseRecord>> importCSV (Path location) {
		try{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
			
			Iterable<CSVRecord> records = READERCSVFORMAT.parse(Files.newBufferedReader(Path.of("./expenses.csv")));
			
			List<ExpenseRecord> newlist = new ArrayList<>();
			
			for(CSVRecord r : records) {
				int id = Integer.valueOf(r.get("id"));
				String str = r.get(Headers.timeCreated);
				LocalDateTime timeCreated = LocalDate.parse(str, formatter).atStartOfDay();
				String description = r.get(Headers.description);
				int amount = Integer.valueOf(r.get(Headers.amount));
				
				ExpenseRecord expense = new ExpenseRecord(id, timeCreated, description, amount);
				newlist.add(expense);
			}
			return Optional.of(newlist);
			
		} catch(IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	/**
	 * Writes the values of all expense records currently in the list to a csv file
	 * @param destination the destination file for writing the csv
	 */
	public void exportCSV (Path destination) throws IOException{
		BufferedWriter writer = Files.newBufferedWriter(destination);
		
		try(CSVPrinter printer = WRITERCSVFORMAT.print(writer)) {
			if(!(Files.exists(destination))){
				Files.createFile(destination);
			}		
			this.list.stream().forEach(e -> {
				try {
					printer.printRecord(e.id(), e.timeCreated().format(DATETIMEFORMAT), e.desc(), e.amount());
				}
				catch(IOException x) {
					System.out.println("IO error in exportCSV stream");
					x.printStackTrace();
				}
			});
		}
		catch(IOException e) {
			System.out.println("exportCSV IO error");
			e.printStackTrace();
		}

	}
	
	/**
	 * Serializes this singleton instance
	 * @return an Optional containing the Sting representation of this instance
	 */
	public Optional<String> toJSONOptional() {
		try {
			String json = OBJECT_MAPPER.writeValueAsString(this.list);
			return Optional.of(json);
		}
		catch(JsonProcessingException e) {
			System.out.printf("An error occured parsing the record list: %s%n", e.getMessage());
			return Optional.empty();
		}
	}
	
	/**
	 * Deserializes a list of expense records
	 * @param json the JSON representation of a list of ExpenseRecord Objects
	 * @return an ExpenseList object
	 */
	public static Optional<List<ExpenseRecord>> fromJSONOptional(String json) {
		try {
			List<ExpenseRecord> newList = OBJECT_MAPPER.readValue(json, new TypeReference<>() {});
			return Optional.of(newList);
		}
		catch(JsonProcessingException e) {
			System.out.printf("An error occured deserializing from json: %s%n", e.getMessage());
			return Optional.empty();
		}

	}
	
	/**
	 * returns the list field of the current instance
	 * @return the list field of the current instance
	 */
	public List<ExpenseRecord> getList(){
		return this.list;
	}
	
	/**
	 * sets the list field of the current instance
	 * @param list the list to replace current one with
	 */
	public void setList(List<ExpenseRecord> list) {
		this.list = list;
	}
}
