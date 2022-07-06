import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

// Homework 1: Sales Register Program
// Course: CIS357
// Due date: 7/6/2022
// Name: Dakota Morr
// GitHub: https://github.com/DMMorr/cis357-hw1-Morr
// Instructor: Il-Hyung Cho
// Program description: This program generate a cash register for a user based on the Items from the input file.
// This program can be used to add up quantities of items, find the subtotal/total with tax, and give change based on a Tendered amount.

/** 
 * @author Dakota Morr
 * The item class contains a constructor for each item from the given data file, 
 * methods to get item names/unit price, and a method to read said input data
 */
class Item {
    // Arrays to store data
	/** itemCode - the code to input into the cash register for each item */
    public int[] itemCode;
    /** itemName - the name assosciated with each itemCode */
    public String[] itemName;
    /** unitPrice - the price assosciated with each item */
    public float[] unitPrice;

    // Item constructor
    Item() {
        String[] data = new String[0];
        // calls method to read data from the file into an array
        data = this.readFile(data); 
        // Seperate variables based on each line of data
        itemCode = new int[data.length];
        itemName = new String[data.length];
        unitPrice = new float[data.length];
        // splits data by each comma and applies them to each variable
        for (int i = 0;i < data.length;i++) { 
            String[] splitData = data[i].split(",");
            itemCode[i] = Integer.parseInt(splitData[0]);
            itemName[i] = splitData[1];
            unitPrice[i] = Float.parseFloat(splitData[2]);
        }
    }

    /**
     * 
     * @param fileData - puts each line of the input file in an array
     * @return fileData - data of the file in an array
     */
    public String[] readFile(String[] fileData) {
        try {
            File inFile = new File("input.txt"); // input file
            int arrSize = 0 ;
            String line;

            BufferedReader br = new BufferedReader(new FileReader(inFile));
            while ((line = br.readLine())!=null) { // add array size for each line until null
                arrSize ++;
            }
            br.close(); //close buffered reader
         // creates new data for how many lines there is in the file
            fileData = new String[arrSize]; 
            br = new BufferedReader(new FileReader(inFile));
            int i = 0;
            while ((line = br.readLine())!=null) { // assigns data from each line to its slot in the fileData array
                fileData[i] = line;
                i++;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace(); // throw exceptions for File errors during execution
        }
        return fileData; // returns file data if there's no exception
    }
/**
 * @param code - number assosciated with itemName
 * @return itemName
 */
    // getName function - gets itemName
    public String getName(int code) {
        return itemName[code-1];
    }
/**
 * @param code - number assosciated with unitPrice
 * @return unitPrice
 */
    // getPrice function - get unitPrice
    public double getPrice(int code) {
        return unitPrice[code - 1];
    }

}

/** 
 * @author Dakota Morr
 * The CashRegister class is the driver class for the program. This class only contains the main method, 
 * and this handles all computing in the program. This method takes in all items and their quantities, 
 * computes the total with/without tax, handles change from a given amount, and handles all user input errors that could be involved.
 */
public class CashRegister {
    public static void main(String[] args) {
        // creating scanner object for user input
        Scanner s = new Scanner(System.in);
        // Variables to store data
        int[] quantityTrack = new int[100];
        String[] data = new String[100];
        int count = 0;
        int code;
        double subtotal = 0;
        double sales = 0;
        boolean repeat = false;
        System.out.println("Welcome to Morr's cash register system!");
        System.out.println("");
        System.out.print("Beginning a new sale (Y/N) ");
        String ans = s.next();
        System.out.println("");
        System.out.println("--------------------");
        while (ans.toLowerCase(Locale.ROOT).equals("y")) { // checks for each case of y to make sure letter is correct no matter upper/lower
        	subtotal = 0;
        	if (ans.toLowerCase(Locale.ROOT).equals("y")) {
            	do { 
                    Item item1 = new Item();
                    System.out.print("Enter product code: ");
                    String productCode = s.next();
                    // Checking for errors in the input and asking for another if invalid
                    while (productCode.charAt(0) < 45 || productCode.charAt(0) > 57 || productCode.charAt(0) == 46 || productCode.charAt(0) == 47 || Integer.parseInt(productCode) < -1 || Integer.parseInt(productCode) > item1.itemName.length) {
                        System.out.println("!!! Invalid product code");
                        System.out.println("");
                        System.out.print("Enter product code: ");
                        productCode = s.next();
                    }
                    code = Integer.parseInt(productCode);                    
                    if (code == -1) { // end when the product code is -1
                        continue;
                    }
                    System.out.println("         item name: " + item1.getName(code));
                    String itemName = item1.getName(code);
                    double price = item1.getPrice(code);
                    // assign name, price, and get the quantity of the item
                    System.out.print("Enter quantity:     ");
                    productCode = s.next(); 
                    // Check for errors in the input and asking for another if invalid
                    while (productCode.charAt(0) < 48 || productCode.charAt(0) > 57) {
                        System.out.println("!!! Invalid quantity");
                        System.out.println("");
                        System.out.print("Enter quantity code: ");
                        productCode = s.next();
                    }
                    int quantity = Integer.parseInt(productCode);
                    double total = quantity * price; // get and print total
                    System.out.println("        item total: $  " + String.format("%.2f", total));
                    System.out.println("");
                    subtotal = subtotal + total;
                    for (int i = 0; i < count; i++) {
                    if (data[i].contains(itemName)) { // Add quantity to the first instance of an item if one exists to avoid repeats
                    	repeat = true;
                    	quantityTrack[i] = quantityTrack[i] + quantity;
                    	data[i] = String.format("%2d %-13s $  %.2f%n", quantityTrack[i], itemName, (quantityTrack[i])*(price));
                    }
                    }
                    if (repeat == true) { // Only run statement if not a repeat to avoid repeats in the list
                    	repeat = false;
                    } else {
                    quantityTrack[count] = quantity;
                    data[count] = String.format("%2d %-13s $  %.2f%n", quantity, itemName, total); // add data to end of the list if first instance
                    count++;
                    }
                } while (code != -1); // go until the item code is -1
            	
            	for (int i = 0; i < count-1; i++) { // For statement to align the data alphabetically
            		if (data[i].charAt(3) < data[i+1].charAt(3)) {
            			String tmp = data[i];
            			data[i] = data[i+1];
            			data[i+1] = tmp;
            		}
            	}
            	for (int i = 0; i < count / 2; i++) { // reverse the array so the list is alphabetical
            	    String temp = data[i];
            	    data[i] = data[count - i - 1];
            	    data[count - i - 1] = temp;
            	}
                System.out.println("----------------------------");
                System.out.println("Items list:");
                for (String dataOut : data) { // prints item list using other variables
                    if (dataOut!=null) {
                        System.out.println("   " + dataOut);
                    }
                }
                for (int i = 0; i < count; i++) { // make data null at the start of a new sale
                    data[i] = null;	
                    }
                    count = 0;
                System.out.println("Subtotal            $  " + String.format("%.2f", subtotal));
                double totalWithTax = subtotal + subtotal * 6 / 100; // get tax from subtotal and format outputs of the list
                System.out.println("Total with Tax (6%) $  " + String.format("%.2f", totalWithTax));
                System.out.print("Tendered amount     $  ");
                double tenderedAmt = Double.parseDouble(s.next());
                // If the tendered amount is less, asks again until enough is given
                while (true) {
                    if (totalWithTax - tenderedAmt > 0) {
                        System.out.println("Enter tendered amount again.");
                        System.out.print("                    $");
                        tenderedAmt = Double.parseDouble(s.next());
                    } else {
                        break;
                    }
                }

                sales  = sales + subtotal; // add subtotal for each sale together for the end of day count
                double change = tenderedAmt > totalWithTax ? tenderedAmt - totalWithTax : 0;
                System.out.println("Change              $  " + String.format("%.2f", change));
                System.out.println("---------------------------");
                
                System.out.println();
                System.out.println();
                
                System.out.print("Beginning a new sale (Y/N) "); 
                ans = s.next(); // input if another sale will be started or not
                System.out.println("--------------------");
            }
        }

        if (ans.toLowerCase(Locale.ROOT).equals("n")) { // if the answer to product code is n, print sales for the day and say goodbye
            System.out.println("Total sales of the day is $ "+String.format("%.2f", sales));
            System.out.println("Thanks for using POST System. Goodbye.");
        }
    }
}
