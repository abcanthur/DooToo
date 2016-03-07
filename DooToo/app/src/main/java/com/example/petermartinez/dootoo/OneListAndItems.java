package com.example.petermartinez.dootoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by petermartinez on 3/2/16.
 */
public class OneListAndItems extends AppCompatActivity {


    public static final String LIST_POSITION = "0";


    private TextView listTopText;
    private  ListView mListView;
    private Button addNewItem;
    private ArrayList<String> myList;
    private EditText editNewItem;
    private int theListPosition;
    private int theItemPosition;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> toDoListItemsTemp;
    private static final int ITEMEDITTEXTRC = 3950;
    private static final String TAG = "ONE_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        myList = extras.getStringArrayList("theList");
        theListPosition = extras.getInt("theListPosition");
        theItemPosition = extras.getInt("theItemPosition");



        setViews();

        toDoListItemsTemp = new ArrayList<String>();
        setToDoItems();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toDoListItemsTemp);
        mListView.setAdapter(mAdapter);


        listTopText.setText(myList.get(0));



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OneListAndItems.this, "strike through " + parent.getAdapter().getItem(position) + " item", Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startEditExistingTextActivity(position);
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMain();
            }
        });


        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                String newItem = editNewItem.getText().toString();

                if (newItem.length() > 0) {
                    Toast.makeText(OneListAndItems.this, "Let's add " + newItem + " to the list!", Toast.LENGTH_SHORT).show();
                    myList.add(newItem);
                    setToDoItems();
                    Log.i(TAG, "added new item " + newItem);
                    for(int i = 1; i < (myList.size()-1); i++){
                        Log.i(TAG, "the dootoo items " + i + " " + myList.get(i));
                    }
                } else {
                    Toast.makeText(OneListAndItems.this, "enter an item name", Toast.LENGTH_SHORT).show();
                }
                editNewItem.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editNewItem.getWindowToken(), 0);
            }

        });


    }

    private void setViews(){
        listTopText = (TextView) findViewById(R.id.listTopText);
        mListView = (ListView) findViewById(R.id.list_list_view);
        editNewItem = (EditText) findViewById(R.id.editNewItem);
        addNewItem = (Button) findViewById(R.id.addNewItem);
    }

    private void setToDoItems(){
        toDoListItemsTemp.clear();
        for(int i = 1; i< myList.size(); i++){
            toDoListItemsTemp.add(myList.get(i));
        }
    }

    public void onBackPressed(){
        goBackToMain();
    }

    private void goBackToMain(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("theList", myList);
        bundle.putInt("theItemPosition", theItemPosition);
        bundle.putInt("theListPosition", theListPosition);
        setResult(RESULT_OK, intent);
        intent.putExtras(bundle);
        Log.i(TAG, "we are accepting the input: and returning to caller activity");
        finish();
    }

    private void startEditExistingTextActivity(int whichItemPosition){
        Intent intent = new Intent(this, EditExistingText.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("theList", myList);
        bundle.putInt("theItemPosition", whichItemPosition + 1);
        bundle.putInt("theListPosition", theListPosition);
        intent.putExtras(bundle);
        startActivityForResult(intent, ITEMEDITTEXTRC);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEMEDITTEXTRC) {
            if (resultCode == RESULT_OK) {

                ArrayList<String> theReturnedList = data.getStringArrayListExtra("theList");
                theItemPosition = data.getIntExtra("theItemPosition", 0);

                theListPosition = data.getIntExtra("theListPosition", 0);

                if(theReturnedList.get(theItemPosition).equals("")){
                    myList.remove(theItemPosition);
                } else {
                    myList.set(theItemPosition, theReturnedList.get(theItemPosition));
                }
            } else if (resultCode == RESULT_CANCELED){
                //do nothing
            }
        }
        setToDoItems();
        mAdapter.notifyDataSetChanged();
    }


}
