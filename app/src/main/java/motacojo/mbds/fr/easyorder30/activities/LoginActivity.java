package motacojo.mbds.fr.easyorder30.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
