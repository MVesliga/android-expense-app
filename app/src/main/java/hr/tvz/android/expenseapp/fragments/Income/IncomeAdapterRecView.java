package hr.tvz.android.expenseapp.fragments.Income;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import hr.tvz.android.expenseapp.model.Income;
import hr.tvz.android.mvpexample.R;

public class IncomeAdapterRecView extends RecyclerView.Adapter<IncomeAdapterRecView.IncomeViewHolder>{
    private ArrayList<Income> mIncomeList;
    private IncomeAdapterRecView.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(IncomeAdapterRecView.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        public TextView incomeNumber;
        public TextView incomeName;
        public TextView incomeEntryDate;

        public IncomeViewHolder(@NonNull View itemView, final IncomeAdapterRecView.OnItemClickListener listener) {
            super(itemView);
            incomeNumber = itemView.findViewById(R.id.view_income_number);
            incomeName = itemView.findViewById(R.id.view_income_name);
            incomeEntryDate = itemView.findViewById(R.id.view_income_date);

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

    public IncomeAdapterRecView(ArrayList<Income> incomeList) {
        mIncomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeAdapterRecView.IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item_view, parent, false);
        IncomeAdapterRecView.IncomeViewHolder ivh = new IncomeAdapterRecView.IncomeViewHolder(v, mListener);

        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdapterRecView.IncomeViewHolder incomeViewHolder, int position) {
        Income income = mIncomeList.get(position);

        incomeViewHolder.incomeNumber.setText(Integer.toString(position + 1 ));
        incomeViewHolder.incomeName.setText(income.getIncomeName());
        incomeViewHolder.incomeEntryDate.setText(income.getEntryDate());
    }

    @Override
    public int getItemCount() {
        return mIncomeList.size();
    }
}
