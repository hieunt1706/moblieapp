package Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tutorial_v1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import Model.UserAccount;
import Retrofit.IMyService;
import dmax.dialog.SpotsDialog;
import retrofit2.Retrofit;
import Retrofit.RetrofitClient;
import Fragment.AccountFragment;
import Fragment.FeatureFragment;
import Fragment.SearchFragment;
import Fragment.mycoursesFragment;
import Fragment.cartFragment;

import static android.view.View.GONE;


public class HomeScreenActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    IMyService iMyService;
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    public  UserAccount userAccount=new UserAccount();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setUIReference();
        alertDialog= new SpotsDialog.Builder().setContext(this).build();
        Retrofit retrofitClient= RetrofitClient.getInstance();
        iMyService=retrofitClient.create(IMyService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userAccount= new UserAccount();
        userAccount.setHoten(sharedPreferences.getString("name","default"));
        userAccount.setSdt(sharedPreferences.getString("phone","default"));
        userAccount.setMail(sharedPreferences.getString("email","default"));
        userAccount.setAva(sharedPreferences.getString("image","default"));
        userAccount.setMota(sharedPreferences.getString("description","default"));
        userAccount.setGioitinh(sharedPreferences.getString("gender","default"));
        userAccount.setDiachia(sharedPreferences.getString("address","default"));
        userAccount.setToken(sharedPreferences.getString("token","default"));
        userAccount.setMatkhau(sharedPreferences.getString("password","default"));
        bottomNav.setOnNavigationItemSelectedListener(bottomNavMethod);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new FeatureFragment()).commit();


    }

    BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment =new Fragment();

            switch (item.getItemId())
            {
                case R.id.feature:
                    fragment=new FeatureFragment();
                    break;

                case R.id.search_frag:
                    fragment=new SearchFragment();
                    break;

                case R.id.my_course_frag:
                    fragment=new mycoursesFragment();
                    break;

                case R.id.cart_frag:
                    fragment=new cartFragment();
                    break;

                case R.id.account_frag:
                    fragment=new AccountFragment(userAccount);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            return true;
        }
    };

    private void setUIReference() {
        bottomNav=findViewById(R.id.bottomNav);
    }
}