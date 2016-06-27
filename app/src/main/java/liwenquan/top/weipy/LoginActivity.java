package liwenquan.top.weipy;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    //private static final String urlPath = "http://e.tju.edu.cn/Main/toModule.do?prefix=/Main&page=/logon.jsp";
    private EditText mcount;
    private EditText mPassWord;
    private Button mButton;
    private ImageView mImageView;
    private String[] count;
    GetPicThread thread;
    private String urlString = "http://e.tju.edu.cn/Kaptcha.jpg";

    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 == 0x112)
            {
                Bitmap bitmap = (Bitmap) msg.obj;
                mImageView.setImageBitmap(bitmap);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mcount= (EditText) findViewById(R.id.count);
        mPassWord= (EditText) findViewById(R.id.password);
        String mycount=mcount.getText().toString();
        String myPassword=mPassWord.getText().toString();
        count=new String[]{mycount,myPassword};
        mButton= (Button) findViewById(R.id.login_button);

        mImageView= (ImageView) findViewById(R.id.imageview_securitycode);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPicThread(handler,urlString).start();
            }
        });
        thread=new GetPicThread(handler,urlString);
        thread.start();
//        new Thread(){
//            public void run() {
//
//                Message message = handler.obtainMessage();
//                message.obj = getHttpBitmap(urlString);
//                message.arg1 = 0x112;
//                handler.sendMessage(message);
//            };
//
//        }.start();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String,Void,Void>() {

                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url=new URL(params[0]);
                            HttpURLConnection connection= (HttpURLConnection) url.openConnection();

                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");


                            OutputStreamWriter osw=new OutputStreamWriter(connection.getOutputStream(),"utf-8");
                            BufferedWriter bufferedWriter=new BufferedWriter(osw);
                            bufferedWriter.write("");
                            bufferedWriter.flush();


                            InputStream is=connection.getInputStream();
                            InputStreamReader isr=new InputStreamReader(is,"utf-8");
                            BufferedReader bufferedReader=new BufferedReader(isr);
                            String line;
                            while ((line=bufferedReader.readLine())!=null){
                                System.out.println(line);
                            }
                            bufferedReader.close();
                            isr.close();
                            is.close();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute("http://e.tju.edu.cn/toModule.do;jsessionid=Bss4qu1aujpooWiglSBCX0LEngCkfykI8PfMW9Naw0JYc3LFT8RM!-975914582?prefix=/Main&page=/logon.jsp");
                //Toast.makeText(LoginActivity.this,"欢迎登陆",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
