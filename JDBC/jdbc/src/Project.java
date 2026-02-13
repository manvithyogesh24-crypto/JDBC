import java.sql.*;
import java.util.Scanner;

public class Project {

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {

            // 1️⃣ Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2️⃣ Connect Database
            String url = "jdbc:mysql://localhost:3306/company_db?createDatabaseIfNotExist=true";
            String user = "root";
            String password = "password";   // change if needed

            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to Database!");

            // 3️⃣ Create Table
            Statement stmt = con.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS employees (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE," +
                    "department VARCHAR(50)," +
                    "salary DOUBLE)";
            stmt.executeUpdate(createTable);

            // MENU LOOP
            while (true) {

                System.out.println("\n==== Employee Management System ====");
                System.out.println("1. Add Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Search Employee");
                System.out.println("6. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                switch (choice) {

                    case 1: addEmployee(); break;
                    case 2: viewEmployees(); break;
                    case 3: updateEmployee(); break;
                    case 4: deleteEmployee(); break;
                    case 5: searchEmployee(); break;
                    case 6:
                        System.out.println("Thank You!");
                        con.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid Choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ➕ ADD EMPLOYEE
    static void addEmployee() throws SQLException {

        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Department: ");
        String dept = sc.nextLine();

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        String query = "INSERT INTO employees(name,email,department,salary) VALUES(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, dept);
        ps.setDouble(4, salary);

        ps.executeUpdate();
        System.out.println("Employee Added Successfully!");
    }

    // 📋 VIEW ALL
    static void viewEmployees() throws SQLException {

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

        System.out.println("\nID | Name | Email | Department | Salary");
        System.out.println("-------------------------------------------");

        while (rs.next()) {
            System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("email") + " | " +
                    rs.getString("department") + " | " +
                    rs.getDouble("salary")
            );
        }
    }

    // ✏️ UPDATE
    static void updateEmployee() throws SQLException {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        sc.nextLine();
        System.out.print("New Department: ");
        String dept = sc.nextLine();

        System.out.print("New Salary: ");
        double salary = sc.nextDouble();

        String query = "UPDATE employees SET department=?, salary=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, dept);
        ps.setDouble(2, salary);
        ps.setInt(3, id);

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Employee Updated!");
        else
            System.out.println("Employee Not Found!");
    }

    // ❌ DELETE
    static void deleteEmployee() throws SQLException {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        String query = "DELETE FROM employees WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();

        if (rows > 0)
            System.out.println("Employee Deleted!");
        else
            System.out.println("Employee Not Found!");
    }

    // 🔍 SEARCH
    static void searchEmployee() throws SQLException {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        String query = "SELECT * FROM employees WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\nEmployee Found:");
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Department: " + rs.getString("department"));
            System.out.println("Salary: " + rs.getDouble("salary"));
        } else {
            System.out.println("Employee Not Found!");
        }
    }
}