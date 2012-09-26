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

public class HuffmanNode implements Comparable<HuffmanNode> {
	
	// *** global variables ***************************************************
	private CharCode charCode;
	private HuffmanNode left;
	private HuffmanNode right;
	private int frequency;
	
    // *** constructors *******************************************************
    /**
     * creates a new Huffman Node using a charCode
     */
	public HuffmanNode(CharCode newCharCode) {
		this.charCode = newCharCode;
		this.frequency = charCode.getFrequency();
	}
	
    /**
     * creates a new Huffman Node using two Huffman nodes
     */
	public HuffmanNode(HuffmanNode newLeft, HuffmanNode newRight) {
		this.left = newLeft;
		this.right = newRight;
		this.frequency = newLeft.frequency + newRight.frequency;
	}
	
	// *** helper methods ******************************************************
    /**
     * recursively finds and returns the smallest character value in the specified
     * huffman tree with the root of the tree being the inputed node
     */
	public char getSmallestChar(HuffmanNode node) {
		// node is a leaf, return the character for comparison
		if (node.isLeaf()) {
			return node.getCharCode().getChar();
			
		// node is not a leaf, find the smallest values in the left and right
		// sub-Huffman trees
		} else {
			if (getSmallestChar(node.left) < getSmallestChar(node.right)) {
				return getSmallestChar(node.left);
			} else {
				return getSmallestChar(node.right);
			}
		}
	}
	
	// *** functions ***********************************************************
    /**
     * returns the left child (0), or null if this node is a leaf
     */
	public HuffmanNode getLeft() {
		return this.left;
	}
	
    /**
     * returns the right child (1), or null if this node is a leaf
     */
	public HuffmanNode getRight() {
		return this.right;
	}
	
    /**
     * returns the sum of the frequencies of the CharCodes
     * that the leafs below this node store, as an int
     */
	public int getFrequency() {	
		return this.frequency;
	}
	
    /**
     * returns true if this node is a leaf
     */
	public boolean isLeaf() {
		
		// has children -> is a leaf
		if ((left != null) && (right != null)) {
			return false;
			
		// is not a leaf
		} else {
			return true;
		}
		
	}
	
    /**
     * returns the CharCode associated wit this leaf,
     * or null if this node is not a leaf.
     */
	public CharCode getCharCode() {
		return this.charCode;
	}

    /**
     * Compares this HuffmanNode with the given argument. Returns either
     * a negative, zero, or positive int if this HuffmanNode is less than,
     * equal, or greater than the other node.
     */
	@Override
	public int compareTo(HuffmanNode compare) {
		
		// less than
		if (getFrequency() < compare.getFrequency()) {
			return -1;
			
		// the frequencies are equal, return whichever node stores the character
		// with the smallest integer value
		} else if (getFrequency() == compare.getFrequency()) {
			if (getSmallestChar(this) < getSmallestChar(compare)) {
				return -1;
			} else {
				return 1;
			}
		
		// greater than
		} else {
			return 1;
		}
		
	}
}


