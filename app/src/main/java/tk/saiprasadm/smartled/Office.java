package tk.saiprasadm.smartled;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class Office extends AppCompatActivity {

    private final Context context = this;
    int redProgress, greenProgress, blueProgress;
    private Button send;

    private SeekBar red_intensity, blue_intensity, green_intensity;
    private EditText username;
    private TextView redTV, blueTV, greenTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        checkforInternet();
        initUI();


    }

    private void checkforInternet() {
        ConnectivityManager check = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check.getActiveNetworkInfo() != null) {
            Toast.makeText(getApplicationContext(), "Please turn on your Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private void initUI() {
        final Context con = getApplicationContext();
        red_intensity = (SeekBar) findViewById(R.id.red_intensity_wifi);
        send = (Button) findViewById(R.id.set_color_btn_wifi);
        username = (EditText) findViewById(R.id.username);
        blue_intensity = (SeekBar) findViewById(R.id.blue_intensity_wifi);
        green_intensity = (SeekBar) findViewById(R.id.green_intensity_wifi);
        redTV = (TextView) findViewById(R.id.redColor_tv_wifi);
        greenTV = (TextView) findViewById(R.id.greenColor_tv_wifi);
        blueTV = (TextView) findViewById(R.id.blueColor_tv_wifi);



        red_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redTV.setText("Red Color Intensity : " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                redTV.setText("Red Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");
            }
        });
        blue_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueTV.setText("Blue Color Intensity : " + progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                blueTV.setText("Blue Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");

            }
        });
        green_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenTV.setText("Green Color Intensity" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                greenTV.setText("Green Color Intensity : " + seekBar.getProgress() + "%");
                double intensity = seekBar.getProgress() * 2.54;
                Integer sin = Integer.getInteger(intensity + "");

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double red_ = red_intensity.getProgress() * 2.54;
                double blue_ = blue_intensity.getProgress() * 2.54;
                double green_ = green_intensity.getProgress() * 2.54;
                int red = (int) red_;
                int blue = (int) blue_;
                int green = (int) green_;
                Log.v("Integer", red + "");
                String getData = "r" + String.format("%03d", red)
                        + "b" + String.format("%03d", blue)
                        + "g" + String.format("%03d", green);
                sendtoCloud(getData, con);
            }
        });

    }

    private void sendMessage(final String ip, String port, String message, final Context con) {
        String url = "http://" + ip + ":" + port + "/" + message;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Response ", response);
                Toast.makeText(con, "Data Sent to the IP Adress " + ip, Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Office.this)
                        .setCancelable(true)
                        .setTitle("Response from ESP8266")
                        .setMessage("Success: " + response)
                        .create();
                dialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v("Connection Response", volleyError.toString());
                volleyError.printStackTrace();
                Toast.makeText(con, volleyError.toString(), Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Office.this)
                        .setCancelable(true)
                        .setTitle("Response from Android")
                        .setMessage("Bhai check your IP and  Port")
                        .create();
                dialog.show();
            }
        });
        Volley.newRequestQueue(con).add(stringRequest);

    }

    private void sendtoCloud(String getData, final Context con) {

        String url = "http://45.58.34.139/esp/fetch.php?key=" + getData + "&user=" + username.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.v("Connection Response", s);
                Toast.makeText(con, "Response from web : " + s, Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Office.this)
                        .setCancelable(true)
                        .setTitle("Response from web ")
                        .setMessage(s)
                        .create();
                dialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.print("Error Occured");
                Log.v("Connection Response", volleyError.toString());
                volleyError.printStackTrace();
                Toast.makeText(con, volleyError.toString(), Toast.LENGTH_SHORT).show();

            }
        }

        );
        Volley.newRequestQueue(getApplication()).add(stringRequest);


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
            toActivity = new Intent(Office.this, Settings.class);
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
