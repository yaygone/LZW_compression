import java.util.ArrayList;

class LZWpack
{
	public static void main(String[] args)
	{
		if (args.length > 2)
		{
			System.err.println("Usage: <optional:initial dict passthrough> <comma separated code, eg 0,1,2,1,0,4,3...>");
			return;
		}
		String outputString = "";
		if (args.length == 2)
		{
			String dict = "";
			for (String s : args[0].split(","))
				dict += s;
			outputString += dict + ' ';
		}
		int argsIndex = args.length == 1 ? 0 : 1;
		String[] input0 = args[argsIndex].split(",");
		char[] input1 = new char[input0.length];
		char maxVal = 0;
		for (int i = 0; i < input1.length; i++)
		{
			input1[i] = input0[i].toCharArray()[0];
			if (input1[i] > maxVal) maxVal = input1[i];
		}
		int bitCount = (int)Math.ceil(Math.log(maxVal) / Math.log(2));
		outputString += (char)bitCount + ' ';
		for (char c : new LZWpack().process(input1, bitCount))
			outputString += c;
		outputString += ' ';
		System.out.println(outputString.trim());
	}
	public char[] process(char[] input, int frameSize)
	{
		char[] finalOutput = new char[0];
		try
		{
			char tempData = 0;
			int inputOverhang = 0;
			int outputRemaining = 8;
			char currOutput = 0;
			ArrayList<Character> outputList = new ArrayList<Character>();
			for (char b : input)
			{
				if (outputRemaining >= frameSize)
				{
					outputRemaining -= frameSize;
					b <<= outputRemaining;
					currOutput |= b;
					// If there is no more space, then store away the unit and prep a fresh one for the next loop.
					if (outputRemaining == 0)
					{
						outputList.add(currOutput);
						currOutput = 0;
						outputRemaining = 16;
					}
				}
				else
				{
					// Byte can't fit into current unit's remaining bits, and must be split into head (for current unit) and tail (for next unit)
					tempData = b;
					// Input overhang, aka tail size, the number of bits that can't be stored in the current unit's remaining bits
					inputOverhang = frameSize - outputRemaining;
					// Store the head into current unit's remaining bits
					b >>>= inputOverhang;
					currOutput |= b;
					// Store away the full unit and prep a new one
					outputList.add(currOutput);
					currOutput = 0;
					// Now store the tail by shifting the frame left. Move by frame size, plus the head bits that have been stored away.
					tempData <<= 8 - frameSize + outputRemaining;
					currOutput |= tempData;
					// Update output remaining to 8 - tail size
					outputRemaining = 16 - inputOverhang;
				}
			}
			if (currOutput != 0) outputList.add(currOutput);
			finalOutput = new char[outputList.size()];
			for (int i = 0; i < finalOutput.length; i++)
				finalOutput[i] = outputList.get(i);
		}
		catch (Exception e) {}
		return finalOutput;
	}
}