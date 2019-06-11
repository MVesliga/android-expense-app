package hr.tvz.android.expenseapp.fragments.Balance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.tvz.android.expenseapp.fragments.Expense.ExpenseAdapterRecView;
import hr.tvz.android.expenseapp.model.Balance;
import hr.tvz.android.expenseapp.model.Expense;
import hr.tvz.android.mvpexample.R;

public class BalanceAdapterRecView extends RecyclerView.Adapter<BalanceAdapterRecView.BalanceViewHolder>{

    private ArrayList<Balance> mBalanceList;
    private BalanceAdapterRecView.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(BalanceAdapterRecView.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class BalanceViewHolder extends RecyclerView.ViewHolder {
        public TextView balanceMonth;
        public TextView balanceAmount;

        public BalanceViewHolder(@NonNull View itemView, final BalanceAdapterRecView.OnItemClickListener listener) {
            super(itemView);
            balanceMonth = itemView.findViewById(R.id.balance_month);
            balanceAmount = itemView.findViewById(R.id.balance_amount);

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

    public BalanceAdapterRecView(ArrayList<Balance> balanceList) {
        mBalanceList = balanceList;
    }

    @NonNull
    @Override
    public BalanceAdapterRecView.BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_item_view, parent, false);
        BalanceAdapterRecView.BalanceViewHolder bvh = new BalanceAdapterRecView.BalanceViewHolder(v, mListener);

        return bvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceAdapterRecView.BalanceViewHolder balanceViewHolder, int position) {
        Balance balance = mBalanceList.get(position);

        if(balance.getMonth() == 1){
            balanceViewHolder.balanceMonth.setText(R.string.month_january);
        }
        else if(balance.getMonth() == 2){
            balanceViewHolder.balanceMonth.setText(R.string.month_february);
        }
        else if(balance.getMonth() == 3){
            balanceViewHolder.balanceMonth.setText(R.string.month_march);
        }
        else if(balance.getMonth() == 4){
            balanceViewHolder.balanceMonth.setText(R.string.month_april);
        }
        else if(balance.getMonth() == 5){
            balanceViewHolder.balanceMonth.setText(R.string.month_may);
        }
        else if(balance.getMonth() == 6){
            balanceViewHolder.balanceMonth.setText(R.string.month_june);
        }
        else if(balance.getMonth() == 7){
            balanceViewHolder.balanceMonth.setText(R.string.month_july);
        }
        else if(balance.getMonth() == 8){
            balanceViewHolder.balanceMonth.setText(R.string.month_august);
        }
        else if(balance.getMonth() == 9){
            balanceViewHolder.balanceMonth.setText(R.string.month_september);
        }
        else if(balance.getMonth() == 10){
            balanceViewHolder.balanceMonth.setText(R.string.month_october);
        }
        else if(balance.getMonth() == 11){
            balanceViewHolder.balanceMonth.setText(R.string.month_november);
        }
        else if(balance.getMonth() == 12){
            balanceViewHolder.balanceMonth.setText(R.string.month_december);
        }

        balanceViewHolder.balanceAmount.setText("Amount: " + balance.getSum().toString() + " kn.");

    }

    @Override
    public int getItemCount() {
        return mBalanceList.size();
    }
}
