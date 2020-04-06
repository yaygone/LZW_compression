import java.io.*;
import java.util.*;

/**
 * Unpacks the input file to a console output of index codes. Assumes maximum range of 255 for first number.
 * Can be piped to LZWdecode to get the full uncompressed output.
 * COMPX301-20A Assignment 1
 * @author Shashank Mylarapu 1502775, Ye-Gon Ryoo 1126331
 */
class LZWunpack
{
	static String inputString = "";
	static int entryCount = 256;
	static int readSize = 0;
	
	public static void main(String[] args) throws IOException
	{
		LZWunpack unpack = new LZWunpack();
		unpack.getCode(args[0]);
		unpack.process();
	}

	public void getCode(String file) throws IOException
	{
		FileInputStream fs = new FileInputStream(file);
		ArrayList<Byte> str = new ArrayList<>();
		for (int b = fs.read(); b != -1; b = fs.read())
			str.add((byte)b);
		for (byte b : str)
			inputString += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	}

	public void process() throws IOException
	{
		int phrase = 0;
		int start = 0;
		String subs = "";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.unpack")));
		while (true)
		{
			readSize = (int)Math.ceil((Math.log(entryCount++) / Math.log(2)));
			subs = sub(start);
			if (subs == null) break;
			phrase = Integer.parseInt(subs, 2);

			writer.write(Integer.toString(phrase));
			writer.write("\n");
			System.out.println(Integer.toString(phrase));
			start += readSize;
			if (start >= inputString.length() - 1) break;
		}
		int finalNo = Integer.parseInt(inputString.substring(start), 2);
		if (start == inputString.length() - readSize) writer.write(finalNo);
		writer.flush();
		writer.close();
	}
	public String sub(int i)
	{
		try { return inputString.substring(i, i + readSize); }
		catch (Exception e) { return null; }
	}
}