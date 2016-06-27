package liwenquan.top.weipy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LWQ on 2016/6/10.
 */
public class GetPicThread extends Thread {
    Handler handler;
    private String urlString;
    GetPicThread(Handler handler,String url){
        this.handler=handler;
        this.urlString=url;
    }
    public void run() {

        Message message = handler.obtainMessage();
        message.obj = getHttpBitmap(urlString);
        message.arg1 = 0x112;
        handler.sendMessage(message);
    };
    //获取网络图片
    private Bitmap getHttpBitmap(String urlString)
    {
        URL url;
        Bitmap bitmap = null;

        try
        {
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setDoInput(true);
            connection.setUseCaches(true);

            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;


    }
}
