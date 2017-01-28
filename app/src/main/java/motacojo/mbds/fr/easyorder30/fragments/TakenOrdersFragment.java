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
import motacojo.mbds.fr.easyorder30.adapters.TakenOrderItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class TakenOrdersFragment extends Fragment {

    View view;
    GlobalVariables gv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.taken_orders_layout, container, false);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LoadOrderList", "onCreate");
        gv = (GlobalVariables) getActivity().getApplication();

        LoadOrderList ru = new LoadOrderList();
        ru.execute();
    }

    ProgressDialog progressDialog;
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

    class LoadOrderList extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... champs) {
            try{
                URL url = new URL("http://95.142.161.35:8080/menu/");

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

            ListView lst = (ListView)getActivity().findViewById(R.id.taken_orders_list_view);
            List<Order> orders = new ArrayList<>();

            GlobalVariables gv = (GlobalVariables) getActivity().getApplication();
            try {

                JSONArray list = new JSONArray(result);
                for(int i = 0; i < list.length(); i++) {
                    //Récupérer la commande
                    JSONObject order = list.getJSONObject(i);

                    //Récupérer le serveur de la commande
                    String serverId;
                    try {
                        //On essaie de récuperer l'object JSON 'server'
                        JSONObject server = order.getJSONObject("server");
                        serverId = server.optString("id", null);
                    } catch (JSONException e) {
                        //Si une exception est renvoyée, on considère l'id du serveur null
                        serverId = null;
                    }

                    //Si l'id du serveur est null, on ignore l'entrée
                    //Sinon ...
                    if (serverId != null) {
                        //... on récupère la personne associée
                        Person waiter = Person.getById(gv, serverId);
                        //Si l'id ne correspond à personne, on ignore l'entrée
                        //Sinon, tout va bien
                        if (waiter != null) {
                            //On récupère la liste des items de la commande i
                            JSONArray items = order.getJSONArray("items");
                            if (items.length() != 0) {
                                List<Product> products = new ArrayList<>();
                                for(int j = 0; j < items.length(); j++) {
                                    JSONObject itemId = items.getJSONObject(j);
                                    Product p = Product.getById(gv, itemId.getString("id"));
                                    if (p != null) {
                                        products.add(p);
                                    }
                                }
                                Order o = new Order(products, new Person(), waiter);
                                o.setId(order.getString("id"));
                                orders.add(o);
                            }

                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("LoadPeopleList", "erreur");
                e.printStackTrace();
            }

            TakenOrderItemAdapter adapter = new TakenOrderItemAdapter(getContext(), orders);
            lst.setAdapter(adapter);
        }
    }



}
