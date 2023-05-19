package com.example.test.java;

import org.junit.Test;

import java.sql.SQLException;

import static com.example.kursovaya.server.MultiThreadServer.db;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DatabaseTest {

    @Test
    public void checkCredentials() throws SQLException {
        assertEquals(1, db.checkCredentials("admin", "admin"));
    }
    @Test
    public void checkCredentialsForClient() throws SQLException {
        assertEquals(3, db.checkCredentials("client2", "client2"));
    }

    @Test
    public void getClientId() {
        assertEquals(3, db.getClientId(5));
    }

    @Test
    public void getClientIdSecond() {
        assertEquals(2, db.getClientId(3));
    }


    @Test
    public void getAdminId() {
        assertEquals(3, db.getAdminId(4));
    }

    @Test
    public void getAdminIdSecond() {
        assertEquals(1, db.getAdminId(1));
    }

    @Test
    public void isLoginFree() {
        assertEquals(db.isLoginFree("admin"),
                false);
    }
    @Test
    public void isLoginFreeSecond() {
        assertEquals(db.isLoginFree("client"),
                false);
    }

    @Test
    public void getUserAvailableId() {
        assertNotEquals(db.getUserAvailableId(), 5);
    }


    @Test
    public void getUserAvailableIdSecond() {
        assertNotEquals(db.getUserAvailableId(), 1);
    }

    @Test
    public void getClientAvailableId() {
        assertNotEquals(db.getUserAvailableId(), 3);
    }

    @Test
    public void getClientAvailableIdSecond() {
        assertNotEquals(db.getUserAvailableId(), 1);
    }

}