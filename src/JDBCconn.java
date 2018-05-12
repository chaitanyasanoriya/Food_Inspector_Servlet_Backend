import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class JDBCconn
{
	File file;
	BufferedImage image;
	ByteArrayOutputStream baos;
	String encoded;
	protected void update_table(String name, String url, String Device_id)
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		month = month +1;
		int date1 = calendar.get(Calendar.DATE);
		int dateplus = date1+3;
		int year = calendar.get(Calendar.YEAR);
		StringBuffer d = new StringBuffer(date1);
		d.append(date1);
		d.append("-");
		d.append(month);
		d.append("-");
		d.append(year);
		StringBuffer ds = new StringBuffer(dateplus);
		ds.append(dateplus);
		ds.append("-");
		ds.append(month);
		ds.append("-");
		ds.append(year);
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.43.191:1521:XE", "CARPOOL", "123456");
			String sql = "INSERT INTO HISTORY(DEVICE_ID,WORD,URL,DATE_READ,DATE_SPOIL) VALUES ('" + Device_id + "','"
					+ name + "','" + url + "','" + d + "','" + ds + "')";
			PreparedStatement ps = con.prepareStatement(sql);
			int rowsins = ps.executeUpdate();
			if(rowsins>0)
			{
				
			}
			else
			{
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected StringBuffer getData(String devicekey)
	{
		StringBuffer str = new StringBuffer("");
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.43.191:1521:XE", "CARPOOL", "123456");
			String sql = "SELECT * FROM HISTORY WHERE DEVICE_ID='"+devicekey+"'";
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				str.append(resultSet.getString(2));
				str.append("\\n");
				file = new File(resultSet.getString(3)+".jpg");
				image = ImageIO.read(file);
				baos = new ByteArrayOutputStream();
				ImageIO.write(image, "jpg", baos);
				byte [] res = baos.toByteArray();
				encoded = Base64.encode(res, Base64.BASE64DEFAULTLENGTH);
				str.append(encoded);
				str.append("\\n");
				str.append(resultSet.getString(4));
				str.append("\\n");
				str.append(resultSet.getString(5));
				str.append("\\n");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}
}
