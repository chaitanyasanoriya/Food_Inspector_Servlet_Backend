import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Readfr
{
	protected StringBuffer read(String url) throws IOException, InterruptedException
	{
		Thread.sleep(2500);
		BufferedReader brTest = new BufferedReader(new FileReader(url+".txt"));
		
		String text = brTest.readLine();
		brTest.close();
		StringBuffer text1 = new StringBuffer("");
		char [] msg = text.toCharArray();
		for(int i=0;i<msg.length;i++)
		{
			if(Character.isDigit(msg[i]))
			{
				break;
			}
			else
			{
				text1.append(msg[i]);	
			}
		}
		return text1;
	}
}
