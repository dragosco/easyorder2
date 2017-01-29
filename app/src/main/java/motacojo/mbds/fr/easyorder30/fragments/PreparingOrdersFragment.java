package motacojo.mbds.fr.easyorder30.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.adapters.PreparingOrderItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class PreparingOrdersFragment extends Fragment {

    View view;
    GlobalVariables gv;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.preparing_orders_layout, container, false);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = (GlobalVariables) getActivity().getApplication();

        PreparingOrdersLoader pol = new PreparingOrdersLoader();
        pol.execute();
    }

    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
                progressDialog = new ProgressDialog(this.getActivity());
                progressDialog.setMessage(this.getResources().getString(R.string.please_wait));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        progressDialog = null;
                    }
                });
                progressDialog.show();
            }
        }
        else {
            if(progressDialog!=null) {
                progressDialog.dismiss();
            }
        }
    }

    class PreparingOrdersLoader extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... champs) {
            try{
                URL url = new URL("http://95.142.161.35:8080/person/menus/" + gv.getConnectedUser().getId());

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("LoadOrderList", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("LoadOrderList", "onPostExecute");
            showProgressDialog(false);

            ListView lst = (ListView)getActivity().findViewById(R.id.preparing_orders_list_view);
            List<Order> orders = new ArrayList<>();

            try {
                JSONObject resultJSON = new JSONObject(result);
                if (resultJSON.getBoolean("status")) {

                    JSONArray menus = resultJSON.getJSONArray("menus");
                    for (int i = 0; i < menus.length(); i++) {
                        JSONObject menu = menus.getJSONObject(i);
                        List<Product> productList = new ArrayList<>();
                        JSONArray menuItems = menu.getJSONArray("items");
                        for (int j = 0; j < menuItems.length(); j++) {
                            JSONObject menuItem = menuItems.getJSONObject(j);
                            productList.add(Product.getById(gv, menuItem.getString("id")));
                        }
                        Order o = new Order(productList, new Person(), gv.getConnectedUser());
                        orders.add(o);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PreparingOrderItemAdapter adapter = new PreparingOrderItemAdapter(getContext(), orders);
            lst.setAdapter(adapter);
        }
    }
}
