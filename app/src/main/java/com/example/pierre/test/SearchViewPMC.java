package com.example.pierre.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchViewPMC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_pmc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button_search = (Button) findViewById(R.id.button_getData);
        button_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                PMCSearch search = new PMCSearch(50, 50);
                search.getDataFromDB();
                TextView text_result = (TextView) findViewById(R.id.text_result);
                text_result.setText(search.toString());
            }
        });
    }

}
