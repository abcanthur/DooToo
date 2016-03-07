package com.example.petermartinez.dootoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by petermartinez on 3/1/16.
 */
public class EditExistingText extends AppCompatActivity {

    private TextView editTopText;
    private EditText editText;
    private Button acceptButton;
    private Button cancelButton;
    private Button deleteButton;
    private TextView editText_delete;
    private Button cancelButtonDelete;
    private Button deleteButtonDelete;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> myList;
    private int theItemPosition;
    private int theListPosition;
    private String theItem;
    private ArrayList<String> toDoListItemsTemp;
    private static final String TAG = "EDIT_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        setViews();

        Bundle extras = getIntent().getExtras(); //unbundle!
        myList = extras.getStringArrayList("theList");
        theItemPosition = extras.getInt("theItemPosition");
        theListPosition = extras.getInt("theListPosition");
        theItem = myList.get(theItemPosition);
        editTopText.setText("You are editing " + theItem);
        editText.setHint(theItem);

        toDoListItemsTemp = new ArrayList<String>();
        setToDoItems(); //populate for adapter

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toDoListItemsTemp);
        mListView.setAdapter(mAdapter);

        setOnClickListeners();


    }

    private void acceptButtonClick(){
        if(editText.getText().toString().length() == 0){
            deleteButtonClick(); //shift to delete confirmation sequence
        } else { //otherwise send the updated text back to originating activity
            String newItem = editText.getText().toString();
            myList.set(theItemPosition, newItem);

            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("theList", myList);
            bundle.putInt("theItemPosition", theItemPosition);
            bundle.putInt("theListPosition", theListPosition);
            setResult(RESULT_OK, intent);
            intent.putExtras(bundle);
            Log.i(TAG, "we are accepting the input: " + newItem + " and returning to caller activity");
            finish();
        }
    }

    private void cancelButtonClick(){
        setResult(RESULT_CANCELED); //this message will simply do nothing upon return to activity because no changes are desired
        Log.i(TAG, "we are cancelling the edit and returning to caller activity");
        finish();
    }

    private void deleteButtonClick(){
        editText_delete.setText("Delete " + theItem + " " + editText.getText() + " ?"); //ask for confirmation
        editText_delete.setVisibility(View.VISIBLE); //pop up these three new views, text and two buttons
        cancelButtonDelete.setVisibility(View.VISIBLE);
        deleteButtonDelete.setVisibility(View.VISIBLE);
    }

    private void cancelButtonDeleteClick(){
        editText_delete.setVisibility(View.GONE); //simply make the three views go back away
        cancelButtonDelete.setVisibility(View.GONE);
        deleteButtonDelete.setVisibility(View.GONE);
    }

    private void deleteButtonDeleteClick(){
        myList.set(theItemPosition, ""); //because we're deleting, and they may not have keyboard deleted the text, we do so now
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("theList", myList);
        bundle.putInt("theItemPosition", theItemPosition);
        bundle.putInt("theListPosition", theListPosition);
        setResult(RESULT_OK, intent);
        intent.putExtras(bundle);
        Log.i(TAG, "they are going to delete, input is:" + myList.get(theItemPosition) + ":and returning to caller activity");
        finish();
    }

    private void setViews(){
        editTopText = (TextView) findViewById(R.id.editTopText);
        editText = (EditText) findViewById(R.id.editText);
        acceptButton = (Button) findViewById(R.id.accept_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        editText_delete = (TextView) findViewById(R.id.editText_delete);
        cancelButtonDelete = (Button) findViewById(R.id.cancel_button_delete);
        deleteButtonDelete = (Button) findViewById(R.id.delete_button_delete);
        mListView = (ListView) findViewById(R.id.edit_list_view);

    }

    private void setToDoItems(){
        for(int i = 0; i< myList.size(); i++){ //populate the list minus the item to edit
            if(i != theItemPosition) {
                toDoListItemsTemp.add(myList.get(i));
            }
        }
    }

    private void setOnClickListeners(){
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptButtonClick();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonClick();
            }
        });

        cancelButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonDeleteClick();
            }
        });

        deleteButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonDeleteClick();
            }
        });
    }
}
