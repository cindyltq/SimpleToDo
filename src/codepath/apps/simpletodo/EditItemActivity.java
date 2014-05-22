package codepath.apps.simpletodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;

public class EditItemActivity extends ActionBarActivity
{
    int position;
    String dueDate;
    EditText editText;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edit_item);
	setupDatePickerListener();
	
	position = getIntent().getIntExtra("position",0);
	editText = (EditText)findViewById(R.id.etItem);
	editText.setText(getIntent().getStringExtra("oldItemText"));
	editText.requestFocus();
    }
    
    public void setupDatePickerListener()
    {
	final Calendar c = Calendar.getInstance();
	int currentYear = c.get(Calendar.YEAR);
	int currentMonth = c.get(Calendar.MONTH);
	int currentDay = c.get(Calendar.DAY_OF_MONTH);

	datePicker = (DatePicker) findViewById(R.id.dpDueDate);
	
	datePicker.init(currentYear, currentMonth, currentDay, new OnDateChangedListener ()
	{
	    @Override
	    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
	    {		
		Calendar datePicked = Calendar.getInstance();
		datePicked.set(Calendar.DAY_OF_MONTH,dayOfMonth);
		datePicked.set(Calendar.MONTH,monthOfYear); 
		datePicked.set(Calendar.YEAR, year);
		 
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		dueDate = sdf.format(datePicked.getTime());	
	    }
	    
	});	
    }
    
    public void saveEditedItem(View v)
    {
	  // Prepare data intent 
	  Intent returnResultIntent = new Intent();
	  
	  // Pass relevant data back as a result
	  returnResultIntent.putExtra("newItemText", editText.getText().toString());
	  returnResultIntent.putExtra("position", position);	  
	  returnResultIntent.putExtra("dueDate", dueDate);
	  
	  // Activity finished ok, return the data
	  setResult(RESULT_OK, returnResultIntent); // set result code and bundle data for response
	  finish(); // closes the activity, pass data to parent

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.edit_item, menu);
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

}
