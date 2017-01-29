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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.adapters.PersonItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class UsersFragment extends Fragment implements View.OnClickListener{

    View view;

    PersonItemAdapter adapter;
    GlobalVariables gv;
    ImageButton buzzer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.users_list_layout, container, false);
        ListView lst = (ListView)view.findViewById(R.id.users_list_view);
        gv = (GlobalVariables) getActivity().getApplication();
        List<Person> users = new ArrayList<>(gv.getAllUsers().values());

        buzzer = (ImageButton)view.findViewById(R.id.imgBtn_status_list);
        adapter = new PersonItemAdapter(getContext(), users, UsersFragment.this);
        lst.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("LoadPeopleList", "onCreate");

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

        switch(v.getId()) {
            case R.id.imageButton5:
                DeleteWaiter deleteWaiter = new DeleteWaiter();
                deleteWaiter.execute((String)v.getTag());

                break;
            case R.id.imgBtn_status_list:
                PushNotification pushNotification = new PushNotification();
                pushNotification.execute((Person)v.getTag());
                ((ImageButton)view.findViewWithTag(v.getTag())).setColorFilter(getActivity().getResources().getColor(R.color.colorAccent));
                break;
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
                        Log.e("delete waiter", resultJSON.getString("id"));
                        gv.removeUserFromAllUsers(resultJSON.getString("id"));

                    } else {
                        Toast.makeText(getActivity(),"Une erreur s'est produite", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Log.e("LoadPeopleList", "erreur");
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }

    class PushNotification extends AsyncTask<Person,Void,String> {

        @Override
        protected String doInBackground(Person... params) {
            try{

                URL url = new URL("http://95.142.161.35:8080/buzz/");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                JSONObject jsonParam = new JSONObject();

                JSONObject receiverJson = new JSONObject();
                receiverJson.put("nom", params[0].getNom());
                receiverJson.put("prenom", params[0].getPrenom());
                receiverJson.put("sexe", params[0].getSexe());
                receiverJson.put("telephone", params[0].getTelephone());
                receiverJson.put("email", params[0].getEmail());
                receiverJson.put("createdBy", params[0].getCreatedBy());
                receiverJson.put("password", params[0].getPassword());
                receiverJson.put("connected", params[0].isConnected());
                receiverJson.put("id", params[0].getId());
                receiverJson.put("gcmKey", params[0].getGcmKey());

                Person sender = gv.getConnectedUser();
                JSONObject senderJson = new JSONObject();
                senderJson.put("nom", sender.getNom());
                senderJson.put("prenom", sender.getPrenom());
                senderJson.put("sexe", sender.getSexe());
                senderJson.put("telephone", sender.getTelephone());
                senderJson.put("email", sender.getEmail());
                senderJson.put("createdBy", sender.getCreatedBy());
                senderJson.put("password", sender.getPassword());
                senderJson.put("connected", sender.isConnected());
                senderJson.put("id", sender.getId());
                senderJson.put("gcmKey", sender.getGcmKey());

                jsonParam.put("receiver", receiverJson);
                jsonParam.put("sender", senderJson);

                Log.w("JsonParam", jsonParam.toString());

                OutputStream out = urlConnection.getOutputStream();
                out.write(jsonParam.toString().getBytes());
                out.flush();
                out.close();

                InputStream in = urlConnection.getInputStream();
                Log.e("RegisterUser", "\nSending 'POST' request to URL : " + url);
                Log.e("RegisterUser", "Post parameters : " + in);
                Log.e("RegisterUser", "Response Code : " + urlConnection.getResponseCode());
                return "Push sent";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("PushNotification", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("PushNotification", "onPostExecute");
            showProgressDialog(false);

            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        }
    }
}
