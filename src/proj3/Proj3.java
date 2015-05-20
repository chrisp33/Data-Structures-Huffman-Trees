package proj3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Compresson and decompression program using Huffman's algorithm
 * @version 4/26/13
 * @author Christopher Pagan <chrisp3@umbc.edu>
 * @project CMSC 341 Data Structures Project 3
 * @section 02 
 */
public class Proj3 {

	/**
	 * Map of characters in the file and their Huffman codes
	 */
	private static Map<Character, String> codes;

	
	/**
	 * Compress and uncompress text files
	 * @param args - array of command line arguments
	 * @throws IOException - file IO error
	 */
	public static void main(String[] args) throws IOException {
		
		//Check command line arguments
		if (args.length != 3)
		{
			System.out.println("This program needs three arguments to run:\n(1) The input file,");
			System.out.println("(2) the name of the file to which the input needs to be compressed, and");
			System.out.println("(3) the name of the file to which the second file is decompressed into.");
			System.exit(0);
		}
		String inputFile = args[0];
		String compressedFile = args[1];
		String decompressedFile = args[2];
		
		System.out.println("Reading input file: \"" + inputFile + "\"...\n");
		try
		{
			PriorityQueue<BinaryTree<CharacterMapping>> initialForest = readFile(inputFile);
		
			//create tree
			BinaryTree<CharacterMapping> encodingTree = createEncodingTree(initialForest);		
			//Create and print encoding table
			createEncodingTable(encodingTree);
			//Encode to file
			encodeFile(inputFile, compressedFile);
			//Decode the encoded file
			decodeFile(compressedFile, decompressedFile, encodingTree);
			}
			catch(IOException e)
			{
				System.out.println("Error reading/writing file(s).");
			}
	}
	
	/**
	 * Reads the file, creates a map of character keys to frequency values, and outputs info to user
	 * @param inputFile - the name of the file to be compressed
	 * @return the priority queue of mappings of character keys to their frequency values
	 * @throws FileNotFoundException - file does not exist
	 */
	public static PriorityQueue<BinaryTree<CharacterMapping>> readFile(String inputFile) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new FileReader(inputFile));
		scan.useDelimiter("");
		Map<Character, Integer> map = new TreeMap<Character, Integer>();//use TreeMap to keep characters
																		//sorted by ascii value
		
		//Obtain characters and character counts 
		while (scan.hasNext())
		{
			Character key = scan.next().charAt(0);   

			if ( !map.containsKey(key) ) //New character found
				map.put(key, 1);
			else
				map.put( key, (map.get(key) + 1) ); //Increase count for a duplicate character
		}
		scan.close();
		
		//setup for output and initial forest
		int chars = 0;
		PriorityQueue<BinaryTree<CharacterMapping>> forest = new PriorityQueue<BinaryTree<CharacterMapping>>();
		
		System.out.println("Characters and their Frequencies:\n---------------------------------");
		for (Character k : map.keySet())
		{
			chars += map.get(k);
			System.out.println(k + " : " + map.get(k));
			
			ArrayList<Character> chrs = new ArrayList<Character>(); //CharacterMapping takes a Character array for constructor
			chrs.add(k);
			forest.add( new BinaryTree<CharacterMapping>(new CharacterMapping(chrs, map.get(k))) ); // create initial forest
		}
		System.out.println("\nTotal number of characters in the file: " + chars);
		System.out.println("Number of character types: " + map.size());
		
		// include pseudo-EOF character
		ArrayList<Character> eof = new ArrayList<Character>();
		eof.add('^');
		forest.add( new BinaryTree<CharacterMapping>(new CharacterMapping(eof, 1)) );
		
		return forest;
	}
	
	/**
	 * Create a single binary tree to be used for the Huffman encoding
	 * @param initialForest - the priority queue of characters, prioritized by frequency
	 * @return the single tree to be used for the Huffman encoding
	 */
	public static BinaryTree<CharacterMapping> createEncodingTree(PriorityQueue<BinaryTree<CharacterMapping>> initialForest)
	{
		while(initialForest.size() > 1)
		{
			//find two smallest nodes
			BinaryTree<CharacterMapping> tree1 = initialForest.remove();
			BinaryTree<CharacterMapping> tree2 = initialForest.remove();
			
			//Combine data from root nodes
			int size1 = tree1.getRoot().getElement().getFreq();
			int size2 = tree2.getRoot().getElement().getFreq();
			int newRootSize = size1 + size2;
			ArrayList<Character> chars = tree1.getRoot().getElement().getCh();
			ArrayList<Character> chars2 = tree2.getRoot().getElement().getCh();
			ArrayList<Character> combinedCharacters = new ArrayList<Character>(chars);
			combinedCharacters.addAll(chars2);
			CharacterMapping combined = new CharacterMapping(combinedCharacters, newRootSize);
			
			//Merge the two nodes
			tree1.merge(combined, tree1, tree2);
			
			//Add new tree to the forest
			initialForest.add(tree1);
		}
		
		return initialForest.element();
	}
	
	/**
	 * Creates a map of encodings to characters and outputs this info to the user
	 * @param huffTree - The Huffman tree used to encode
	 * @return a map of character/encoding string pairs
	 */
	public static void createEncodingTable(BinaryTree<CharacterMapping> huffTree)
	{
		String code = "";
		codes = new TreeMap<Character, String>();

		System.out.println();
		findCodes(huffTree.getRoot(), code);
		
		System.out.println("Characters and their Encodings:");
		System.out.println("-------------------------------");
		for (Character key: codes.keySet())
		{
			System.out.println("" + key + " : " + codes.get(key));
		}
	}

	/**
	 * Recursively traverse to leaf nodes of Huffman tree to find the encodings for the characters
	 * @param huffTree - the Huffman tree
	 * @return map of character/encoding string pairs
	 */
	public static void findCodes(BinaryNode<CharacterMapping> huffTree, String code) {
		
		if (null == huffTree.getElement()) //Null node; just return
		{
			return;
		}
		if (null == huffTree.getLeft() && null == huffTree.getRight()) //Leaf node; add to map
		{
			codes.put(huffTree.getElement().getCh().get(0), code);
		}
		
		if (null != huffTree.getLeft() ) //Check left tree
		{
			code += "0"; //Add to encoding string
			findCodes(huffTree.getLeft(), code);
		}
		
		if (null != huffTree.getRight()) //Check right tree
		{
			if (null != huffTree.getLeft())
				code = code.substring(0, code.length() - 1); //Remove last bit because bit is already included
			
			code += "1";
			findCodes(huffTree.getRight(), code);
		}
	}

	/**
	 * Writes the contents of the original file to a compressed file using the Huffman algorithm
	 * @param inFile - the name of the input file
	 * @param compressedFile - the name of the file that is encoded and compressed
	 * @param  - the Huffman tree
	 * @throws IOException - file IO error
	 */
	public static void encodeFile(String inFile, String compressedFile) throws IOException{

		System.out.println("\nCompressing \"" + inFile + "\" into \"" + compressedFile + "\".");
		System.out.println("This may take a while...\n");

		File f = new File(compressedFile);
		if (f.exists())
			f.delete();
		
		Scanner scan = new Scanner(new FileReader(inFile));
		scan.useDelimiter("");
		
		//Create bit string for the data in the input file
		StringBuffer code = new StringBuffer("");
		while (scan.hasNext())
		{
			String k = scan.next();
			Character key = k.charAt(0);
			StringBuffer code2 = new StringBuffer(codes.get(key));
			
			//Add characters one at a time to total bit string so that no bits are backwards
			for (int i=0; i < code2.length(); i++)
				code.append(code2.charAt(i));
		}
		scan.close();
		
		//Include pseudo-eof character to bit string
		String pseudoeof = codes.get('^');
		for (int i=0; i < pseudoeof.length(); i++)
			code.append(pseudoeof.charAt(i));
		
		//Calculate the amount of padding needed to get a whole number of bytes
		int padding = 8 - Math.abs((8 - code.length()) % 8);
		
		for (int i=1; i <= padding; i++)
			code.append("0");
			
		int numBytes = code.length() / 8;
		byte[] bytes = new byte[numBytes];
		for (int i = 0; i <= numBytes - 1 ; i++)
		{
			String codeByte = code.substring(i*8, i*8 + 8);
			bytes[i] = (byte) Integer.parseInt(codeByte,2);
		}
		
		f.createNewFile();
		FileOutputStream output = new FileOutputStream(f);
		output.write(bytes);	
		output.close();
		System.out.println("File compression complete.\n");
	}

	/**
	 * Decodes a file which has been encoded by the Huffman algorithm
	 * @param compressedFile - the name of the file which has been compressed
	 * @param decompressedFile - the name of the file that is to be decoded into
	 * @param encodingTree - the Huffman encoding tree
	 * @throws IOException - file IO error
	 */
	public static void decodeFile(String compressedFile, String decompressedFile, BinaryTree<CharacterMapping> encodingTree) throws IOException {
	
		System.out.println("Reading encoded file: \""+ compressedFile + "\"...\n");
		File f = new File(decompressedFile);
		if (f.exists())
			f.delete();
		
		//Read encoded file
		StringBuffer bitString = new StringBuffer("");
		FileInputStream input = new FileInputStream(compressedFile);
		while (input.available() > 0)
		{
			byte data = (byte) input.read();
			String bits = Integer.toBinaryString(( data+256 ) % 256);
			int leadingZeros = 8 - bits.length();
			StringBuffer pad = new StringBuffer("");
			for (int i=1; i <= leadingZeros; i++)
				pad.append("0");
			pad.append(bits);
			bitString.append(pad);
				
		}
		input.close();
		
		System.out.println("Deompressing \"" + compressedFile + "\" into \"" + decompressedFile + "\"");
		System.out.println("This may take a while...\n");
		
		f.createNewFile();
		PrintWriter writer = new PrintWriter(f);
		BinaryNode<CharacterMapping> node = encodingTree.getRoot();
		
		//Traverse the Huffman tree using the string of bits
		for (int i = 0; i < bitString.length(); i++) //Tree was not well formed if all bits are read unless there
		{											 //was no bit string padding added
			char bit = bitString.charAt(i);
		
			//Change node that is being looked at
			if (bit == '0')
				node = node.getLeft();
			else
				node = node.getRight();
				
			//Determine whether or not to stop reading bits or write to the file
			if (null == node.getLeft() && null == node.getRight())
			{
				if (node.getElement().getCh().get(0) == '^')
						break;
				else
				{
					writer.write(node.getElement().getCh().get(0));
					node = encodingTree.getRoot();
				}
			}
		}	
		
		writer.close();
		System.out.println("File decompression complete.");

	}
	
}
