package ba.unsa.etf.rpr.bugtracker.common.database;

import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import ba.unsa.etf.rpr.bugtracker.models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    private static Database instance = null;
    private Connection conn = null;
    private String databaseFolder;

    private PreparedStatement deleteFromUsers = null;
    private PreparedStatement deleteFromBugs = null;
    private PreparedStatement deleteFromSolutions = null;
    private PreparedStatement deleteFromSequence = null;

    private PreparedStatement selectUserByUsername = null;
    private PreparedStatement selectUserByEmail = null;

    private PreparedStatement insertNewUser = null;
    private PreparedStatement selectAllUsers = null;

    private Database() {
        databaseFolder = Paths.get(System.getProperty("user.dir"), "src", "ba", "unsa", "etf", "rpr", "bugtracker", "common", "database").toString();
        String connectionString = "jdbc:sqlite:" + Paths.get(databaseFolder, "database.db");

        try {
            conn = DriverManager.getConnection(connectionString);
            prepareStatements();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void restoreDefaultDatabase() throws SQLException {
        deleteFromSolutions.executeUpdate();
        deleteFromBugs.executeUpdate();
        deleteFromUsers.executeUpdate();
        deleteFromSequence.executeUpdate();

        this.createFromDump();
    }

    private void createFromDump() {
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileInputStream(Paths.get(databaseFolder, "database.db.sql").toString()));
            StringBuilder sqlUpit = new StringBuilder();
            while (ulaz.hasNext()) {
                sqlUpit.append(ulaz.nextLine());
                if ( sqlUpit.length() > 1 && sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit.toString());
                        sqlUpit = new StringBuilder();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            System.err.println("SQL dump does not exist!");
        }
    }

    public Connection getConn() {
        return conn;
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        instance = null;
    }

    public User getUserByEmail(String email) {
        try {
            selectUserByEmail.setString(1, email);
            return getUser(selectUserByEmail);
        } catch (SQLException | InvalidIndexException e) {
            e.printStackTrace();
        }

        return null;
    }

    private User getUser(PreparedStatement stmt) throws SQLException, InvalidIndexException {
        var rs = stmt.executeQuery();

        if (rs.next()) {
            //id(1), username(4), lastname(3), firstname(2), email(6), password(5), department(7)
            return new User(rs.getInt(1), rs.getString(4), rs.getString(3), rs.getString(2), rs.getString(6), rs.getString(5), Department.intToDepartment(rs.getInt(7)));
        }

        return null;
    }

    public User getUserByUsername(String username) {
        try {
            selectUserByUsername.setString(1, username);
            return getUser(selectUserByUsername);
        } catch (SQLException | InvalidIndexException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void prepareStatements() throws SQLException {
        deleteFromUsers = conn.prepareStatement("DELETE FROM User;");
        deleteFromBugs = conn.prepareStatement("DELETE FROM Bug;");
        deleteFromSolutions = conn.prepareStatement("DELETE FROM Solution;");
        deleteFromSequence = conn.prepareStatement("DELETE FROM Sqlite_sequence;");

        selectUserByUsername = conn.prepareStatement("SELECT * FROM User WHERE username = ?;");
        selectUserByEmail = conn.prepareStatement("SELECT * FROM User WHERE email = ?;");
        insertNewUser = conn.prepareStatement("INSERT INTO User(firstname, lastname, username, password, email, department) VALUES(?,?,?,?,?,?)");
        selectAllUsers = conn.prepareStatement("SELECT * FROM User;");
    }

    List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            var rs = selectAllUsers.executeQuery();
            while (rs.next())
                list.add(new User(rs.getInt(1), rs.getString(4), rs.getString(3), rs.getString(2), rs.getString(6), rs.getString(5), Department.intToDepartment(rs.getInt(7))));
        } catch (SQLException | InvalidIndexException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void storeUser(User user) {

        try {
            insertNewUser.setString(1, user.getFirstname());
            insertNewUser.setString(2, user.getLastname());
            insertNewUser.setString(3, user.getUsername());
            insertNewUser.setString(4, user.getPassword());
            insertNewUser.setString(5, user.getEmail());
            insertNewUser.setInt(6, user.getDepartment().ordinal());
            insertNewUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
