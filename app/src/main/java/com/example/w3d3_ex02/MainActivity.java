package com.example.w3d3_ex02;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private static final String CODE_EXTRA = "com.example.w3d3_ex02.MainActivity.CODE_EXTRA";

    TextView resultTV;
    Button getCodeBTN;
    Button clearContentBTN;
    EditText urlET;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message = msg.getData().getString(CODE_EXTRA);
            setResultTextView(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTV = (TextView) findViewById(R.id.tv_result);
        getCodeBTN = (Button) findViewById(R.id.btn_get_result);
        clearContentBTN = (Button) findViewById(R.id.btn_get_result);
        urlET = (EditText) findViewById(R.id.et_url);

        setResultTextView(""); //executed in the main thread

/*
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            Log.d(TAG, "onCreate:");
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }*/


    }

    public void doWithHandlerThread(View view) {
        final String message = "FROM THE THREAD WITH HANDLER";
        Thread thread = new Thread(){
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                Bundle data = new Bundle();
                try {
                    URL url = new URL(urlET.getText().toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null){
                        result.append(line);
                    }
                    data.putString(CODE_EXTRA, result.toString());
                    msg.setData(data);
                    handler.sendMessage(msg);
                    Log.d(TAG, "onCreate:" + result);
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setResultTextView(String message){
        String result = String.format(getString(R.string.lbl_result), message);
        resultTV.setText(result);
    }

    public void clearContent(View view) {
        setResultTextView("");
    }
}
