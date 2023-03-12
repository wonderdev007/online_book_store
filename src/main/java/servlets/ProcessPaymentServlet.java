package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.config.DBUtil;
import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.UserRole;

public class ProcessPaymentServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        if (!DBUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            Connection con = DBUtil.getConnection();
            String bookIds = req.getParameter("selected");// comma seperated bookIds
            System.out.println("BookIds: " + bookIds);
            System.out.println("another:" + req.getAttribute("bookIds"));
            if (bookIds == null)
                bookIds = "1";
            RequestDispatcher rd = req.getRequestDispatcher("receipt.html");
            rd.include(req, res);
            pw.println("<div class=\"container\">\r\n"
                    + "        <div class=\"card-columns\">");
            String query = "select * from " + BooksDBConstants.TABLE_BOOK + " where " +
                    BooksDBConstants.COLUMN_BARCODE + " in (" + bookIds + ")";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            double total = 0.0;
            while (rs.next()) {
                int bPrice = rs.getInt(BooksDBConstants.COLUMN_PRICE);
                String bCode = rs.getString(BooksDBConstants.COLUMN_BARCODE);
                String bName = rs.getString(BooksDBConstants.COLUMN_NAME);
                String bAuthor = rs.getString(BooksDBConstants.COLUMN_AUTHOR);
                int bQty = rs.getInt(BooksDBConstants.COLUMN_QUANTITY);
                System.out.println("bName " + bPrice);
                String qt = "qty_" + bCode;
                int quantity = Integer.parseInt(req.getParameter(qt));
                int amount = bPrice * quantity;
                total = total + amount;
                bQty = bQty - quantity;
                PreparedStatement ps1 = con.prepareStatement("update " + BooksDBConstants.TABLE_BOOK + " set "
                        + BooksDBConstants.COLUMN_QUANTITY + "=? where " + BooksDBConstants.COLUMN_BARCODE + "=?");
                ps1.setInt(1, bQty);
                ps1.setString(2, bCode);
                ps1.executeUpdate();
                pw.println(this.addBookToCard(bCode, bName, bAuthor, bPrice, bQty));
            }
            pw.println("</div>\r\n"
                    + "    </div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addBookToCard(String bCode, String bName, String bAuthor, int bPrice, int bQty) {
        String button = "<a href=\"#\" class=\"btn btn-info\">Order Placed</a>\r\n";
        return "<div class=\"card\">\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <img class=\"col-sm-6\" src=\"logo.png\" alt=\"Card image cap\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <h5 class=\"card-title text-success\">" + bName + "</h5>\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Author: <span class=\"text-primary\" style=\"font-weight:bold;\"> " + bAuthor
                + "</span><br>\r\n"
                + "                        </p>\r\n"
                + "                        \r\n"
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "                <div class=\"row card-body\">\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        <span style='color:blue;'>Order Id: ORD" + bCode + "TM </span>\r\n"
                + "                        <br><span class=\"text-danger\">Item Yet to be Delivered</span>\r\n"
                + "                        </p>\r\n"
                + "                    </div>\r\n"
                + "                    <div class=\"col-sm-6\">\r\n"
                + "                        <p class=\"card-text\">\r\n"
                + "                        Amout Paid: <span style=\"font-weight:bold; color:green\"> &#8377; " + bPrice
                + " </span>\r\n"
                + "                        </p>\r\n"
                + button
                + "                    </div>\r\n"
                + "                </div>\r\n"
                + "            </div>";
    }
}
