package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class ToDoActivity extends Activity
{
    ArrayList<String> items;
    MyIconArrayAdaptor itemsAdapter;
    ListView lvItems;
    // REQUEST_CODE can be any value we like, used to determine the result type
    // later
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_todo);
	lvItems = (ListView) findViewById(R.id.lvItems);		
	lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	
	readItems();
	//itemsAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);
	
	itemsAdapter = new MyIconArrayAdaptor(ToDoActivity.this, items);
	lvItems.setAdapter(itemsAdapter);
	setupListViewListener();

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
		String oldItemText = StringUtils.substringBefore(items.get(position), "Due");
		editItemIntent.putExtra("oldItemText", oldItemText); 
		
		editItemIntent.putExtra("position", position);
		startActivityForResult(editItemIntent, REQUEST_CODE); // brings	    up the	 second	activity
	    }

	});
    }

    public void addTodoItem(View v)
    {
	EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
	//itemsAdapter.add(etNewItem.getText().toString());
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
	   	    
	    items.set(position, newText+ "  Due Date[" + dueDate + "]");
	    itemsAdapter.notifyDataSetChanged();
	    saveItems();
	  
	   
	}
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
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
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
