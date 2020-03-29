import java.io.*;
import java.util.*;

class LZWencode
{
	int BUFFER_SIZE = 256;
	static int count = -1;
	static byte[] input = null;
	static int dictSize = 0;
	static String code = "";

	//node class
	class TrieNode
	{
		byte[] dict = new byte[BUFFER_SIZE];
		byte value;
		int level = count++;
		TrieNode[] children = new TrieNode[BUFFER_SIZE];

		public TrieNode(byte ch)
		{ value = ch; }

		//constructor
		public TrieNode()
		{
			for (int i = 0; i < BUFFER_SIZE; i++)
				children[i] = new TrieNode((byte)i);
			dictSize = count;
		}

		public void encode(byte[] charset)
		{
			input = charset;
			while (input.length > 0) find();	
		}
		
		public void find()
		{
			if (input.length > 0)
			{
				byte curr = input[0];
				int emptyIndex = -1;
				for (int i = 0; i < dictSize; i++)
				{
					if (children[i] != null && curr == children[i].value)
					{
						sub();
						children[i].find();
						return;
					}
					if (emptyIndex == -1 && children[i] == null) emptyIndex = i;
				}
				children[emptyIndex] = new TrieNode(curr);
			}
			System.out.println(level);
			code += level + "\n";
		}
		
		public void sub()
		{
			byte[] st = new byte[input.length - 1];
			for (int i = 1; i < input.length; i++)
				st[i - 1] = input[i];
			input = st;
		}
	}

	public static void main(String[] args)
	{ new LZWencode().run(args); }

	public void run(String[] args)
	{
		ArrayList<Byte> list = new ArrayList<>();
		int x = 0;
		try
		{
			FileInputStream is = new FileInputStream(args[0]);
			for(x = is.read(); x != -1; x = is.read())
				list.add((byte)x);
			input = new byte[list.size()];
			for(int i = 0; i < list.size(); i++)
				input[i] = (byte)list.get(i);
			
			TrieNode root = new TrieNode();
			root.encode(input);
			FileWriter wr = new FileWriter("compressed.file");
			wr.write(code);
			wr.close();
		}
		catch(Exception ex)
		{ System.err.println(ex); }
	}
}