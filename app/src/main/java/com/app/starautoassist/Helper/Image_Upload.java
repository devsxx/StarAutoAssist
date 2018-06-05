package com.app.starautoassist.Helper;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Image_Upload implements Runnable {
    URL connectURL;
    String responseString;
    String Title;
    String fileName;
    String Description;
    byte[] dataToServer;
    FileInputStream fileInputStream = null;

    public Image_Upload(String urlString, String file) {
        try {
            connectURL = new URL(urlString);
            fileName = file;
        } catch (Exception ex) {
            Log.i("HttpFileUpload", "URL Malformatted");
        }
    }

    public Boolean Send_Now(FileInputStream fStream) {
        fileInputStream = fStream;
        return Sending();
    }
    Boolean Sending() {
        System.out.println("file Name is :" + fileName);
        String iFileName = fileName;
        HttpURLConnection conn = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        DataInputStream inStream = null;
        String Json = "";
        StringBuilder builder = new StringBuilder();
        String boundary = "*****";
        String Tag = "fSnd";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        try {
            Log.e(Tag, "Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            Log.e("MediaPlayer", "Headers are written");
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            Log.v("buffer", "buffer" + buffer);

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                Log.v("bytesRead", "bytesRead" + bytesRead);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            Log.v("in", "" + in);
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            Log.e("MediaPlayer", "File is written");
            fileInputStream.close();
            Json = builder.toString();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
        }
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;
            while ((str = inStream.readLine()) != null) {
                Log.e("MediaPlayer", "Server Response" + str);
            }
            inStream.close();
        } catch (IOException ioex) {
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }
        return false;
    }

    @Override
    public void run() {

    }
}

