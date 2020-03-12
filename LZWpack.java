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
					tempData = b;
					inputOverhang = frameSize - outputRemaining;
					b >>>= inputOverhang;
					currOutput |= b;
					outputList.add(currOutput);
					currOutput = 0;
					tempData <<= 8 - frameSize + inputOverhang;
					currOutput |= tempData;
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