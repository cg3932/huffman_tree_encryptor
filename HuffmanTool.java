package huffman;

import java.util.ArrayList;

/**
 * @author Christian Gallai
 * 
 * Student Number: 260218797
 * Date: 22/03/2011
 * Course: COMP 250 - Winter 2011
 * Professor: Michael Langer
 * Assignment: Assignment 3
 */

public class HuffmanTool{
	
    public static void main(String[] args) {
    	
    	// get the input and output files
    	String inputFile = args[1];
    	String outputFile = args[2];
    	
    	// Compress file "inputFile" to file "outputFile"
    	if (args[0].equals("c")) {
    		
    		//********** Text Sequence **************************
            //Create a TextSequence object from a String.
            TextSequence text = new TextSequence("<this is a test>", 0);
         
            //read it back into a new Text Sequence
            TextSequence text2 = TextSequence.readFromFile(inputFile);
            String toEncode = text2.getText();

    		// create Huffman Tree from toEncode String
    		HuffmanTree huffmanTree = new HuffmanTree(toEncode);
    		
    		// Store header and data BitSequence in output file
    		BitSequence header = huffmanTree.toBitSequence();
    		BitSequence data = huffmanTree.encodeString(toEncode);
    		header.add(data);
    		header.writeToFile(outputFile);
    		
    		// output specified values
    		huffmanTree.printTree();
    		huffmanTree.printTable();
    		System.out.println("uncompressed size(char): " + (text2.getByteSize()*8));
    		System.out.println("total compressed size: " + (huffmanTree.getCompressedDataSize() + huffmanTree.getCompressedHeaderSize()));
    		System.out.println("compressed headersize: " + huffmanTree.getCompressedHeaderSize());
    		System.out.println("compressed data size: " + huffmanTree.getCompressedDataSize());
    		System.out.println("min/max code word length: " + huffmanTree.getMinCodeWordLength() + "/" + huffmanTree.getMaxCodeWordLength());
    		
    	// Decompress file "inputFile" to file "outputFile"	
    	} else if (args[0].equals("d")) {
    		
    		// Display msg that large files may take a bit of time to decompress
    		System.out.println("decompression started");
    		
    		// get the bitsequence from the file
            BitSequence bitSequence = BitSequence.readFromFile(inputFile);
	
    		// get the characters and frequencies in the header and create a bitSequence for them
    		ArrayList characters = bitSequence.nextList();
    		ArrayList frequencies = bitSequence.nextList();
    		BitSequence header = new BitSequence();
    		header.add(characters);
    		header.add(frequencies);
    		
    		// uncompress the bitsequence using the Huffman Tree
    		HuffmanTree huffmanTree = new HuffmanTree(header);
    		String decompressed = huffmanTree.decodeString(bitSequence, huffmanTree.getNumChars());
    		
    		// write the decompressed String to the output file
    		TextSequence textSequence = new TextSequence(decompressed, huffmanTree.getNumChars());
    		textSequence.writeToFile(outputFile);
    		System.out.println("decompression complete, output stored in: " + outputFile);
    	}
    }
}

