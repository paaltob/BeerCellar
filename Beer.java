import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class for a beer
 * Stores the name, country and beer type of the beer as strings.
 * Stores expiration date as a Date object
 * Stores alcohol by volume as an double
 * Stores volume (in cl) as an double
 * Stores the number of bottles/cans as an integer
 */
public class Beer {
    String name, country, type;
    LocalDate expireDate;
    double abv, volume;
    int count;

    /**
     * Sets all the values of the Beer
     *
     * @param name The name of the beer
     * @param country The country of the beer
     * @param type The beer style
     * @param expireDate The expiration date of the beer
     * @param abv The ABV of the beer
     * @param volume The volume of the beer in cl
     * @param count The number of bottles/cans
     */
    public Beer(String name, String country, String type, LocalDate expireDate, 
		double abv, double volume, int count) {
	this.name = name;
	this.country = country;
	this.type = type;
	this.expireDate = expireDate;
	this.abv = abv;
	this.volume = volume;
	this.count = count;
    }

    /**
     * To get a String representation of the Beer
     *
     * @return An String representation of the Beer
     */
    public String toString() {
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	return String.format("%s;%s;%s;%.1f;%.1f;%s;%d", name, country, type, abv, volume, 
			     expireDate.format(dateFormat), count);
    }

    /**
     * Add new bottles to the Beer
     *
     * @param newBottles The number of bottles to be added
     */
    public void addBottles(int newBottles) {
	count += newBottles;
    }

    /**
     * Removes bottles from the Beer. It checks if the number of bottles becomes 0 (or lower)
     *
     * @param oldBottles The number of bottles to be removed
     * @return true if the number of bottles becomes 0 (or lower), false if it doesn't
     */
    public boolean removeBottles(int oldBottles) {
	count -= oldBottles;
	if(count <= 0) return true;
	else return false;
    }
}
