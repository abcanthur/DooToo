package com.example.petermartinez.dootoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView mListView;
    private EditText newListTitle;
    private Button addNewList;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<ArrayList<String>> myList;
    private ArrayList<String> toDoListTitlesTemp;
    private static final int ONELISTRC = 3245;
    private static final int EDITTEXTRC = 9983;
    private int theItemPosition;
    private int theListPosition;
    private boolean isFirstRun = true;

    //app home screen scnreenshot: http://i.imgur.com/FSvNGNe.png




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toDoListTitlesTemp = new ArrayList<String>();
        setViews();

        if(isFirstRun) {
            setToDoLists();
            isFirstRun = false;

        }


        setToDoListTitles();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toDoListTitlesTemp);
        mListView.setAdapter(mAdapter);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                //.setAction("Action", null).show();
//                String newList = newListTitle.getText().toString();
//
//                if (newList.length() > 0) {
//                    Toast.makeText(MainActivity.this, "Let's make a new list called " + newList, Toast.LENGTH_SHORT).show();
//                    ArrayList<String> newListArray = new ArrayList<String>();
//                    newListArray.add(newList);
//                    myList.add(newListArray);
//                    startOneListAndItemsActivity(myList.size() - 1);
//                } else {
//                    Toast.makeText(MainActivity.this, "enter a list title", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        addNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newList = newListTitle.getText().toString();

                if (newList.length() > 0) {
                    Toast.makeText(MainActivity.this, "Let's make a new list called " + newList, Toast.LENGTH_SHORT).show();
                    ArrayList<String> newListArray = new ArrayList<String>();
                    newListArray.add(newList);
                    myList.add(newListArray);
                    startOneListAndItemsActivity(myList.size() - 1);
                } else {
                    Toast.makeText(MainActivity.this, "enter a list title", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "entering the " + parent.getAdapter().getItem(position) + " list", Toast.LENGTH_SHORT).show();
                startOneListAndItemsActivity(position);

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startEditExistingTextActivity(position);
                return true;
            }
        });
    }




    private void setViews() {
        mListView = (ListView) findViewById(R.id.main_list_view);
        newListTitle = (EditText) findViewById(R.id.editNewListTitle);
        addNewList = (Button) findViewById(R.id.addNewList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  //default code from activity template, unused, possibly causing toolbar popdown semi-bug
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //default code from template, unused
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setToDoListTitles() { //loads titles from myList into temp array, iteratively adds them to listview adapter

        ArrayList<String> toDoListTitlesSetter = new ArrayList<String>();
        for (int i = 0; i < myList.size(); i++) {
            toDoListTitlesSetter.add(myList.get(i).get(0));
        }
        toDoListTitlesTemp.clear();
        for(int i = 0; i < toDoListTitlesSetter.size(); i++){
            toDoListTitlesTemp.add(toDoListTitlesSetter.get(i));
        }

        Log.i(TAG, "we have set the list titles");
    }

    private void startOneListAndItemsActivity(int whichListPosition){ //calls activity for viewing a lists contents
        ArrayList<String> listToExpand = new ArrayList<String>();
        listToExpand = myList.get(whichListPosition);
        Intent intent = new Intent(this, OneListAndItems.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("theList", listToExpand);
        bundle.putInt("theListPosition", whichListPosition);
        bundle.putInt("theItemPosition", 0);
        intent.putExtras(bundle);
        startActivityForResult(intent, ONELISTRC);
    }

    private void startEditExistingTextActivity(int whichListPosition){ //calls activity to edit a list title
        ArrayList<String> listToExpand = new ArrayList<String>();
        listToExpand = myList.get(whichListPosition);
        Intent intent = new Intent(this, EditExistingText.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("theList", listToExpand);
        bundle.putInt("theListPosition", whichListPosition);
        bundle.putInt("theItemPosition", 0);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDITTEXTRC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITTEXTRC) { //if returns from edit text activity, replaces edited item, or deletes item if it was deleted
            if (resultCode == RESULT_OK) {
                ArrayList<String> theReturnedList = data.getStringArrayListExtra("theList");
                theItemPosition = data.getIntExtra("theItem", 0);
                theListPosition = data.getIntExtra("theListPosition", 0);

                if(theReturnedList.get(theItemPosition).equals("")){ //deleting an item is accomplished by passing back a blank string
                    myList.remove(theListPosition);
                } else {
                    myList.set(theListPosition, theReturnedList);
                }
            } else if (resultCode == RESULT_CANCELED){
                //do nothing
            }
        } else if (requestCode == ONELISTRC) { //if returns from viewing a lists contents, updates myList to contain the possibly edited/expanded list
            if (resultCode == RESULT_OK){
                ArrayList<String> theReturnedList = data.getStringArrayListExtra("theList");
                theItemPosition = data.getIntExtra("theItem", 0);
                theListPosition = data.getIntExtra("theListPosition", 0);
                myList.set(theListPosition, theReturnedList);
            }
        }

        setToDoListTitles();
        mAdapter.notifyDataSetChanged();
    }

    private void setToDoLists(){ //let's start out with some sample lists
        //actually, because I start with these lists, I never tested with no data to begin with
        //and may not have proper instantiation of arraylists from scratch upon user input

        ArrayList<String> groceries = new ArrayList<String>();
        groceries.add("groceries");
        groceries.add("apples");
        groceries.add("gogurts");
        groceries.add("cereal");
        groceries.add("fruit roll ups");
        groceries.add("lunch meat");
        groceries.add("milk");
        groceries.add("something for dessert");
        groceries.add("steak");
        groceries.add("milksteak");
        groceries.add("cookies");
        groceries.add("brewzongs");

        ArrayList<String> bills = new ArrayList<String>();
        bills.add("bills");
        bills.add("car loan");
        bills.add("cable");
        bills.add("rent");

        ArrayList<String> emails = new ArrayList<String>();
        emails.add("emails");

        ArrayList<ArrayList<String>> tmpMyList;
        tmpMyList = new ArrayList<ArrayList<String>>();

        tmpMyList.add(groceries);
        tmpMyList.add(bills);
        tmpMyList.add(emails);

        myList = tmpMyList;

    }
}
