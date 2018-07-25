package test;
import test.DBConnection;
import java.sql.*;
import javax.servlet.*;
import java.io.*;
public class RemoveBookServlet extends GenericServlet{
	public void service(ServletRequest req,ServletResponse res) throws IOException,ServletException
	{
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");
		String bkid= req.getParameter("book_code");
		try {
			Connection con = DBConnection.getCon();
			PreparedStatement ps = con.prepareStatement("delete from book6 where bcode=?");
			ps.setString(1, bkid);
			int k = ps.executeUpdate();
			if(k==1)
			{
				RequestDispatcher rd = req.getRequestDispatcher("Sample.html");
				rd.include(req, res);
				pw.println("<div class=\"tab\">Book Removed Successfully</div>");
				pw.println("<div class=\"tab\"><a href=\"RemoveBooks.html\">Remove more Books</a></div>");
				
			}
			else
			{
				RequestDispatcher rd = req.getRequestDispatcher("Sample.html");
				rd.include(req, res);
				pw.println("<div class=\"tab\">Book Not Available In The Store</div>");
				pw.println("<div class=\"tab\"><a href=\"RemoveBooks.html\">Remove more Books</a></div>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
