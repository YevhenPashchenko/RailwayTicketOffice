package com.my.railwayticketoffice.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for methods from {@link PasswordAuthentication}.
 *
 * @author Yevhen Pashchenko
 */
public class PasswordAuthenticationTest {

    /**
     * Test for method check from {@link PasswordAuthentication}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testCheck() throws Exception {

        String rightPassword = "rightPassword";
        String wrongPassword = "wrongPassword";

        String hashedPassword = PasswordAuthentication.getSaltedHash(rightPassword);

        assertTrue(PasswordAuthentication.check(rightPassword, hashedPassword));
        assertFalse(PasswordAuthentication.check(wrongPassword, hashedPassword));
    }

    /**
     * Test for method getSaltedHash from {@link PasswordAuthentication}.
     */
    @Test
    public void testGetSaltedHash() {

        assertThrows(NullPointerException.class, () -> PasswordAuthentication.getSaltedHash(null));
    }
}
