package degenius.ekspedisi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TentangKami extends AppCompatActivity {

    private GridView gridFoto;
    private ArrayList<String> foto;
    private ArrayList<String> nama;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_kami);
        setTitle("Tentang Kami");

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        gridFoto = (GridView)findViewById(R.id.gridKurir);

        foto = new ArrayList<>();
        nama = new ArrayList<>();

        tampilGrid();
    }

    public void tampilGrid(){
        pd.show();
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.cobaGambar(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray ja = null;
                try {
                    ja = response.getJSONArray("data");
                    showGrid(ja);
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
        jor.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jor);
    }

    public void showGrid(JSONArray ja){
        for (int i=0;i<ja.length();i++){
            JSONObject jo = null;
            try {
                jo = ja.getJSONObject(i);
                foto.add(jo.getString("foto"));
                nama.add(jo.getString("nama"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        GridViewAdapter gva = new GridViewAdapter(this,foto,nama);
        //Toast.makeText(TentangKami.this,foto.get(0),Toast.LENGTH_SHORT).show();
        gridFoto.setAdapter(gva);
    }
}
