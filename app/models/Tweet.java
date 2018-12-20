package models;

/**
 * 
 * Basic setter getter class for Tweet with a default constructor.
 * Stores data from form during POST call.
 * 
 * @author Sadaf
 * @since V_1.2
 * @version 1.0.0
 * 
 */
public class Tweet {

	public String name;

	public Tweet() {
	}

	/**
	 * Class default Constructor accepting name as argument
	 * @param name - String
	 */
	
	public Tweet(String name) {
		this.name = name;
	}

	/**
	 * Gets the name
	 * @return name.
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * @param name - String
	 */
	
	public void setName(String name) {
		this.name = name;
	}
}
