package secapstone.helper;

import org.junit.Test;

import secapstone.helper.model.Artisan;

import static org.junit.Assert.*;

public class ArtisanUnitTest {
    @Test
    public void name_isCorrect()
    {
        Artisan a = new Artisan();
        a.setName("Test name");
        assertEquals("Test name", a.getName());
    }
}