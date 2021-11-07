package e1800958.vamk.assign10;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    private  boolean isSearch;
    DBAdapter dbAdapter=null;
    String font_color;
    String bg_color;
    float fontSize;
    Button btnBack;
    private ArrayList<View> tv;

    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";



    private void changeStyleFontSize(){
        for (View v: tv) {
            if (v instanceof TextView){
                TextView t =(TextView) v;
                t.setTextSize(fontSize);
            }

        }
    }
    private void changeStyleFontColor(){
        for (View v: tv) {
            if (v instanceof TextView){
                TextView t =(TextView) v;
                if (font_color.equals(getString(R.string.red))){
                    t.setTextColor(Color.RED);
                }else if(font_color.equals(getString(R.string.yellow))){
                    t.setTextColor(Color.YELLOW);
                }else{
                    t.setTextColor(Color.BLACK);
                }
            }

        }
    }
    private  void changeStyleBgColor(){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root);
        if (bg_color.equals(getString(R.string.red))){
            relativeLayout.setBackgroundColor(Color.RED);
        }else if(bg_color.equals(getString(R.string.yellow))){
            relativeLayout.setBackgroundColor(Color.YELLOW);;
        }
        else{
            relativeLayout.setBackgroundColor(Color.WHITE);;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        ArrayList<Meeting> meetings;
        dbAdapter = new DBAdapter(getApplicationContext());
        if( getIntent().getSerializableExtra("MeetingsSearch") != null){
            meetings = (ArrayList<Meeting>) getIntent().getSerializableExtra("MeetingsSearch");
            isSearch=true;
        }else{
            meetings= dbAdapter.getAllMeetings();
            isSearch=false;
        }
        //ArrayList<Meeting> meetings= dbAdapter.getAllMeetings();
        SharedPreferences loadedSharedPrefs =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
        fontSize = loadedSharedPrefs.getFloat(FONT_SIZE_GUI, 14.0f);
        font_color= loadedSharedPrefs.getString(FONT_COLOR_GUI, getString(R.string.black));
        bg_color= loadedSharedPrefs.getString(BG_COLOR_GUI, getString(R.string.white));

        changeStyleBgColor();

        tv= new ArrayList<View>();

        btnBack= findViewById(R.id.button_back);
        tv.add(btnBack);



        displayTable(meetings);
    }
    private void displayTable(ArrayList<Meeting> meetings){
        TableLayout tll = findViewById(R.id.summary);

        int count = tll.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tll.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        tll.setStretchAllColumns(true);
        tll.bringToFront();

        TableRow tbrow0 = new TableRow(SummaryActivity.this);


        TextView tv1 = new TextView(SummaryActivity.this);
        tv1.setText(R.string.title);
        tv.add(tv1);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(SummaryActivity.this);
        tv2.setText(R.string.place);
        tv.add(tv2);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(SummaryActivity.this);
        tv3.setText(R.string.participants);
        tv.add(tv3);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(SummaryActivity.this);
        tv4.setText(R.string.date);
        tv.add(tv4);
        tbrow0.addView(tv4);


        TextView tv5 = new TextView(SummaryActivity.this);
        tv5.setText(R.string.time);
        tbrow0.addView(tv5);
        tv.add(tv5);
        tll.addView(tbrow0);


        for(final Meeting m: meetings){
            TableRow tbrow = new TableRow(SummaryActivity.this);


            TextView t1 = new TextView(SummaryActivity.this);
            t1.setText(m.getTitle());
            tv.add(t1);
            tbrow.addView(t1);

            TextView t2 = new TextView(SummaryActivity.this);
            t2.setText(m.getPlace());
            tv.add(t2);
            tbrow.addView(t2);

            TextView t3 = new TextView(SummaryActivity.this);
            t3.setText(m.getParticipantText());
            tv.add(t3);
            tbrow.addView(t3);

            TextView t4 = new TextView(SummaryActivity.this);
            t4.setText(m.getDate());
            tv.add(t4);
            tbrow.addView(t4);

            TextView t5 = new TextView(SummaryActivity.this);
            t5.setText(m.getTime());
            tv.add(t5);
            tbrow.addView(t5);

            Button btn= new Button(this);
            btn.setText(R.string.btn_update);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent();
                    intent.putExtra("Meeting", m);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            tv.add(btn);
            if (!isSearch) tbrow.addView(btn);


            Button btn1= new Button(this);
            btn1.setText(R.string.btn_delete);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean flag=dbAdapter.deleteMeeting(m.getTitle());
                    if(flag){
                        View row = (View) v.getParent();
                        // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
                        ViewGroup container = ((ViewGroup)row.getParent());
                        // delete the row and invalidate your view so it gets redrawn
                        container.removeView(row);
                        container.invalidate();
                    }

                }
            });
            tv.add(btn1);
            if (!isSearch) tbrow.addView(btn1);

            tll.addView(tbrow);
        }

        changeStyleFontColor();
        changeStyleFontSize();
    }

    public void back(View v){
        finish();
    }
}