package e1800958.vamk.assign12;



import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    EditText edDate, edTime, edParticipant;
    Button btnDate, btnTime,  btnSearch, btnBack;
    TextView tvParticipant, tvDate, tvTime;
    ArrayList<View> tv= new ArrayList<View>();
    String font_color;
    String bg_color;
    float fontSize;

    Boolean dbCreated=false;
    String dirName;
    String dbPath;
    String dbName;
    String tableName;

    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";

    DBAdapter dbAdapter=null;

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

        dirName=getString(R.string.dir_name);
        //The following line defines the directory path for the database inside Android/data/data/app_package/
        //on the device SD card. It is used to store any required files for the app.
        //Once the app is uninstalled, any data stored in this folder will be removed
        dbPath=getApplicationContext().getExternalFilesDir(dirName).getAbsolutePath();
        Boolean dbCreated=false;

        dbName=getString(R.string.db_name);
        tableName=getString(R.string.table_name);
        //dbAdapter = new DBAdapter(getApplicationContext());

        dbAdapter = new DBAdapter(getApplicationContext(), dbPath, dbName, tableName );
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
        SharedPreferences loadedSharedPrefs =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
        fontSize = loadedSharedPrefs.getFloat(FONT_SIZE_GUI, 14.0f);
        font_color= loadedSharedPrefs.getString(FONT_COLOR_GUI, getString(R.string.black));
        bg_color= loadedSharedPrefs.getString(BG_COLOR_GUI, getString(R.string.white));

        changeStyleFontColor();
        changeStyleFontSize();
        changeStyleBgColor();


    }

    public void Search(View v){
//        if(!dbCreated) {
//            copyDBFile();
//        }
        ArrayList<Meeting> res= new ArrayList<Meeting>();
        String par="";
        String date="";
        String time="";

        if(edParticipant.getText().length()!=0 || edDate.getText().length() !=0 ||
                edTime.getText().length() !=0) {
            if (edParticipant.getText().length() != 0) {

                par= edParticipant.getText().toString();
                //Log.d("cccc",String.valueOf(res.size()));
            }
            if (edDate.getText().length() != 0) {
                date=edDate.getText().toString();

            }
            if (edTime.getText().length() != 0) {
                time=edTime.getText().toString();
            }
            res= dbAdapter.getMeeting(par, date, time);
        }else{
            res= new ArrayList<Meeting>();
        }
        Intent intent = new Intent(getApplication(), SummaryActivity.class);
        intent.putExtra("MeetingsSearch", res);
        intent.putExtra("isSearch", true);
        startActivity(intent);

    }

    public void back(View v){
        finish();
    }

    public void selectDateTime(View v) {

        if (v == btnDate) {
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