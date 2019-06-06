package hr.tvz.android.expenseapp.fragments.Expense;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.mvpexample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpenseDetailsFragment extends Fragment {

    private Expense expense;
    private TextView detailsExpenseName;
    private TextView detailsExpenseAmount;
    private TextView detailsExpenseType;
    private TextView detailsExpenseEntryDate;
    private TextView detailsExpenseDeleted;
    private Button btnDeleteExpense;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_expense_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle =this.getArguments();
        if(bundle != null){
            expense = bundle.getParcelable("expense");

            detailsExpenseName = getView().findViewById(R.id.details_expense_name);
            detailsExpenseAmount = getView().findViewById(R.id.details_expense_amount);
            detailsExpenseType = getView().findViewById(R.id.details_expense_type);
            detailsExpenseEntryDate = getView().findViewById(R.id.details_expense_entry_date);
            detailsExpenseDeleted = getView().findViewById(R.id.details_expense_delete_text);
            detailsExpenseDeleted.setVisibility(View.GONE);
            btnDeleteExpense = getView().findViewById(R.id.btn_expense_delete);

            detailsExpenseName.setText("Expense name: " + expense.getExpenseName());
            detailsExpenseAmount.setText("Expense amount: " + expense.getExpenseAmount().toString() + " kn");
            detailsExpenseType.setText("Expense type: " + expense.getExpenseType());
            detailsExpenseEntryDate.setText("Entry date: " + expense.getEntryDate());

            btnDeleteExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.expense_dialog_message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:8080/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                       JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

                        Call<Void> call = jsonPlaceholderApi.deleteExpense(expense.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                detailsExpenseName.setText("");
                                detailsExpenseAmount.setText("");
                                detailsExpenseType.setText("");
                                detailsExpenseEntryDate.setText("");
                                btnDeleteExpense.setVisibility(View.GONE);
                                detailsExpenseDeleted.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                detailsExpenseDeleted.setVisibility(View.VISIBLE);
                                detailsExpenseDeleted.setText(t.getMessage());
                            }
                        });
                    }
                });
        builder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
