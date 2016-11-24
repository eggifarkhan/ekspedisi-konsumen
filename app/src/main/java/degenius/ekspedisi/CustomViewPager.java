package degenius.ekspedisi;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by HP on 11/16/2016.
 */

public class CustomViewPager extends PagerAdapter {
    private ArrayList<String> gambar;
    //private LayoutInflater inflater;
    private Context ct;
    private ImageLoader il;

    public CustomViewPager(Context ct, ArrayList<String> gambar){
        this.ct = ct;
        this.gambar = gambar;
        //inflater = LayoutInflater.from(ct);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return gambar.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout layout = new RelativeLayout(ct);
        //View layout = inflater.inflate(R.layout.gambar_viewpager, container, false);
        //container.removeView(layout);
        //NetworkImageView niv = (NetworkImageView)layout.findViewById(R.id.imagePager);

        NetworkImageView niv = new NetworkImageView(ct);
        il = CustomImageLoader.getInstance(ct).getIl();
        il.get(gambar.get(position),ImageLoader.getImageListener(niv, R.drawable.abu2, R.drawable.not_found));

        niv.setImageUrl(gambar.get(position),il);
        niv.setScaleType(ImageView.ScaleType.FIT_CENTER);

        //assert layout != null;
        //iv.setImageURI(Uri.parse(gambar.get(position)));
        //iv.setImageResource(il);
        if (niv.getParent()!=null)
            ((ViewGroup)niv.getParent()).removeView(niv);
        ((ViewPager)container).addView(niv);
        return niv;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
