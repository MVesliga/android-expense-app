package hr.tvz.android.expenseapp.fragments.Income;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.fragments.Expense.ExpenseAdapterRecView;
import hr.tvz.android.expenseapp.fragments.Expense.ExpenseDetailsFragment;
import hr.tvz.android.expenseapp.model.Income;
import hr.tvz.android.mvpexample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IncomeListFragment extends Fragment {

    private RecyclerView incomeRecyclerView;
    private IncomeAdapterRecView incomeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewResult;
    private TextView textEmptyMessage;
    private ArrayList<Income> incomeArrayList;
    private JsonPlaceholderApi jsonPlaceholderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewResult = getView().findViewById(R.id.income_apiText);
        textEmptyMessage = getView().findViewById(R.id.income_empty_message);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        getIncomes();
    }

    private void getIncomes() {
        Long userId = getActivity().getIntent().getLongExtra("userId", 0);
        Call<ArrayList<Income>> call = jsonPlaceholderApi.geTIncomes(userId);

        call.enqueue(new Callback<ArrayList<Income>>() {
            @Override
            public void onResponse(Call<ArrayList<Income>> call, Response<ArrayList<Income>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                incomeArrayList = response.body();

                if(incomeArrayList.size() != 0){
                    textEmptyMessage.setVisibility(View.GONE);
                }

                incomeRecyclerView = getView().findViewById(R.id.income_recycler_view);
                incomeRecyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity());
                incomeAdapter = new IncomeAdapterRecView(incomeArrayList);

                incomeRecyclerView.setLayoutManager(layoutManager);
                incomeRecyclerView.setAdapter(incomeAdapter);

                incomeAdapter.setOnItemClickListener(new IncomeAdapterRecView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        IncomeDetailsFragment incomeDetailsFragment = new IncomeDetailsFragment();
                        Bundle bundle = new Bundle();

                        Toast.makeText(getActivity(), "Expense clicked!", Toast.LENGTH_SHORT).show();

                        bundle.putParcelable("income", incomeArrayList.get(position));
                        incomeDetailsFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.fragment_container, incomeDetailsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<Income>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
