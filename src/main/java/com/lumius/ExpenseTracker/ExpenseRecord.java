package com.lumius.ExpenseTracker;

import java.time.LocalDateTime;

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

}
