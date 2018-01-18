package fr.m2i.webservicesdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrateur on 17/01/2018.
 */

public class MonThread extends Thread {

    private String adress;
    private String response = "";

    public String getAdress() {
        return adress;
    }

    public String getRes() {
        return response;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Override
    public void run() {
        // traitement à réaliser
        URL url = null;
        HttpURLConnection cnt = null;

        try {
            url = new URL(getAdress());
            cnt = (HttpURLConnection) url.openConnection();
            InputStream stream = cnt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";

            while ((line = reader.readLine()) != null) {
                response += line;
            }

            stream.close();
            reader.close();
        } catch (Exception ex) {
            response += "\nErreur : " + ex.getMessage();
        } finally {
            cnt.disconnect();
        }


    }


}
