package poly.qn.hoanganh.myapplication.Tab;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import poly.qn.hoanganh.myapplication.R;
import poly.qn.hoanganh.myapplication.activities.NoiDung;
import poly.qn.hoanganh.myapplication.adapters.ListTruyenMoi;
import poly.qn.hoanganh.myapplication.adapters.Truyen;

public class TabTruyenMoi extends Fragment {

    ListView lv;
    ArrayList<Truyen> mang;

    public static  final String TENTRUYEN = "TENTRUYEN";
    public static  final String TENTACGIA = "TENTACGIA";
    public static  final String THELOAI = "THELOAI";
    public static  final String LINKANH = "LINKANH";
    public static  final String NOIDUNG = "NOIDUNG";
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_truyen_moi, container, false);
        lv = (ListView) v.findViewById(R.id.lv_tentruyen);
        mang = new ArrayList<Truyen>();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute("http://qnhoanganh.pe.hu/websevice.php");
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Collections.reverse(mang);
                String tentruyen = mang.get(i).tentruyen;
                String tentacgia = mang.get(i).tentacgia;
                String theloai = mang.get(i).theloai;
                String linkanh = mang.get(i).linkanh;
                String noidung = mang.get(i).noidung;
                putEXTRA(tentruyen,tentacgia,theloai,linkanh, noidung);
            }
        });
        return v;
	}


    public void putEXTRA(String tentruyen, String tentacgia, String theloai , String linkanh, String noidung ){
        Intent intent = new Intent(getContext().getApplicationContext(), NoiDung.class);
        intent.putExtra(TENTACGIA, tentacgia);
        intent.putExtra(TENTRUYEN, tentruyen);
        intent.putExtra(THELOAI, theloai);
        intent.putExtra(LINKANH, linkanh);
        intent.putExtra(NOIDUNG, noidung);
        startActivity(intent);
    }

    class docJson extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray mangJSON = new JSONArray(s);
                if(mangJSON.length()> 0){
                    for(int i = 0; i < mangJSON.length() ;i ++){
                        JSONObject tr = mangJSON.getJSONObject(i);
                        Truyen truyen = new Truyen();
                        mang.add(new Truyen(
                                tr.getString("idtheloai"),
                                tr.getString("tentruyen"),
                                tr.getString("tentacgia"),
                                tr.getString("theloai"),
                                tr.getString("noidung"),
                                tr.getString("linkanh")
                        ));
                    }
                }
                ListTruyenMoi listAdapter = new ListTruyenMoi(getContext().getApplicationContext(),
                        R.layout.dong_san_pham,
                        mang
                );
                lv.setAdapter(listAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        try
        {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

}
