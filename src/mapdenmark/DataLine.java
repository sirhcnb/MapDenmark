package mapdenmark;

import java.util.HashMap;

/**
 * This class parses a line of comma-separated lines of data, with
 * strings delimited by single-quotes and optionally containing
 * commas. 
 *
 * String values are manually interned, i.e., each string in the 
 * parsed data will be represented in-core only once. 
 *
 * @author Peter Tiedemann petert@itu.dk
 *
 * Peter Sestoft 2008: Modified to avoid building and 
 * destroying a LinkedList.
 *
 * Søren Debois 2014: Moved Peter Sestoft's manual string interning 
 * here. 
 */
public class DataLine {

	private static HashMap<String,String> interner = 
		new HashMap<String,String>();

	private String intern(String s){
		String interned = interner.get(s);
		if (interned != null) 
			return interned;
		else {
			interner.put(s, s);
			return s;
		}
	}

	/**
	 * Reset the interner map. This may conserve space if not all
	 * strings in the input data set are used. 
	 */
	public static void resetInterner() {
		interner = new HashMap<String,String>();
	}

	private final String line;
	int next;

	public DataLine(String line){
		this.line = line;
		next = 0;
	}

	/**
	 * Returns true if there are more tokens, and false otherwise
	 */
	public boolean hasMore() {
		return next < line.length();
	}

	/**
	 * Returns the next token. If apostrophes surround the token, they overrule commas, and the token is returned without the apostrophes
	 */
	private String nextToken() {
		if (line.charAt(next) != '\'') {
			int comma = line.indexOf(',', next);
			String token;
			if (comma >= 0) { // Comma separator found
				token = line.substring(next, comma);
				next = comma + 1;
			} else {          // This is the last data field
				token = line.substring(next);
				next = line.length();
			}      
			return token;
		} else { 
			int quote = line.indexOf('\'', next+1);
			String token;
			if (quote >= 0) { // End of string found
				token = line.substring(next+1, quote);
				next = quote + 2;
			} else {          // Malformed string
				next = line.length();
				throw new IllegalArgumentException("Cannot parse: " + line.substring(next));
			}      
			return token;
		}
	}

	/**
	 * Attempts to parse the next token as an integer
	 */
	public int getInt() {
		String s = nextToken();
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			if (s.charAt(0) == '*') return -1;
			throw new IllegalArgumentException(s + " is not a integer!");
		}
	}

	/**
	 * Attempts to parse the next token as a double
	 */
	public double getDouble() {
		String s = nextToken();
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(s + " is not a double!");
		}
	}

	/**
	 * Returns the next token as a string
	 * @return
	 */
	public String getString(){
		return intern(nextToken());
	}

	/**
	 * Discard the current token
	 *
	 */
	public void discard(){
		nextToken();
	}
}