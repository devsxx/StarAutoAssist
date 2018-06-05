package com.app.starautoassist.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import com.app.starautoassist.Adapter.ServiceAdapter;
import com.app.starautoassist.Data.Service;
import com.app.starautoassist.Helper.GetSet;
import com.app.starautoassist.Others.Constants;
import com.app.starautoassist.R;


import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    public static ArrayList<HashMap<String, String>> Services_list = new ArrayList<HashMap<String, String>>();

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        new Get_Services_Async(getActivity()).execute();
        recyclerView = view.findViewById(R.id.rv_home);
        return view;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @SuppressLint("StaticFieldLeak")
    public class Get_Services_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        private String url = Constants.BaseURL + Constants.getservices;
        ProgressDialog progress;
        @Nullable
        String user_id;
        HashMap<String, String> map;
        public Get_Services_Async(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Please wait ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            progress.dismiss();
            String sname,simg,sid,scharge,sprice;
            Log.v("result", "" + jsonData);
            JSONObject jonj = null;
            try {
                jonj = new JSONObject(jsonData);
                if (jonj.getString("status").equalsIgnoreCase(
                        "success")) {
                    String data=jonj.getString("data");
                    JSONArray array=new JSONArray(data);
                    Services_list.clear();
                    for(int i=0;i<array.length();i++){
                        map = new HashMap<String, String>();
                        JSONObject object=array.getJSONObject(i);
                        sid=object.getString(Constants.serviceid);
                        sname=object.getString(Constants.servicename);
                        simg=object.getString(Constants.serviceimg);
                        scharge=object.getString(Constants.servicecharge);


                        map.put(Constants.serviceid,sid);
                        map.put(Constants.servicename,sname);
                        map.put(Constants.serviceimg,simg);
                        map.put(Constants.servicecharge,scharge);
                        Services_list.add(map);
                    }
                    serviceAdapter = new ServiceAdapter(getActivity(), Services_list);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(serviceAdapter);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
