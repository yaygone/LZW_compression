import java.io.*;
import java.util.*;

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
		int b = 0;
		ArrayList<Byte> str = new ArrayList<>();
		for (b = fs.read(); b != -1; b = fs.read())
			str.add((byte)b);
		
		for (byte a : str)
		{
			String st = String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0');
			inputString += st;
		}
	}

	public void process() throws IOException
	{
		int phrase = 0;
		int start = 0;
		String subs = "";
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.unpack")));
		while (true)
		{
			readSize = (int)Math.ceil((Math.log(entryCount++) / Math.log(2)));
			subs = sub(start);
			if (subs == null)
			{
				br.flush();
				br.close();
				break;
			}
			phrase = Integer.parseInt(subs, 2);

			br.write(Integer.toString(phrase));
			br.write("\n");
			System.out.println(Integer.toString(phrase));
			start += readSize;
			if (start >= inputString.length() - 1) break;
		}
	}
	public String sub(int i)
	{ try { String returnString = inputString.substring(i, i + readSize); 
		return returnString; } catch (Exception e) { return null; } }
}