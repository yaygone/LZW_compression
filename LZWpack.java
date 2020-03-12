import java.util.ArrayList;

class LZWpack
{
	String input = "";
	String output = "";
	public static void main(String[] args)
	{

	}
	public byte[] process(byte[] input, int frameSize)
	{
		byte[] finalOutput = new byte[0];
		try
		{
			byte tempData = 0;
			int inputOverhang = 0;
			int outputRemaining = 8;
			byte currOutput = 0;
			ArrayList<Byte> outputList = new ArrayList<Byte>();
			for (byte b : input)
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
						outputRemaining = 8;
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
					outputRemaining = 8 - inputOverhang;
				}
			}
			if (currOutput != 0) outputList.add(currOutput);
			finalOutput = new byte[outputList.size()];
			for (int i = 0; i < finalOutput.length; i++)
				finalOutput[i] = outputList.get(i);
		}
		catch (Exception e) {}
		return finalOutput;
	}
}