package e1800958.vamk.assign9;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    EditText edDate, edTime, edParticipant;
    Button btnDate, btnTime,  btnSearch, btnBack;
    TextView tvParticipant, tvDate, tvTime;
    ArrayList<View> tv= new ArrayList<View>();
    ArrayList<Meeting> meetings= new ArrayList<Meeting>();
    String font_color;
    String bg_color;
    float fontSize;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        meetings= (ArrayList<Meeting>) getIntent().getSerializableExtra("Meetings");

        btnDate= findViewById(R.id.button_date);
        tv.add(btnDate);
        btnTime= findViewById(R.id.button_time);
        tv.add(btnTime);
        btnSearch= findViewById(R.id.button_search);
        tv.add(btnSearch);
        btnBack= findViewById(R.id.button_back);
        tv.add(btnBack);

        tvParticipant= findViewById(R.id.tv_participant);
        tv.add(tvParticipant);
        tvDate= findViewById(R.id.tv_date);
        tv.add(tvDate);
        tvTime= findViewById(R.id.tv_time);
        tv.add(tvTime);


        edParticipant= findViewById(R.id.ed_participant);
        tv.add(edParticipant);
        edDate= findViewById(R.id.ed_date);
        tv.add(edDate);
        edTime= findViewById(R.id.ed_time);
        tv.add(edTime);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            fontSize= extras.getFloat("font_size");
            font_color= extras.getString("font_color");
            bg_color= extras.getString("bg_color");
        }

//        SharedPreferences loadedSharedPrefs =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
//        fontSize = loadedSharedPrefs.getFloat(FONT_SIZE_GUI, 14.0f);
//        font_color= loadedSharedPrefs.getString(FONT_COLOR_GUI, getString(R.string.black));
//        bg_color= loadedSharedPrefs.getString(BG_COLOR_GUI, getString(R.string.white));

        changeStyleFontColor();
        changeStyleFontSize();
        changeStyleBgColor();

    }

    public void Search(View v){
        ArrayList<Meeting> res= meetings;
        if(edParticipant.getText().length()!=0 || edDate.getText().length() !=0 ||
                edTime.getText().length() !=0) {
            if (edParticipant.getText().length() != 0) {
                res = Meeting.SearchByParticipant(res, edParticipant.getText().toString());
            }
            if (edDate.getText().length() != 0) {
                res = Meeting.SearchByDate(res, edDate.getText().toString());
            }
            if (edTime.getText().length() != 0) {
                res = Meeting.SearchByTime(res, edTime.getText().toString());
            }

        }else{
            res= new ArrayList<Meeting>();
        }
        Intent intent = new Intent(getApplication(), SummaryActivity.class);
        intent.putExtra("MeetingsSearch", res);
        intent.putExtra("font_size", fontSize);
        intent.putExtra("font_color", font_color);
        intent.putExtra("bg_color", bg_color);
        startActivity(intent);

    }

    public void back(View v){
        finish();
    }

    public void selectDateTime(View v) {

        if (v == btnDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTime) {

            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            edTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}