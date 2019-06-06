package hr.tvz.android.expenseapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Expense implements Parcelable {
    private Long id;
    private String expenseName;
    private Double amount;
    private String expenseType;
    private String entryDate;
    private Long userIdentifier;

    public Expense(){}

    public Expense(String expenseName, Double expenseAmount, String expenseType, String entryDate, Long userId) {
        this.expenseName = expenseName;
        this.amount = expenseAmount;
        this.expenseType = expenseType;
        this.entryDate = entryDate;
        this.userIdentifier = userId;
    }

    public Expense(Parcel source){
        List<String> lista = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd.mm.yyyy.");
        if(lista != null && lista.size() == 6){
            this.id = Long.parseLong(lista.get(0));
            this.expenseName = lista.get(1);
            this.amount = Double.parseDouble(lista.get(2));
            this.expenseType = lista.get(3);
            this.entryDate = lista.get(4);
            this.userIdentifier = Long.parseLong(lista.get(5));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Double getExpenseAmount() {
        return amount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.amount = expenseAmount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Long getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(Long userId) {
        this.userIdentifier = userId;
    }

    @Override
    public int describeContents() {
        return expenseName.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> lista = new ArrayList<>();

        lista.add(String.valueOf(id));
        lista.add(expenseName);
        lista.add(String.valueOf(amount));
        lista.add(expenseType);
        lista.add(entryDate);
        lista.add(String.valueOf(userIdentifier));

        dest.writeStringList(lista);
    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel source) {
            return new Expense(source);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
