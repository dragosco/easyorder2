package motacojo.mbds.fr.easyorder30.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Order;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;

/**
 * Created by cojoc on 27/01/2017.
 */

public class ResourceLoader extends AsyncTask<String,Void,String> {

    private Activity activity;
    private String resourceUrlString;
    private Class resourceClass;
    private ProgressDialog progressDialog;

    public ResourceLoader(Activity activity, Class resourceClass, String resourceUrlString) {
        this.activity = activity;
        this.resourceUrlString = resourceUrlString;
        this.resourceClass = resourceClass;
    }

    @Override
    protected String doInBackground(String... champs) {
        try{

            URL url = new URL(resourceUrlString);

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
        Log.e("ResourceLoader", "onPreExecute");
        showProgressDialog(true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        showProgressDialog(false);

        try {
            if (resourceClass == Person.class) {
                parseJSONArray(Person.class, result);
            } else if (resourceClass == Product.class) {
                parseJSONArray(Product.class, result);
            } else if (resourceClass == Order.class) {
                parseJSONArray(Order.class, result);
            } else {
                Log.e("ResourceLoader", "Invalid resource");
            }
        } catch (JSONException e) {
            Log.e("ResourceLoader", "Invalid resource");
            e.printStackTrace();
        }
    }


    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getResources().getString(R.string.please_wait));
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

    private void parseJSONArray(Class expectedResource, String jsonArrayString) throws JSONException {
        JSONArray list = new JSONArray(jsonArrayString);

        if (expectedResource == Person.class) {
            HashMap<String, Person> people = new HashMap<>();
            for(int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = list.getJSONObject(i);
                Person user = Person.parseJSON(jsonObject);
                people.put(user.getId(), user);
            }
            GlobalVariables gv = ((GlobalVariables) activity.getApplication());
            gv.setAllUsers(people);
        } else if (expectedResource == Product.class) {
            HashMap<String, Product> products = new HashMap<>();
            for(int i = 0; i < list.length(); i++) {
                JSONObject product = list.getJSONObject(i);
                Product prod = Product.parseJSON(product);
                products.put(prod.getId(), prod);
            }
            GlobalVariables gv = ((GlobalVariables) activity.getApplication());
            gv.setAllProducts(products);
        } else if (expectedResource == Order.class) {

        } else {
            Log.e("ResourceLoader", "Not accepted class");
        }

    }
}
