package huffman;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * @author Christian Gallai
 * 
 * Student Number: 260218797
 * Date: 22/03/2011
 * Course: COMP 250 - Winter 2011
 * Professor: Michael Langer
 * Assignment: Assignment 3
 */

public class HuffmanTree{
	
	// *** global variables ***************************************************
	private CharCode[] charCode;
	private HuffmanNode root;
	
	// *** constructor ********************************************************
    /**
     * constructs the Huffman tree from the String
     */
	public HuffmanTree(String toConvert) {

		// construct the Huffman Tree
		fillInFrequencies(toConvert);
		buildHuffmanTree();
		
		// Fill in the codewords for the characters based on the Huffman Tree
		BitSequence sequence = new BitSequence();
		fillInCodeWords(sequence, root);
	}
	
    /**
     * constructs the HuffmanTree by reading it from the BitSequence
     */
	public HuffmanTree(BitSequence bitSequence) {
		// intialize new char code
		charCode = new CharCode[65536];
		
		// get the characters and frequencies in the header
		ArrayList characters = bitSequence.nextList();
		ArrayList frequencies = bitSequence.nextList();

		// construct charCode based on the characters and frequencies
		for (int i=0; i<characters.size(); i++) {
			int toConvert = (Integer) characters.get(i);
			char charToAdd = (char)(toConvert);
			CharCode toInsert = new CharCode(charToAdd);
			
			int freqToAdd = (Integer)frequencies.get(i);
			toInsert.setFrequency(freqToAdd);
			charCode[(Integer) characters.get(i)] = toInsert;
		}
	
		// build the Huffman Tree corresponding to charCode and fill in the code words
		// for the characters based on the constructed Huffman Tree
		buildHuffmanTree();
		BitSequence sequence = new BitSequence();
		fillInCodeWords(sequence, root);
	}
	
	// *** private methods ****************************************************
    /**
     * initializes the CharCode array from the String
     */
	private void fillInFrequencies(String toConvert) {
		
		// intialize new char code
		charCode = new CharCode[65536];

		// go through each character of the provided String
		for(int i=0; i<toConvert.length(); i++) {

			// if the char code hasn't been inserted into the array yet, then add a new charCode
			// with frequency 1
			if (charCode[toConvert.charAt(i)] == null) {
				CharCode toInsert = new CharCode(toConvert.charAt(i));
				toInsert.setFrequency(1);
				charCode[toConvert.charAt(i)] = toInsert;
				
			// the char code has already been inserted, increment the frequency
			} else {
				charCode[toConvert.charAt(i)].setFrequency(charCode[toConvert.charAt(i)].getFrequency() + 1);
			}	
		}
	}
	
    /**
     * builds the Huffman tree
     */
	private void buildHuffmanTree() {
		
		// initialize queue
		PriorityQueue buildTree = new PriorityQueue<HuffmanNode>();

		// create a leaf node for each symbol and add it to the priority queue
		for (int i=0; i<charCode.length; i++) {
			if (charCode[i] != null) {
				HuffmanNode addNode = new HuffmanNode(charCode[i]);
				buildTree.add(addNode);
			}
		}
		
		System.out.println("");
		
		// build the Huffman Tree using the algorithm specifies in the assignment description
		while (buildTree.size() > 1) {
			HuffmanNode node1 = (HuffmanNode) buildTree.poll();
			HuffmanNode node2 = (HuffmanNode) buildTree.poll();
			HuffmanNode newNode;
			newNode = new HuffmanNode(node1, node2);
			buildTree.offer(newNode);
		}
		
		// save the Tree
		root = (HuffmanNode) buildTree.poll();
	}
	
    /**
     * sets the code words of all the CharCodes below the HuffmanNode
     */
	private void fillInCodeWords(BitSequence bitSequence, HuffmanNode huffmanNode) {
		if (huffmanNode.isLeaf()) {
			// set the code word to the appropriate char in charCode
			charCode[huffmanNode.getCharCode().getChar()].setCodeWord(bitSequence);
			huffmanNode.getCharCode().setCodeWord(bitSequence);
			
		} else {
			// traverse the right Huffman node
			BitSequence tempLeftSequence = new BitSequence(bitSequence);
			tempLeftSequence.add(false);
			fillInCodeWords(tempLeftSequence, huffmanNode.getLeft());
			
			// traverse the left Huffman node
			BitSequence tempRightSequence = new BitSequence(bitSequence);
			tempRightSequence.add(true);
			fillInCodeWords(tempRightSequence, huffmanNode.getRight());
		}

	}
	
	// *** helper methods *****************************************************
    /**
     * prints the tree structure starting at the root, using the algorithm given
     * on page 7 of the Assignment
     */
	public void printTree(String prefix, HuffmanNode node) {
		if (node.isLeaf()) {
			System.out.print("+-");
			System.out.print(node.getFrequency());
			System.out.print(" : ");
			System.out.println(node.getCharCode().getCharAsString());
		} else {
			System.out.print("+=");
			System.out.println(node.getFrequency());
			System.out.print(prefix);
			printTree(prefix + "| ", node.getRight());
			System.out.print(prefix);
			printTree(prefix + "| ", node.getLeft());
		}
	}
	
    /**
     * prints the table of code words using a recursive algorithm with the Huffman tree
     */
	public void printTable(HuffmanNode huffmanNode) {
		// print the bitSequence for each leaf
		if (huffmanNode.isLeaf()) {
			System.out.println(huffmanNode.getCharCode().toString());
			
		// node in not a leaf, continue to traverse left and right building the bitSequence
		} else {
			// traverse the right Huffman node
			printTable(huffmanNode.getLeft());
			
			// traverse the left Huffman node
			printTable(huffmanNode.getRight());
		}
	}
	
	public void printFrequenciesFromHuffmanTree(HuffmanNode node) {
		// print the bitSequence for each leaf
		if (node.isLeaf()) {
			System.out.println(node.getCharCode().getCharAsString() + "   : " + node.getFrequency());
			
		// node in not a leaf, continue to traverse left and right building the bitSequence
		} else {
			// traverse the right Huffman node
			printFrequenciesFromHuffmanTree(node.getLeft());
			
			// traverse the left Huffman node
			printFrequenciesFromHuffmanTree(node.getRight());
		}
	}
	
    /**
     * returns the char code of a bitSequence by recursively traversing the Huffman Tree based
     * on the provided BitSequence
     */
	public char decodeCharFromHuffmanTree(BitSequence Sequence, HuffmanNode node) {
		
		// node is a leaf, return the character
		if (node.isLeaf()) {
			return node.getCharCode().getChar();
			
		// node is not a leaf, continue to traverse the Huffman Tree
		} else {
			if (Sequence.nextBit()) {
				return decodeCharFromHuffmanTree(Sequence, node.getRight());
			} else {
				return decodeCharFromHuffmanTree(Sequence, node.getLeft());
			}
		}
	}
	
    /**
     * returns the minimum code word length by recursively traversing the Huffman Tree to find the lowest
     * lengths between the left and right node branches
     */
	public int getMinCodeWordLengthFromHuffmanTree(HuffmanNode node) {
		// node is a leaf, return the length of the code word
		if (node.isLeaf()) {
			return node.getCharCode().getCodeWord().length();
			
		// node is not a leaf, find the smallest code word length in the left and right
		// sub-Huffman trees
		} else {
			if (getMinCodeWordLengthFromHuffmanTree(node.getLeft()) < getMinCodeWordLengthFromHuffmanTree(node.getRight())) {
				return getMinCodeWordLengthFromHuffmanTree(node.getLeft());
			} else {
				return getMinCodeWordLengthFromHuffmanTree(node.getRight());
			}
		}
	}
	
    /**
     * returns the maximum code word length by recursively traversing the Huffman Tree to find the highest
     * lengths between the left and right node branches
     */
	public int getMaxCodeWordLengthFromHuffmanTree(HuffmanNode node) {
		// node is a leaf, return the length of the code word
		if (node.isLeaf()) {
			return node.getCharCode().getCodeWord().length();
			
		// node is not a leaf, find the largest code word length in the left and right
		// sub-Huffman trees
		} else {
			if (getMaxCodeWordLengthFromHuffmanTree(node.getLeft()) > getMaxCodeWordLengthFromHuffmanTree(node.getRight())) {
				return getMaxCodeWordLengthFromHuffmanTree(node.getLeft());
			} else {
				return getMaxCodeWordLengthFromHuffmanTree(node.getRight());
			}
		}
	}
	
    /**
     * returns the compressed data size by recursively traversing the Huffman Tree to find the data size
     * for each leaf of the tree and adding them to sum
     */
	public int getCompressedDataSizeFromHuffmanTree(HuffmanNode node) {
		// node is a leaf, return the length of the char code times the frequency
		if (node.isLeaf()) {
			return node.getCharCode().getCodeWord().length()*node.getFrequency();
			
		// node is not a leaf, return the sum of the datasize for both the left and the
		// right sub-Huffman trees
		} else {
			int sum = 0;
			sum += getCompressedDataSizeFromHuffmanTree(node.getLeft());
			sum += getCompressedDataSizeFromHuffmanTree(node.getRight());
			return sum;
		}
	}
	
    /**
     * returns an ArrayList of all the characters in the Huffman tree by recursively
     * traversing the tree and adding all characters in the leaves to an ArrayList
     */
	public ArrayList<Integer> getCharactersFromHuffmanTree(HuffmanNode node) {
		// node is a leaf, return an ArrayList containing the node character
		if (node.isLeaf()) {
			ArrayList<Integer> character = new ArrayList<Integer>();
			int toAdd = node.getCharCode().getChar();
			character.add(toAdd);
			return character;
			
		// node is not a leaf, return the ArraList of characters from the left and right
		// sub-Huffman trees
		} else {
			ArrayList<Integer> characters = new ArrayList<Integer>();
			characters.addAll(getCharactersFromHuffmanTree(node.getLeft()));
			characters.addAll(getCharactersFromHuffmanTree(node.getRight()));
			return characters;
		}
	}
	
    /**
     * returns an ArrayList of all the frequencies in the Huffman tree by recursively
     * traversing the tree and adding all frequencies in the leaves to an ArrayList
     */
	public ArrayList<Integer> getFrequenciesFromHuffmanTree(HuffmanNode node) {
		// node is a leaf, return an ArrayList containing the node frequency
		if (node.isLeaf()) {
			ArrayList<Integer> frequency = new ArrayList<Integer>();
			frequency.add(node.getFrequency());
			return frequency;
			
		// node is not a leaf, return the ArraList of frequencies from the left and right
		// sub-Huffman trees
		} else {
			ArrayList<Integer> frequencies = new ArrayList<Integer>();
			frequencies.addAll(getFrequenciesFromHuffmanTree(node.getLeft()));
			frequencies.addAll(getFrequenciesFromHuffmanTree(node.getRight()));
			return frequencies;
		}
	}
	
	// *** methods ************************************************************
    /**
     * prints all characters and their frequencies
     */
	public void printFrequencies() {

		// use the recursive helper method below to print the frequencies from the HuffmanTree
		printFrequenciesFromHuffmanTree(root);
	}
	
    /**
     * prints the Huffman tree structure on screen
     */
	public void printTree() {
		
		// uses the recursive helper method below to print the tree
		printTree(" ", root);
	}
	
    /**
     * prints all CharCodes in code word order
     */
	public void printTable() {
		
		// uses the recursive helper method below to print the tree
		printTable(root);
	}
	
    /**
     * returns a copy of the character's code word as a BitSequence
     */
	public BitSequence encodeChar(char toEncode) {
		return charCode[toEncode].getCodeWord();
	}
	
    /**
     * returns an encoded version of the String as a BitSequence
     */
	public BitSequence encodeString(String toEncode) {
		BitSequence sequence = new BitSequence();
		for (int i=0; i<toEncode.length(); i++) {
			// add the encoded char to the sequence using the encodeChar(char toEncode) method
			sequence.add(encodeChar(toEncode.charAt(i)));
		}
		return sequence;
	}
	
    /**
     * decodes and returns a char from the BitSequence
     */
	public char decodeChar(BitSequence toDecode) {
		
		// use the recursive helper method below to retrieve the character
		return decodeCharFromHuffmanTree(toDecode, root);
		
	}
	
    /**
     * decodes n characters from the BitSequence and returns them as a String
     */
	public String decodeString(BitSequence toDecode, int n) {
		// intialize the String Buffer
		StringBuffer stringBuffer = new StringBuffer(n);
		
		// loop for n characters
		for (int i=0; i<n; i++) {
			
			// decode the first character from the BitSequence
			char toAdd = decodeCharFromHuffmanTree(toDecode, root);
			stringBuffer.append(toAdd);

		}
		
		// return the decoded String
		return stringBuffer.toString();
	}
	
    /**
     * returns the sum of all frequencies as an int
     */
	public int getNumChars() {
		return root.getFrequency();
	}
	
    /**
     * returns an int denoting the number of bits needed to encode the String with
     * which this HuffmanTree was constructed
     */
	public int getCompressedDataSize() {
		
		// use the recursive helper method below to get the compressed data size
		return getCompressedDataSizeFromHuffmanTree(root);
	}
	
    /**
     * returns the length of the shortest code word as an int
     */
	public int getMinCodeWordLength() {

		// use the recursive helper method below to get the min code word length
		return getMinCodeWordLengthFromHuffmanTree(root);
	}
	
    /**
     * returns the length of the longest code word as an int
     */
	public int getMaxCodeWordLength() {

		// use the recursive helper method below to get the max code word length
		return getMaxCodeWordLengthFromHuffmanTree(root);
	}
	
    /**
     * returns a BitSequence representation of this HuffmanTree
     */
	public BitSequence toBitSequence() {

		// get the arraylist of characters from the recursive helper method below
		ArrayList<Integer> characters = getCharactersFromHuffmanTree(root);
		
		// get the arraylist of frequencies from the recursive helper method below
		ArrayList<Integer> frequencies = getFrequenciesFromHuffmanTree(root);
		
		// create and return the associated BitSequence
		BitSequence sequence = new BitSequence();
		
		// add arraylists to the BitSequence and return
		sequence.add(characters);
		sequence.add(frequencies);
		return sequence;
	}
	
    /**
     * returns an int denoting the number of bits required to store this HuffmanTree
     * in a BitSequence
     */
	public int getCompressedHeaderSize() {
		return toBitSequence().length();
	}

}



