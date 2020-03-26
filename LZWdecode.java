import java.io.*;
import java.util.*;

class LZWdecode
{
	public int bufferSize = (int)Math.pow(2, 8);
	public MapKey[] dictionary;
	public FileOutputStream outputStream;

	public static void main(String[] args)
	{
		if (args.length == 0 || args.length > 2) System.err.println("Usage requires target file to be decompressed, and (optional) output file. "
		 + "Defaults to \"output.file\" if not given." + "\n" + "example: java LZWdecode input.file output.mp3");
		else try { new LZWdecode().process(args); } catch (Exception e) { System.err.println(e); }
	}

	public void process(String[] args) throws FileNotFoundException, IOException
	{
		BufferedReader inputStream = new BufferedReader(new FileReader(args[0]));
		List<String> inputRaw = new ArrayList<String>();
		for (String s = inputStream.readLine(); s != null; s = inputStream.readLine())
			inputRaw.add(s);
		inputStream.close();
		int[] input = new int[inputRaw.size()];
		for (int i = 0; i < input.length; i++)
			input[i] = Integer.parseInt(inputRaw.toArray()[i].toString());
		dictionary = new MapKey[input.length + bufferSize];
		for (int i = 0; i < dictionary.length; i++)
		{
			if (i < bufferSize) dictionary[i] = new MapKey(i);
			else
			{
				MapKey tempNode = new MapKey(input[i - bufferSize]);
				dictionary[i] = tempNode;
				if (i != dictionary.length - 1)
				{
			 		if (i == input[i + 1 - bufferSize]) tempNode.next = tempNode.returnFirstSymbol();
					else tempNode.next = dictionary[input[i + 1 - bufferSize]].returnFirstSymbol();
				}
			}
		}
		outputStream = new FileOutputStream(new File(args.length == 2 ? args[1] : "output.txt"), false);
		for (int i : input)
		{
			//System.out.println("next symbol being processed!");
			dictionary[i].output(true);
		}
		outputStream.close();
		}
		
	public class MapKey
	{
		int parentAddress;
		int next = -1;
		
		public MapKey(int valueInput)
		{ parentAddress = valueInput; }
		
		public void output(boolean topLevel) throws IOException
		{
			if (parentAddress >= bufferSize)
			{
				//System.out.println("key value " + parentAddress + " is not in byte range. Searching now...");
				dictionary[parentAddress].output(false);
			}
			else
			{
				outputStream.write((byte)parentAddress);
			
				
			}
			if (next != -1)
			{
				//System.out.println("Going to print next character " + next);
				outputStream.write((byte)next);
				//System.out.println((byte)next);
			}
			 //System.out.println("no next pointer, nothing to print...");
		}

		public int returnFirstSymbol()
		{ return (parentAddress < bufferSize) ? parentAddress : dictionary[parentAddress].returnFirstSymbol(); }
	}
}