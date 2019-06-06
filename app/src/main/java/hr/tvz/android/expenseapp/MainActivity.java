package hr.tvz.android.expenseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import hr.tvz.android.expenseapp.fragments.Balance.BalanceFragment;
import hr.tvz.android.expenseapp.fragments.Expense.ExpenseAddFragment;
import hr.tvz.android.expenseapp.fragments.Expense.ExpenseListFragment;
import hr.tvz.android.expenseapp.fragments.Income.IncomeAddFragment;
import hr.tvz.android.expenseapp.fragments.Income.IncomeListFragment;
import hr.tvz.android.mvpexample.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private TextView usernameText;
    private TextView emailText;
    private DrawerLayout drawer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        usernameText = headerView.findViewById(R.id.nav_header_username);
        emailText = headerView.findViewById(R.id.nav_header_email);

        usernameText.setText(username);
        emailText.setText(email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpenseListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_expenses_list);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_expenses_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpenseListFragment()).commit();
                break;
            case R.id.nav_expenses_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExpenseAddFragment()).commit();
                break;
            case R.id.nav_incomes_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IncomeListFragment()).commit();
                break;
            case R.id.nav_incomes_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IncomeAddFragment()).commit();
                break;
            case R.id.nav_balance:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BalanceFragment()).commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}
