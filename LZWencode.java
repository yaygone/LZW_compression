import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

class LZWencode
{
	int BUFFER_SIZE = 256;
	static int count = -1;
	static byte[] input = null;
	static String code = "";
	static int dictSize = 0;

	//node class
	class trie_node
	{
		byte[] dict = new byte[BUFFER_SIZE];
		byte value;
		int level = count++;
		trie_node[] children = new trie_node[BUFFER_SIZE];

		public trie_node(byte ch)
		{ value = ch; }

		//constructor
		public trie_node()
		{
			
			
			for (int i = 0; i < BUFFER_SIZE; i++)
			{
				children[i] = new trie_node((byte)i);
			}
			dictSize=count;
		}

		public void encode(byte[] charset)
		{
			input = charset;
			while (input.length > 0)
				find();	
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
				children[emptyIndex] = new trie_node(curr);
			}
			code += level+"\n";
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
		int x=0;
        try
        {
			FileInputStream is = new FileInputStream(args[0]);
			for(x=is.read();x!=-1;x=is.read())
			{
				list.add((byte)x);
			}
			input = new byte[list.size()];
			for(int i=0;i<list.size();i++)
			{
				input[i] = (byte)list.get(i);
			}
            trie_node root = new trie_node();
			root.encode(input);
			FileWriter wr = new FileWriter("Compressed.txt");
			wr.write(code);
			wr.close();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
	}
}