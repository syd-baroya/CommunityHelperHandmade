package secapstone.helper;

import org.junit.Test;

import secapstone.helper.model.ProductTransaction;
import secapstone.helper.model.Shipments;

import static org.junit.Assert.assertEquals;

public class ShipmentUnitTest {
    @Test
    public void constructor_isCorrect()
    {
        Shipments s = new Shipments("aaa", "bbb", 5);
        assertEquals("aaa", s.getCgaID());
        assertEquals("bbb", s.getArtisanID());
        assertEquals(5, (int)s.getAmount());
    }

    @Test
    public void productID_isCorrect()
    {
        Shipments s = new Shipments("aaa", "bbb", 5);
        s.setProductID("ccc");
        assertEquals("ccc", s.getProductID());
    }
}
