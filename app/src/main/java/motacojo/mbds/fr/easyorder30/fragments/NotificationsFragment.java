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
import android.widget.TextView;

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
import motacojo.mbds.fr.easyorder30.adapters.NotificationItemAdapter;
import motacojo.mbds.fr.easyorder30.entities.Buzz;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class NotificationsFragment extends Fragment {

    View view;
    GlobalVariables gv;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notifications_layout, container, false);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = (GlobalVariables) getActivity().getApplication();

        NotificationsLoader notificationsLoader = new NotificationsLoader();
        notificationsLoader.execute();
    }

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

    class NotificationsLoader extends AsyncTask<Person,Void,String> {

        @Override
        protected String doInBackground(Person... params) {
            try{

                URL url = new URL("http://95.142.161.35:8080/buzz/" + gv.getConnectedUser().getId());

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                Log.e("NotificationsLoader", "\nSending 'POST' request to URL : " + url);
                Log.e("NotificationsLoader", "Post parameters : " + in);
                Log.e("NotificationsLoader", "Response Code : " + urlConnection.getResponseCode());

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
            Log.e("NotificationsLoader", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("NotificationsLoader", "onPostExecute");
            showProgressDialog(false);

            ListView lst = (ListView)getActivity().findViewById(R.id.notifications_list_view);
            List<Buzz> buzzes = new ArrayList<>();

            /*for (int i = 0; i < 10; i++) {
                Buzz buzz = new Buzz(new Person());
                buzz.setMessage("Message " + i);
                buzzes.add(buzz);
            }*/

            try {
                JSONObject resultJSON = new JSONObject(result);
                if (resultJSON.getBoolean("success")) {
                    JSONArray notifs = resultJSON.getJSONArray("data");
                    for (int i = 0; i < notifs.length(); i++) {
                        JSONObject buzz = notifs.getJSONObject(i);
                        //on considere qu'il envoie le id
                        String id = buzz.getString("id");
                        //on considere qu'il envoie la date denvoie
                        String date = buzz.getString("id");
                        //on considere qu'il envoie un message
                        String message = buzz.getString("id");

                        Person person = Person.getById(gv, id);
                        Buzz b = new Buzz(new Person());
                        b.setMessage(message);
                        buzzes.add(b);
                    }

                    TextView emptyText = (TextView)getActivity().findViewById(R.id.tv_no_notifications);
                    emptyText.setText("");

                    NotificationItemAdapter adapter = new NotificationItemAdapter(getContext(), buzzes);
                    lst.setAdapter(adapter);
                } else {
                    TextView emptyText = (TextView)getActivity().findViewById(R.id.tv_no_notifications);
                    emptyText.setText(getString(R.string.buzz_no_notification));
                    lst.setEmptyView(emptyText);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
