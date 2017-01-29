package motacojo.mbds.fr.easyorder30.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import motacojo.mbds.fr.easyorder30.R;
import motacojo.mbds.fr.easyorder30.entities.Person;
import motacojo.mbds.fr.easyorder30.entities.Product;
import motacojo.mbds.fr.easyorder30.fragments.NewOrderFragment;
import motacojo.mbds.fr.easyorder30.fragments.NotificationsFragment;
import motacojo.mbds.fr.easyorder30.fragments.PreparingOrdersFragment;
import motacojo.mbds.fr.easyorder30.fragments.TakenOrdersFragment;
import motacojo.mbds.fr.easyorder30.fragments.UsersFragment;
import motacojo.mbds.fr.easyorder30.utils.GlobalVariables;
import motacojo.mbds.fr.easyorder30.utils.ResourceLoader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Person connectedUser;
    GlobalVariables gv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gv = (GlobalVariables) getApplication();

        checkIfConnetedUserExists();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        /*connectedUser = new Person(
                bundle.getString("nom"),
                bundle.getString("prenom"),
                bundle.getString("sexe"),
                bundle.getString("telephone"),
                bundle.getString("email"),
                bundle.getString("password"));
        connectedUser.setId(bundle.getString("id"));

        Log.e("nom", connectedUser.getId());
        gv.setConnectedUser(connectedUser);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ResourceLoader usersLoader = new ResourceLoader(MainActivity.this, Person.class, "http://95.142.161.35:8080/person/");
        usersLoader.execute();

        ResourceLoader productsLoader = new ResourceLoader(MainActivity.this, Product.class, "http://95.142.161.35:8080/product/");
        productsLoader.execute();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new NewOrderFragment())
                .commit();

    }

    private void checkIfConnetedUserExists() {
        if(gv.getConnectedUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                /*MainActivity.SingOut signOut = new MainActivity.SingOut();
                signOut.execute(txt_email, txt_password);*/
                gv.setConnectedUser(null);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_users) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new UsersFragment())
                    .commit();
        } else if (id == R.id.nav_new_order) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new NewOrderFragment())
                    .commit();
        } else if (id == R.id.nav_taken_orders) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new TakenOrdersFragment())
                    .commit();
        } else if (id == R.id.nav_preparing_orders) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new PreparingOrdersFragment())
                    .commit();
        } else if (id == R.id.nav_notifications) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new NotificationsFragment())
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProgressDialog(boolean isVisible) {
        if (isVisible) {
            if(progressDialog==null) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.please_wait));
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

    /*class SingOut extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... champs) {
            try {
                URL url = new URL("http://95.142.161.35:8080/person/login");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
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
                Log.e("SingOut", "\nSending 'POST' request to URL : " + url);
                Log.e("SingOut", "Post parameters : " + in);
                Log.e("SingOut", "Response Code : " + urlConnection.getResponseCode());

                BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
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
            Log.e("SingOut", "onPreExecute");
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("SingOut", "onPostExecute");
            showProgressDialog(false);

            try {
                JSONObject resultJSON = new JSONObject(result);

                //Traiter la person
                if (result != null) {
                    if ((resultJSON.getBoolean("success"))) {
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

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("prenom", p.getPrenom());
                        bundle.putString("nom", p.getNom());
                        bundle.putString("sexe", p.getSexe());
                        bundle.putString("telephone", p.getTelephone());
                        bundle.putString("email", p.getEmail());
                        bundle.putString("password", p.getPassword());
                        bundle.putString("id", p.getId());

                        intent.putExtras(bundle);

                        // verify if gcmKey received is the same as gv.token
                        // if not http://95.142.161.35:8080/addkey/[user_id]/[key]/[firebase_key]

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), resultJSON.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.e("MainActivity", "erreur");
                }
                //Renvoyer vers le login
                //Fermer l'activité login
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
