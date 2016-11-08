package com.vitin.mylaptop.project2_tipcalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;




public class MainActivity extends AppCompatActivity implements OnEditorActionListener, OnClickListener {
    //define data into widget
    private EditText billAmountEditText;
    private TextView tipTextView;
    private TextView totaltextView;
    private SeekBar percentSeekBar;
    private TextView percentTextView;
    private Button applyButton;


    //define sharedPreferences object
    private SharedPreferences savedValues;

    //define instance variables that needs to be save
    private float tipPercent = .15f;
    private String billAmountString = "";
    private Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get refence into widget
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totaltextView = (TextView) findViewById(R.id.totalTextView);
        percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        applyButton = (Button) findViewById(R.id.applyButton);

        //set listeners
        billAmountEditText.setOnEditorActionListener(this);
        applyButton.setOnClickListener(this);

        percentSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 15;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                percentTextView.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                progress = seekBar.getProgress();
                tipPercent = (float) progress / 100;
                calculateAndDisplay(progress, tipPercent);

            }
        });

        //sharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }


    @Override
    protected void onPause() {
        //save instance variables
        Editor edtitor = savedValues.edit();
        edtitor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        //set bill amount on widgets
        billAmountEditText.setText(billAmountString);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void calculateAndDisplay(int progress, float tipPercent) {

        // get the bill amount
        String billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if (billAmountString.equals("")) {
            billAmount = 0;
        } else {
            billAmount = Float.parseFloat(billAmountString);
        }
// get tip percent

        tipPercent = (float) progress / 100;

        //calculate tip and total
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;


// display the other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totaltextView.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));

    }

    @Override
    public void onClick(View v) {


        }



    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
}



