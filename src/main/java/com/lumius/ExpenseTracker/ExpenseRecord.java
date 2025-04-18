package com.lumius.ExpenseTracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExpenseRecord -- A record of an expense event, containing info about id, time, description, and dollar amount
 * @author Razvan Rotundu
 */
public record ExpenseRecord(
		int id,
		LocalDateTime timeCreated,
		String desc,
		int amount
		) {
	
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
		// ID  Date        Description  Amount
		// 1   2024-08-06  Lunch        $20
		
		String s = String.format("%-2d  %-10s  %-11s  $%-6s%n", id, timeCreated.format(formatter), desc, String.valueOf(amount));
		return s;
		
	}

}
