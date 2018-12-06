package secapstone.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginUnitTest
{

    @Test
    public void passwordLengthValidator() {

        LoginActivity myObjectUnderTest = new LoginActivity();

        assertTrue(myObjectUnderTest.isValidPasswordLength("password1234"));
    }
}
