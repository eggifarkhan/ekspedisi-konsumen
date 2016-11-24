package degenius.ekspedisi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by HP on 11/12/2016.
 */

public class GridViewAdapter extends BaseAdapter {

    private ImageLoader il;
    private Context context;
    private ArrayList<String> foto;
    private ArrayList<String> nama;

    public GridViewAdapter(Context ct, ArrayList<String> ft, ArrayList<String> nm){
        this.context = ct;
        this.foto = ft;
        this.nama = nm;
    }

    @Override
    public int getCount() {
        return foto.size();
    }

    @Override
    public Object getItem(int i) {
        return foto.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        NetworkImageView niv = new NetworkImageView(context);

        il = CustomImageLoader.getInstance(context).getIl();
        il.get(foto.get(i),ImageLoader.getImageListener(niv, R.drawable.abu2, R.drawable.not_found));

        niv.setImageUrl(foto.get(i),il);

        TextView tv = new TextView(context);
        tv.setText(nama.get(i));

        niv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        niv.setLayoutParams(new GridView.LayoutParams(200,200));

        ll.addView(niv);
        ll.addView(tv);

        return ll;
    }
}
