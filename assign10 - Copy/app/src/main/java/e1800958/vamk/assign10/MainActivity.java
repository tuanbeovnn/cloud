package e1800958.vamk.assign10;


import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText edDate, edTime, edTitle, edPlace, edParticipant;
    Button btnDate, btnTime, btnSubmit, btnAdd, btnSummary, btnSearch, btnSave, btnAbout;
    TextView tvMain, tvTitle, tvPlace, tvParticipants, tvDate, tvTime;
    RadioGroup r1, r2;
    boolean isUpdate=false;
    int index;
    final int summaryRequestCode=1;
    final int searchRequestCode=2;
    final int aboutRequestCode=2;
    SeekBar seekBar1;

    SharedPreferences sharedPreferences;
    private  ArrayList<Meeting> meetings;
    private ArrayList<View> tv;
    private String commentFileName;
    String path;

    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";

    String font_color;
    String bg_color;
    float fontSize;

    DBAdapter dbAdapter=null;
    Meeting m_update;


    private void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
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


    private void saveData() {
        try{
            FileOutputStream fos = openFileOutput(commentFileName, MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(meetings);
            os.close();
            fos.close();
        }catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_not_found), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.io_excp), Toast.LENGTH_LONG).show();
        }

    }
    private ArrayList<Meeting> getData(){
        ArrayList<Meeting> m= new ArrayList<Meeting>();
        try{
            FileInputStream fis = openFileInput(commentFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            m= (ArrayList<Meeting>) is.readObject();
            is.close();
            fis.close();
        }catch (FileNotFoundException e) {
            return m;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.io_excp), Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  m;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAdapter = new DBAdapter(getApplicationContext());

        commentFileName=getResources().getString(R.string.data);
        path = getFilesDir().getAbsolutePath() + "/";

        meetings= getData();

        r1= findViewById(R.id.radio_group_font_gui);
        r2= findViewById(R.id.radio_group_background);

        tv= new ArrayList<View>();

        tvMain= findViewById(R.id.main);
        tv.add(tvMain);
        tvTitle= findViewById(R.id.tv_title);
        tv.add(tvTitle);
        tvPlace= findViewById(R.id.tv_place);
        tv.add(tvPlace);
        tvParticipants= findViewById(R.id.tv_participant);
        tv.add(tvParticipants);
        tvDate= findViewById(R.id.tv_date);
        tv.add(tvDate);
        tvTime= findViewById(R.id.tv_time);
        tv.add(tvTime);

        btnDate= findViewById(R.id.button_date);
        tv.add(btnDate);
        btnTime= findViewById(R.id.button_time);
        tv.add(btnTime);
        btnAdd= findViewById(R.id.button_add);
        tv.add(btnAdd);
        btnSubmit= findViewById(R.id.button_submit);
        tv.add(btnSubmit);
        btnSummary= findViewById(R.id.button_summary);
        tv.add(btnSummary);
        btnSearch= findViewById(R.id.button_search);
        tv.add(btnSearch);
        btnAbout= findViewById(R.id.button_about);
        tv.add(btnAbout);


        edTitle= findViewById(R.id.ed_title);
        tv.add(edTitle);
        edPlace= findViewById(R.id.ed_place);
        tv.add(edPlace);
        edParticipant= findViewById(R.id.ed_participant);
        tv.add(edParticipant);
        edDate= findViewById(R.id.ed_date);
        tv.add(edDate);
        edTime= findViewById(R.id.ed_time);
        tv.add(edTime);


        seekBar1= findViewById(R.id.seekbar);

        btnSave= findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(FONT_SIZE_GUI, seekBar1.getProgress());
                editor.putString(FONT_COLOR_GUI, font_color);
                editor.putString(BG_COLOR_GUI, bg_color);
                editor.commit();
                Toast.makeText(getApplicationContext(), R.string.setting_msg, Toast.LENGTH_LONG).show();
            }
        });


        SharedPreferences loadedSharedPrefs =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
        fontSize = loadedSharedPrefs.getFloat(FONT_SIZE_GUI, 14.0f);
        font_color= loadedSharedPrefs.getString(FONT_COLOR_GUI, getString(R.string.black));
        bg_color= loadedSharedPrefs.getString(BG_COLOR_GUI, getString(R.string.white));
        seekBar1.setProgress((int) fontSize);
        changeStyleFontColor();
        changeStyleFontSize();
        changeStyleBgColor();


        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontSize=Float.parseFloat(""+progress);
                changeStyleFontSize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        r1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group
                        .getCheckedRadioButtonId());
                font_color= (String) radioButton.getText();
                changeStyleFontColor();
            }
        });


        r2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group
                        .getCheckedRadioButtonId());
                bg_color= (String) radioButton.getText();
                changeStyleBgColor();
            }
        });


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

    public void addMoreParticipant(View v){
        if (edParticipant.getText().length()!=0){
            edParticipant.append("\n");
        }
    }

    public void Submit(View v){
        boolean check = edTitle.getText().length()!=0 && edPlace.getText().length()!=0
                && edDate.getText().length()!=0 && edTime.getText().length()!=0 &&
                edParticipant.getText().length() != 0;
        if (check){
            String title=edTitle.getText().toString();
            String place= edPlace.getText().toString();
            String date= edDate.getText().toString();
            String time= edTime.getText().toString();
            String[] participants= edParticipant.getText().toString().split("\n");

            Meeting temp= new Meeting(title, place, Arrays.asList(participants), date, time);
            boolean flag;
            if(isUpdate){
                flag= dbAdapter.updateMeeting(temp);
                Log.d("cccc", place);
                Log.d("cccc", String.valueOf(flag));
            }
            else{

                flag= dbAdapter.addMeeting(temp);
            }
            edTitle.setText("");
            edPlace.setText("");
            edParticipant.setText("");
            edDate.setText("");
            edTime.setText("");

            if(isUpdate){
                btnSummary.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                btnAbout.setVisibility(View.VISIBLE);
                edTitle.setEnabled(true);
                btnSubmit.setText(R.string.btn_submit);
                isUpdate=false;
            }
            if (flag) displayToast(getString(R.string.msg));
            else displayToast(getString(R.string.error4));
        }else{
            displayToast(getString(R.string.error1));
        }
    }


    public void getSummary(View v){
        Intent intent = new Intent(getApplication(), SummaryActivity.class);
        startActivityForResult(intent, summaryRequestCode);
    }

    public  void getSearch(View v){
        Log.d("cccc", "yes");
        Intent intent = new Intent(getApplication(), SearchActivity.class);
        startActivityForResult(intent, searchRequestCode);
    }

    public  void getAbout(View v){
        Intent intent = new Intent(getApplication(), AboutActivity.class);
        startActivityForResult(intent, aboutRequestCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (requestCode == summaryRequestCode){
            if (resultCode ==RESULT_OK){
                m_update= (Meeting) dataIntent.getSerializableExtra("Meeting");
                btnSummary.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                btnSubmit.setText(R.string.btn_update);

                edTitle.setText(m_update.getTitle());
                edTitle.setEnabled(false);
                edPlace.setText(m_update.getPlace());
                edParticipant.setText(m_update.getParticipantText());
                edDate.setText(m_update.getDate());
                edTime.setText(m_update.getTime());

                //index= Meeting.Search(meetings, m);
                isUpdate=true;
            }
        }
    }

}