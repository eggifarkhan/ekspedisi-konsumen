package degenius.ekspedisi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HalamanUtama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView iduser;
    private TextView logout;
    private ViewPager vp;
    private ArrayList<String> promo;
    private static int promoSekarang;
    private static int jum_promo;
    //ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HalamanUtama.this,PesanBaru.class);
                startActivity(it);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vp = (ViewPager)findViewById(R.id.viewPager);
        promo = new ArrayList<>();

        iduser = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textUsername);
        logout = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textLogout);
        logout.setText("Logout");

        //pd = new ProgressDialog(this);
        //pd.setMessage("Loading...");

        bacaSession();
        tampilPromo();
        logout();
    }

    boolean back2 = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (back2){
            finish();
        }else{
            this.back2 = true;
            Toast.makeText(HalamanUtama.this, "Tekan Back Sekali lagi untuk Keluar",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    back2 = false;
                }
            },2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.halaman_utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_pesan) {
            Intent it = new Intent(HalamanUtama.this, PesanBaru.class);
            startActivity(it);
        } else if (id == R.id.menu_status) {
            Intent it = new Intent(HalamanUtama.this, LihatStatus.class);
            startActivity(it);
        } else if (id == R.id.menu_history) {
            Intent it = new Intent(HalamanUtama.this, HistoriTransaksi.class);
            startActivity(it);
        } else if (id == R.id.menu_tentang) {
            Intent it = new Intent(HalamanUtama.this, TentangKami.class);
            startActivity(it);
        } else if (id == R.id.menu_pass) {
            Intent it = new Intent(HalamanUtama.this, GantiPassword.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void bacaSession(){
        SharedPreferences sp = getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        String username = sp.getString("namal",null);
        iduser.setText(username);
        //Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
    }

    public void logout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(MainActivity.pref,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent it = new Intent(HalamanUtama.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    public void tampilPromo(){
        //pd.show();
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, CRUD.cobaPromo(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //pd.dismiss();
                    JSONArray ja = response.getJSONArray("promo");
                    showVp(ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pd.dismiss();
                Toast.makeText(getBaseContext(),"Periksa Koneksi Internet Anda",Toast.LENGTH_SHORT).show();
            }
        });
        jor.setRetryPolicy(new DefaultRetryPolicy(7000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jor);
    }

    public void showVp(JSONArray jsa){
        for (int i=0;i<jsa.length();i++){
            JSONObject jo = null;
            try {
                jo = jsa.getJSONObject(i);
                promo.add(jo.getString("foto1"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        vp.setAdapter(new CustomViewPager(HalamanUtama.this,promo));
        //Toast.makeText(HalamanUtama.this,promo.get(2),Toast.LENGTH_SHORT).show();
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5*density);
        jum_promo = promo.size();

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (promoSekarang==jum_promo){
                    promoSekarang=0;
                }
                vp.setCurrentItem(promoSekarang++,true);
            }
        };
        Timer waktuSlide = new Timer();
        waktuSlide.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },3000,3000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                promoSekarang = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
