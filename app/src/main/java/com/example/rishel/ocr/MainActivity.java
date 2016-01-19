package com.example.rishel.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishel on 1/18/16.
 */
public class MainActivity extends Activity  implements AdapterView.OnItemSelectedListener {
    protected Spinner card;
    protected Spinner cardtype;
    protected Button b;
    protected TextView t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data);

        card = (Spinner) findViewById(R.id.spinner);
        cardtype = (Spinner) findViewById(R.id.spinner2);
        b=(Button) findViewById(R.id.button3);
        t=(TextView) findViewById(R.id.textView8);
        cardtype.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
        t.setVisibility(View.INVISIBLE);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this, tags.class);
                startActivity(myintent);
            }
        });
        card.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    cardtype.setVisibility(View.VISIBLE);
                    t.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cardtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    b.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> cards = new ArrayList<String>();
        cards.add(" ");
        cards.add("NID");
        cards.add("Driving Licence");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cards);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        card.setAdapter(dataAdapter);

        List<String> cardtypes = new ArrayList<String>();
        cardtypes.add(" ");
        cardtypes.add("NID 1");
        cardtypes.add("NID 2");
        cardtypes.add("DL 1");
        cardtypes.add("DL 2");



        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cardtypes);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardtype.setAdapter(dataAdapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
