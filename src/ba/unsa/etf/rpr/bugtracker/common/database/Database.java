package ba.unsa.etf.rpr.bugtracker.common.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Database {
    private static Database instance = null;
    private Connection conn = null;
    private String databaseFolder;

    private PreparedStatement deleteFromUsers = null;
    private PreparedStatement deleteFromBugs = null;
    private PreparedStatement deleteFromSolutions = null;
    private PreparedStatement deleteFromSequence = null;

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

    private void prepareStatements() throws SQLException {
        deleteFromUsers = conn.prepareStatement("DELETE FROM User;");
        deleteFromBugs = conn.prepareStatement("DELETE FROM Bug;");
        deleteFromSolutions = conn.prepareStatement("DELETE FROM Solution;");
        deleteFromSequence = conn.prepareStatement("DELETE FROM Sqlite_sequence;");
    }
}
