package e1800958.vamk.assign11;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutActivity  extends AppCompatActivity {
    EditText ed_about;
    Button back;
    private final String lineSeparator=System.getProperty("line.separator");

    String font_color;
    String bg_color;
    float fontSize;

    private static final String FONT_SIZE_GUI="font_size";
    private static final String FONT_COLOR_GUI="font_color";
    private static final String BG_COLOR_GUI="bg_color";


    private void changeStyleFontSize(){
        ed_about.setTextSize(fontSize);
        back.setTextSize(fontSize);

    }
    private void changeStyleFontColor(){
        if (font_color.equals(getString(R.string.red))){
            ed_about.setTextColor(Color.RED);
            back.setTextColor(Color.RED);
        }else if(font_color.equals(getString(R.string.yellow))){
            ed_about.setTextColor(Color.YELLOW);
            back.setTextColor(Color.YELLOW);
        }else{
            ed_about.setTextColor(Color.BLACK);
            back.setTextColor(Color.BLACK);
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
        setContentView(R.layout.activity_about);
        ed_about= findViewById(R.id.et_comment);
        back= findViewById(R.id.button_back);
        InputStream inputStream = getResources().openRawResource(R.raw.about);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            StringBuilder fileContent = new StringBuilder();
            while((line=reader.readLine())!=null){
                fileContent.append(line).append(lineSeparator);
            }
            ed_about.setText(fileContent);
            inputStream.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SharedPreferences loadedSharedPrefs =getSharedPreferences(getString(R.string.setting1), MODE_PRIVATE);
        fontSize = loadedSharedPrefs.getFloat(FONT_SIZE_GUI, 14.0f);
        font_color= loadedSharedPrefs.getString(FONT_COLOR_GUI, getString(R.string.black));
        bg_color= loadedSharedPrefs.getString(BG_COLOR_GUI, getString(R.string.white));

        changeStyleFontColor();
        changeStyleFontSize();
        changeStyleBgColor();
    }
    public void back(View v){
        finish();
    }
}
