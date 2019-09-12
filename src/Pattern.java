import java.io.*;
/**
 * Pattern class, converts user's string history into a pattern
 * @author John
 *
 */
public class Pattern implements Serializable {
	/**
	 * string to convert into a pattern
	 */
	private String pattern;
	
	/**
	 * default constructor
	 * @param p string to convert
	 */
	public Pattern(String p)
	{
		pattern = p;
	}
	
	/**
	 * returns pattern in string form
	 * @return pattern in string form
	 */
	public String getPattern()
	{
		return pattern;
	}
	
	@Override
	/**
	 * returns if a pattern is equal to another pattern
	 * @param o pattern to compare with
	 * @return true if pattern is equal to another pattern
	 */
	public boolean equals(Object o)
	{
		if (o instanceof Pattern)
		{
			Pattern p = (Pattern) o;
			return pattern.equals(p.pattern);
		}
		return false;
	}
	
	@Override
	/**
	 * overrides hashcode function
	 * @return hashcode of pattern
	 */
	public int hashCode()
	{
		final int prime = 5;
		int result = 1;
		result = prime * result + pattern.hashCode();
		return result;
	}

}
