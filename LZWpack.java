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
	public void process(int[] input) throws IOException
	{
		int byteBitCount = 8;
		byte outputBuffer = 0;
		int remainingInputBits;
		int remainingOutputBits = byteBitCount;
		for (int i : input)
		{
			remainingInputBits = (int)Math.ceil(Math.log(entryCount++) / Math.log(2));
			while (remainingInputBits > remainingOutputBits)
			{
				int tempData = i;
				tempData >>>= remainingInputBits - remainingOutputBits;
				tempData &= (int)(Math.pow(2, remainingOutputBits) - 1);
				outputBuffer |= tempData;
				outputStream.write(outputBuffer);
				outputBuffer = 0;
				remainingInputBits -= remainingOutputBits;
				remainingOutputBits = byteBitCount;
			}
			int tempData = i;
			tempData &= (int)(Math.pow(2, remainingInputBits) - 1);
			tempData <<= remainingOutputBits - remainingInputBits;
			outputBuffer |= tempData;
			remainingOutputBits -= remainingInputBits;
			if (remainingOutputBits == 0)
			{
				outputStream.write(outputBuffer);
				outputBuffer = 0;
				remainingOutputBits = byteBitCount;
			}
		}
		if (outputBuffer != 0) outputStream.write(outputBuffer);
		outputStream.flush();
		outputStream.close();
	}
}