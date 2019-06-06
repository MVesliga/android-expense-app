package hr.tvz.android.expenseapp.fragments.Expense;

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
import hr.tvz.android.mvpexample.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpenseAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private TextView expenseName;
    private TextView expenseAmount;
    private Spinner expenseTypeSpinner;
    private Button expenseDateButton;
    private Button addExpenseButton;
    private TextView textExpenseDate;
    private String dateText;
    private String date;
    private boolean ok;
    private TextView test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_add, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        expenseName = getActivity().findViewById(R.id.add_expense_name);
        expenseAmount = getActivity().findViewById(R.id.add_expense_amount);
        expenseTypeSpinner = getActivity().findViewById(R.id.expenseTypeSpinner);
        textExpenseDate = getActivity().findViewById(R.id.textExpenseDate);
        expenseDateButton = getActivity().findViewById(R.id.btn_expenseDate);
        addExpenseButton = getActivity().findViewById(R.id.btn_add_expense);
        test = getActivity().findViewById(R.id.add_expense_test);

        expenseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.expense_types, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(staticAdapter);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense(v);
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
        textExpenseDate.setText(dateText);
    }

    public void addExpense(View v){
        ok = true;

        if(expenseName.getText().toString().isEmpty()){
            expenseName.setError("Enter expense name!");
            ok = false;
        }
        if(expenseAmount.getText().toString().isEmpty()){
            expenseAmount.setError("Enter expense amount!");
            ok = false;
        }

        if(ok){
            Expense expense = new Expense();
            expense.setExpenseName(expenseName.getText().toString());
            expense.setExpenseAmount(Double.parseDouble(expenseAmount.getText().toString()));
            expense.setExpenseType(expenseTypeSpinner.getSelectedItem().toString());
            expense.setEntryDate(date);
            Long userId = getActivity().getIntent().getLongExtra("userId",0);
            expense.setUserIdentifier(userId);

            expenseName.setText("");
            expenseAmount.setText("");
            expenseTypeSpinner.setSelection(0);
            textExpenseDate.setText("");

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

            Call<Expense> call = jsonPlaceholderApi.createExpense(expense);
            call.enqueue(new Callback<Expense>() {
                @Override
                public void onResponse(Call<Expense> call, Response<Expense> response) {
                    if (!response.isSuccessful()) {
                        test.setText("Code: " + response.code());
                        return;
                    }

                    Expense expenseResponse = response.body();

                    test.append("Code: " + response.code() + "\n");
                    test.append("amount: " + expenseResponse.getExpenseAmount() + "\n");
                    test.append("userId: " + expenseResponse.getUserIdentifier() + "\n\n ");

                }

                @Override
                public void onFailure(Call<Expense> call, Throwable t) {
                    test.setText(t.getMessage());
                }
            });

            Toast.makeText(getActivity(), "Expense added successfully!", Toast.LENGTH_LONG).show();
        }
    }
}
