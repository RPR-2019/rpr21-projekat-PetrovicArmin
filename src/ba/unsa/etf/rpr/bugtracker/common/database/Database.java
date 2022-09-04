package ba.unsa.etf.rpr.bugtracker.common.database;

import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import ba.unsa.etf.rpr.bugtracker.models.*;
import org.apache.commons.lang.ObjectUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private PreparedStatement updateUserByUsername = null;


    private PreparedStatement insertNewBug = null;
    private PreparedStatement getBugByTitle = null;
    private PreparedStatement getAllBugs = null;

    private PreparedStatement getSolutionForBug = null;
    private PreparedStatement storeSolution = null;

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

    public void updateUser(User user) {
        try {
            updateUserByUsername.setString(1, user.getLastname());
            updateUserByUsername.setString(2, user.getFirstname());
            updateUserByUsername.setString(3, user.getPassword());
            updateUserByUsername.setInt(4, user.getDepartment().ordinal());
            updateUserByUsername.setString(5, user.getUsername());
            updateUserByUsername.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        deleteFromUsers = conn.prepareStatement("DELETE FROM User;");
        deleteFromBugs = conn.prepareStatement("DELETE FROM Bug;");
        deleteFromSolutions = conn.prepareStatement("DELETE FROM Solution;");
        deleteFromSequence = conn.prepareStatement("DELETE FROM Sqlite_sequence;");

        selectUserByUsername = conn.prepareStatement("SELECT * FROM User WHERE username = ?;");
        selectUserByEmail = conn.prepareStatement("SELECT * FROM User WHERE email = ?;");
        selectAllUsers = conn.prepareStatement("SELECT * FROM User;");

        insertNewUser = conn.prepareStatement("INSERT INTO User(firstname, lastname, username, password, email, department) VALUES(?,?,?,?,?,?)");

        updateUserByUsername = conn.prepareStatement("UPDATE User SET lastname = ?, firstname = ?, password = ?, department = ? WHERE username = ?;");

        insertNewBug = conn.prepareStatement("INSERT INTO Bug(title, description, language, urgency, keywords, code, date, imageUrl, asker_id) VALUES(?,?,?,?,?,?,?,?,?);");
        getBugByTitle = conn.prepareStatement("SELECT * FROM Bug WHERE title = ?;");
        getAllBugs = conn.prepareStatement("SELECT * FROM Bug;");
        getSolutionForBug = conn.prepareStatement("SELECT * FROM Solution WHERE id = ?;");
        storeSolution = conn.prepareStatement("INSERT INTO Solution (id, description, date, code, imageUrl, solver_id) VALUES (?,?,?,?,?,?);");
    }

    public User getUserById(int id) {
        return getAllUsers().stream().filter(usr -> usr.getId() == id).findFirst().orElse(null);
    }

    public List<Bug> getAllBugs() {
        List<Bug> allBugs = new ArrayList<>();
        try {
            var rs = getAllBugs.executeQuery();
            while (rs.next()) {
                var solution = getSolutionForBug(rs.getInt(1));
                var usrId = rs.getInt(10);
                if (solution == null)
                    allBugs.add(new ActiveBug(rs.getInt(1), rs.getString(2), rs.getString(3), Language.intToLanguage(rs.getInt(4)), Urgency.intToUrgency(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(9), getUserById(usrId), LocalDate.parse(rs.getString(8))));
                else
                    allBugs.add(new SolvedBug(rs.getInt(1), rs.getString(2), rs.getString(3), Language.intToLanguage(rs.getInt(4)), Urgency.intToUrgency(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(9), getUserById(usrId), LocalDate.parse(rs.getString(8)), solution));
            }
        } catch (SQLException | InvalidIndexException throwables) {
            throwables.printStackTrace();
        }

        return allBugs;
    }

    public Solution getSolutionForBug(int id) {
        try {
            getSolutionForBug.setInt(1, id);
            var rs = getSolutionForBug.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt(6);
                return new Solution(id, rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4), rs.getString(5), getUserById(userId));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public Bug getBugByTitle(String title) {
        try {
            getBugByTitle.setString(1, title);

            var rs = getBugByTitle.executeQuery();

            if (rs.next()) {
                var userId = rs.getInt(10);
                var solution = getSolutionForBug(rs.getInt(1));
                if (solution == null)
                    return new ActiveBug(rs.getInt(1), rs.getString(2), rs.getString(3), Language.intToLanguage(rs.getInt(4)), Urgency.intToUrgency(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(9), getUserById(userId), LocalDate.parse(rs.getString(8)));
                else
                    return new SolvedBug(rs.getInt(1), rs.getString(2), rs.getString(3), Language.intToLanguage(rs.getInt(4)), Urgency.intToUrgency(rs.getInt(5)), rs.getString(6), rs.getString(7), rs.getString(9), getUserById(userId), LocalDate.parse(rs.getString(8)), solution);
            }
        } catch (SQLException | InvalidIndexException throwables) {
            throwables.printStackTrace();
        }

        return null;
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

    public void storeBug(ActiveBug bug) {
        try {
            insertNewBug.setString(1, bug.getTitle());
            insertNewBug.setString(2, bug.getDescription());
            insertNewBug.setInt(3, bug.getLanguage().ordinal());
            insertNewBug.setInt(4, bug.getUrgency().ordinal());
            insertNewBug.setString(5, bug.getKeywords());
            insertNewBug.setString(6, bug.getCode());
            insertNewBug.setString(7, bug.getDatePosted().format(DateTimeFormatter.ISO_LOCAL_DATE));
            insertNewBug.setString(8, bug.getImageUrl());
            insertNewBug.setInt(9, bug.getUserWhoAsked().getId());

            insertNewBug.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void storeSolution(Solution solution) {
        try {
            storeSolution.setInt(1, solution.getId());
            storeSolution.setString(2, solution.getDescription());
            storeSolution.setString(3, solution.getDatePosted().toString());
            storeSolution.setString(4, solution.getCode());
            storeSolution.setString(5, solution.getImageUrl());
            storeSolution.setInt(6, solution.getUserWhoSolved().getId());
            storeSolution.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
