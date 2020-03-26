import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

class LZWunpack
{
	String inputString;
	int entryCount=256;
	int readSize=0;
	public static void main(String[] args)throws IOException
	{
		LZWunpack unpack = new LZWunpack();
		unpack.getCode(args[0]);
		unpack.process();
	}
	public void process()throws IOException
	{
		int phrase=0;
		int start=0;
		int u=0;
		String subs="";
		FileOutputStream os = new FileOutputStream("output.unpack");
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
		while(true)
		{
			readSize = (int)Math.ceil((Math.log(entryCount++)/Math.log(2)));
			subs = sub(start);
			if(subs.equals("x"))
			{
				br.flush();
				br.close();
				break;
			}
			phrase = Integer.parseInt(subs,2);

			 br.write(Integer.toString(phrase));
			 br.write("\n");
			start+=readSize;
			if(start>=inputString.length()-1)
			{
				 break;
			}
		}	
	}
	public String sub(int i)
	{
		try{return  inputString.substring(i,i+readSize);}
		catch(Exception e)
		{
			return "x";
		}
		
	}

	public void getCode(String file)throws IOException
	{
		FileInputStream fs = new FileInputStream(file);
		int b=0;
		ArrayList<Byte> str = new ArrayList<>();
		for(b=fs.read();b!=-1;b=fs.read())
		{
			str.add((byte)b);
		}
		
		
		String binarycode="";
		 for (byte a : str) {
			readSize = (int)Math.ceil((Math.log(entryCount++)/Math.log(2)));
			String st = String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0');
			binarycode +=  st;
			
		 }
		inputString = binarycode;
	
		entryCount=256;
		
	}

}