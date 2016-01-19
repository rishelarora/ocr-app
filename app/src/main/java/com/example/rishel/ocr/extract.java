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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rishel on 1/13/16.
 */
public class extract extends Activity {
    String [] tagArray;
    LinearLayout tags,data;

    protected Button b1;
    String text;


    private List<TextView> TextViewList = new ArrayList<>();
    private List<EditText> editTextList = new ArrayList<EditText>();
    String mid="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        tagArray = getIntent().getExtras().getStringArray("tags");

        text = getIntent().getStringExtra("TEXT");
        b1 = (Button) findViewById(R.id.button4);
        tags=(LinearLayout)findViewById(R.id.tag);
        data=(LinearLayout)findViewById(R.id.data);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String forjson="{ \"Data\": {" +
//                                        "\""+TextViewList.get(0).getText().toString()+"\":\""+editTextList.get(0).getText().toString()+ "\"," +
//                                        "\""+key[1]+"\":\""+editTextList.get(1).getText().toString()+ "\"," +
//                                        "\""+key[2]+"\":\""+editTextList.get(2).getText().toString()+ "\"," +
//                                        "\""+key[3]+"\":\""+editTextList.get(3).getText().toString()+ "\"," +
//                                        "\""+key[4]+"\":\""+editTextList.get(4).getText().toString()+ "\"" +
//                                       "}" +
//                                     "}";
                for(int i=0;i<editTextList.size();i++) {

                    if (i == (editTextList.size() - 1)) {
                        mid = mid +"\""+TextViewList.get(i).getText().toString() + "\":\"" + editTextList.get(i).getText().toString() + "\"";
                    } else {
                       mid = mid +"\""+TextViewList.get(i).getText().toString() + "\":\"" + editTextList.get(i).getText().toString() + "\",";
                    }
                }


                String forjson="{ \"Data\": {" +

                        mid+
                        "}" +
                        "}";

                try {
                    JSONObject jsonObj = new JSONObject(forjson);
                    Toast.makeText(getApplicationContext(),jsonObj.toString(),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


            if (text != null) {

                for (int j = 0; j < tagArray.length-1; j++) {
                    tags.addView(TextView(j));
                    data.addView(editText(j));
                    matchset(tagArray[j],tagArray[j+1],TextViewList.get(j),j );
                }





            }
                }

    public void matchset(String tag1, String tag2, TextView t, int c){
        t.setText(tag1);
        Pattern pattern = Pattern.compile(Pattern.quote(tag1)
                + "(.*?)"
                + Pattern.quote(tag2));
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {

            if (matcher.group(1)!=null) {

                editTextList.get(c).setText(matcher.group(1));
                Log.v(tag1, matcher.group(1));

            }
        }
    }

    private TextView TextView(int _intID) {
        TextView tag = new TextView(this);
        tag.setId(_intID);
        tag.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        tag.setLayoutParams(params);
        TextViewList.add(tag);
        return tag;
    }

    private EditText editText(int _intID) {
        EditText editText = new EditText(this);
        editText.setId(_intID);
        editText.setHint("Tag " + _intID);
        editText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        editText.setLayoutParams(params);
        editTextList.add(editText);
        return editText;
    }

 }




