package secapstone.helper;

import org.junit.Test;

import secapstone.helper.Pages.Login.LoginActivity;

import static org.junit.Assert.*;

public class LoginUnitTest
{

    @Test
    public void passwordLengthValidator() {

        LoginActivity myObjectUnderTest = new LoginActivity();

        assertTrue(myObjectUnderTest.isValidPassword("password1234"));
    }
}
