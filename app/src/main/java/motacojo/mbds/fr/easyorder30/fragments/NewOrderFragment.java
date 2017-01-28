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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.activities.MainActivity;
import motacojo.mbds.fr.easyorder30.adapters.ProductItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class NewOrderFragment extends Fragment implements View.OnClickListener {

    View view;
    GlobalVariables gv;
    ImageButton less;
    ImageButton more;
    Button submitOrder;
    ProductItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.new_order_layout, container, false);
        ListView lst = (ListView)view.findViewById(R.id.products_list_view);

        gv = (GlobalVariables) getActivity().getApplication();
        List<Product> products = new ArrayList<>(gv.getAllProducts().values());

        less = (ImageButton)view.findViewById(R.id.img_btn_less_product);
        more = (ImageButton)view.findViewById(R.id.img_btn_more_product);
        submitOrder = (Button)view.findViewById(R.id.btn_new_order_submit);
        submitOrder.setOnClickListener(this);

        adapter = new ProductItemAdapter(getContext(), products, NewOrderFragment.this);
        lst.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LoadProductList", "onCreate");

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.img_btn_less_product:
                gv.removeProductFromOrder((String)v.getTag(R.id.img_btn_less_product));
                ((TextView)view.findViewById(R.id.tv_product_qty)).setText(String.valueOf(gv.getOrderInProgressQty((String)v.getTag(R.id.tv_product_qty))));
                adapter.notifyDataSetChanged();
                break;
            case R.id.img_btn_more_product:
                gv.addProductToOrder((String)v.getTag(R.id.img_btn_more_product));
                ((TextView)view.findViewById(R.id.tv_product_qty)).setText(String.valueOf(gv.getOrderInProgressQty((String)v.getTag(R.id.tv_product_qty))));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_new_order_submit:
                Log.e("dans le bouton", "yey");
                HashMap<String, Integer> orderItems = gv.getOrderInProgress();
                List<Product> items = new ArrayList<>();
                for (Map.Entry<String, Integer> orderLine : orderItems.entrySet()) {
                    String productId = orderLine.getKey();
                    Integer qty = orderLine.getValue();
                    for (int i = 0; i < qty; i++) {
                        items.add(Product.getById(gv, productId));
                    }
                }
                Order newOrder = new Order(items, new Person(), ((MainActivity)getActivity()).connectedUser);
                for (Product p : newOrder.getItems()) {
                    Log.e("item", p.getId());
                }

                Log.e("waiter", ((MainActivity)getActivity()).connectedUser.getId());
                SubmitOrder so = new SubmitOrder();
                so.execute(newOrder);
                break;
        }
    }

    class SubmitOrder extends AsyncTask<Order,Void,Order> {
        @Override
        protected Order doInBackground(Order... orders) {
            Log.e("SubmitOrder", "OUI");
            Order order = orders[0];
            Log.e("server", order.getWaiter().getId());
            for (Product p : order.getItems()) {
                Log.e("item insede async", p.getId());
            }
            try{
                URL url = new URL("http://95.142.161.35:8080/menu/");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                JSONObject jsonParam = new JSONObject();
                JSONObject server = new JSONObject();
                server.put("id", order.getWaiter().getId());
                jsonParam.put("server", server);

                //sjsonParam.put("cooker", order.getCooker().getId());
                jsonParam.put("createdBy", "Dragos");
                JSONArray jsonArray = new JSONArray();
                List<Product> items = order.getItems();
                for (Product p : items) {
                    JSONObject item = new JSONObject();
                    item.put("id", p.getId());
                    jsonArray.put(item);
                }
                jsonParam.put("items", jsonArray);

                OutputStream out = urlConnection.getOutputStream();
                out.write(jsonParam.toString().getBytes());
                out.flush();
                out.close();

                InputStream in = urlConnection.getInputStream();
                Log.e("SubmitOrder", "\nSending 'POST' request to URL : " + url);
                Log.e("SubmitOrder", "Post parameters : " + in);
                Log.e("SubmitOrder", "Response Code : " + urlConnection.getResponseCode());

                BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                JSONObject resultJSON = new JSONObject(result.toString());

                List<Product> itemsResponse = new ArrayList<>();
                JSONArray itemsResponseJSON = resultJSON.getJSONArray("items");
                for (int i = 0; i < itemsResponseJSON.length(); i++) {
                    itemsResponse.add(Product.getById(gv, itemsResponseJSON.getJSONObject(i).getString("id")));
                }
                Order o = new Order(
                        itemsResponse,
                        new Person(),
                        Person.getById(gv, resultJSON.getJSONObject("server").getString("id")));
                o.setId(resultJSON.getString("id"));
                return o;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("RegisterUser", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(Order order) {
            super.onPostExecute(order);
            Log.e("RegisterUser", "onPostExecute");
            showProgressDialog(false);

            Toast.makeText(getActivity().getApplicationContext(), R.string.order_successfully_sent, Toast.LENGTH_LONG).show();
            gv.setOrderInProgress(new HashMap<String, Integer>());
            adapter.notifyDataSetChanged();
            //Renvoyer vers le login
            //Fermer l'activité Enregistrer
        }
    }
}
