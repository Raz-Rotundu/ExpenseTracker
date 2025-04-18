package com.lumius.ExpenseTracker;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ExpenseTracker application -- track expenses at the command line, save the values, import and export to CSV
 * @author Razvan ROtundu
 */
public class App 
{
    public static void main( String[] args )
    {
        ExpenseList list = ExpenseList.getInstance();
        
        list.add(new ExpenseRecord(0, LocalDateTime.now(), "Test0", 000));
        list.add(new ExpenseRecord(1, LocalDateTime.now(), "Test1", 100));
        list.add(new ExpenseRecord(2, LocalDateTime.now(), "Test2", 200));
        
        String json = list.toJSONOptional().get();
        List<ExpenseRecord> generated = ExpenseList.fromJSONOptional(json).get();
        generated.add(new ExpenseRecord(3, LocalDateTime.now(), "AHOY", 420));
        list.setList(generated);
        
        System.out.println(list.getAll());
    }
}
