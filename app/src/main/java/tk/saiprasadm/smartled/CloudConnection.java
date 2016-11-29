package tk.saiprasadm.smartled;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by SAI on 09-06-2016.
 */
public class CloudConnection {
    Context context;

    public void getData(Context context) {
        this.context = context;
        String url = "http://saiprasadm.tk/krititech/fetch.php?key=g212";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("Connection Response", s);
                getData_(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.print("Error Occured");
                Log.v("Connection Response", volleyError.toString());
                volleyError.printStackTrace();
            }
        }

        );
        Volley.newRequestQueue(context).add(stringRequest);


    }

    private void getData_(String s) {
        Toast.makeText(context, "Data Recieved \"" + s + "\"", Toast.LENGTH_SHORT).show();
    }
}
