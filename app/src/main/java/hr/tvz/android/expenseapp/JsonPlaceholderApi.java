package hr.tvz.android.expenseapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import hr.tvz.android.expenseapp.model.Balance;
import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.expenseapp.model.Income;
import hr.tvz.android.expenseapp.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceholderApi {

    @GET("users")
    Call<List<User>> getUsers();

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("expenses/{userIdentifier}")
    Call<ArrayList<Expense>> getExpenses(@Path("userIdentifier") Long userIdentifier);

    @POST("expenses")
    Call<Expense> createExpense(@Body Expense expense);

    @DELETE("expenses/{expenseId}")
    Call<Void> deleteExpense(@Path("expenseId") Long expenseId);

    @GET("incomes/{userIdentifier}")
    Call<ArrayList<Income>> geTIncomes(@Path("userIdentifier") Long userIdentifier);

    @POST("incomes")
    Call<Income> createIncome(@Body Income income);

    @DELETE("incomes/{incomeId}")
    Call<Void> deleteIncome(@Path("incomeId") Long incomeId);

    @GET("expenses/balance/{yearOfEntry}")
    Call<ArrayList<Balance>> listByExpenseMonth(@Path("yearOfEntry") int yearOfEntry);


}
