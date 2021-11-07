package e1800958.vamk.assign12;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    EditText edDate, edTime, edTitle, edPlace, edParticipant;
    Button btnDate, btnTime, btnSubmit, btnAdd, btnSummary, btnSearch, btnSave, btnAbout, btnUpload;
    TextView tvMain, tvTitle, tvPlace, tvParticipants, tvDate, tvTime;
    RadioGroup r1, r2;
    ImageView selectedImageView;
    boolean isUpdate=false;
    final int summaryRequestCode=1;
    final int searchRequestCode=2;
    final int aboutRequestCode=3;
    final int galleryRequestCode=4;
    SeekBar seekBar1;

    SharedPreferences sharedPreferences;
    private  ArrayList<Meeting> meetings;
    private ArrayList<View> tv;



    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";

    String font_color;
    String bg_color;
    float fontSize;

    DBAdapter dbAdapter=null;
    Meeting m_update;

    Boolean dbCreated=false;
    String dirName;
    String dbPath;
    String dbName;
    String tableName;

    Bitmap imageBitmap;
    BackgroundTask backgroundTask=null;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dirName=getString(R.string.dir_name);
        //The following line defines the directory path for the database inside Android/data/data/app_package/
        //on the device SD card. It is used to store any required files for the app.
        //Once the app is uninstalled, any data stored in this folder will be removed
        dbPath=getApplicationContext().getExternalFilesDir(dirName).getAbsolutePath();


        dbName=getString(R.string.db_name);
        tableName=getString(R.string.table_name);

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
        btnUpload= findViewById(R.id.button_img);
        tv.add(btnUpload);

        selectedImageView = findViewById(R.id.image_view);


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


        copyDBFile();

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

    private void copyDBFile() {
        File dbAbsolutePathFile = new File(dbPath + dbName);
        File dbPathFile = new File(dbPath);
        //Here we make sure that the dorectory path for the database exists
        if(!dbPathFile.exists()) {
            dbPathFile.mkdirs();
        }
        File dbNameFile = new File(dbName);
        //Here we check whether the database file exists or not. If not, we then create it
        if(!dbNameFile.exists()) {
            try {
                //Here we call the copyDB() method.
                copyDB(getApplicationContext().getAssets().open(dbName), new FileOutputStream(dbPath + dbName));
            } catch (FileNotFoundException e) {
                displayToast("FileNotFound: " + e.getLocalizedMessage());
            } catch (IOException e) {
                displayToast("IOException: " + e.getLocalizedMessage());
            }
        }
        displayToast(getString(R.string.file_exists) + " " + dbAbsolutePathFile.exists() + "\n" + dbAbsolutePathFile.getAbsolutePath());
        dbAdapter = new DBAdapter(getApplicationContext(), dbPath, dbName, tableName );
        //Here we indicate that the database file has been copied successfully.
        dbCreated=true;

    }
    private void copyDB(InputStream inputStream, OutputStream outputStream) {
        byte[] buffer = new byte[1024];
        int length;
        //Here we copy 1K bytes at a time
        try {
            while((length=inputStream.read(buffer))>0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            displayToast("IOException: " + e.getLocalizedMessage());
        }
    }


    public void Submit(View v){
        if(!dbCreated) {
            copyDBFile();
        }
        boolean check = edTitle.getText().length()!=0 && edPlace.getText().length()!=0
                && edDate.getText().length()!=0 && edTime.getText().length()!=0 &&
                edParticipant.getText().length() != 0;
        if (check){
            String title=edTitle.getText().toString();
            String place= edPlace.getText().toString();
            String date= edDate.getText().toString();
            String time= edTime.getText().toString();
            String[] participants= edParticipant.getText().toString().split("\n");
            imageBitmap = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            boolean compressResult = imageBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            byte[] img=outputStream.toByteArray();;
            Meeting temp= new Meeting(title, place, Arrays.asList(participants), img,date, time);
            boolean flag;

            if(isUpdate){
                flag= dbAdapter.updateMeeting(temp);

                //Log.d("cccc", String.valueOf(flag));
            }
            else{

                flag= dbAdapter.addMeeting(temp);
            }
            edTitle.setText("");
            edPlace.setText("");
            edParticipant.setText("");
            edDate.setText("");
            edTime.setText("");
            selectedImageView.setImageBitmap(null);

            if(isUpdate){
                btnSummary.setText(R.string.btn_summary);
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
        if(isUpdate){
            String title=edTitle.getText().toString();
            dbAdapter.deleteMeeting(title);
            edTitle.setText("");
            edPlace.setText("");
            edParticipant.setText("");
            edDate.setText("");
            edTime.setText("");
            selectedImageView.setImageBitmap(null);
            btnSummary.setText(R.string.btn_summary);
            btnSearch.setVisibility(View.VISIBLE);
            btnAbout.setVisibility(View.VISIBLE);
            edTitle.setEnabled(true);
            btnSubmit.setText(R.string.btn_submit);
            isUpdate=false;
        }else{
            ArrayList<Meeting> meetings= dbAdapter.getAllMeetings();
            Intent intent = new Intent(getApplication(), SummaryActivity.class);
            intent.putExtra("Meetings", meetings);
            startActivityForResult(intent, summaryRequestCode);
        }

    }

    public  void getSearch(View v){
        Intent intent = new Intent(getApplication(), SearchActivity.class);
        startActivityForResult(intent, searchRequestCode);
    }

    public  void getAbout(View v){
        Intent intent = new Intent(getApplication(), AboutActivity.class);
        startActivityForResult(intent, aboutRequestCode);
    }

    public void uploadImg(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Here we invoke the gallery application to pick an image
        startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.select_image_title)), galleryRequestCode);
    }


    private Bitmap decodeFile(Uri selectedImageUri) throws IOException{
        //Here we open an input stream to access the content of the image
        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
        //Decode image size
        BitmapFactory.Options imageSizeOptions = new BitmapFactory.Options();
        //If set to true, the decoder will return null (no bitmap), but
        //the out... fields will still be set, allowing the caller to query
        //the bitmap without having to allocate the memory for its pixels.
        imageSizeOptions.inJustDecodeBounds = true;
        //Here we fetch image meta data
        BitmapFactory.decodeStream(inputStream, null, imageSizeOptions);
        //The new size we want to scale to
        final int REQUIRED_SIZE=150;
        //Here we find the correct scale value. It should be a power of 2.
        int scale=1;
        //Here we scale the result height and width of the image based on the required size
        while(imageSizeOptions.outWidth/scale/2>=REQUIRED_SIZE && imageSizeOptions.outHeight/scale/2>=REQUIRED_SIZE)
            scale*=2;
        //Decode with inSampleSize
        BitmapFactory.Options inSampleSizeOption = new BitmapFactory.Options();
        //Here we do the actual decoding. If set to a value > 1, requests the decoder to subsample the
        //original image, returning a smaller image to save memory. The
        //sample size is the number of pixels in either dimension that correspond
        //to a single pixel in the decoded bitmap.
        inSampleSizeOption.inSampleSize=scale;
        inputStream.close();
        //Here we initialize the inputStream again
        inputStream = getContentResolver().openInputStream(selectedImageUri);
        return BitmapFactory.decodeStream(inputStream, null, inSampleSizeOption);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (requestCode == summaryRequestCode){
            if (resultCode ==RESULT_OK){
                m_update= (Meeting) dataIntent.getSerializableExtra("Meeting");
                //btnSummary.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.INVISIBLE);
                btnAbout.setVisibility(View.INVISIBLE);
                btnSubmit.setText(R.string.btn_update);
                btnSummary.setText(R.string.btn_delete);

                edTitle.setText(m_update.getTitle());
                edTitle.setEnabled(false);
                edPlace.setText(m_update.getPlace());
                edParticipant.setText(m_update.getParticipantText());
                edDate.setText(m_update.getDate());
                edTime.setText(m_update.getTime());

                Bitmap bmp= BitmapFactory.decodeByteArray(m_update.getImage(),0,m_update.getImage().length);
                selectedImageView.setImageBitmap(bmp);

                //index= Meeting.Search(meetings, m);
                isUpdate=true;
            }
        }

        if (requestCode == galleryRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = dataIntent.getData();
                InputStream inputStream = null;
                Bitmap bitmap = null;
                try {
                    //We call the decodeFile() method to scale down the image
                    bitmap = decodeFile(selectedImageUri);
                    //Here we set the image of the ImageView to the selected image
                    if (bitmap != null) {
                        selectedImageView.setImageBitmap(bitmap);
                    } else {
                        displayToast(getString(R.string.null_bitmap));
                    }
                } catch (FileNotFoundException e) {
                    displayToast(e.getLocalizedMessage());
                } catch (IOException e) {
                    displayToast(e.getLocalizedMessage());
                } finally {
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            displayToast(e.getLocalizedMessage());
                        }
                }
            }
        }
    }

    private InputStream getInputStream(String urlString) throws IOException {
        //Here we declare an object of type InputStream
        InputStream inputStream = null;
        //Here we create an URL object
        URL url = new URL(urlString);
        //Here we make an URL connection
        URLConnection urlConnection = url.openConnection();
        try {
            //Here we create a URLConnection with support for HTTP-specific features
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            //Here we determine whether if required the system can ask the user
            //for additional input
            httpConnection.setAllowUserInteraction(false);
            // Sets whether HTTP redirects (requests with response code 3xx)
            //should be automatically followed by this HttpURLConnection instance.
            httpConnection.setInstanceFollowRedirects(true);
            //Set the method for the URL request
            httpConnection.setRequestMethod("GET");
            //Here we establish HttpURLConnection
            httpConnection.connect();
            //Here we make sure that the HttpURL connection has been successfully
            //established
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Here we get the input stream that reads from this open connection
                inputStream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }

    private  class BackgroundTask extends AsyncTask<String, Integer, Bitmap> {
        //This method receives an array of URLs as strings and returns
        //an object of type Bitmap. This method calls getInputStream() method
        //which makes HTTP connection to the given URL and returns the input stream
        @Override
        protected Bitmap doInBackground(String...urls) {
            Bitmap bitmap=null;
            try {
                //Here we create an InputStream object
                InputStream inputStream = getInputStream(urls[0]);
                //Here we create a bitmap out of the received input stream
                bitmap= BitmapFactory.decodeStream(inputStream);
                if(bitmap!=null)
                    //Here we update the progress of the background job
                    //by passing the number o fbytes in the bitmap
                    publishProgress(bitmap.getByteCount());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        //This method updates the progress
        @Override
        protected void onProgressUpdate(Integer...progress){
            //Here we display the size of the bitmap
            displayToast("Bitmap size: " + progress[0]);
        }
        //This method will be called when the background job has finished
        @Override
        protected void onPostExecute(Bitmap bitmap){
            //Here we se the image of the imageView object
            selectedImageView.setImageBitmap(bitmap);
        }
    }

    public void uploadImgFromInternet(View v){
        backgroundTask = new BackgroundTask();
        try {
            backgroundTask.execute("https://images.freecreatives.com/wp-content/uploads/2016/04/Wild-Spring-Flowers-Wallpaper.jpg").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}