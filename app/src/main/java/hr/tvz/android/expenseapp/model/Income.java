package hr.tvz.android.expenseapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Income implements Parcelable {
    private Long id;
    private String incomeName;
    private Double amount;
    private String incomeType;
    private String entryDate;
    private Long userIdentifier;

    public Income(){}

    public Income(String incomeName, Double amount, String incomeType, String entryDate, Long userIdentifier) {
        this.incomeName = incomeName;
        this.amount = amount;
        this.incomeType = incomeType;
        this.entryDate = entryDate;
        this.userIdentifier = userIdentifier;
    }

    public Income(Parcel source){
        List<String> lista = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd.mm.yyyy.");
        if(lista != null && lista.size() == 6){
            this.id = Long.parseLong(lista.get(0));
            this.incomeName = lista.get(1);
            this.amount = Double.parseDouble(lista.get(2));
            this.incomeType = lista.get(3);
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

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
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

    public void setUserIdentifier(Long userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    @Override
    public int describeContents() {
        return incomeName.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> lista = new ArrayList<>();

        lista.add(String.valueOf(id));
        lista.add(incomeName);
        lista.add(String.valueOf(amount));
        lista.add(incomeType);
        lista.add(entryDate);
        lista.add(String.valueOf(userIdentifier));

        dest.writeStringList(lista);
    }

    public static final Parcelable.Creator<Income> CREATOR = new Parcelable.Creator<Income>() {
        @Override
        public Income createFromParcel(Parcel source) {
            return new Income(source);
        }

        @Override
        public Income[] newArray(int size) {
            return new Income[size];
        }
    };
}
