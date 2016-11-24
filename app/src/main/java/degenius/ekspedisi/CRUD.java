package degenius.ekspedisi;

import android.net.Uri;

/**
 * Created by HP on 11/2/2016.
 */

public class CRUD {
    public static final String ALLOWED_URI = "@#&=*+-_.,:!?()/~'%";

    public static String selectPaket(){
        return "http://eksekutifsistem.com/Ekspedisi_city/select_paket.php";
    }

    public static String loginAplikasi(String iduser, String pass){
        return Uri.encode("http://eksekutifsistem.com/Ekspedisi_city/login.php?username="+iduser+"&password="+pass,ALLOWED_URI);
    }

    public static String insertPesan(String barang, String jumlah, String tujuan, String iduser){
        return Uri.encode("http://eksekutifsistem.com/Ekspedisi_city/insert_pesan.php?brg="+barang+"&jum="+jumlah+"&tujuan="+tujuan+"&iduser="+iduser,ALLOWED_URI);
    }

    public static String selectStatus(String iduser){
        return "http://eksekutifsistem.com/Ekspedisi_city/select_status.php?iduser="+iduser;
    }

    public static String selectHistory(String iduser){
        return "http://eksekutifsistem.com/Ekspedisi_city/select_history.php?iduser="+iduser;
    }

    public static String batalPesan(String nomor){
        return "http://eksekutifsistem.com/Ekspedisi_city/batal_pesan.php?nomor="+nomor;
    }

    public static String cobaGambar(){
        return "http://eksekutifsistem.com/Ekspedisi_city/coba_gambar.php";
    }

    public static String cobaPromo(){
        return "http://eksekutifsistem.com/Ekspedisi_city/coba_promo.php";
    }

    public static String gantiPassword(String iduser, String pass){
        return Uri.encode("http://eksekutifsistem.com/Ekspedisi_city/ganti_password.php?user="+iduser+"&pass="+pass,ALLOWED_URI);
    }
}
