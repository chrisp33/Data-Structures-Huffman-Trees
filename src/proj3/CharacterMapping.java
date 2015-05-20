package proj3;

import java.util.ArrayList;

/**
 * Holds ArrayLists of Characters and their frequency counts
 * @author Chrisopher Pagan
 */
public class CharacterMapping implements Comparable<CharacterMapping>{

	/**
	 * The ArrayList of Characters
	 */
	private ArrayList<Character> ch;
	/**
	 * The frequency of every character in the ArrayList
	 */
	private Integer freq;
	
	/**
	 * Constructor which initializes the data members 
	 * @param ch - the ArrayList of characters passed in
	 * @param freq - the frequency of each character
	 */
	public CharacterMapping(ArrayList<Character> ch, Integer freq)
	{
		this.ch = ch;
		this.freq = freq;
	}
	
	/**
	 * Gets the list of characters
	 * @return the list of characters
	 */
	public ArrayList<Character> getCh()
	{
		return ch;
	}
	
	/**
	 * Gets the frequency of the characters
	 * @return
	 */
	public Integer getFreq()
	{
		return freq;
	}

	/**
	 * Allows CharacterMapping objects to be compared
	 * @param otherMap - the other CharacterMapping object
	 * @return negative if this instance of CharacterMap has a frequency less than the other instance's
	 * 		   positive if this instance of CharacterMap has a frequency greater than the other instance's
	 * 		   0 if both instances have the same frequency count
	 */ 
	public int compareTo(CharacterMapping otherMap) {
		
		if (this.getFreq() < otherMap.getFreq())
			return -1;
		else if (this.getFreq() > otherMap.getFreq())
			return 1;
		else
			return 0;
	}
	
	/**
	 * String representataion for CharacterMapping
	 * @return the string representataion for this object
	 */
	public String toString()
	{ 
		String s = "[";
		ArrayList<Character> chs = getCh();
		for (Character c : chs)
		{
			if (chs.indexOf(c) == chs.size() - 1)
				s += c;
			else
				s += c + ", ";
		}
		s += "] : " + getFreq();
		return s;
	}
}
