package degenius.ekspedisi;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class LihatStatus extends AppCompatActivity {

    private ListView lv;

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
        setContentView(R.layout.activity_lihat_status);
        setTitle("Lihat Status");

        lv = (ListView)findViewById(R.id.listStatus);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        tampilStatus();
    }

    public void cDialog(String status){
        Dialog cd = new Dialog(LihatStatus.this);
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

        if (!status.equals("MULAI")){
            batal.setVisibility(View.INVISIBLE);
        }

        cd.show();
    }

    public void tampilStatus(){
        SharedPreferences sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        String iduser = sp.getString("nama",null);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.selectStatus(iduser), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pd.dismiss();
                    JSONArray ja = response.getJSONArray("data");
                    final String[] arrNomor = new String[ja.length()];
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
                        arrNomor[j] = jo.getString("nomor");
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
                    CustomList cl = new CustomList(LihatStatus.this,arrBarang,arrTanggal,arrStatus,arrTujuan);
                    //ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_expandable_list_item_1,arrBarang);
                    lv.setAdapter(cl);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            cDialog(arrStatus[i]);
                            tBarang.setText(arrBarang[i]);
                            tTgl.setText(arrTanggal[i]);
                            tHarga.setText(arrHarga[i]);
                            tKurir.setText(arrKurir[i]);
                            tBm.setText(arrBm[i]);
                            tStat.setText(arrStatus[i]);
                            tTujuan.setText(arrTujuan[i]);
                            tJumlah.setText(arrJumlah[i]);
                            tJam.setText(arrJam[i]);
                            batal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pd.show();
                                    RequestQueue rq1 = Volley.newRequestQueue(getApplicationContext());
                                    JsonObjectRequest jor1 = new JsonObjectRequest(Request.Method.GET, CRUD.batalPesan(arrNomor[i]), new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            pd.dismiss();
                                            try {
                                                int sukses = response.getInt("sukses");
                                                if (sukses==1){
                                                    Toast.makeText(getApplicationContext(),"Pesanan Berhasil Dibatalkan",Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"Gagal Membatalkan Pesan",Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            //tampilStatus();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    jor1.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    rq1.add(jor1);
                                }
                            });
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
