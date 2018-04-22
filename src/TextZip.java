import java.io.*;
import java.util.regex.*;
import java.util.*;

//import javax.swing.tree.TreeNode;

/**
	可以为这个类添加额外的方法及数据成员.
	ID就是指学号, 下面的作者一定要写上你的名字和学号
	作业中出现的示范数据abdc001需要改成学生的学号数据
	@author  	陈佳新  and 201392200
	@version	THE DATE
**/

public class TextZip {  //用正则表达式进行匹配  用map来构建huffman树 把map中key是字符与value是freq 拿出来做一个结点TreeNode加入到TreeSet 
                                                         // huffman获取编码同样需要map key是编码 value是字符
	//ID, 该学号的值需要修改!
	private static final String ID = "201392200";	
	private static String allChar = "";
	
	private static String FREQ = "mmm.freq";
	private static String Compressed = "mmm.txz";
	private static String DeCompressed = "mmm.txt";
	private static String Copy = "dess.txt";
	
	private static String beforeCompressed = "";
	private static String afterCompressed = "";
	
	private static HashMap<Character, String> enCoder = new HashMap<Character, String>();
	private static HashMap<String, Character> deCoder = new HashMap<String, Character>();
	
	
	
	
	
  /**
	* This method generates the huffman tree for the text: "abracadabra!"
	*
	* @return the root of the huffman tree
 * @throws IOException 
	*/
	
	public static ArrayList<TreeNode> GetFrequency() throws IOException {
		FileInputStream fis = new FileInputStream(DeCompressed);
		InputStreamReader fos = new InputStreamReader(fis, "gbk");
		BufferedReader isr = new BufferedReader(fos);
		
		String tmp = "";
		while((tmp = isr.readLine()) != null)
			allChar += tmp;
			
		HashMap<Character, Integer> Char2Freq = new HashMap<Character, Integer>();
		
		for(int i=0;i<allChar.length();i++) {
			if(Char2Freq.containsKey(allChar.charAt(i)))
				Char2Freq.put(allChar.charAt(i), Char2Freq.get(allChar.charAt(i)) + 1);
			else
				Char2Freq.put(allChar.charAt(i), 1);
		}
		
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		
		FileOutputStream Foutput =new FileOutputStream(FREQ);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(Foutput, "gbk"));
		
		for(Character key: Char2Freq.keySet()) {	
			nodeList.add(new TreeNode(new CharFreq(key ,Char2Freq.get(key))));
			pw.println(key + "\t" + Char2Freq.get(key));
		}
		
		pw.close();

		return nodeList;
        
	}
	
	public static TreeNode GetHuffmanTree(ArrayList<TreeNode> nodeList) throws IOException {
		
		TreeNode parent, lc, rc, root = new TreeNode(new CharFreq(' ', -1));
		
		while(nodeList.size() > 1) {
			
			lc = removeMin(nodeList);
			rc = removeMin(nodeList);
			parent = new TreeNode(new CharFreq(' ', lc.item.freq + rc.item.freq), lc, rc);
			
			nodeList.add(parent);
			root = parent;
			
		}
		return root;
		
	}
	
	public static void getLength(String filename) throws IOException{
		
		File file = new File(filename);
		if(file.exists() && file.isFile())
			System.out.println("The size of " + filename + " is " + file.length() + " bytes");
	}
	
	public static void GetHuffmanCode(TreeNode root, String code) throws IOException{
		
		if(!root.isLeaf()) {
			GetHuffmanCode(root.getLeft(), code + "0");
			GetHuffmanCode(root.getRight(), code + "1");
		}
		else {
			enCoder.put(root.getItem().c, code);
			return;
		}
	}
	
	
	
  /**
	* This method decompresses a huffman compressed text file.  The compressed
	* file must be read one bit at a time using the supplied BitReader, and
	* then by traversing the supplied huffman tree, each sequence of compressed
	* bits should be converted to their corresponding characters.  The
	* decompressed characters should be written to the FileWriter
	*
	* @param  br      the BitReader which reads one bit at a time from the
	*                 compressed file
	*         huffman the huffman tree that was used for compression, and
	*                 hence should be used for decompression
	*         fw      a FileWriter for storing the decompressed text file
	*/
	public static void decompress() throws Exception {
		
		// IMPLEMENT THIS METHOD
		FileOutputStream Foutput = new FileOutputStream(Copy);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(Foutput, "gbk"));
		
		BitReader br = new BitReader(Compressed);
		String tmp = "";
		
		while(br.hasNext()) {
			
			if(br.next())
				tmp += "1";
			else
				tmp += "0";
			
			if(deCoder.containsKey(tmp)) {
				pw.write(deCoder.get(tmp));
				tmp = "";
			}		
		}
		
		pw.close();	

	}
	
   /**
	* This method traverses the supplied huffman tree and prints out the
	* codes associated with each character
	*
	* @param  t    the root of the huffman tree to be traversed
	*         code a String used to build the code for each character as
	*              the tree is traversed recursively
	*/
	
  /**
	* This method removes the TreeNode, from an ArrayList of TreeNodes,  which
	* contains the smallest item.  The items stored in each TreeNode must
	* implement the Comparable interface.
	* The ArrayList must contain at least one element.
	*
	* @param  a an ArrayList containing TreeNode objects
	*
	* @return the TreeNode in the ArrayList which contains the smallest item.
	*         This TreeNode is removed from the ArrayList.
	*/
	public static TreeNode removeMin(ArrayList<TreeNode> nodeList) {
		
		int minInx = 0;
		
		for(int i = 1;i<nodeList.size();i++) {
			TreeNode curMin = nodeList.get(minInx);
			TreeNode tmp = nodeList.get(i);
			
			if(curMin.item.compareTo(tmp.item) > 0)
				minInx = i;			
		}
		
		TreeNode minNode = nodeList.remove(minInx);
		return minNode;
	}

  /**
	* This method counts the frequencies of each character in the supplied
	* FileReader, and produces an output text file which lists (on each line)
	* each character followed by the frequency count of that character.  This
	* method also returns an ArrayList which contains TreeNodes.  The item stored
	* in each TreeNode in the returned ArrayList is a CharFreq object, which
	* stores a character and its corresponding frequency
	*
	* @param  fr the FileReader for which the character frequencies are being
	*            counted
	*         pw the PrintWriter which is used to produce the output text file
	*            listing the character frequencies
	*
	* @return the ArrayList containing TreeNodes.  The item stored in each
	*         TreeNode is a CharFreq object.
	*/
	

  /**
	* This method builds a huffman tree from the supplied ArrayList of TreeNodes.
	* Initially, the items in each TreeNode in the ArrayList store a CharFreq object.
	* As the tree is built, the smallest two items in the ArrayList are removed,
	* merged to form a tree with a CharFreq object storing the sum of the frequencies
	* as the root, and the two original CharFreq objects as the children.  The right
	* child must be the second of the two elements removed from the ArrayList (where
	* the ArrayList is scanned from left to right when the minimum element is found).
	* When the ArrayList contains just one element, this will be the root of the
	* completed huffman tree.
	*
	* @param  trees the ArrayList containing the TreeNodes used in the algorithm
	*               for generating the huffman tree
	*
	* @return the TreeNode referring to the root of the completed huffman tree
	*/
	

  /**
	* This method compresses a text file using huffman encoding.  Initially, the
	* supplied huffman tree is traversed to generate a lookup table of codes for
	* each character.  The text file is then read one character at a time, and
	* each character is encoded by using the lookup table.  The encoded bits for
	* each character are written one at a time to the specified BitWriter.
	*
	* @param  fr      the FileReader which contains the text file to be encoded
	*         huffman the huffman tree that was used for compression, and
	*                 hence should be used for decompression
	*         bw      the BitWriter used to write the compressed bits to file
	*/
	public static void compress() throws Exception {

		BitWriter bw = new BitWriter(Compressed);
		String code = "";

		
		for(int i =0;i<allChar.length();i++) {
			code = enCoder.get(allChar.charAt(i));
			for(int j=0;j<code.length();j++)
				bw.writeBit(code.charAt(j) - '0');

		}		
		bw.close();
		
	}

  /**
	* This method reads a frequency file (such as those generated by the
	* countFrequencies() method) and initialises an ArrayList of TreeNodes
	* where the item of each TreeNode is a CharFreq object storing a character
	* from the frequency file and its corresponding frequency.  This method provides
	* the same functionality as the countFrequencies() method, but takes in a
	* frequency file as parameter rather than a text file.
	*
	* @param  inputFreqFile the frequency file which stores characters and their
	*                       frequency (one character per line)
	*
	* @return the ArrayList containing TreeNodes.  The item stored in each
	*         TreeNode is a CharFreq object.
	*/
	public static ArrayList readFrequencies() throws Exception {

		FileInputStream fis = new FileInputStream(FREQ);
		InputStreamReader fos = new InputStreamReader(fis, "gbk");
		BufferedReader isr = new BufferedReader(fos);
		
		HashMap<Character, Integer> Char2Freq = new HashMap<Character, Integer>();
		
		String tmp = "";
		while((tmp = isr.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(tmp, "\t");
			Char2Freq.put(st.nextToken().charAt(0), Integer.parseInt(st.nextToken()));
		}
		
		
		ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
		
		for(Character key : Char2Freq.keySet())
			nodeList.add(new TreeNode(new CharFreq(key, Char2Freq.get(key))));
		
		return nodeList;
		
		
	}
	
	public static void deCoderGeneration() {
		
		for(Character key : enCoder.keySet())
			deCoder.put(enCoder.get(key), key);
		
	}

	/* This TextZip application should support the following command line flags:

	QUESTION 2 PART 1
	=================
		 -a : this uses a default prefix code tree and its compressed
		      file, "a.txz", and decompresses the file, storing the output
		      in the text file, "a.txt".  It should also print out the size
		      of the compressed file (in bytes), the size of the decompressed
		      file (in bytes) and the compression ratio

	QUESTION 2 PART 2
	=================
		 -f : given a text file (args[1]) and the name of an output frequency file
		      (args[2]) this should count the character frequencies in the text file
		      and store these in the frequency file (with one character and its
		      frequency per line).  It should then build the huffman tree based on
		      the character frequencies, and then print out the prefix code for each
		      character

	QUESTION 2 PART 3
	=================
		 -c : given a text file (args[1]) and the name of an output frequency file
		      (args[2]) and the name of the output compressed file (args[3]), this
		      should compress file

	QUESTION 2 PART 4
	=================
		 -d : given a compressed file (args[1]) and its corresponding frequency file
		      (args[2]) and the name of the output decompressed text file (args[3]),
		      this should decompress the file

	*/

	public static void main(String[] args) throws Exception {
		
		/*
		ArrayList<TreeNode> nodeList = GetFrequency();
		TreeNode root = GetHuffmanTree(nodeList);
		GetHuffmanCode(root, "");
		
		compress();
		
		
		nodeList = readFrequencies();
		root = GetHuffmanTree(nodeList);
		GetHuffmanCode(root, "");
		deCoderGeneration();
		decompress();
	
		*/
		
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input your option: ");
		String opt = sc.nextLine();
		
		
		if (opt.equals("-a")) {
			
			DeCompressed = "a.txt";
			Compressed = "a.txz";
			FREQ = "a.freq";
			Copy = "a_dess.txt";
			
			ArrayList<TreeNode> nodeList = GetFrequency();
			TreeNode root = GetHuffmanTree(nodeList);
			GetHuffmanCode(root, "");
			
			getLength(DeCompressed);
			
			compress();
			
			getLength(Compressed);
			
			nodeList = readFrequencies();
			root = GetHuffmanTree(nodeList);
			GetHuffmanCode(root, "");
			deCoderGeneration();
			decompress();
			
		}
		
		
		else if (opt.equals("-f")) {
			
			System.out.println("Please input your text file name: ");
			
			String tmp = sc.nextLine();
			StringTokenizer st = new StringTokenizer(tmp, ".");
			tmp = st.nextToken();
			
			DeCompressed = tmp + ".txt";
			Compressed = tmp + ".txz";
			FREQ = tmp + ".freq";
			Copy = tmp + "_dess.txt";
			
			ArrayList<TreeNode> nodeList = GetFrequency();
			TreeNode root = GetHuffmanTree(nodeList);
			GetHuffmanCode(root, "");
			
			for(Character key : enCoder.keySet())
				System.out.println("Character: " + key + ", Code: " + enCoder.get(key));
			
		}

		
		else if (opt.equals("-c")) {

			System.out.println("Please input your text file name: ");
			
			String tmp = sc.nextLine();
			StringTokenizer st = new StringTokenizer(tmp, ".");
			tmp = st.nextToken();
			
			DeCompressed = tmp + ".txt";
			Compressed = tmp + ".txz";
			FREQ = tmp + ".freq";
			Copy = tmp + "_dess.txt";
			
			ArrayList<TreeNode> nodeList = GetFrequency();
			TreeNode root = GetHuffmanTree(nodeList);
			GetHuffmanCode(root, "");
			
			compress();
			
			// IMPLEMENT NEXT 
			// Finish the compress function here

			

			// then output the compression ratio
			// Write your own implementation here.

		}
		
		
		else if (opt.equals("-d")) {
			
			System.out.println("Please input your text file name: ");
			
			String tmp = sc.nextLine();
			StringTokenizer st = new StringTokenizer(tmp, ".");
			tmp = st.nextToken();
			
			DeCompressed = tmp + ".txt";
			Compressed = tmp + ".txz";
			FREQ = tmp + ".freq";
			Copy = tmp + "_dess.txt";
			
			
			ArrayList<TreeNode> nodeList = readFrequencies();
			TreeNode root = GetHuffmanTree(nodeList);
			GetHuffmanCode(root, "");
			deCoderGeneration();
			decompress();

			// Output the compression ratio
			// Write your own implementation here.
		
		}
		
		else
			System.out.println("Wrong Option");
	}
}
