package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ToDoActivity extends Activity
{
    private TextView tvDisplayDate;
    private ArrayList<String> items;
    private MyIconArrayAdaptor itemsAdapter;
    private ListView lvItems;
    private EditText etNewItem;

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_todo);

	etNewItem = (EditText) findViewById(R.id.etNewItem);
	etNewItem.clearFocus();
	
	setCurrentDateOnView();
	setupListViewItems();
	setupListViewListener();
    }

    private void setupListViewItems()
    {
	lvItems = (ListView) findViewById(R.id.lvItems);
	lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	readItems();

	itemsAdapter = new MyIconArrayAdaptor(ToDoActivity.this, items);
	lvItems.setAdapter(itemsAdapter);
    }

    // display current date
    private void setCurrentDateOnView()
    {
	tvDisplayDate = (TextView) findViewById(R.id.tvDate);
	// set current date into textview
	tvDisplayDate.setText(getCurrentDateString());
    }

    private void setupListViewListener()
    {
	lvItems.setOnItemLongClickListener(new OnItemLongClickListener()
	{

	    @Override
	    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	    {
		items.remove(position);
		itemsAdapter.notifyDataSetChanged();
		saveItems();
		return true;
	    }

	});

	lvItems.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    {
		Intent editItemIntent = new Intent(ToDoActivity.this, EditItemActivity.class);

		// put "extras" into the bundle for access in the second activity
		String oldItemText = StringUtils.substringBefore(items.get(position), "[");
		editItemIntent.putExtra("oldItemText", oldItemText);
		
		String oldDueDate = CommonUtil.getDueDateString(items.get(position));
		editItemIntent.putExtra("oldDueDate", oldDueDate == null? "" : oldDueDate);
		
		String isDoneValue = StringUtils.substringAfter(items.get(position), "=");
		editItemIntent.putExtra("isDone", isDoneValue);

		editItemIntent.putExtra("position", position);
		startActivityForResult(editItemIntent, REQUEST_CODE); // brings up the second activity
	    }

	});
    }

    public void addTodoItem(View v)
    {	
	// itemsAdapter.add(etNewItem.getText().toString());
	items.add(etNewItem.getText().toString());
	itemsAdapter.notifyDataSetChanged();
	etNewItem.setText("");
	saveItems();
    }

    private void readItems()
    {
	File filsDir = getFilesDir();
	File todoFile = new File(filsDir, "todo.txt");
	try
	{
	    items = new ArrayList<String>(FileUtils.readLines(todoFile));

	}
	catch (IOException e)
	{
	    items = new ArrayList<String>();
	    e.printStackTrace();
	}
    }

    private void saveItems()
    {
	File filsDir = getFilesDir();
	File todoFile = new File(filsDir, "todo.txt");
	try
	{
	    FileUtils.writeLines(todoFile, items);

	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	// REQUEST_CODE is defined above
	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
	{
	    // Extract value from result extras
	    String newText = data.getExtras().getString("newItemText");
	    int position = data.getExtras().getInt("position");
	    String dueDate = data.getExtras().getString("dueDate");
	    Boolean isDone = data.getExtras().getBoolean("isDone", false);

	    items.set(position, newText + "  [" + dueDate + "] DONE=" + isDone.toString());

	    itemsAdapter.notifyDataSetChanged();
	    saveItems();
	}
    }

    private String getCurrentDateString()
    {
	int year;
	int month;
	int day;

	final Calendar c = Calendar.getInstance();
	year = c.get(Calendar.YEAR);
	month = c.get(Calendar.MONTH);
	day = c.get(Calendar.DAY_OF_MONTH);

	return new StringBuilder()
		.append("Today: ")
		.append(month + 1).append("/").append(day).append("/") // Month is 0 based, just add 1
		.append(year).append(" ").toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.to_do, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings)
	{
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

	public PlaceholderFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    View rootView = inflater.inflate(R.layout.fragment_todo, container, false);
	    return rootView;
	}
    }

}
