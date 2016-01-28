package com.example.rishel.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rishel on 1/21/16.
 */
public class extract2 extends Activity implements AdapterView.OnItemSelectedListener {
    String text;
    LinearLayout tags,data;
    private List<EditText> editTextList = new ArrayList<EditText>();
    private List<EditText> editTextListFinaltags = new ArrayList<EditText>();
    private List<EditText> editTextListFinaldata = new ArrayList<EditText>();

    private List<Spinner> spinnerList = new ArrayList<>();
    private ArrayList<String> taglist = new ArrayList<String>();
    private ArrayList<String> datalist = new ArrayList<String>();
    List<String> spinnerl;
    ArrayAdapter<String> dataAdapter1;
    int _id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form2);
        spinnerl = new ArrayList<String>();
        spinnerl.add("Choose field for data on the left.");
        spinnerl.add("Name");
        spinnerl.add("Date of birth");
        spinnerl.add("NID number");
        spinnerl.add("Issue date");
        dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerl);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text = getIntent().getStringExtra("TEXT");
        tags=(LinearLayout)findViewById(R.id.tag);
        data=(LinearLayout)findViewById(R.id.data);
        if (text != null) {
            int c=0;
              Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {

            if (matcher.group()!=null) {
                tags.addView(editText(c,1));
                data.addView(spinner(c));
                editTextList.get(c).setText(matcher.group());
                c++;
                Log.v("  ", matcher.group());

            }
        }

        }
    }





    private EditText editText(int _intID, int type) {
        EditText editText = new EditText(this);
        editText.setId(_intID);
        editText.setHint("Tag " + _intID);
        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        editText.setLayoutParams(params);
        switch(type)
        {
            case 1:
                editTextList.add(editText);
            case 2:
                editTextListFinaltags.add(editText);
            case 3:
                editTextListFinaldata.add(editText);
        }

        return editText;
    }
    private Spinner spinner(int _intID) {
        Spinner spinner = new Spinner(this);
        spinner.setId(_intID);

        Log.v(" ID ", String.valueOf(_intID));
        spinner.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        spinner.setLayoutParams(params);


        spinner.setAdapter(dataAdapter1);
        spinner.setOnItemSelectedListener(this);
        spinnerList.add(spinner);
        return spinner;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    _id=parent.getId();



        if(position!=0){

            String tag = parent.getItemAtPosition(position).toString();
            String field= editTextList.get(_id).getText().toString();
            taglist.add(tag);
            datalist.add(field);
            editTextList.get(_id).setVisibility(View.GONE);
            spinnerList.get(_id).setVisibility(View.GONE);

            dataAdapter1.remove((String) parent.getSelectedItem());
            dataAdapter1.notifyDataSetChanged();
            for(int i=0;i<spinnerList.size();i++){
                spinnerList.get(i).setAdapter(dataAdapter1);
            }
            int j=dataAdapter1.getCount();

            if(dataAdapter1.getCount()==1)
            {
                datasend();

            }

    }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void datasend(){

        tags.removeAllViews();
        data.removeAllViews();

        Intent intent=new Intent(extract2.this,senddata2.class);
        intent.putStringArrayListExtra("tags", taglist);
        intent.putStringArrayListExtra("data", datalist);
        startActivity(intent);


    }
}
