package motacojo.mbds.fr.easyorder30.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import motacojo.mbds.fr.easyorder30.adapters.PersonItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Person;

public class UsersFragment extends Fragment implements View.OnClickListener{

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.users_list_layout, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("LoadPeopleList", "onCreate");

        LoadPeopleList ru = new LoadPeopleList();
        ru.execute();
    }

    ProgressDialog progressDialog;
    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
                progressDialog = new ProgressDialog(getActivity());
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
        System.out.println("TEST " + v.getTag());
        DeleteWaiter deleteWaiter = new DeleteWaiter();
        deleteWaiter.execute((String)v.getTag());
    }

    class LoadPeopleList extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... champs) {
            try{
                URL url = new URL("http://95.142.161.35:8080/person/");

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
            Log.e("LoadPeopleList", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("LoadPeopleList", "onPostExecute");
            showProgressDialog(false);

            ListView lst = (ListView)getView().findViewById(R.id.users_list_view);
            List<Person> people = new ArrayList<Person>();

            //Traiter la liste de personnes
            try {

                JSONArray list = new JSONArray(result);

                for(int i = 0; i < list.length(); i++) {
                    JSONObject person = list.getJSONObject(i);
                    Log.e("ActivityList", person.toString());
                    Person p = new Person(
                            person.optString("nom", "John"),
                            person.optString("prenom", "John"),
                            person.optString("sexe", "John"),
                            person.optString("telephone", "John"),
                            person.optString("email", "John"),
                            person.optString("password", "John"));
                    p.setConnected(person.optBoolean("connected", false));
                    p.setId(person.optString("id", "99999999999999999999999"));

                    people.add(p);

                }
            } catch (JSONException e) {
                Log.e("LoadPeopleList", "erreur");
                e.printStackTrace();
            }

            PersonItemAdapter adapter = new PersonItemAdapter(getContext(), people, UsersFragment.this);
            Log.e("PersonListActivity", "people length " + people.size());
            lst.setAdapter(adapter);
        }
    }

    class DeleteWaiter extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try{

                URL url = new URL("http://95.142.161.35:8080/person/" + params[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
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
            Log.e("DeleteWaiter", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("DeleteWaiter", "onPostExecute");
            showProgressDialog(false);

            try {
                JSONObject resultJSON = new JSONObject(result);
                if (result != null) {
                    if(resultJSON.has("id")) {
                        Toast.makeText(getActivity(),R.string.deleted_user_message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Une erreur s'est produite", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Log.e("LoadPeopleList", "erreur");
                e.printStackTrace();
            }
        }
    }
}
