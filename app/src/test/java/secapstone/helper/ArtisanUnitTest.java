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

    @Test
    public void description_isCorrect()
    {
        Artisan a = new Artisan();
        a.setDescription("Test desc");
        assertEquals("Test desc", a.getDescription());
    }

    @Test
    public void phoneNumber_isCorrect()
    {
        Artisan a = new Artisan();
        a.setPhoneNumber("111");
        assertEquals("111", a.getPhoneNumber());
    }

    @Test
    public void address_isCorrect()
    {
        Artisan a = new Artisan();
        a.setAddress("111 St.");
        assertEquals("111 St.", a.getAddress());
    }

    @Test
    public void countryRegion_isCorrect()
    {
        Artisan a = new Artisan();
        a.setCountryRegion("test");
        assertEquals("test", a.getCountryRegion());
    }

    @Test
    public void zipPostalCode_isCorrect()
    {
        Artisan a = new Artisan();
        a.setZipPostalCode("11111");
        assertEquals("11111", a.getZipPostalCode());
    }

    @Test
    public void firstName_isCorrect()
    {
        Artisan a = new Artisan();
        a.setFirstName("John");
        assertEquals("John", a.getFirstName());
    }

    @Test
    public void lastName_isCorrect()
    {
        Artisan a = new Artisan();
        a.setLastName("Smith");
        assertEquals("Smith", a.getLastName());
    }
}