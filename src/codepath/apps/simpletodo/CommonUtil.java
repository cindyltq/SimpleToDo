package codepath.apps.simpletodo;

public class CommonUtil
{

    public static String getDueDateString(String itemText)
    {
	String dateString = null;
	int startIndex = itemText.indexOf("[");
	int endIndex = itemText.indexOf("]");
	if (startIndex != -1 && endIndex != -1)
	    dateString = itemText.substring(startIndex + 1, endIndex);

	return dateString;
    }
    
}
