package secapstone.helper.model;
import java.util.ArrayList;
import java.util.List;

public class Listing
{
    public enum ProductCategory
    {
        ART, JEWELRY, PET_FOOD, CLOTHES, OTHER;
    }

    private String title = ""; // Cannot contain more than 200 characters.
    private List<String> images = new ArrayList<String>(); //TODO Make this a list of files, not strings.
    private String description = "";
    private ProductCategory category;
    private int productID;
    private float price;
    private boolean isFulfilledByAmazon;

    public Listing()
    {

    }

    public Listing(String newTitle, String newDescription, ProductCategory newCategory)
    {
        this.title = newTitle;
        this.description = newDescription;
        this.category = newCategory;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    public String getTitle()
    {
        return title;
    }

    public void setDescription(String newDescription) { description = newDescription; }

    public String getDescription() { return description; }

    public void addImage(String newImage)
    {
        images.add(newImage);
    }

    public List<String> getAllImages()
    {
        return images;
    }

    public void setCategory(ProductCategory newCategory)
    {
        this.category = newCategory;
    }

    public ProductCategory getCategory()
    {
        return category;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setProductID(int newProductID)
    {
        productID = newProductID;
    }
}
