package codepath.apps.simpletodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyIconArrayAdaptor extends ArrayAdapter<String>
{
    private Activity context;
    private ArrayList<String> itemArray;

    public MyIconArrayAdaptor(Activity context, ArrayList<String> itemArray)
    {
	super(context, R.layout.list_single, itemArray);
	this.context = context;
	this.itemArray = itemArray;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView = inflater.inflate(R.layout.list_single, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
	txtTitle.setText(itemArray.get(position));

	int daysCount = getDaysCount(position);

	if (daysCount > 0)
	{
	    imageView.setImageResource(R.drawable.yes);
	}
	else
	{
	    imageView.setImageResource(R.drawable.no);
	}

	return rowView;
    }

    private int getDaysCount(int position)
    {
	int daysCount = 1; // default - 1 day left, assuming today is the last day

	try
	{
	    String itemText = itemArray.get(position);
	    int startIndex = itemText.indexOf("[");
	    int endIndex = itemText.indexOf("]");
	    if (startIndex != -1 && endIndex != -1)
	    {
		String dateString = itemText.substring(startIndex + 1, endIndex);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dueDate = sdf.parse(dateString);

		daysCount = daysBetween(new Date(), dueDate, true);
	    }
	}
	catch (ParseException e)
	{
	    System.out.print("Error parsing date string");
	}

	return daysCount;
    }

    public int daysBetween(Date startDate, Date endDate, boolean countWeekends)
    {
	if (startDate == null || endDate == null || startDate.compareTo(endDate) == 1)
	    return 0;

	Calendar start = Calendar.getInstance();
	start.setTime(startDate);

	Calendar end = Calendar.getInstance();
	end.setTime(endDate);

	Calendar date = (Calendar) start.clone();
	// assert(start.before(end));
	int days = 0;
	while (date.before(end))
	{
	    date.add(Calendar.DAY_OF_MONTH, 1);

	    if (countWeekends)
	   	days++;
	    else
	    {
		if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
    			continue;
		else
		    days++;
	    }
	}
	return days;
    }

    public void refresh(ArrayList<String> items)
    {
	this.itemArray = items;
	notifyDataSetChanged();
    }

}
