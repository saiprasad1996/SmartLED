package Telnet;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Locale;

public class TelnetConnection {
    private final String SERVER_IP;
    private final int SERVERPORT;
    private org.apache.commons.net.telnet.TelnetClient client = null;

    public TelnetConnection(String ip, int port) throws IOException {
        SERVER_IP = ip;
        SERVERPORT = port;
        client = new org.apache.commons.net.telnet.TelnetClient();
    }

    public void connect() throws IOException {
        try {
            client.connect(SERVER_IP, SERVERPORT);
        } catch (SocketException ex) {
            throw new SocketException("Connection error...");
        }
    }

    public BufferedInputStream getReader() {
        return new BufferedInputStream(client.getInputStream());
    }

    public OutputStream getOutput() {
        return client.getOutputStream();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public boolean sendCommand(String cmd) {
        if (client == null || !client.isConnected()) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(cmd.toUpperCase(Locale.ENGLISH));
        stringBuilder.append("\n\r");

        byte[] cmdbyte = stringBuilder.toString().getBytes();

        OutputStream outstream = client.getOutputStream();
        Log.i("command", (new String(cmdbyte, 0, cmdbyte.length)));

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();
            return true;
        } catch (Exception e1) {
            Log.e("Error writing to output", e1.getMessage());
            return false;
        }
    }

    //exits telnet session and cleans up the telnet console
    public boolean disconnect() {
        try {
            client.disconnect();
        } catch (IOException e) {
            Log.e("Couldn't disconnect", e.getMessage());
            return false;
        }
        return true;
    }

    public org.apache.commons.net.telnet.TelnetClient getConnection() {
        return client;
    }
}

