package secapstone.helper;


//Must match ActionItem in Firebase!

public class ActionItem {

    private String action;
    private String date;


    public ActionItem(String actionItem, String date) {
        this.action = actionItem;
        this.date = date;
    }

    public ActionItem() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
