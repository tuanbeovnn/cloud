package e1800958.vamk.assign9;


import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
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
    Button btnDate, btnTime, btnSubmit, btnAdd, btnSummary, btnSearch, btnSave;
    TextView tvMain, tvTitle, tvPlace, tvParticipants, tvDate, tvTime;
    RadioGroup r1, r2, r3;
    boolean isUpdate=false;
    int index;
    final int summaryRequestCode=1;
    final int searchRequestCode=2;
    SeekBar seekBar1;

    SharedPreferences sharedPreferences;
    private  ArrayList<Meeting> meetings;
    private  ArrayList<Meeting> meetings1;
    private ArrayList<View> tv;
    private String data_internal;
    private String data_external;

    String dirName;

    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";

    String font_color;
    String bg_color;
    float fontSize;


    File destPathFile;
    boolean isInternal;


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


    private void saveData(boolean isInternal, ArrayList<Meeting> me) {
        try{
            FileOutputStream fos;
            if(isInternal)  fos = openFileOutput(data_internal, MODE_PRIVATE);
            else fos=new FileOutputStream(new File(destPathFile, data_external));
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(me);
            os.close();
            fos.close();
        }catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_not_found), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.io_excp), Toast.LENGTH_LONG).show();
        }

    }
    private ArrayList<Meeting> getData(boolean isInternal){
        ArrayList<Meeting> m= new ArrayList<Meeting>();
        try{
            FileInputStream fis;
            if(isInternal) fis = openFileInput(data_internal);
            else fis= new FileInputStream(new File(destPathFile, data_external));
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

        data_internal=getResources().getString(R.string.data);
        data_external=getResources().getString(R.string.data);
        dirName=getResources().getString(R.string.dir_name);
        destPathFile= getApplicationContext().getExternalFilesDir(dirName);
        if(!destPathFile.exists()) {
            //Here we create the directory on the SD card
            destPathFile.mkdirs();
        }


        meetings= getData(true);
        meetings1= getData(false);
        isInternal=true;

        r1= findViewById(R.id.radio_group_font_gui);
        r2= findViewById(R.id.radio_group_background);
        r3= findViewById(R.id.radio_group_storage);

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

        r3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group
                        .getCheckedRadioButtonId());
                isInternal=!isInternal;
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
        Button b= (Button) v;
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
            if(isUpdate){
                if(isInternal)  {
                    meetings= Meeting.Update(meetings, new Meeting(title, place, Arrays.asList(participants), date, time), index );
                    saveData(isInternal, meetings);
                }
                else {
                    meetings1= Meeting.Update(meetings1, new Meeting(title, place, Arrays.asList(participants), date, time), index );
                    saveData(isInternal, meetings1);
                }

            }
            else{
                int index= Meeting.Search(meetings, temp);
                if (index==-1) {
                    if(isInternal) {
                        meetings.add(new Meeting(title, place, Arrays.asList(participants), date, time));
                        saveData(isInternal, meetings);
                    }
                    else {
                        meetings1.add(new Meeting(title, place, Arrays.asList(participants), date, time));
                        saveData(isInternal, meetings1);
                    }


                }
                else displayToast(getString(R.string.error3));
            }
            edTitle.setText("");
            edPlace.setText("");
            edParticipant.setText("");
            edDate.setText("");
            edTime.setText("");

            if(isUpdate){
                btnSummary.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                btnSubmit.setText(R.string.btn_submit);
                isUpdate=false;
            }
            displayToast(getString(R.string.msg));

        }else{
            displayToast(getString(R.string.error1));
        }
    }


    public void getSummary(View v){
        Button b= (Button) v;

        Intent intent = new Intent(getApplication(), SummaryActivity.class);
        if(isInternal) intent.putExtra("Meetings", meetings);
        else intent.putExtra("Meetings", meetings1);
        intent.putExtra("isInternal", isInternal);
        intent.putExtra("font_size", fontSize);
        intent.putExtra("font_color", font_color);
        intent.putExtra("bg_color", bg_color);
        startActivityForResult(intent, summaryRequestCode);
    }

    public  void getSearch(View v){
        Button b= (Button) v;
        Intent intent = new Intent(getApplication(), SearchActivity.class);
        if(isInternal) intent.putExtra("Meetings", meetings);
        else intent.putExtra("Meetings", meetings1);
        intent.putExtra("font_size", fontSize);
        intent.putExtra("font_color", font_color);
        intent.putExtra("bg_color", bg_color);
        startActivityForResult(intent, searchRequestCode);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (requestCode == summaryRequestCode){
            if (resultCode ==RESULT_OK){
                Meeting m= (Meeting) dataIntent.getSerializableExtra("Meeting");
                isInternal= dataIntent.getBooleanExtra("isInternal", true);
                btnSummary.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.INVISIBLE);
                btnSubmit.setText(R.string.btn_update);


                edTitle.setText(m.getTitle());
                edPlace.setText(m.getPlace());
                edParticipant.setText(m.getParticipantText());
                edDate.setText(m.getDate());
                edTime.setText(m.getTime());

                if(isInternal) index= Meeting.Search(meetings, m);
                else index= Meeting.Search(meetings1, m);
                isUpdate=true;
            }
        }
    }

}