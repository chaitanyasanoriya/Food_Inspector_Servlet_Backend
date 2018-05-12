import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;

public class SaveImage implements Runnable
{
	private String encoded;
	private RandomStringGenerator rsg = new RandomStringGenerator();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private Date date = new Date();
	protected String url;

	protected void getImageData(String encoded)
	{
		this.encoded = encoded;
	}

	@Override
	public void run()
	{
		// url = "D:\\Server\\"+dateFormat.format(date)+"_"+rsg.getSaltString();
		url = "/home/tomcat/uploads/" + dateFormat.format(date) + "_" + rsg.getSaltString();
		url = url.trim();
		byte[] data = Base64.decodeBase64(encoded);
		try (OutputStream stream = new FileOutputStream(url + ".jpg"))
		{
			stream.write(data);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
