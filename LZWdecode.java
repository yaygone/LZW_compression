import java.io.*;
import java.util.*;

class LZWdecode
{
	public int bufferSize = (int)Math.pow(2, 8);
	public MapKey[] indicesMap;
	public BufferedReader inputStream;
	public FileOutputStream outputStream;

	public static void main(String[] args)
	{
		if (args.length == 0 || args.length > 2) System.err.println("Usage requires target file to be decompressed, and (optional) output file. Default output to output.file if not given."
		+ "\n" + "example: java LZWdecode input.file output.mp3");
		else
			try {new LZWdecode().process(args);} catch (Exception e) {System.err.println(e);}
	}

	public void process(String[] args) throws FileNotFoundException, IOException
	{
		inputStream = new BufferedReader(new FileReader(args[0]));
		List<String> inputRaw = new ArrayList<String>();
		String s = inputStream.readLine();
		while (s != null)
		{
			inputRaw.add(s);
			s = inputStream.readLine();
		}
		int[] input = new int[inputRaw.toArray().length];
		for (int i = 0; i < input.length; i++)
		{
			input[i] = Integer.parseInt(inputRaw.toArray()[i].toString());
			System.out.println("Next input: " + input[i]);
		}

		indicesMap = new MapKey[input.length + bufferSize];
		for (int i = 0; i < indicesMap.length; i++)
		{
			if (i < bufferSize) indicesMap[i] = new MapKey(i);
			else
			{
				indicesMap[i] = new MapKey(input[i - bufferSize]);
				if (i != indicesMap.length - 1) indicesMap[i].next = indicesMap[input[i + 1 - bufferSize]];
			}
		}
		outputStream = new FileOutputStream(new File(args.length == 2 ? args[1] : "output.txt"), false);
		for (int i : input)
			indicesMap[i].output();
		outputStream.close();
	}
		
	public class MapKey
	{
		int value;
		MapKey next = null;
		
		public MapKey(int valueInput)
		{ value = valueInput; }
		
		public void output() throws IOException
		{
			if (value >= bufferSize)
				indicesMap[value].output();
			outputStream.write(value);
			if (next != null) outputStream.write(next.returnFirstSymbol());
		}

		public int returnFirstSymbol()
		{
			if (value < bufferSize)
				return value;
			else return next.returnFirstSymbol();
		}
	}
}