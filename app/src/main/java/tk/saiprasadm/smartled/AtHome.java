package tk.saiprasadm.smartled;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class AtHome extends AppCompatActivity {
    Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter mbtAdapter;
    private BluetoothSocket btSocket;
    private BluetoothDevice ledDevice;
    private OutputStream btoutputStream; //This is for output to write data
    private InputStream btinputStream;//This is for inputstream
    //Setting up standard serial port service id
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button getDevices, connect, sendData;
    private EditText data_text;
    private SeekBar rslider, bslider, gslider;
    private Spinner devicesListSpinner;
    private TextView redTextView, blueTextView, greenTextView;
    private double intensity;
    private Switch btSwitch;
    private Context context = AtHome.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_home);
        mbtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mbtAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth isn't Enabled", Toast.LENGTH_SHORT);
            finish();
        } else {
            initGUI();
            showBTDevices();
        }
    }

    private void initGUI() {
        getDevices = (Button) findViewById(R.id.showDevices_btn);
        connect = (Button) findViewById(R.id.setDeviceName);
        data_text = (EditText) findViewById(R.id.data_text);
        sendData = (Button) findViewById(R.id.sendData_btn);
        rslider = (SeekBar) findViewById(R.id.red_intensity);
        bslider = (SeekBar) findViewById(R.id.blue_intensity);
        gslider = (SeekBar) findViewById(R.id.green_intensity);
        redTextView = (TextView) findViewById(R.id.redColor_tv);
        blueTextView = (TextView) findViewById(R.id.blueColor_tv);
        greenTextView = (TextView) findViewById(R.id.greenColor_tv);
        devicesListSpinner = (Spinner) findViewById(R.id.devicesList_sp);
        btSwitch = (Switch) findViewById(R.id.switch_bt);
        btSwitch.setChecked(true);
        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "Turning Bluetooth OFF", Toast.LENGTH_SHORT).show();
                mbtAdapter.disable();
                finish();

            }


        });
        getDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBTDevices();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToDevice();
            }
        });
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDatafromField();
            }
        });
        rslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redTextView.setText("Red Color Intensity : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                redTextView.setText("Red Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");
                try {
                    String stIntensity = "r" + (intensity) + "\n";
                    btoutputStream.write((stIntensity).getBytes());
                } catch (IOException ioe) {

                    Toast.makeText(getApplicationContext(), "Exception Caught: Device not connected ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenTextView.setText("Green Color Intensity : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                greenTextView.setText("Green Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");
                try {
                    String stIntensity = "g" + (intensity) + "\n";
                    btoutputStream.write((stIntensity).getBytes());
                } catch (IOException ioe) {

                    Toast.makeText(getApplicationContext(), "Exception Caught: Device not connected ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueTextView.setText("Blue Color Intensity : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                blueTextView.setText("Blue Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");
                try {
                    String stIntensity = "b" + (intensity) + "\n";
                    btoutputStream.write((stIntensity).getBytes());
                } catch (IOException ioe) {

                    Toast.makeText(getApplicationContext(), "Exception Caught: Device not connected ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendDatafromField() {
        String data = data_text.getText().toString();
        if (data != null || data != "") {
            data += "\n";
            try {
                btoutputStream.write(data.getBytes());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Exception Caught: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Noting to send", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectToDevice() {
        try {
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals((String) devicesListSpinner.getSelectedItem().toString())) {
                        Log.i("Bluetooth", "spinner output : " + devicesListSpinner.getSelectedItem());
                        Toast.makeText(getApplicationContext(), devicesListSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        ledDevice = device;
                        break;
                    }
                }
            }
            if (ledDevice != null) {
                btSocket = ledDevice.createRfcommSocketToServiceRecord(uuid);
                btSocket.connect();
                btoutputStream = btSocket.getOutputStream();

            } else {
                Toast.makeText(getApplicationContext(), "Select the device first", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "No connectivity found with the LED", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBTDevices() {
        ArrayList<String> devicesArray = new ArrayList<String>();
        this.pairedDevices = mbtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesArray.add(device.getName());
            }

            //creating an array adapter
            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, devicesArray);
            //drop down layout style

            data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            devicesListSpinner.setAdapter(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent toActivity;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            toActivity = new Intent(AtHome.this, Settings.class);
            startActivity(toActivity);
            return true;
        } else if (id == R.id.item_about) {
            String aboutMessage = "This App is developed by Developers at KritiTech Innvoation Lab in Bhubaneswar.\n" +
                    "Hope you will enjoy using it as we enjoyed developing it";
            AlertDialog dialog = new AlertDialog.Builder(context).
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
        } else if (id == R.id.item_help) {
            String helpMessage = "In the Home Button Selected whichever place is applicable to you.\n" +
                    "Then turn on the Bluetooth for usage at home and Turn on the Internet Connection or Wifi for usage at workplace.\n" +
                    "Once corresponding devices are activated go on and play with the control sliders  to change the color of the light";
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Help").setMessage(helpMessage)
                    .setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


}
