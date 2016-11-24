package degenius.ekspedisi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private EditText id;
    private EditText password;
    private Button login;

    public static final String pref = "preferences";
    SharedPreferences sp;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        String username = sp.getString("nama",null);
        if (username==null){
            pd = new ProgressDialog(this);
            pd.setMessage("Loading...");

            id = (EditText)findViewById(R.id.editId);
            password = (EditText)findViewById(R.id.editPassword);
            login = (Button)findViewById(R.id.buttonLogin);

            cekLogin();
        }else{
            Intent it = new Intent(MainActivity.this,HalamanUtama.class);
            startActivity(it);
            finish();
        }
    }

    public void cekLogin(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.loginAplikasi(id.getText().toString(), password.getText().toString()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sukses = response.getInt("sukses");
                            if (sukses==1){
                                JSONArray ja = response.getJSONArray("data");
                                JSONObject jo = ja.getJSONObject(0);
                                String namal = jo.getString("namal");
                                //nama = jo.getString("username");
                                //pass = jo.getString("pass");
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("nama", id.getText().toString());
                                editor.putString("pass", password.getText().toString());
                                editor.putString("namal", namal);
                                editor.commit();
                                pd.dismiss();

                                Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(MainActivity.this, HalamanUtama.class);
                                startActivity(it);
                                finish();
                            }else{
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"User atau Password Salah",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        });
    }
}
