package tk.saiprasadm.smartled;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    final Context context = Home.this;
    private Button bluetooth_con, wifi_con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        bluetooth_con = (Button) findViewById(R.id.bluetooth_btn);
        wifi_con = (Button) findViewById(R.id.wifi_btn);
        //Action performed on clicking bluetooth Con
        bluetooth_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToBTActivity();
            }
        });
        wifi_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOffice();
            }
        });
    }

    private void toOffice() {
        Intent getToOffice = new Intent(this, Office.class);
        startActivity(getToOffice);
    }

    private void getToBTActivity() {
        Intent btIntent;
        int REQUEST_ENABLE_BT = 1;
        BluetoothAdapter mbtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mbtAdapter.isEnabled()) {
            Intent enablebt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enablebt, REQUEST_ENABLE_BT);
            if (mbtAdapter.isEnabled()) {
                btIntent = new Intent(this, AtHome.class);
                startActivity(btIntent);
            }
        } else {
            Toast.makeText(getApplication(), "Getting you to your home control", Toast.LENGTH_SHORT).show();
            btIntent = new Intent(this, AtHome.class);
            startActivity(btIntent);
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
            toActivity = new Intent(Home.this, Settings.class);
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
