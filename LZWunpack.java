import java.io.FileInputStream;
import java.io.IOException;
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
		unpack.getCode();
		unpack.process();
	}
	public void process()
	{
		int phrase=0;
		int start=0;
		int u=0;
		while(true)
		{
			readSize = (int)Math.ceil((Math.log(entryCount++)/Math.log(2)));
			phrase = Integer.parseInt(sub(start),2);
			 System.out.println(phrase);
			start+=readSize;
			if(start>=inputString.length()-1)
			{
				 break;
			}
		}	
	}
	public String sub(int i)
	{
		return  inputString.substring(i,i+readSize);
	}

	public void getCode()throws IOException
	{
		Scanner sc = new Scanner(System.in);	
		ArrayList<String> blist = new ArrayList<>();
		String s=sc.next();
		byte[] input = s.getBytes();
		String binarycode="";
		 for (byte b : input) {
			readSize = (int)Math.ceil((Math.log(entryCount++)/Math.log(2)));
			binarycode +=  String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		 }
		inputString = binarycode;
		System.out.println(inputString);
		entryCount=256;
		
	}

}