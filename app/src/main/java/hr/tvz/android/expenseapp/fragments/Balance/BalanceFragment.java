package hr.tvz.android.expenseapp.fragments.Balance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
import retrofit2.http.Path;

public class BalanceFragment extends Fragment {

    private Spinner balanceTypeSpinner;
    private EditText yearOfEntry;
    private Button btnShow;
    private TextView balancePreviewText;
    private ArrayList<Balance> listOfBalances;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
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
                if(balanceTypeSpinner.getSelectedItem().equals("Expense")){
                    getExpensesPerMonth();
                }
                else{
                    getIncomesPerMonth();
                }
            }
        });
    }

    public void getExpensesPerMonth(){

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

        Call<ArrayList<Balance>>call = jsonPlaceholderApi.listByExpenseMonth(Integer.valueOf(yearOfEntry.getText().toString()));

        call.enqueue(new Callback<ArrayList<Balance>>() {
            @Override
            public void onResponse(Call<ArrayList<Balance>> call, Response<ArrayList<Balance>> response) {
                if(!response.isSuccessful()){
                    balancePreviewText.setText("Code: " + response.code());
                    return;
                }

                listOfBalances = response.body();

                for(Balance b : listOfBalances){
                    String content = "";
                    content += "Month: " + b.getMonth() + ". Amount: " + b.getSum() + " kn.\n";
                    balancePreviewText.append(content);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Balance>> call, Throwable t) {
                balancePreviewText.setText(t.getMessage());
            }
        });
    }

    public void getIncomesPerMonth(){

    }
}
