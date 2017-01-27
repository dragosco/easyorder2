package motacojo.mbds.fr.easyorder30.activities;

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
import motacojo.mbds.fr.easyorder30.utils.ResourceLoader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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

        //ResourceLoader ordersLoader = new ResourceLoader(MainActivity.this, Order.class, "http://95.142.161.35:8080/menu/");
        //ordersLoader.execute();
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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

}
