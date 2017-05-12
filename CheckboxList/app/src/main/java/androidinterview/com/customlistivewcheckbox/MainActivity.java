package androidinterview.com.customlistivewcheckbox;

import android.app.Activity;
import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends/* ListActivity */Activity implements ListAdapter
{

	String[] city= {
			"Bangalore",
			"Chennai",
			"Mumbai",
			"Pune",
			"Delhi",
			"Jabalpur",
			"Indore",
			"Ranchi",
			"Hyderabad",
			"Ahmedabad",
			"Kolkata",
			"Bhopal"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// -- Display mode of the ListView

		ListView listview = new ListView(this);
	//	ListView listview= getListView();
	//	listview.setChoiceMode(listview.CHOICE_MODE_NONE);
	//	listview.setChoiceMode(listview.CHOICE_MODE_SINGLE);
		listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
		
		//--	text filtering
		listview.setTextFilterEnabled(true);
		listview.setAdapter(this);
		listview.check
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				CheckedTextView item = (CheckedTextView) view;
				Toast.makeText(getApplicationContext(), city[position] + " checked : " +
				item.isChecked(), Toast.LENGTH_SHORT).show();
			}
		});
		//	setListAdapter(new ArrayAdapter<String>(this,
			//		android.R.layout.simple_list_item_checked,city));
		setContentView(listview);
	}
	
	/*public void onListItemClick(ListView parent, View v,int position,long id){
		CheckedTextView item = (CheckedTextView) v;
		Toast.makeText(this, city[position] + " checked : " +
		item.isChecked(), Toast.LENGTH_SHORT).show();
	}*/

	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{

	}

	@Override
	public int getCount()
	{
		return city.length;
	}

	@Override
	public Object getItem(int position)
	{
		return city[position];
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		CheckedTextView textView = new CheckedTextView(this);
		textView.setText(city[position]);
		return textView;
	}

	@Override
	public int getItemViewType(int position)
	{
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return getCount() > 0;
	}
}
