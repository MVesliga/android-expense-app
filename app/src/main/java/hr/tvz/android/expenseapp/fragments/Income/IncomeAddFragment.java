package hr.tvz.android.expenseapp.fragments.Income;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.expenseapp.model.Income;
import hr.tvz.android.mvpexample.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IncomeAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private TextView incomeName;
    private TextView incomeAmount;
    private Spinner incomeTypeSpinner;
    private Button incomeDateButton;
    private Button addIncomeButton;
    private TextView textIncomeDate;
    private String dateText;
    private String date;
    private boolean ok;
    private TextView test;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_add, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        incomeName = getActivity().findViewById(R.id.add_income_name);
        incomeAmount = getActivity().findViewById(R.id.add_income_amount);
        incomeTypeSpinner = getActivity().findViewById(R.id.incomeTypeSpinner);
        textIncomeDate = getActivity().findViewById(R.id.textIncomeDate);
        incomeDateButton = getActivity().findViewById(R.id.btn_incomeDate);
        addIncomeButton = getActivity().findViewById(R.id.btn_add_income);
        test = getActivity().findViewById(R.id.add_income_test);

        incomeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.income_types, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeTypeSpinner.setAdapter(staticAdapter);

        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome(v);
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateText = "Datum: " + dayOfMonth + "." + month + "." + year + ".";
        date = year + "-" + month + "-" + dayOfMonth;
        textIncomeDate.setText(dateText);
    }

    public void addIncome(View v){
        ok = true;

        if(incomeName.getText().toString().isEmpty()){
            incomeName.setError("Enter expense name!");
            ok = false;
        }
        if(incomeAmount.getText().toString().isEmpty()){
            incomeAmount.setError("Enter expense amount!");
            ok = false;
        }

        if(ok){
            Income income = new Income();
            income.setIncomeName(incomeName.getText().toString());
            income.setAmount(Double.parseDouble(incomeAmount.getText().toString()));
            income.setIncomeType(incomeTypeSpinner.getSelectedItem().toString());
            income.setEntryDate(date);
            Long userId = getActivity().getIntent().getLongExtra("userId",0);
            income.setUserIdentifier(userId);

            incomeName.setText("");
            incomeAmount.setText("");
            incomeTypeSpinner.setSelection(0);
            textIncomeDate.setText("");

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<Income> call = jsonPlaceholderApi.createIncome(income);
            call.enqueue(new Callback<Income>() {
                @Override
                public void onResponse(Call<Income> call, Response<Income> response) {
                    if (!response.isSuccessful()) {
                        test.setText("Code: " + response.code());
                        return;
                    }

                    Income incomeResponse = response.body();

                    test.append("Code: " + response.code() + "\n");
                    test.append("amount: " + incomeResponse.getIncomeName() + "\n");
                    test.append("userId: " + incomeResponse.getUserIdentifier() + "\n\n ");

                }

                @Override
                public void onFailure(Call<Income> call, Throwable t) {
                    test.setText(t.getMessage());
                }
            });

            Toast.makeText(getActivity(), "Income added successfully!", Toast.LENGTH_LONG).show();
        }
    }
}
