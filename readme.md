# Expense Tracker Program
A CLI program to track your expenses

## Features
* Add and delete expenses to the list
* Print a complete record of all expenses to the terminal
* Print a summary of all expenses total, or for specific month
* Export expenses to CSV format

## Dependencies
**Java**: JRE 14+ (Uses records)
**Maven**: 3.6+

## Installation
Clone the repository into the desired folder. In the project directory run the following command:

```Shell
mvn clean package
```
The pom.xml is configured to create **fat jars**, so in the /target directory there should now be 2 files:

* ExpenseTracker-1.0.0.jar
* original-ExpenseTracker-1.0.0.jar

Run the following command to execute the program from the command line:

```Shell
java -jar ExpenseTracker-1.0.0.jar <options>
```
## Usage
Adding Expense:

```Shell
java -jar ExpenseTracker-1.0.0.jar add --description "Lunch" --amount 20
```
Deleting expense:

```Shell
java -jar ExpenseTracker-1.0.0.jar delete --id 2
```
Listing all expenses

```Shell
java -jar ExpenseTracker-1.0.0.jar list
```

Showing a general or monthly summary

```Shell
java -jar ExpenseTracker-1.0.0.jar summary
java -jar ExpenseTracker-1.0.0.jar summary --month 8
```
Export saved data to csv( Data dill be saved in **expenses.csv** in the project directory

```Shell
java -jar ExpenseTracker-1.0.0.jar export
```