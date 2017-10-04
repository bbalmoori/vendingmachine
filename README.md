SYNOPSIS
--------
Program for a vending machine which either dispenses items and appropriate change if available or throws appropriate exception(s).

PREREQUISITES
-------------
To build the code, follow these steps.

1. Ensure that Java 8 and maven installed in your machine and path variables are set
2. Uses lombok to generate builder class (https://projectlombok.org/) [might be overkill for this project, but useful to save time]

BUILD
-----
 You can build the code and generate the package by using the following commands under the project folder

 mvn clean install

DESIGN
------
The VendingMachine class is designed with following behaviour:

 1. isOn -> check if the machine is on

 2. setOn -> turn on the machine 

	This will initialise the machine if it is not already done and initialise the machine with coin store and item store

 3. setOff -> turn off the machine

 4. insertMoney -> allows us to insert money. 

	It only accepts 10, 20, 50, 100 pence coins. 
	If any other coins are inserted (i.e. foreign coins) or if coins are inserted whilst the machine is turned off then it will throw exception


 5. selectItem	
	
	This will check the available items in the store and allows you to select an item if the machine is turned on, 
	otherwise it will throw item not available exception 

 6. coinReturn

	This will allow the user to return the coins without selecting any items, useful when we dont want to buy anything but just return the coins.
	
 7. getItemAndChange

	This will return the selected item and remaining change if appropriate denomination is available, 
	otherwise it will throw insufficient change exception and return the inserted coins wihtout the item
	
 8. CoinStore

	Returns the available coins 
	
 9. ItemStore	
	
	Returns available items in the store
	
TESTS	
-----
Appropriate unit tests are covered as required, most of the tests are contained in VendingMachineTest and ChangeDispenserTest contains few tests

KNOWN ISSUES
------------	
There is no command line interface for this program, although you can test this through unit tests provided	
	
HISTORY
-------
 Intial version of the vending machine 0.0.1
	
	
FUTURE ENHANCEMENTS
-------------------
Cucumber Tests/Integration tests
Spring Boot
Log back