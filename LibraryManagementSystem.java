package jdbc_simple01;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    static void createBooksTable(String dbName) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "SAM2005")) {
            String query = "CREATE TABLE IF NOT EXISTS books (" +
                           "id SERIAL PRIMARY KEY," +
                           "title VARCHAR(100) NOT NULL," +
                           "author VARCHAR(100) NOT NULL," +
                           "published_year INT NOT NULL," +
                           "price DECIMAL(10, 2) NOT NULL)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.executeUpdate();
            System.out.println("Books table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertBook(String dbName, String title, String author, int publishedYear, double price) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "SAM2005")) {
            String query = "INSERT INTO books (title, author, published_year, price) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, title);
            pst.setString(2, author);
            pst.setInt(3, publishedYear);
            pst.setDouble(4, price);
            pst.executeUpdate();
            System.out.println("Book inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void retrieveBooks(String dbName) {
        String query = "SELECT * FROM books";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "SAM2005");
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author: " + rs.getString("author"));
                System.out.println("Published Year: " + rs.getInt("published_year"));
                System.out.println("Price: " + rs.getBigDecimal("price"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBook(String dbName, int id, String newTitle, String newAuthor, int newPublishedYear, double newPrice) {
        String query = "UPDATE books SET title = ?, author = ?, published_year = ?, price = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "SAM2005");
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newAuthor);
            pstmt.setInt(3, newPublishedYear);
            pstmt.setDouble(4, newPrice);
            pstmt.setInt(5, id);
            int updateRowCount = pstmt.executeUpdate();
            System.out.println("Rows updated: " + updateRowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBook(String dbName, int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "SAM2005");
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int updateRowCount = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + updateRowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dbName = "Library"; 
        createBooksTable(dbName);

        while (true) {
            System.out.println("Library Management System");
            System.out.println("1. Insert Book");
            System.out.println("2. Retrieve Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter published year: ");
                    int publishedYear = scanner.nextInt();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    insertBook(dbName, title, author, publishedYear, price);
                    break;

                case 2:
                    System.out.println("Books in the library:");
                    retrieveBooks(dbName);
                    break;

                case 3:
                    System.out.print("Enter book ID to update: ");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new author: ");
                    String newAuthor = scanner.nextLine();
                    System.out.print("Enter new published year: ");
                    int newPublishedYear = scanner.nextInt();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    updateBook(dbName, idToUpdate, newTitle, newAuthor, newPublishedYear, newPrice);
                    break;

                case 4:
                    System.out.print("Enter book ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    deleteBook(dbName, idToDelete);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
