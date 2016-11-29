package tk.saiprasadm.smartled;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import Telnet.PioneerController;


public class Settings extends AppCompatActivity {

    public final Context context = Settings.this;
    //Debugging
    private final boolean debug = true;
    private final String espssid = "Lionia";
    private final String esppassword = "password";
    //private final IP telnetIP = new IP("192.168.2.117:333");
    private final String SERVER_IP = "192.168.2.117";
    private final int SERVER_PORT = 333;
    String message = new String();
    //Telnet Setup
    PioneerController client = null;
    // Create a new sleepThreadforConnecting inside your Actvity.
    //WIFI setup
    private WifiManager wifi;
    private WifiConfiguration wifiConfiguration;
    private EditText uid, ssid, wifipassword;

    //Message
    private String _ssid, _password, _uid;

    private Button setup, wifiConnect, wifiEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Reference Intialisation

        uid = (EditText) findViewById(R.id.settings_uid_et);
        ssid = (EditText) findViewById(R.id.settings_ssid_et);
        wifipassword = (EditText) findViewById(R.id.settings_password_et);

        setup = (Button) findViewById(R.id.settings_telnet_btn);
        wifiConnect = (Button) findViewById(R.id.settings_connectLED);
        wifiEnable = (Button) findViewById(R.id.settings_connect_wifi);

        setup.setText("Step-2\nSetup");
        wifiConnect.setText("Step-2\nConnect SmartLED");
        wifiEnable.setText("Step-1\nEnable Wifi");

        wifiConnect.setVisibility(View.INVISIBLE);
        setup.setVisibility(View.INVISIBLE);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiConfiguration = new WifiConfiguration();

        //TODO enable wifi and connect to the network
        //TODO connect to telnet
        //TODO send data

        wifiEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!EditTextIsEmpty(uid) && !EditTextIsEmpty(ssid) && !EditTextIsEmpty(wifipassword)) {

                    _password = wifipassword.getText().toString();
                    _ssid = ssid.getText().toString().trim();
                    _uid = uid.getText().toString().trim();
                    if (wifi.isWifiEnabled()) {
                        Log.e("Connection", "SSID : " + wifi.getConnectionInfo().getSSID());
                        if (wifi.getConnectionInfo().getSSID().equals("\"Lionia\"")) {
                            telnetConnection();
                        } else {
                            Toaster("Your Phone is not connected to SmartLED. Check if LED is turned on");
                        }
                    } else {
                        Toaster("Wifi isn't enabled!.. Enable the wifi and connect to LED Bulb in Settings");
                    }

                } else {
                    Toaster("Fields are empty.. Fill them first");
                }
            }
        });
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = _ssid + "&" + _password + "&" + _uid;
                sendCommand(message);
            }
        });

    }



    /*
        *Telnet Connection Functions
        * These functions are synchronised with UI and runs in other Thread
     */

    /**
     * This method sends message via a Telnet Connection created earlier
     *
     * @param message message to be sent via Telnet Connection
     */
    private void sendCommand(String message) {

        if (client == null || !client.isConnected()) {
            Toaster("Not connected to a server");
            return;
        }

        client.sendCommand(message);
        debugToaster("Done sending", debug);
        telnetDisconnect();


        finish();

        // wifi.disconnect();
        // wifi.setWifiEnabled(false);


    }

    @Override
    public void finish() {
        wifi.disconnect();
        //wifi.setWifiEnabled(false);
        super.finish();
    }

    private void debugToaster(String s, boolean b) {
        if (b) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Connects to Telnet Server . Here to the Smartled Bulb
     *
     * @return void
     */
    private void telnetConnection() {
        if (client != null && client.isConnected()) {
            Toaster("Already Connected");
        } else {
            new AsyncTask<Settings, Void, Void>() {

                @Override
                protected Void doInBackground(Settings... params) {
                    try {
                        client = new PioneerController(SERVER_IP, SERVER_PORT, params[0]);
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                        Toaster("Exception Caught..  Connection failed");
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toaster("Connected");
                    setup.setVisibility(View.VISIBLE);
                    wifiConnect.setVisibility(View.INVISIBLE);
                    wifiEnable.setVisibility(View.INVISIBLE);
                }
            }.execute(this);
        }
    }

    private void telnetDisconnect() {
        if (client != null && client.isConnected()) {
            if (disconnect()) {
                Toaster("Disconnected from the server");
            } else {
                Toaster("Already disconnected");
            }
        }
    }

    private boolean disconnect() {
        if (client.disconnect()) {
            return true;
        }
        return false;
    }

    /**
     * Toasts message to the user interface
     *
     * @param message
     */
    void Toaster(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Takes reference of an EditText Widget and checks whether the EditText is empty or not
     *
     * @param etText Reference of an EditText Widget Object
     * @return true if EditText is empty else false
     */
    private boolean EditTextIsEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    /**
     * Shows up the Alert Dialog box with successful message sending
     */
    private void showUpDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Message")
                .setMessage("Setup Complete")
                .create();
        dialog.show();
    }
    /*
        *
        *The Below portion is handling the Option Menu button click handling
        *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog dialog;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.item_about:
                String aboutMessage = "This App is developed by Developers at KritiTech Innvoation Lab in Bhubaneswar.\n" +
                        "Hope you will enjoy using it as we enjoyed developing it";
                dialog = new AlertDialog.Builder(context).
                        setCancelable(true).setTitle("About App").
                        setMessage(aboutMessage).
                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        create();
                dialog.show();
                return true;
            case R.id.item_help:
                String helpMessage = "In the Home Button Selected whichever place is applicable to you.\n" +
                        "Then turn on the Bluetooth for usage at home and Turn on the Internet Connection or Wifi for usage at workplace.\n" +
                        "Once corresponding devices are activated go on and play with the control sliders  to change the color of the light";
                dialog = new AlertDialog.Builder(context).setTitle("Help").setMessage(helpMessage)
                        .setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


//TODOS : -
//TODO Create the UerInterface [done]
//TODO Create the references [done]
//TODO Set onclick Listeners to the connect and send Buttons [done]
//TODO connect to wifi network with given ssid and password [done connected to esp]
//TODO encrypt data with 128bit encryption
//TODO open telnet connection and send encrypted data to LED BULB
//TODO close telnet connection
//TODO show confirmation dialog
//TODO Test the app
