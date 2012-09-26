package huffman;

/**
 * @author Christian Gallai
 * 
 * Student Number: 260218797
 * Date: 22/03/2011
 * Course: COMP 250 - Winter 2011
 * Professor: Michael Langer
 * Assignment: Assignment 3
 */

public class CharCode{
	
	// *** global variables ***************************************************
	private char character;
	private int frequency;
	private BitSequence codeWord;
	
    // *** constructor ********************************************************
    /**
     * creates a new charcode instance with the provided character
     */
	public CharCode(char setCharacter) {
		this.character = setCharacter;
	}
	
	// *** getters and setters ************************************************
    /**
     * returns the stored char
     */
	public char getChar() {
		return this.character;
	}
	
    /**
     * sets the Frequency for the CharCode
     */
	public void setFrequency(int newFrequency) {
		this.frequency = newFrequency;
	}
	
    /**
     * returns the character's frequency as an int
     */
	public int getFrequency() {
		return this.frequency;
	}
	
    /**
     * sets the CodeWord for the CharCode
     */
	public void setCodeWord(BitSequence newCodeWord) {
		this.codeWord = newCodeWord;
	}
	
    /**
     * returns the characters' code word, as a BitSequence
     */
	public BitSequence getCodeWord() {
		return this.codeWord;
	}
	
    /**
     * returns a String representation of this object, in the form
     * "<char> : <code word> (<frequency>)"
     */
	public String toString() {
		String representation = getCharAsString() + "   : " + codeWord.toString() + " (" + Integer.toString(frequency) + ")";
		return representation;
	}
	
    /**
     * returns a String representation of the character
     */
	public String getCharAsString() {
		return codeWord.getCharAsPrintableString(character);
	}
}