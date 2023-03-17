package com.bittercode.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.service.BookService;
import com.bittercode.util.DBUtil;

public class BookServiceImpl implements BookService {

    private static final String getAllBooksQuery = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK;
    private static final String getBookByIdQuery = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK
            + " WHERE BARCODE = ?";

    private static final String deleteBookByIdQuery = "DELETE FROM " + BooksDBConstants.TABLE_BOOK + "  WHERE "
            + BooksDBConstants.COLUMN_BARCODE + "=?";

    private static final String addBookQuery = "INSERT INTO " + BooksDBConstants.TABLE_BOOK + "  VALUES(?,?,?,?,?)";

    private static final String updateBookQtyByIdQuery = "UPDATE " + BooksDBConstants.TABLE_BOOK + " SET "
            + BooksDBConstants.COLUMN_QUANTITY + "=? WHERE " + BooksDBConstants.COLUMN_BARCODE
            + "=?";

    private static final String updateBookByIdQuery = "UPDATE " + BooksDBConstants.TABLE_BOOK + " SET "
            + BooksDBConstants.COLUMN_NAME + "=? , "
            + BooksDBConstants.COLUMN_AUTHOR + "=?, "
            + BooksDBConstants.COLUMN_PRICE + "=?, "
            + BooksDBConstants.COLUMN_QUANTITY + "=? "
            + "  WHERE " + BooksDBConstants.COLUMN_BARCODE
            + "=?";

    @Override
    public Book getBookById(String bookId) {
        Book book = null;

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(getBookByIdQuery);
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                book = new Book(bCode, bName, bAuthor, bPrice, bQty);
            }
        } catch (SQLException e) {

        }
        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<Book>();

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(getAllBooksQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
                books.add(book);
            }
        } catch (SQLException e) {

        }
        return books;
    }

    @Override
    public String deleteBookById(String bookId) {
        String response = "FAILED";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(deleteBookByIdQuery);
            ps.setString(1, bookId);
            int k = ps.executeUpdate();
            if (k == 1) {
                response = "SUCCESS";
            }
        } catch (Exception e) {
            response += " : " + e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public String addBook(Book book) {
        String responseCode = "FAILED";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(addBookQuery);
            ps.setString(1, book.getBarcode());
            ps.setString(2, book.getName());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setInt(5, book.getQuantity());
            int k = ps.executeUpdate();
            if (k == 1) {
                responseCode = "SUCCESS";
            }
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public String updateBookQtyById(String bookId, int quantity) {
        String responseCode = "FAILED";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(updateBookQtyByIdQuery);
            ps.setInt(1, quantity);
            ps.setString(2, bookId);
            ps.executeUpdate();
            responseCode = "SUCCESS";
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public List<Book> getBooksByCommaSeperatedBookIds(String commaSeperatedBookIds) {
        List<Book> books = new ArrayList<Book>();

        try {
            Connection con = DBUtil.getConnection();
            String getBooksByCommaSeperatedBookIdsQuery = "SELECT * FROM " + BooksDBConstants.TABLE_BOOK
                    + " WHERE " +
                    BooksDBConstants.COLUMN_BARCODE + " IN ( " + commaSeperatedBookIds + " )";
            PreparedStatement ps = con.prepareStatement(getBooksByCommaSeperatedBookIdsQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
                books.add(book);
            }
        } catch (SQLException e) {

        }
        return books;
    }

    @Override
    public String updateBook(Book book) {
        String responseCode = "FAILED";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(updateBookByIdQuery);
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setInt(4, book.getQuantity());
            ps.setString(5, book.getBarcode());
            ps.executeUpdate();
            responseCode = "SUCCESS";
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

}
