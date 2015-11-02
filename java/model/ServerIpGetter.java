package model;

import android.content.Context;

import com.example.pc.tabtest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by PC on 5/3/2015.
 */
// Util class
public class ServerIpGetter {

    private static ServerIpGetter ipGetter;
    private Context con;
    private String ip;

    private ServerIpGetter(Context c){
        this.con = c;
        ip = loadIp();
    }

    /*
    loads Server IP from file
     */
    private String loadIp() {

        String contents = "";
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = con.getAssets().open("serverIp");
            reader = new BufferedReader(new InputStreamReader(is));
            contents = reader.readLine(); //1 line on file

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        //System.out.println("IP="+contents);
        return contents;
    }

    public static ServerIpGetter getInstance(Context c){
        if(ipGetter == null)
            ipGetter = new ServerIpGetter(c);
        return ipGetter;
    }

    public String getIp() {
        return ip;
    }
}
