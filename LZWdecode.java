class LZWdecode
{
	public static int nodeCount = -1;
	public static int[] inputData;
	byte[] compressedData;
	static ArrayList<TrieNode> outputList = new ArrayList<TrieNode>();

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
		ArrayList<TrieNode> children = new ArrayList<TrieNode>();
		int index;
		byte content;
		
		public TrieNode(byte s)
		{
			content = s;
			index = nodeCount;
			nodeCount++;
		}

		public int buildTrie(Byte[] data)
		{
			if (index == (int)data[0])
			{
				outputList.add(this);
				int nextIndex = data.length == 1 ? -1 : date[1];
				TrieNode newChild = new TrieNode(mamaNode.headSymbol(nextIndex == nodeCount ? index : nextIndex));
				children.add(newChild);
			}
			else
			{
				for (TrieNode child : children)
					child.buildTrie(data);
			}
		}


	}
}