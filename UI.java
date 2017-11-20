import java.util.Scanner; 
import java.util.Iterator;

/**
 * Handles the user interface
 */
public class UI {
    String filename;
    Scanner in = new Scanner(System.in);
    Control control;
    
    /**
     * Takes the filename to be used for reading in beers from and to
     * write the beers to when quitting.
     * Creates the Control-object.
     *
     * @param filename The filename of the file used for storing the beers
     */
    public UI(String filename) {
	this.filename = filename;
	control = new Control(filename);
    }
    
    /**
     * Runs a command loop, which takes in commands from the user and executes that command
     */
    public void run() {
	int command = -1;
	while(command != 0) {
	    System.out.println("\nWhat do you want to do?");
	    System.out.println("0. Quit");
	    System.out.println("1. Add beer");
	    System.out.println("2. Remove beer");
	    System.out.println("3. Show beers");
	    
	    System.out.print("Enter command: ");
	    command = in.nextInt();in.nextLine();

	    switch(command) {
	    case 0:
		quit();
		break;
	    case 1:
		addBeer();
		break;
	    case 2:
		removeBeer();
		break;
	    case 3:
		showBeers();
		break;
	    default:
		System.out.println("Illegal command! Try again.");
	    }
	}

    }
    /**
     * Writes to file and quits
     */
    private void quit() {
	control.writeFile();
	System.out.println("\nQuitting! Goodbye!");
    }

    /**
     * Gets the name, expiration date and number of bottles from the user.
     * If the beer already exist, then that Beer is updated with the new bottles.
     * Else the it gets beer style, country, abv, and volume (in cl) from the user
     * and creates the beer
     */
    private void addBeer() {
	System.out.print("\nEnter name of beer: ");
	String name = in.nextLine().trim();
	System.out.print("Enter expiration date (dd/mm/yyyy): ");
	String dateString = in.nextLine().trim();
	System.out.print("Enter number of bottles: ");
	int count = in.nextInt();in.nextLine();

	/* Checks if the beer exists or not, and calls the correct method in Control 
	 * Takes in additonal info if needed
	 */
	if(control.hasBeer(name, dateString)) {
	    control.addBeer(name, dateString, count);
	}
	else {
	    System.out.print("Enter beer style: ");
	    String type = in.nextLine().trim();
	    System.out.print("Enter country: ");
	    String country = in.nextLine().trim();
	    System.out.print("Enter ABV: ");
	    double abv = in.nextDouble();in.nextLine();
	    System.out.print("Enter volume of bottle/can (in cl): ");
	    double volume = in.nextDouble();in.nextLine();
	    
	    control.addBeer(name, dateString, count, country, type, abv, volume);
	}
    }

    /**
     * Asks the user for name, expiration date and number of bottles, and
     * then removes the number of bottles.
     */
    private void removeBeer() {
	System.out.print("\nEnter name of beer: ");
	String name = in.nextLine().trim();
	System.out.print("Enter expiration date (dd/mm/yyyy): ");
	String dateString = in.nextLine().trim();
	System.out.print("Enter number of bottles to remove: ");
	int count = in.nextInt();in.nextLine();

	control.removeBeer(name, dateString, count);
    }

    /**
     * Gets how the user want the beers to be sorted and then gets the beers sorted that way.
     * It then prints out the beers to terminal in an formatted way
     */
    private void showBeers() {
	int command;
	Iterator<Beer> beerIt = null;

	System.out.println("\nHow do you want the beers sorted?");
	System.out.println("1. By name");
	System.out.println("2. By country");
	System.out.println("3. By beer style");
	System.out.println("4. By expiration date");
	
	// Keeps on until the user gives an legal command
	do {
	    System.out.print("Enter command: ");
	    command = in.nextInt();in.nextLine();
	    if(command < 1 || command > 4) {
		System.out.println("Illegal command!");
	    }
	} while(command < 1 || command > 4);	

	switch(command) {
	case 1:
	    beerIt = control.getBeersByName();
	    break;
	case 2:
	    beerIt = control.getBeersByCountry();
	    break;
	case 3:
	    beerIt = control.getBeersByType();
	    break;
	case 4:
	    beerIt = control.getBeersByDate();
	    break;
	}
	
	// First prints out an header line
	System.out.format("%-30s | %-10s | %-15s | %6s | %8s | %15s | %5s\n", "Name", "Country", "Beer style", 
			  "ABV", "Volume", "Expiration date", "Number of bottles");
	
	// Prints out an line to separate the header from the beers
	for(int i=0;i < 119; i++) System.out.print("-");
	System.out.println("");

	// Prints out all the beers
	String[] beerFields;
	while(beerIt.hasNext()) {
	    beerFields = beerIt.next().toString().split(";");
	    System.out.format("%-30s | %-10s | %-15s | %5s%% | %5s cl | %15s | %5s\n", beerFields[0], beerFields[1], 
			      beerFields[2], beerFields[3], beerFields[4], beerFields[5], beerFields[6]); 
	}
    }
}
