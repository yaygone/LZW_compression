import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
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
			byte[] charset = input.clone();
			Arrays.sort(charset);
			//remove dulplicates from the array
			for (int j = 0; j < charset.length; j++)
			{
				if (charset[j] == 0) continue;
				for (int i = j; i < charset.length; i++)
					charset[i] = charset[j] == charset[i] && i != j ? 0 : charset[i];
				dict[dictSize++] = charset[j];
			}
			for (int i = 0; i < dictSize; i++)
			{
				children[i] = new trie_node(dict[i]);
				System.out.println(String.valueOf((char)(children[i].value)) + " = " + children[i].level);
			}
			System.out.println("Size = " + count);
		}

		public void encode(byte[] charset)
		{
			input = charset;
			while (input.length > 0)
				find();
			System.out.println(code);
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
			code += level;
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
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
	}
}