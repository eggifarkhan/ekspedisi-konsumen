package degenius.ekspedisi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class HistoriTransaksi extends AppCompatActivity {

    private ListView lv;
    private LihatStatus ls = new LihatStatus();
    private TextView tBarang;
    private TextView tTgl;
    private TextView tHarga;
    private TextView tTujuan;
    private TextView tJumlah;
    private TextView tKurir;
    private TextView tBm;
    private TextView tStat;
    private TextView tJam;
    private Button batal;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori_transaksi);
        setTitle("Histori Transaksi");

        lv = (ListView)findViewById(R.id.listHistory);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        tampilHistory();
    }

    public void cDialog(){
        Dialog cd = new Dialog(HistoriTransaksi.this);
        cd.setContentView(R.layout.custom_dialog);
        cd.setCancelable(true);
        cd.setTitle("Detail Status");
        //cd.getWindow().setLayout(700,700);
        tBarang = (TextView)cd.findViewById(R.id.txtBar);
        tTgl = (TextView)cd.findViewById(R.id.txtTgl);
        tHarga = (TextView)cd.findViewById(R.id.txtHg);
        tKurir = (TextView)cd.findViewById(R.id.txtKur);
        tBm = (TextView)cd.findViewById(R.id.txtBm);
        tStat = (TextView)cd.findViewById(R.id.txtStat);
        tTujuan = (TextView)cd.findViewById(R.id.txtTujuan);
        tJumlah = (TextView)cd.findViewById(R.id.txtJumlah);
        tJam = (TextView)cd.findViewById(R.id.txtJam);
        batal = (Button)cd.findViewById(R.id.buttonBatal);
        batal.setVisibility(View.INVISIBLE);

        cd.show();
    }

    public void tampilHistory(){
        SharedPreferences sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        String iduser = sp.getString("nama",null);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.selectHistory(iduser), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pd.dismiss();
                    JSONArray ja = response.getJSONArray("data");
                    final String[] arrBarang = new String[ja.length()];
                    final String[] arrStatus = new String[ja.length()];
                    final String[] arrTanggal = new String [ja.length()];
                    final String[] arrHarga = new String[ja.length()];
                    final String[] arrKurir = new String[ja.length()];
                    final String[] arrBm = new String[ja.length()];
                    final String[] arrTujuan = new String[ja.length()];
                    final String[] arrJumlah = new String[ja.length()];
                    final String[] arrJam = new String[ja.length()];
                    //Integer arrGambar=R.drawable.paket;
                    for (int j=0;j<ja.length();j++){
                        JSONObject jo = ja.getJSONObject(j);
                        arrBarang[j] = jo.getString("barang");
                        arrTanggal[j] = jo.getString("tgl");
                        arrStatus[j] = jo.getString("status");
                        arrHarga[j] = jo.getString("harga");
                        arrKurir[j] = jo.getString("kurir");
                        arrBm[j] = jo.getString("nopol");
                        arrTujuan[j] = jo.getString("tujuan");
                        arrJumlah[j] = jo.getString("jumlah");
                        arrJam[j] = jo.getString("jam");
                    }
                    CustomList cl = new CustomList(HistoriTransaksi.this,arrBarang,arrTanggal,arrStatus,arrTujuan);
                    //ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_expandable_list_item_1,arrBarang);
                    lv.setAdapter(cl);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            cDialog();
                            tBarang.setText(arrBarang[i]);
                            tTgl.setText(arrTanggal[i]);
                            tHarga.setText(arrHarga[i]);
                            tKurir.setText(arrKurir[i]);
                            tBm.setText(arrBm[i]);
                            tStat.setText(arrStatus[i]);
                            tTujuan.setText(arrTujuan[i]);
                            tJumlah.setText(arrJumlah[i]);
                            tJam.setText(arrJam[i]);
                            //Toast.makeText(getApplicationContext(),arrBarang[i],Toast.LENGTH_SHORT).show();
                        }
                    });
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
}
