import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public Servlet()
	{
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.getOutputStream().println("Hurray !! This Servlet Works");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SaveImage si = new SaveImage();
		Thread t1 = new Thread(si);
		Readfr readfr = new Readfr();
		try
		{
			int length = request.getContentLength();
			byte[] input = new byte[length];
			ServletInputStream sin = request.getInputStream();
			int c, count = 0;
			while ((c = sin.read(input, count, input.length - count)) != -1)
			{
				count += c;
			}
			sin.close();
			String recievedString = new String(input);
			response.setStatus(HttpServletResponse.SC_OK);
			String devicekey = recievedString.substring(0, 18);
			recievedString = recievedString.substring(18, recievedString.length());
			si.getImageData(recievedString);
			t1.start();
			Thread.sleep(500);
			String url = si.url;
			StringBuffer strbuff = new StringBuffer("cd /root/Downloads/tensorflow && ");
			strbuff.append("IMAGE_SIZE=224 && ");
			strbuff.append("ARCHITECTURE=\"mobilenet_0.50_${IMAGE_SIZE}\" && ");
			strbuff.append("python -m scripts.label_image \\--graph=tf_files/retrained_graph.pb \\--image=");
			strbuff.append(url);
			strbuff.append(".jpg ");
			strbuff.append(">> ");
			strbuff.append(url);
			strbuff.append(".txt");
			String[] args = new String[] {"/bin/bash", "-c",String.valueOf(strbuff), "with", "args"};
			new ProcessBuilder(args).start();
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
			StringBuffer returnstr = readfr.read(url);
			writer.write(String.valueOf(returnstr));
			writer.flush();
			writer.close();
			JDBCconn con = new JDBCconn();
			con.update_table(String.valueOf(returnstr),String.valueOf(url), devicekey);
		} catch (IOException e)
		{

			try
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe)
			{
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}