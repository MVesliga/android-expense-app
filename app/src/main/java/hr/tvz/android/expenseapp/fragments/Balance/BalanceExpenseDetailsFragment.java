package hr.tvz.android.expenseapp.fragments.Balance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.model.Balance;
import hr.tvz.android.expenseapp.model.Type;
import hr.tvz.android.mvpexample.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BalanceExpenseDetailsFragment extends Fragment {

    private Balance balance;
    private int yearOfEntry;
    private TextView balanceDetails;
    private ArrayList<Type> listExpenseByType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_balance_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            balance = bundle.getParcelable("balance");
            yearOfEntry = bundle.getInt("year");

            balanceDetails = getView().findViewById(R.id.balance_details);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<ArrayList<Type>> call = jsonPlaceholderApi.listByExpenseType(balance.getMonth(), yearOfEntry);

            call.enqueue(new Callback<ArrayList<Type>>() {
                @Override
                public void onResponse(Call<ArrayList<Type>> call, Response<ArrayList<Type>> response) {
                    if (!response.isSuccessful()) {
                        balanceDetails.setText("Code: " + response.code());
                        return;
                    }

                    listExpenseByType = response.body();

                    for(Type t : listExpenseByType){
                        String content = "";
                        balanceDetails.append(t.getTypeName() + ": " + t.getAmount() + " kn.\n");
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Type>> call, Throwable t) {
                    balanceDetails.setText(t.getMessage());
                }
            });

            }
        }
    }
