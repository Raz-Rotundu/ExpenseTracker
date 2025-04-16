package com.lumius.ExpenseTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ExpenseList -- A singleton class containing a list of expense items, as well as defining operations to modify them
 * @author Razvan Rotundu
 */
public class ExpenseList {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
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
	
	
	//TODO handling a nonexistent ID
	/**
	 * Method to delete the expense record with the given ID out of the list
	 * @param id the ID value of the expense to be deleted
	 */
	public void delete(int id) {
		list.stream()
		.filter(r -> r.id() == id)
		.collect(Collectors.toCollection(ArrayList<ExpenseRecord>::new));
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
			out.append("ID  Date       Description  Amount");
			out.append("----------------------------------");
			
			list.stream()
			.forEach(out::append);
			
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
	
	public String getSummary(int month){
		Optional<Integer> sum = list.stream()
				.filter(k -> k.timeCreated().getMonthValue() == month)
				.map(ExpenseRecord::amount)
				.reduce((t, a) -> t + a);
		if(sum.isEmpty()) {
			return "There are no entries this month";
		} else {
			return(String.format("Total expenses for %s: $%d", Month.of(month), sum));
		}
		
	}
	
	public Optional<List<ExpenseRecord>> importCSV (Path location) {
		try{
			Headers[] HEADERS= {Headers.id, Headers.timeCreated, Headers.description, Headers.amount};
			CSVFormat csvformat = CSVFormat.DEFAULT.builder()
					.setHeader(Headers.class)
					.setSkipHeaderRecord(true)
					.get();
			
			Iterable<CSVRecord> records = csvformat.parse(Files.newBufferedReader(Path.of("./expenses.csv")));
			
			List<ExpenseRecord> newlist = new ArrayList<>();
			
			for(CSVRecord r : records) {
				int id = Integer.valueOf(r.get("id"));
				LocalDateTime timeCreated = LocalDateTime.parse(r.get(Headers.timeCreated));
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
	
	public void exportCSV (Path destination) {
		
	}
	
	/**
	 * Serializes this singleton instance
	 * @return an Optional containing the Sting representation of this instance
	 */
	public Optional<String> toJSON() {
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
	 * Deserializes aninstance of ExpenseList
	 * @param json the JSON representation of an ExpenseLIst object
	 * @return an ExpenseList object
	 */
	public Optional<ExpenseList> fromJSON(String json) {
		try {
			instance = OBJECT_MAPPER.readValue(json, this.getClass());
			return Optional.of(instance);
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
