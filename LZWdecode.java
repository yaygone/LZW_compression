import java.util.ArrayList;

class LZWdecode
{
	public static int nodeCount = -1;
	public static ArrayList<Integer> indices;
	TrieNode mamaNode;
	static ArrayList<TrieNode> outputList = new ArrayList<TrieNode>();

	public static void main(String[] args)
	{
		if (args.length != 2)
			System.err.println("Usage: java LZWdecode <a,b,c...> <0,1,0,2,4...>");
		else new LZWdecode().process(args);
	}

	public void process(String[] args)
	{
		String[] dict = args[0].split(",");
		String[] temp = args[1].split(",");
		indices = new ArrayList<Integer>();
		for (int i = 0; i < temp.length; i++)
			indices.add(Integer.parseInt(temp[i]));

		mamaNode = new TrieNode((byte)'_');

		for (int i = 0; i < dict.length; i++)
			mamaNode.children.add(new TrieNode((byte)dict[i].toCharArray()[0]));
		while (indices.size() > 0)
		{
			mamaNode.buildTrie(indices.get(0), indices.size() > 1 ? indices.get(1) : -1);
			indices.remove(0);
		}
		mamaNode.expandContent("");
		printData();
	}

	public void printData()
	{
		String output = "";
		for (TrieNode node : outputList)
			output += node.fullContent;
		System.out.println("Decompressed message: " + output);
	}

	class TrieNode
	{
		ArrayList<TrieNode> children = new ArrayList<TrieNode>();
		int index;
		byte content;
		String fullContent = "";
		
		public TrieNode(byte s)
		{
			content = s;
			index = nodeCount;
			nodeCount++;
		}

		public void buildTrie(int curr, int next)
		{
			if (index == curr)
			{
				outputList.add(this);
				int nextIndex = next;
				TrieNode newChild = new TrieNode(mamaNode.headSymbol(nextIndex == nodeCount ? index : nextIndex));
				children.add(newChild);
			}
			else
				for (TrieNode child : children)
					child.buildTrie(curr, next);
		}

		public byte headSymbol(int searchIndex)
		{
			for (TrieNode child : children)
				if (child.headSymbol(searchIndex) != -1)
					return index == -1 ? child.content : content;
			if (index == searchIndex)
				return content;
			return -1;
		}

		public void expandContent(String prefix)
		{
			try
			{
				if (content != (byte)'_')
					fullContent = prefix + (char)content;
				for (TrieNode child : children)
					child.expandContent(fullContent);
			}
			catch (Exception e)
			{ System.err.println("expandContent error" + e); }
		}

	}
}