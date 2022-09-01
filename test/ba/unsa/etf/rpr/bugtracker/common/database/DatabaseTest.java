package ba.unsa.etf.rpr.bugtracker.common.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    Database instance;
    @BeforeEach
    void setUp() {
        instance = Database.getInstance();
        try {
            instance.restoreDefaultDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        Database.removeInstance();
    }

    @Test
    void restoreDefaultDatabase() throws SQLException {
        PreparedStatement mockStatement = instance.getConn().prepareStatement("INSERT INTO User(firstname,lastname,username,password, email, department) VALUES('a', 'b', 'c', 'd', 'e', 2);");
        mockStatement.executeUpdate();

        instance.restoreDefaultDatabase();

        mockStatement = instance.getConn().prepareStatement("SELECT COUNT(id) from User;");
        var resultSet = mockStatement.executeQuery();
        var numberOfRows = resultSet.getInt(1);

        assertEquals(0, numberOfRows);
    }

    @Test
    void getInstance() {
        instance = Database.getInstance();
        assertNotNull(instance);
    }

    @Test
    void removeInstance() throws SQLException {
        Database.removeInstance();
        assertTrue(instance.getConn().isClosed());
    }
}