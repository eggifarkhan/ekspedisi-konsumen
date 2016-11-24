package degenius.ekspedisi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class PesanBaru extends AppCompatActivity {

    private Spinner barang;
    private EditText jumlah;
    private EditText tujuan;
    private Button submit;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_baru);
        setTitle("Pesan Baru");

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        barang = (Spinner)findViewById(R.id.spinner);
        jumlah = (EditText)findViewById(R.id.editJumlah);
        tujuan = (EditText)findViewById(R.id.editTujuan);
        submit = (Button)findViewById(R.id.buttonPesan);

        tampilSpinner();
        klikOk();
    }

    public void tampilSpinner(){
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.selectPaket(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int sukses = response.getInt("sukses");
                    if (sukses==1){
                        JSONArray ja = response.getJSONArray("data");
                        String[] item = new String[ja.length()];
                        for (int i=0;i<ja.length();i++){
                            JSONObject jo = ja.getJSONObject(i);
                            item[i] = jo.getString("barang");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item,item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        barang.setAdapter(adapter);
                        barang.setSelection(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
            }
        });
        jor.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jor);
    }

    public void klikOk(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((tujuan.getText().toString().equals(""))||jumlah.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"Tujuan dan Jumlah tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    String pesanBarang = barang.getSelectedItem().toString();
                    String pesanJumlah = jumlah.getText().toString();
                    String pesanTujuan = tujuan.getText().toString();
                    SharedPreferences sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
                    String username = sp.getString("nama",null);

                    RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.insertPesan(pesanBarang, pesanJumlah, pesanTujuan, username), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int sukses = response.getInt("sukses");
                                if (sukses==1){
                                    Toast.makeText(getApplicationContext(),"Berhasil Pesan",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Gagal Pesan",Toast.LENGTH_SHORT).show();
                                }
                                jumlah.setText("");
                                tujuan.setText("");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
                        }
                    });
                    jor.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    rq.add(jor);
                }
            }
        });

    }
}
