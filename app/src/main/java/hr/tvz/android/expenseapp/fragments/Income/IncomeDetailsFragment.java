package hr.tvz.android.expenseapp.fragments.Income;

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
import hr.tvz.android.expenseapp.model.Income;
import hr.tvz.android.mvpexample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IncomeDetailsFragment extends Fragment {

    private Income income;
    private TextView detailsIncomeName;
    private TextView detailsIncomeAmount;
    private TextView detailsIncomeType;
    private TextView detailsIncomeEntryDate;
    private TextView detailsIncomeDeleted;
    private Button btnDeleteIncome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_income_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle =this.getArguments();
        if(bundle != null){
            income = bundle.getParcelable("income");

            detailsIncomeName = getView().findViewById(R.id.details_income_name);
            detailsIncomeAmount = getView().findViewById(R.id.details_income_amount);
            detailsIncomeType = getView().findViewById(R.id.details_income_type);
            detailsIncomeEntryDate = getView().findViewById(R.id.details_income_entry_date);
            detailsIncomeDeleted = getView().findViewById(R.id.details_income_delete_text);
            detailsIncomeDeleted.setVisibility(View.GONE);
            btnDeleteIncome = getView().findViewById(R.id.btn_income_delete);

            detailsIncomeName.setText("Income name: " + income.getIncomeName());
            detailsIncomeAmount.setText("Income amount: " + income.getAmount().toString() + " kn");
            detailsIncomeType.setText("Income type: " + income.getIncomeType());
            detailsIncomeEntryDate.setText("Entry date: " + income.getEntryDate());

            btnDeleteIncome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.income_dialog_message);
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

                        Call<Void> call = jsonPlaceholderApi.deleteIncome(income.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                detailsIncomeName.setText("");
                                detailsIncomeAmount.setText("");
                                detailsIncomeType.setText("");
                                detailsIncomeEntryDate.setText("");
                                btnDeleteIncome.setVisibility(View.GONE);
                                detailsIncomeDeleted.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                detailsIncomeDeleted.setVisibility(View.VISIBLE);
                                detailsIncomeDeleted.setText(t.getMessage());
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
