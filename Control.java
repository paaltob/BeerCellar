import java.util.HashMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Class for handling modifing and getting the beers.
 * The beers are stored in an HashMap with an combination of name
 * and date as the keys. It also handles reading in beers from file when
 * the program starts and writing the beers to file when the program quits
 */
public class Control {
    private String filename;
    private HashMap<String, Beer> beers;

    /**
     * Creates the HashMap for the beers and starts reading in the beers
     * from file
     *
     * @param filename The filename of the file that is used for storing the beers
     */
    public Control(String filename) {
	this.filename = filename;
	beers = new HashMap<String, Beer>();
	if(filename != null) {
	    readFile();
	}
    }

    /**
     * Reads in the beers from file if the file exists.
     */
    private void readFile() {
	Scanner fileReader = null;
	try {
	    File file = new File(filename);
	    if(file.isFile()) {
		fileReader = new Scanner(file);
		
		String[] beerFields;
		while(fileReader.hasNextLine()) {
		    beerFields = fileReader.nextLine().trim().split(";");
		    System.out.println("Adding " + beerFields[0]);
		    addBeer(beerFields[0], beerFields[5], Integer.parseInt(beerFields[6]), beerFields[1], 
			    beerFields[2], Double.parseDouble(beerFields[3]), Double.parseDouble(beerFields[4]));
		    
		}
	    }
	}
	catch(Exception e) {
	    System.out.println("Something went wrong reading the file!" + e);
	}
	finally {
	    if(fileReader != null) fileReader.close();
	}
    }

    /**
     * Writes the beers to file. Gets all the beers and writes them to file
     * using the Beer's toString()
     */
    public void writeFile() {
	if(filename == null) {
	    return;
	}

	try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
	    Iterator<Beer> beerIt = getBeersByName();
	    while(beerIt.hasNext()) {
		bw.write(beerIt.next().toString());
		bw.write("\n");
	    }
	}
	catch(Exception e) {
	    System.err.println("Something went wrong wile writing the file!");
	}
    }

    /**
     * Checks if the beer exist. 
     *
     * @param name The name of the beer
     * @param dateString The expiration date of the beer as a String on the format dd/MM/yyyy
     * @return True if the beer exist, false if it doesn't
     */
    public boolean hasBeer(String name, String dateString) {
	String id = name + dateString;
	
	return beers.containsKey(id);
    }

    /**
     * Used for adding new bottles for a beer that already exist
     *
     * @param name The name of the beer
     * @param dateString The expiration date of the beer as String on the format dd/MM/yyyy
     * @param count The number of bottles
     */
    public void addBeer(String name, String dateString, int count) {
	Beer beer = beers.get(name + dateString);
	beer.addBottles(count);
    }

    /**
     * Used for creating a new beer.
     *
     * @param name The name of the beer
     * @param dateString The expiration date of the beer as String on the format dd/MM/yyyy
     * @param count The number of bottles
     * @param country The country of the beer
     * @param type The beer style of the beer
     * @param abv The ABV of the beer
     * @param volume The volume of the beer in cl
     */
    public void addBeer(String name, String dateString, int count, String country, String type, double abv, double volume) {
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	try {
	    Date expireDate = df.parse(dateString);
	    beers.put(name+dateString, new Beer(name, country, type, expireDate, abv, volume, count));
	}
	catch(Exception e) {
	    System.out.println("Something went wrong with parsing date");
	}
    }
    
    /**
     * Removes an given number of bottles from an beer if it exist. If the number of bottles becomes 0, the beer is removed
     *
     * @param name The name of the beer
     * @param dateString The expiration date of the beer as String on the format dd/MM/yyyy
     * @param count The number of bottles to be removed
     */
    public void removeBeer(String name, String dateString, int count) {
	if(!hasBeer(name, dateString)) {
	    System.out.println("Beer doesn't exist!");
	}
	else {
	    Beer beer = beers.get(name + dateString);
	    if(beer.removeBottles(count)) {
		beers.remove(name + dateString);
	    }
	}
    }

    /**
     * Get the beers sorted by an given Comparator
     *
     * @param comp The comparator that is to be used for the sorting
     * @return An Iterator with the beers sorted according to the given Comparator
     */
    private Iterator<Beer> getBeers(Comparator<Beer> comp) {
	List<Beer> beerList = new ArrayList<Beer>(beers.values());
	Collections.sort(beerList, comp);
	Iterator<Beer> beerIt = beerList.iterator();

	return beerIt;
    }
    
    /**
     * Gets all the beers sorted by name. If two (or more) beers have the same name, then those are
     * sorted by expiration date. 
     *
     * @return An Iterator containing the beers sorted by name
     */
    public Iterator<Beer> getBeersByName() {
	/*Creates an comparator with the specified conditions and returns the result from
	 * calling getBeers() with that comparator
	 */
	Comparator<Beer> comp = new Comparator<Beer>() {
	    @Override
	    public int compare(Beer b1, Beer b2) {
		int result = b1.name.compareTo(b2.name);
		if(result == 0) {
		    return b1.expireDate.compareTo(b2.expireDate);
		}
		else return result;
	    }
	};

	return getBeers(comp);
    }

    /**
     * Gets all the beers sorted by country. If two (or more) beers have the same country, then those are
     * sorted by name, and then by expiration date.
     *
     * @return An Iterator containing the beers sorted by country
     */
    public Iterator<Beer> getBeersByCountry() {	
	/*Creates an comparator with the specified conditions and returns the result from
	 * calling getBeers() with that comparator
	 */
	Comparator<Beer> comp = new Comparator<Beer>() {
	    @Override
	    public int compare(Beer b1, Beer b2) {
		int result = b1.country.compareTo(b2.country);
		if(result == 0) {
		    result = b1.name.compareTo(b2.name);
		    if(result == 0) {
			return b1.expireDate.compareTo(b2.expireDate);
		    }
		    else return result;
		}
		else return result;		
	    }
	};

	return getBeers(comp);
    }

    /**
     * Gets all the beers sorted by beer style. If two (or more) beers have the same type, then those are
     * sorted by name, and then by expiration date. 
     *
     * @return An Iterator containing the beers sorted by beer style
     */
    public Iterator<Beer> getBeersByType() {
	/*Creates an comparator with the specified conditions and returns the result from
	 * calling getBeers() with that comparator
	 */
	Comparator<Beer> comp = new Comparator<Beer>() {
	    @Override
	    public int compare(Beer b1, Beer b2) {
		int result = b1.type.compareTo(b2.type);
		if(result == 0) {
		    result = b1.name.compareTo(b2.name);
		    if(result == 0) {
			return b1.expireDate.compareTo(b2.expireDate);
		    }
		    else return result;
		}
		else return result;
	    }
	};

	return getBeers(comp);
    }
    
    /**
     * Gets all the beers sorted by expiration date. If two (or more) beers have the same date, then those are
     * sorted by name.
     *
     * @return An Iterator containing the beers sorted by expiration date
     */
    public Iterator<Beer> getBeersByDate() {
	/*Creates an comparator with the specified conditions and returns the result from
	 * calling getBeers() with that comparator
	 */
	Comparator<Beer> comp = new Comparator<Beer>() {
	    @Override
	    public int compare(Beer b1, Beer b2) {
		int result = b1.expireDate.compareTo(b2.expireDate);
		if(result == 0) {
		    return b1.name.compareTo(b2.name);
		}
		else return result;
	    }
	};
	
	return getBeers(comp);
    }
    
    
}