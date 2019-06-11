package hr.tvz.android.expenseapp.fragments.Balance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.model.Balance;
import hr.tvz.android.mvpexample.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BalanceFragment extends Fragment {

    private RecyclerView balanceRecyclerView;
    private BalanceAdapterRecView balanceAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private Spinner balanceTypeSpinner;
    private EditText yearOfEntry;
    private Button btnShow;
    private TextView balancePreviewText;
    private ArrayList<Balance> listOfBalances;
    private boolean ok;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        balancePreviewText = getView().findViewById(R.id.balance_preview);
        yearOfEntry = getView().findViewById(R.id.balance_year_of_entry);
        balanceTypeSpinner = getView().findViewById(R.id.balance_spinner);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.balance_for, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        balanceTypeSpinner.setAdapter(staticAdapter);

        btnShow = getView().findViewById(R.id.btn_balance_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balanceTypeSpinner.getSelectedItem().equals("Expense")) {
                    getExpensesPerMonth();
                } else {
                    getIncomesPerMonth();
                }
            }
        });
    }

    public void getExpensesPerMonth() {

        ok = true;
        if(yearOfEntry.getText().toString().isEmpty()){
            yearOfEntry.setError("Enter year of entry!");
            ok = false;
        }

        if(ok) {
            balancePreviewText.setText("");
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<ArrayList<Balance>> call = jsonPlaceholderApi.listByExpenseMonth(Integer.valueOf(yearOfEntry.getText().toString()));

            call.enqueue(new Callback<ArrayList<Balance>>() {
                @Override
                public void onResponse(Call<ArrayList<Balance>> call, Response<ArrayList<Balance>> response) {
                    if (!response.isSuccessful()) {
                        balancePreviewText.setText("Code: " + response.code());
                        return;
                    }

                    listOfBalances = response.body();

                    if (listOfBalances.isEmpty()) {
                        balancePreviewText.setText("No entry for that year!");
                        return;
                    }

                    balanceRecyclerView = getView().findViewById(R.id.balance_recycler_view);
                    balanceRecyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getActivity());
                    balanceAdapter = new BalanceAdapterRecView(listOfBalances);

                    balanceRecyclerView.setLayoutManager(layoutManager);
                    balanceRecyclerView.setAdapter(balanceAdapter);

                    balanceAdapter.setOnItemClickListener(new BalanceAdapterRecView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            BalanceExpenseDetailsFragment balanceExpenseDetailsFragment = new BalanceExpenseDetailsFragment();
                            Bundle bundle = new Bundle();

                            bundle.putParcelable("balance", listOfBalances.get(position));
                            bundle.putInt("year", Integer.valueOf(yearOfEntry.getText().toString()));
                            balanceExpenseDetailsFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.fragment_container, balanceExpenseDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Balance>> call, Throwable t) {
                    balancePreviewText.setText(t.getMessage());
                }
            });
        }
    }

    public void getIncomesPerMonth() {
        ok = true;
        if(yearOfEntry.getText().toString().isEmpty()){
            yearOfEntry.setError("Enter year of entry!");
            ok = false;
        }

        if(ok) {
            balancePreviewText.setText("");
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<ArrayList<Balance>> call = jsonPlaceholderApi.listByIncomeMonth(Integer.valueOf(yearOfEntry.getText().toString()));

            call.enqueue(new Callback<ArrayList<Balance>>() {
                @Override
                public void onResponse(Call<ArrayList<Balance>> call, Response<ArrayList<Balance>> response) {
                    if (!response.isSuccessful()) {
                        balancePreviewText.setText("Code: " + response.code());
                        return;
                    }

                    listOfBalances = response.body();

                    if (listOfBalances.isEmpty()) {
                        balancePreviewText.setText("No entry for that year!");
                        return;
                    }

                    balanceRecyclerView = getView().findViewById(R.id.balance_recycler_view);
                    balanceRecyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getActivity());
                    balanceAdapter = new BalanceAdapterRecView(listOfBalances);

                    balanceRecyclerView.setLayoutManager(layoutManager);
                    balanceRecyclerView.setAdapter(balanceAdapter);

                    balanceAdapter.setOnItemClickListener(new BalanceAdapterRecView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            BalanceIncomeDetailsFragment balanceIncomeDetailsFragment = new BalanceIncomeDetailsFragment();
                            Bundle bundle = new Bundle();

                            bundle.putParcelable("balance", listOfBalances.get(position));
                            bundle.putInt("year", Integer.valueOf(yearOfEntry.getText().toString()));
                            balanceIncomeDetailsFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.fragment_container, balanceIncomeDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Balance>> call, Throwable t) {
                    balancePreviewText.setText(t.getMessage());
                }
            });
        }
    }
}
