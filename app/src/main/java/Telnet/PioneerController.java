package Telnet;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import tk.saiprasadm.smartled.Settings;


public class PioneerController {
    protected static final String TAG = "PioneerController";
    private TelnetClient pioneerclient;
    volatile private int volume = -1;
    volatile private boolean power = false;
    private AtomicBoolean changingVolume = new AtomicBoolean(false);
    private Thread statusThread;
    private Settings context;
    private boolean dirtyvol = false;
    private int COMMAND_SPEED = 250;

    public PioneerController(String ip, int port, Settings con) throws IOException, InterruptedException {
        TelnetClient connection = null;
        try {
            connection = new TelnetClient(ip, port);
        } catch (IOException e) {
            Log.e(TAG, "Could not establish connection with server");
            e.printStackTrace();
        }
        pioneerclient = connection;

        context = con;
        final InputStreamReader a = pioneerclient.spawnSpy();
        final BufferedReader reader = new BufferedReader(a);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        final String line = reader.readLine();
                        if (line != null) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //  context.appendToConsole(line);   This function is aasociated with main activity
                                    Log.d("Telnet Connection ", line);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean pioneerIsOn() {
        return power;
    }


    //TODO implement a universal getStateOf(ENUM) function

    public boolean isConnected() {
        return pioneerclient.isConnected();
    }

    public boolean disconnect() {
        return pioneerclient.disconnect();
    }

    public void sendCommand(String s) {
        pioneerclient.sendCommand(s);
    }
}

