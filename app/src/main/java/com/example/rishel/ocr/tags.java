package com.example.rishel.ocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishel on 1/18/16.
 */
public class tags extends Activity {

    protected EditText e;
    protected Integer i;
    protected Button b;
    LinearLayout settags;

    private List<EditText> editTextList = new ArrayList<EditText>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settags);
        e=(EditText)findViewById(R.id.editText);
        b=(Button)findViewById(R.id.submit);
        settags=(LinearLayout)findViewById(R.id.settags);
        b.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] string= new String[editTextList.size()];

                for(int i=0; i < editTextList.size(); i++){
                    string[i] = editTextList.get(i).getText().toString();
                    }

                    Bundle tagbundle = new Bundle();
                    tagbundle.putStringArray("tags",string);
                    Intent intent = new Intent(tags.this, OcrActivity.class);
                    intent.putExtras(tagbundle);
                    startActivity(intent);

            }
        });



        e.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(Editable mEdit) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String no = s.toString();

                try {
                    i = Integer.parseInt(no);
                    for (int j = 0; j <= i; j++) {
                        settags.addView(editText(j));
                    }
                    b.setVisibility(View.VISIBLE);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Enter valid number.", Toast.LENGTH_SHORT).show();

                    settags.removeAllViews();
                    b.setVisibility(View.INVISIBLE);

                }
            }
        });
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
