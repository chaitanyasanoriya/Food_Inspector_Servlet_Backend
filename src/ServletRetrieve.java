import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ServletRetrieve")
public class ServletRetrieve extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public ServletRetrieve()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
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
			JDBCconn cconn = new JDBCconn();
			StringBuffer returnstr = cconn.getData(recievedString);
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
			//Integer doubledValue = Integer.parseInt(recievedString) * 2;

			//writer.write(doubledValue.toString());
			writer.write(String.valueOf(returnstr));
			writer.flush();
			writer.close();
		} catch (Exception e)
		{
			try
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe)
			{
			}
		}
	}

}
