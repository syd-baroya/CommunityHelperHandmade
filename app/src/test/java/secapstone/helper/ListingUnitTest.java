package secapstone.helper;
import org.junit.Test;

import secapstone.helper.model.Listing;

import static org.junit.Assert.assertEquals;


public class ListingUnitTest
{
    @Test
    public void title_isCorrect()
    {
        Listing l = new Listing();
        l.setTitle("Test title");
        assertEquals("Test title", l.getTitle());
    }

    @Test
    public void description_isCorrect()
    {
        Listing l = new Listing();
        l.setDescription("Test desc");
        assertEquals("Test desc", l.getDescription());
    }

    @Test
    public void price_isCorrect()
    {
        Listing l = new Listing();
        l.setPrice(10f);
        assertEquals(10f, l.getPrice(), 0.001f);
    }

    @Test
    public void shippedCount_isCorrect()
    {
        Listing l = new Listing();
        l.setShippedCount(11);
        assertEquals(11, l.getShippedCount());
    }
}
