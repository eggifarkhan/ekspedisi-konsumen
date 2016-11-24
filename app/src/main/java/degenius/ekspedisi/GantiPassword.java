package degenius.ekspedisi;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

public class GantiPassword extends AppCompatActivity {

    private EditText editUser;
    private EditText editPassLama;
    private EditText editPassBaru;
    private EditText editKonf;
    private Button buttonGanti;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        setTitle("Ganti Password");

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        editUser = (EditText)findViewById(R.id.editUserP);
        editPassLama = (EditText)findViewById(R.id.editPLama);
        editPassBaru = (EditText)findViewById(R.id.editPBaru);
        editKonf = (EditText)findViewById(R.id.editKonf);
        buttonGanti = (Button)findViewById(R.id.buttonGanti);

        gantiPassword();
    }

    public void gantiPassword(){
        SharedPreferences sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        final String username = sp.getString("nama",null);
        final String pass = sp.getString("pass",null);
        editUser.setText(username);
        editUser.setEnabled(false);
        buttonGanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                if (editPassLama.getText().toString().equals(pass)){
                    if ((editPassBaru.getText().toString().equals(editKonf.getText().toString()))&&(!editPassBaru.getText().toString().equals(""))){
                        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.gantiPassword(username, editPassBaru.getText().toString()), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    pd.dismiss();
                                    int sukses = response.getInt("sukses");
                                    if (sukses==1){
                                        Toast.makeText(getApplicationContext(),"Password berhasil diubah",Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Gagal ubah password",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                                Toast.makeText(getBaseContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
                            }
                        });
                        jor.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        rq.add(jor);
                    }else{
                        pd.dismiss();
                        Toast.makeText(GantiPassword.this,"Password baru tidak cocok",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    pd.dismiss();
                    Toast.makeText(GantiPassword.this,"Password Lama Salah",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
