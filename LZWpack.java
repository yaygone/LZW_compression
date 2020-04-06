import java.util.*;
import java.io.*;

/**
 * Takes a console input of index numbers that start at maximum range of 255, and bitpacks them into a file "output.pack".
 * COMPX301-20A Assignment 1
 * @author Shashank Mylarapu 1502775, Ye-Gon Ryoo 1126331
 */
class LZWpack
{
	public static List<Integer> inputList = new ArrayList<Integer>();
	public static List<Byte> outputList = new ArrayList<Byte>();
	public static int entryCount = 256;
	public static FileOutputStream outputStream;

	public static void main(String[] args)
	{ try { new LZWpack().run(args); } catch (Exception e) { System.err.println(e); } }

	public void run(String[] args) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// BufferedReader reader = new BufferedReader(new FileReader("packtest.txt"));
		//Transfer all input data into an arraylist for processing
		for (String s = reader.readLine(); s != null; s = reader.readLine())
			inputList.add(Integer.parseInt(s));
		reader.close();

		outputStream = new FileOutputStream(new File("output.pack"));
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
		/* The loop divides each input into chunks, then sends them in byte-sized chunks that I'll call buffers.
		 * Once the bit-size is determined, there might be 22 bits in the input, and 5 bits in the output buffer
		 * from a previous loop. This will move and fit the first five bits into the remaining output buffer,
		 * then the next chunks of 8, until 1 bit is stored into the first bit of the buffer. The buffer will
		 * now be reused for storing the first 7 bits of the next input, and so on.
		 */
		for (int i : input)
		{
			// Start off by determining the bit-size of the input. This will be at least 8.
			remainingInputBits = (int)Math.ceil(Math.log(entryCount++) / Math.log(2));
			// Enter a loop to process the input if it's longer than the space remaining in current buffer byte.
			while (remainingInputBits > remainingOutputBits)
			{
				// Make a copy of the data. int is 32 bits, but remainingInputBits tell us how many of it is actually useful.
				int tempData = i;
				// Shift the bits to align the bits we want with the available empty bits in the buffer.
				tempData >>>= remainingInputBits - remainingOutputBits;
				// Logical operators zero out all the bits that don't matter
				tempData &= (int)(Math.pow(2, remainingOutputBits) - 1);
				outputBuffer |= tempData;
				// Send the full byte buffer and start again with a fresh buffer
				outputStream.write(outputBuffer);
				outputBuffer = 0;
				// Now there's n-fewer bits to store, which means n bits to ignore at the beginning.
				remainingInputBits -= remainingOutputBits;
				remainingOutputBits = byteBitCount;
			}
			// The final part will process the last remaining tail bits of i once that's shorter than a byte.
			int tempData = i;
			tempData &= (int)(Math.pow(2, remainingInputBits) - 1);
			tempData <<= remainingOutputBits - remainingInputBits;
			outputBuffer |= tempData;
			remainingOutputBits -= remainingInputBits;
			// If the buffer is now full, then send it on its way.
			if (remainingOutputBits == 0)
			{
				outputStream.write(outputBuffer);
				outputBuffer = 0;
				remainingOutputBits = byteBitCount;
			} // Otherwise, the buffer is partially full. No matter, the next int i will be picked up from the empty-bits count of the buffer.
		}
		if (outputBuffer != 0) outputStream.write(outputBuffer);
		outputStream.flush();
		outputStream.close();
	}
}