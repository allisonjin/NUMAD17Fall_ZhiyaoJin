package edu.neu.madcourse.zhiyaojin.game;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.neu.madcourse.zhiyaojin.Constants;

public class SendMessageAsyncTask extends AsyncTask<Void, Void, Void> {

    private final String toPath;
    private final String data;
    private final String notificationTitle;
    private final String notificationBody;

    public SendMessageAsyncTask(String toPath, String data,
                                String notificationTitle, String notificationBody) {
        this.toPath = toPath;
        this.data = data;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
    }

    @Override
    public Void doInBackground(Void... params) {
        try {
            JSONObject jFcmData = new JSONObject();
            JSONObject jNotification = new JSONObject();
            jNotification.put("title", notificationTitle);
            jNotification.put("body", notificationBody);
            jFcmData.put("to", "/topics/" + toPath);
            // What to send in GCM message.
            jFcmData.put("notification", jNotification);
            if (data != null) {
                JSONObject jData = new JSONObject();
                jData.put("data", data);
                jFcmData.put("data", jData);
            }

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + Constants.API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jFcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            Log.i("resp", IOUtils.toString(inputStream));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
