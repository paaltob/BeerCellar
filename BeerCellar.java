/**
 * Contains the main-method and starts the UI
 */
public class BeerCellar {
    public static void main(String[] args) {
	System.out.println("Welcome to Beer cellar!");
	UI ui = new UI("beers.txt");
	ui.run();
    }

}
