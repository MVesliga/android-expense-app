package hr.tvz.android.expenseapp.fragments.Expense;

import android.opengl.Visibility;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import hr.tvz.android.expenseapp.JsonPlaceholderApi;
import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.mvpexample.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpenseListFragment extends Fragment {

    private RecyclerView expenseRecyclerView;
    private ExpenseAdapterRecView expenseAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewResult;
    private TextView textEmptyMessage;
    private ArrayList<Expense> expenseArrayList;
    private JsonPlaceholderApi jsonPlaceholderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewResult = getView().findViewById(R.id.expense_apiText);
        textEmptyMessage = getView().findViewById(R.id.expense_empty_message);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        getExpenses();
    }

    private void getExpenses() {
        Long userId = getActivity().getIntent().getLongExtra("userId", 0);
        Call<ArrayList<Expense>> call = jsonPlaceholderApi.getExpenses(userId);

        call.enqueue(new Callback<ArrayList<Expense>>() {
            @Override
            public void onResponse(Call<ArrayList<Expense>> call, Response<ArrayList<Expense>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                expenseArrayList = response.body();

                if (expenseArrayList.size() != 0) {
                    textEmptyMessage.setVisibility(View.GONE);
                }

                expenseRecyclerView = getView().findViewById(R.id.expense_recycler_view);
                expenseRecyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity());
                expenseAdapter = new ExpenseAdapterRecView(expenseArrayList);

                expenseRecyclerView.setLayoutManager(layoutManager);
                expenseRecyclerView.setAdapter(expenseAdapter);

                expenseAdapter.setOnItemClickListener(new ExpenseAdapterRecView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ExpenseDetailsFragment expenseDetailsFragment = new ExpenseDetailsFragment();
                        Bundle bundle = new Bundle();

                        Toast.makeText(getActivity(), "Expense clicked!", Toast.LENGTH_SHORT).show();

                        bundle.putParcelable("expense", expenseArrayList.get(position));
                        expenseDetailsFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.fragment_container, expenseDetailsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<Expense>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
