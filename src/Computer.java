import java.util.*;
import java.io.*;
/**
 * Computer class, contains AI to make prediction based on user's pattern
 * @author John
 *
 */
public class Computer implements Serializable {
	/**
	 * map that holds all patterns and number of times user enters the same pattern
	 */
	private HashMap<Pattern, Integer> map;
	
	/**
	 * history of user entered options
	 */
	private Vector<String> history;
	
	/**
	 * default constructor
	 */
	public Computer()
	{
		map = new HashMap<Pattern, Integer>();
		history = new Vector<String>();
	}
	
	/**
	 * makes a prediction based on user's patterns
	 * @return the winning answer based on user's patterns
	 */
	public String makePrediction()
	{
		String tempHist = "";
		// check if computer has enough info to predict
		if (!map.isEmpty() && history.size() > 3)
		{
			for(int i = 1; i < history.size(); i++)
			{
				tempHist = tempHist + history.get(i);
			}
			//patterns to check if next move is fire water or grass
			Pattern checkF = new Pattern(tempHist + "Fire");
			Pattern checkW = new Pattern(tempHist + "Water");
			Pattern checkG = new Pattern(tempHist + "Grass");
			// check if all patterns are in the hashmap
			if(map.containsKey(checkF) && map.containsKey(checkW) && map.containsKey(checkG))
			{
				// if fire is most frequent, return water
				if(map.get(checkF) > map.get(checkW) && map.get(checkF) > map.get(checkG))
				{
					return "Water";
				}
				// if water is most frequent, return grass
				else if(map.get(checkW) > map.get(checkF) && map.get(checkW) > map.get(checkG))
				{
					return "Grass";
				}
				// if grass is most frequent, return fire
				else if(map.get(checkG) > map.get(checkF) && map.get(checkG) > map.get(checkW))
				{
					return "Fire";
				}
			}
			// if hashmap only has fire and water
			else if(map.containsKey(checkF) && map.containsKey(checkW))
			{
				if(map.get(checkF) > map.get(checkW))
				{
					return "Water";
				}
				else if(map.get(checkW) > map.get(checkF))
				{
					return "Grass";
				}
			}
			// if hashmap only has fire and grass
			else if(map.containsKey(checkF) && map.containsKey(checkG))
			{
				if(map.get(checkF) > map.get(checkG))
				{
					return "Water";
				}
				else if(map.get(checkG) > map.get(checkF))
				{
					return "Fire";
				}
			}
			// if hashmap only has water and grass
			else if(map.containsKey(checkW) && map.containsKey(checkG))
			{
				if(map.get(checkW) > map.get(checkG))
				{
					return "Grass";
				}
				else if(map.get(checkG) > map.get(checkW))
				{
					return "Fire";
				}
			}
			// if hashmap only has fire
			else if(map.containsKey(checkF))
			{
				return "Water";
			}
			// if hashmap only has water
			else if(map.containsKey(checkW))
			{
				return "Grass";
			}
			// if hashmap only has grass
			else if(map.containsKey(checkG))
			{
				return "Fire";
			}
		}
		
		// if there's not enough info in hashmap, make random choice
		Random rand = new Random();
		int decision = rand.nextInt(3)+1;
		if (decision == 1)
		{
			return "Fire";
		}
		else if (decision == 2)
		{
			return "Water";
		}
		else
		{
			return "Grass";
		}
	}
	
	/**
	 * stores the user's history into hashmap, increments value if hashmap already exists
	 */
	public void storePattern()
	{
		if(history.size() > 3)
		{
			String temp = "";
			// convert user history into a pattern
			for(int i = 0; i < history.size(); i++)
			{
				temp = temp + history.get(i);
			}
			Pattern p = new Pattern(temp);
			// if the pattern already exists, increment value
			if (map.containsKey(p))
			{
				int inc = map.get(p);
				map.put(p, inc + 1);
			}
			else
			{
				map.put(p, 1);
			}
		}
		
	}
	
	/**
	 * stores user's option into history list, up to 4
	 * @param s the string to add the the history list
	 */
	public void storeHistory(String s)
	{
		// if history has 4 entries, remove the first one
		if(history.size() > 3)
		{
			history.remove(0);
		}
		history.add(s);
	}
}
