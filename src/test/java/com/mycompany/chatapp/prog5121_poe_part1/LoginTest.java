package com.mycompany.chatapp.prog5121_poe_part1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login validLogin() {
        return new Login("Mash", "Grace", "kyl_1", "Ch&&sec@ke99!", "+27338968976");
    }

    @Test void testLoginSuccessful() { assertTrue(validLogin().loginUser("kyl_1", "Ch&&sec@ke99!")); }
    @Test void testLoginFailedWrongPassword() { assertFalse(validLogin().loginUser("kyl_1", "wrongpassword")); }
    @Test void testLoginBlockedWhenRegistrationInvalid() {
        Login invalid = new Login("Mash", "Grace", "badname", "Ch&&sec@ke99!", "+27338968976");
        assertFalse(invalid.loginUser("badname", "Ch&&sec@ke99!"));
    }
    @Test void testUsernameCorrectlyFormatted() { assertTrue(validLogin().checkUserName()); }
    @Test void testUsernameIncorrectlyFormatted() {
        assertFalse(new Login("Mash", "Grace", "kyle!!!!!!", "Ch&&sec@ke99!", "+27338968976").checkUserName());
    }
    @Test void testPasswordMeetsComplexityRequirements() { assertTrue(validLogin().checkPasswordComplexity()); }
    @Test void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(new Login("Mash", "Grace", "kyl_1", "password", "+27338968976").checkPasswordComplexity());
    }
    @Test void testCellPhoneCorrectlyFormatted() { assertTrue(validLogin().checkCellPhoneNumber()); }
    @Test void testCellPhoneIncorrectlyFormatted() {
        assertFalse(new Login("Mash", "Grace", "kyl_1", "Ch&&sec@ke99!", "08966553").checkCellPhoneNumber());
    }
    @Test void testSuccessfulLoginMessage() {
        assertEquals("Welcome Mash, Grace it is great to see you again.", validLogin().returnLoginStatus(true));
    }
    @Test void testFailedLoginMessage() {
        assertEquals("Username or password incorrect, please try again.", validLogin().returnLoginStatus(false));
    }
    @Test void testUsernameErrorMessage() {
        Login login = new Login("Mash", "Grace", "kyle!!!!!!", "Ch&&sec@ke99!", "+27338968976");
        assertTrue(login.registerUser().startsWith("Username is not correctly formatted"));
    }
    @Test void testPasswordErrorMessage() {
        Login login = new Login("Mash", "Grace", "kyl_1", "password", "+27338968976");
        assertTrue(login.registerUser().startsWith("Password is not correctly formatted"));
    }
    @Test void testPhoneErrorMessage() {
        Login login = new Login("Mash", "Grace", "kyl_1", "Ch&&sec@ke99!", "08575975889");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", login.registerUser());
    }
    @Test void testSuccessfulRegistrationMessage() { assertEquals("User registered successfully.", validLogin().registerUser()); }
}
