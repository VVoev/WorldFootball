package me.worldfootball.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.worldfootball.utils.L;

public class Connection {
    private boolean mCanPost;
    private int mTimeout;
    private HashMap<String, String> map = null;

    protected Connection() {
    }

    public String request(String request) {
        if (request != null && !request.isEmpty()) return response(request);
        else return null;
    }

    private String response(String request) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(mCanPost ? "POST" : "GET");
            if (mTimeout > 0) connection.setConnectTimeout(mTimeout);
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> header : map.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String response = buffer.toString();
            L.i("{NetworkClient.response}(Success)request->" + request);
            L.i("{NetworkClient.response}(Success)response->" + response);
            return response;
        } catch (IOException e) {
            L.e("");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        L.e("{NetworkClient.response}(Error)request->" + request);
        return null;
    }

    protected void setPost(boolean canPost) {
        this.mCanPost = canPost;
    }

    protected void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    protected void putHeader(String key, String value) {
        if (map == null) map = new HashMap<>();
        if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) map.put(key, value);
    }

    public static class Builder {

        private Connection connection = new Connection();

        public Builder setPost(boolean canPost) {
            connection.setPost(canPost);
            return this;
        }

        public Builder setTimeout(int timeout) {
            connection.setTimeout(timeout);
            return this;
        }

        public Builder putHeader(String key, String value) {
            connection.putHeader(key, value);
            return this;
        }

        public Connection build() {
            return connection;
        }
    }
}
