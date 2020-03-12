import java.util.ArrayList;

LZWunpack
{
	String inputString = "";
	public static void main(String[] args)
	{
		args[0].toCharArray();
		
	}

	public byte[] process(byte[] input, int frameSize)
	{
		try
		{
			ArrayList<Byte> outputList = new ArrayList<Byte>();

			byte tempData = 0;
			int tempSize = 0;
			int startIndex = 0;
			byte currOutput = 0;

			for (byte b : input)
			{
				tempData = b;
				if (startIndex > frameSize)
				{
					
				}

				


				startIndex = (startIndex + frameSize) % 8;
			}
		}
		catch (Exception e) {}
		return new byte[1];
	}

}