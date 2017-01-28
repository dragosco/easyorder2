package motacojo.mbds.fr.easyorder30.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.services.GCMRegistrationIntentService;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    GlobalVariables gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gv = (GlobalVariables) getApplication();

        //Initializing broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    gv.setToken(intent.getStringExtra("token"));
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }

        Button btnLog = (Button)findViewById(R.id.btn_Login_Login);
        btnLog.setOnClickListener(this);

        Button btnSgn = (Button)findViewById(R.id.btn_Signup_Login);
        btnSgn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_Login_Login:
                String txt_email = ((EditText)findViewById(R.id.input_Email_Login)).getText().toString();
                String txt_password = ((EditText)findViewById(R.id.input_Mdp_Login)).getText().toString();

                //enregistrer lutilisateur
                ValidateLogin validateLogin = new ValidateLogin();
                validateLogin.execute(txt_email, txt_password);
                break;
            case R.id.btn_Signup_Login:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    ProgressDialog progressDialog;
    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
                progressDialog = new ProgressDialog(this);
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

    class ValidateLogin extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... champs) {
            try{
                URL url = new URL("http://95.142.161.35:8080/person/login");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", champs[0]);
                jsonParam.put("password", champs[1]);

                OutputStream out = urlConnection.getOutputStream();
                out.write(jsonParam.toString().getBytes());
                out.flush();
                out.close();

                InputStream in = urlConnection.getInputStream();
                Log.e("RegisterUser", "\nSending 'POST' request to URL : " + url);
                Log.e("RegisterUser", "Post parameters : " + in);
                Log.e("RegisterUser", "Response Code : " + urlConnection.getResponseCode());

                BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("ValidateLogin", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("ValidateLogin", "onPostExecute");
            showProgressDialog(false);

            try {
                JSONObject resultJSON = new JSONObject(result);

                //Traiter la person
                if (result != null) {
                    if((resultJSON.getBoolean("success"))) {
                        //Toast.makeText(getApplicationContext(),R.string.inscription_ok, Toast.LENGTH_LONG).show();
                        JSONObject user = resultJSON.getJSONObject("user");
                        Person p = new Person(
                                user.getString("nom"),
                                user.getString("prenom"),
                                user.getString("sexe"),
                                user.getString("telephone"),
                                user.getString("email"),
                                user.getString("password"));
                        p.setConnected(user.getBoolean("connected"));
                        p.setId(user.getString("id"));
                        p.setGcmKey(user.getString("gcmKey"));

                        gv.setConnectedUser(p);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("prenom",p.getPrenom());
                        bundle.putString("nom",p.getNom());
                        bundle.putString("sexe",p.getSexe());
                        bundle.putString("telephone",p.getTelephone());
                        bundle.putString("email",p.getEmail());
                        bundle.putString("password",p.getPassword());
                        bundle.putString("id",p.getId());

                        intent.putExtras(bundle);

                        // verify if gcmKey received is the same as gv.token
                        // if not http://95.142.161.35:8080/addkey/[user_id]/[key]/[firebase_key]

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_LONG).show();
                    }

                }else{
                    Log.e("LoginActivity", "erreur");
                }
                //Renvoyer vers le login
                //Fermer l'activité login
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
