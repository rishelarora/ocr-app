package com.example.rishel.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by rishel on 1/22/16.
 */
public class ChooseMethodActivity extends Activity {

    Button b1,b2,b3;//declaring buttons for different methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        b1=(Button)findViewById(R.id.button5);
        b2=(Button)findViewById(R.id.button6); //linking buttons to the ones in layout
        b3=(Button)findViewById(R.id.button7);

        //setting on click listeners for linking to their respective activities
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(ChooseMethodActivity.this, MainActivity.class);
                startActivity(myintent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(ChooseMethodActivity.this, OcrActivity.class).putExtra("check",true);
                startActivity(myintent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(ChooseMethodActivity.this, CropOCRActivity.class);
                startActivity(myintent);
            }
        });
    }


}
