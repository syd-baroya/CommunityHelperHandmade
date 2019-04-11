package secapstone.helper;

import org.junit.Test;

import secapstone.helper.pages.login.LoginActivity;

import static org.junit.Assert.*;

public class LoginUnitTest
{

    @Test
    public void passwordLengthValidator() {

        LoginActivity myObjectUnderTest = new LoginActivity();

        assertTrue(myObjectUnderTest.isValidPassword("password1234"));
    }
}
