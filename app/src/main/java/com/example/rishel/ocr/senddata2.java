package com.example.rishel.ocr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishel on 1/25/16.
 */
public class senddata2 extends Activity {
    private ArrayList<String> taglist = new ArrayList<String>();
    private ArrayList<String> datalist = new ArrayList<String>();
    private List<EditText> editTextListFinaltags = new ArrayList<EditText>();
    private List<EditText> editTextListFinaldata = new ArrayList<EditText>();
    protected Button b1;
    String text;
    JSONObject jsonObj;
    String forjson;
    String mid="";
    LinearLayout tags,data;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forsenddata);
        b1 = (Button) findViewById(R.id.button9);
        tags=(LinearLayout)findViewById(R.id.tag);
        data=(LinearLayout)findViewById(R.id.data);
        taglist=getIntent().getStringArrayListExtra("tags");
        datalist=getIntent().getStringArrayListExtra("data");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//

                try {
                    if(jsonObj!=null)
                    {
                        jsonObj=null;
                    }
                    jsonObj = new JSONObject(forjson);
                    Toast.makeText(getApplicationContext(), jsonObj.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        for(int i=0;i<taglist.size();i++)
        {
            Log.v("tags",taglist.get(i));
            Log.v("data",datalist.get(i));
            tags.addView(editText(i));

            editTextListFinaltags.get(i).setText(taglist.get(i).toString());

        }
        for(int i=0;i<taglist.size();i++)
        {

            data.addView(editText1(i));

            editTextListFinaldata.get(i).setText(datalist.get(i).toString());

        }
        for(int i=0;i<editTextListFinaldata.size();i++) {

            if (i == (editTextListFinaldata.size() - 1)) {
                mid = mid +"\""+editTextListFinaltags.get(i).getText().toString() + "\":\"" + editTextListFinaldata.get(i).getText().toString() + "\"";
            } else {
                mid = mid +"\""+editTextListFinaltags.get(i).getText().toString() + "\":\"" + editTextListFinaldata.get(i).getText().toString() + "\",";
            }
        }


        forjson="{ \"Data\": {" +

                mid+
                "}" +
                "}";
    }

    private EditText editText(int _intID) {
        EditText editText = new EditText(this);
        editText.setId(_intID);

        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        editText.setLayoutParams(params);
         editTextListFinaltags.add(editText);
                editText.setHint("Tag " + _intID);

        return editText;
    }
    private EditText editText1(int _intID) {
        EditText editText = new EditText(this);
        editText.setId(_intID+100);

        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        editText.setLayoutParams(params);
        editTextListFinaldata.add(editText);
        editText.setHint("Data " + _intID);

        return editText;
    }

}
