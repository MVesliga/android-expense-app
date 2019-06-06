package hr.tvz.android.expenseapp.fragments.Expense;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.mvpexample.R;

public class ExpenseAdapterRecView extends RecyclerView.Adapter<ExpenseAdapterRecView.ExpenseViewHolder> {

    private ArrayList<Expense> mExpenseList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public TextView expenseNumber;
        public TextView expenseName;
        public TextView expenseEntryDate;

        public ExpenseViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            expenseNumber = itemView.findViewById(R.id.view_expense_number);
            expenseName = itemView.findViewById(R.id.view_expense_name);
            expenseEntryDate = itemView.findViewById(R.id.view_expense_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExpenseAdapterRecView(ArrayList<Expense> expenseList) {
        mExpenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_view, parent, false);
        ExpenseViewHolder evh = new ExpenseViewHolder(v, mListener);

        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int position) {
        Expense expense = mExpenseList.get(position);

        expenseViewHolder.expenseNumber.setText(Integer.toString(position + 1 ));
        expenseViewHolder.expenseName.setText(expense.getExpenseName());
        expenseViewHolder.expenseEntryDate.setText(expense.getEntryDate());
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }
}
