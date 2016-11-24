package degenius.ekspedisi;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by HP on 11/4/2016.
 */

public class CustomList extends ArrayAdapter<String> {
    private String[] judul;
    private String[] tanggal;
    private String[] status;
    private String[] tujuan;
    private Activity context;

    public CustomList(Activity context, String[] judul, String[] tanggal, String[] status, String[] tujuan){
        super(context, R.layout.activity_lihat_status, judul);
        this.context = context;
        this.judul = judul;
        this.tanggal = tanggal;
        this.status = status;
        this.tujuan = tujuan;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View lvi = inflater.inflate(R.layout.custom_list,null,true);
        TextView txtJudul = (TextView)lvi.findViewById(R.id.textJudul);
        TextView txtTgl = (TextView)lvi.findViewById(R.id.textTanggal);
        TextView txtStatus = (TextView)lvi.findViewById(R.id.textStatus);
        TextView txtTujuan = (TextView)lvi.findViewById(R.id.textTujuan);
        //ImageView gbr = (ImageView)lvi.findViewById(R.id.imageList);

        txtJudul.setText(judul[position]);
        txtTgl.setText(tanggal[position]);
        txtStatus.setText(status[position]);
        txtTujuan.setText(tujuan[position]);
        //gbr.setImageResource(gambar);

        return lvi;
    }
}
