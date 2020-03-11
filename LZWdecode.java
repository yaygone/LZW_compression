class LZWdecode
{
	public static int nodeCount = -1;
	public static int[] inputData;
	byte[] compressedData;

	public static void main(String[] args)
	{
		if (args.length != 2)
			System.err.println("Usage: java LZWdecode <a=0,b=1,c=2...> <0,1,0,2,4...>");
		else new LZWdecode(args[0]).run(args);
	}

	public LZWdecode(String input)
	{ compressedData = input.getBytes(); }

	public void run(String[] args)
	{
		String[] dict = args[0].split(",");
		String[] temp = args[1].split(",");
		inputData = new int[temp.length];
		for (int i = 0; i < temp.length; i++)
			inputData[i] = Integer.parseInt(temp[i]);
		TrieNode mamaNode = new TrieNode((byte)'_');
		for ()
		for (byte b : compressedData)
		{
			
		}
	}

	class TrieNode
	{
		private TrieNode[] children;
		int index;
		byte content;
		
		public TrieNode(byte s)
		{
			content = s;
			index = nodeCount;
			nodeCount++;
		}

		public int findIndex(Byte[] data)
		{
			if (data.length != 1 || data[0] != content)
			{
				Byte[] newData = new Byte[data.length - 1];
				for (int i = 1; i < data.length; i++)
					newData[i - 1] = data[i];
				if (children != null)
					for (TrieNode c : children)
						if (c.matchesByte(data[0])) return findIndex(newData);
			}
			return index;
		}

		/**
		 * Searches for the string of bytes of the index.
		 * 
		 * @param searchIndex
		 * @param data
		 * @return
		 */
		public byte[] findData(int searchIndex, byte[] data)
		{
			byte[] returnVal;
			if (data == null)
				returnVal = new byte[1];
			else
			{
				returnVal = new byte[data.length + 1];
				for (int i = 0; i < data.length; i++)
					returnVal[i] = data[i];
			}
			returnVal[returnVal.length - 1] = content;
			if (children != null)
				for (TrieNode c : children)
					return c.findData(searchIndex, returnVal);
			return returnVal;
		}

		public boolean matchesByte(byte data)
		{
			return content == data;
		}

	}
}