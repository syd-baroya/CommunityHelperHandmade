package secapstone.helper;

import org.junit.Test;

import secapstone.helper.model.PayoutTransaction;

import static org.junit.Assert.assertEquals;

public class PayoutUnitTest {

    @Test
    public void constructor_isCorrect()
    {
        PayoutTransaction pt = new PayoutTransaction("aaa", "vvv", 10f);
        assertEquals("aaa", pt.getCgaID());
        assertEquals("vvv", pt.getArtisanID());
        assertEquals(10f, pt.getAmount(), 0.001f);
    }

    @Test
    public void address_isCorrect()
    {
        PayoutTransaction pt = new PayoutTransaction("aaa", "vvv", 10f);
        pt.setAddress("test");
        assertEquals("test", pt.getAddress());
    }
}
