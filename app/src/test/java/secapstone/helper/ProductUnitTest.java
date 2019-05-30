package secapstone.helper;

import org.junit.Test;

import secapstone.helper.model.ProductTransaction;

import static org.junit.Assert.assertEquals;

public class ProductUnitTest {
    @Test
    public void constructor_isCorrect()
    {
        ProductTransaction p = new ProductTransaction("aaa", "bbb", 5, "1");
        assertEquals("aaa", p.getCgaID());
        assertEquals("bbb", p.getArtisanID());
        assertEquals(5, (int)p.getAmount());
        assertEquals("1", p.getDate());
    }

    @Test
    public void productID_isCorrect()
    {
        ProductTransaction p = new ProductTransaction("aaa", "bbb", 5, "0");
        p.setProductID("ccc");
        assertEquals("ccc", p.getProductID());
    }
}
