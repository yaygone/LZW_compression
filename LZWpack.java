import java.util.*;
import java.io.*;

class LZWpack
{
	public static List<Integer> inputList = new ArrayList<Integer>();
	public static List<Byte> outputList = new ArrayList<Byte>();
	public static int entryCount = 256;
	public static FileOutputStream outputStream;

	public static void main(String[] args)
	{
		if (args.length > 2 || args.length == 0)
			System.err.println("Usage requires target file to be packed, and (optional) output file. "
				+ "Defaults to \"output.pack\" if not given." + "\n" + "example: java LZWpack input.file output.pack");
		else try { new LZWpack().run(args); } catch (Exception e) { System.err.println(e); }
	}

	public void run(String[] args) throws FileNotFoundException, IOException
	{

		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		
		//Transfer all input data into an arraylist for processing
		for (String s = reader.readLine(); s != null; s = reader.readLine())
			inputList.add(Integer.parseInt(s));
		reader.close();

		
		outputStream = new FileOutputStream(new File(args.length == 2 ? args[1] : "output.pack"));
		int[] inputArray = new int[inputList.size()];
		for (int i = 0; i < inputArray.length; i++)
			inputArray[i] = Integer.parseInt(inputList.get(i).toString());
		process(inputArray);
	}
	public void process(int[] input)
	{
		try
		{
			int tempData = 0;
			int remainingInputBits = 0;
			int availOutputBits = 32;
			byte currOutput = 0;
			int frameSize;
			for (int i : input)
			{
				frameSize = (int)Math.ceil(Math.log(entryCount++) / Math.log(2));
				if (availOutputBits >= frameSize)
				{
					availOutputBits -= frameSize;
					i <<= availOutputBits;
					currOutput |= i;
					// If there is no more space, then store away the unit and prep a fresh one for the next loop.
					if (availOutputBits == 0)
					{
						outputStream.write(currOutput);
						currOutput = 0;
						availOutputBits = 8;
					}
				}
				else while (frameSize > availOutputBits)
				{
					// Input overhang, aka tail size, the number of bits that can't be stored in the current unit's remaining bits
					remainingInputBits = frameSize - availOutputBits;
					if (remainingInputBits >= 0)
					{
						// Store the head into current unit's remaining bits
						tempData >>>= remainingInputBits;
						tempData |= (int)(Math.pow(2, availOutputBits) - 1);
						currOutput |= tempData;
						frameSize -= availOutputBits;
						// Store away the full unit and prep a new one
						outputStream.write(currOutput);
						currOutput = 0;
						availOutputBits = 8;
					}
					else
					{
						tempData |= (int)(Math.pow(2, frameSize) - 1);
						tempData <<= (availOutputBits - frameSize);
						currOutput |= tempData;
						availOutputBits -= frameSize;
					}
				}
			}
			if (currOutput != 0) outputStream.write(currOutput);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {System.err.println(e);}
	}
}