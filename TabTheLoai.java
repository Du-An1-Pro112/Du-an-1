package poly.qn.hoanganh.myapplication.Tab;

import poly.qn.hoanganh.myapplication.R;
import poly.qn.hoanganh.myapplication.activities.TheLoaiActivity;
import poly.qn.hoanganh.myapplication.adapters.GirdViewAdapter;
import poly.qn.hoanganh.myapplication.adapters.TheLoai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class TabTheLoai extends Fragment {
	GridView gv;
    ArrayList<TheLoai> mangtl;

    public static final String IDTHELOAI = "IDTHELOAI";
    public static final String TENTHELOAI = "TENTHELOAI";

    @Override
	public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_the_loai, container, false);
        gv = (GridView) v.findViewById(R.id.gridv);
        mangtl = new ArrayList<TheLoai>();

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tentheloai = mangtl.get(i).getTenTL();
                putEXTRA(String.valueOf(i+1),tentheloai);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute("http://qnhoanganh.pe.hu/theloai.php");
            }
        });
		return v;
    }



    public void putEXTRA (String idtheloai, String tentheloai){
        Intent intent = new Intent(getContext().getApplicationContext(), TheLoaiActivity.class);
        intent.putExtra(IDTHELOAI, idtheloai);
        intent.putExtra(TENTHELOAI,tentheloai);
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
                        mangtl.add(new TheLoai(
                                tr.getString("tentheloai"),
                                tr.getString("linkpic")
                        ));
                    }
                }
                GirdViewAdapter girdViewAdapter = new GirdViewAdapter(getContext().getApplicationContext(),
                        R.layout.gird_row,
                        mangtl);
                gv.setAdapter(girdViewAdapter);

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
