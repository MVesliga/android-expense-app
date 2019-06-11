package hr.tvz.android.expenseapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Balance implements Parcelable {
    private int month;
    private Double sum;

    public Balance(){}

    public Balance(int month, Double sum){
        this.month = month;
        this.sum = sum;
    }

    public Balance(Parcel source){
        List<String> lista = new ArrayList<>();
        if(lista != null && lista.size() == 2){
            this.month = Integer.parseInt(lista.get(0));
            this.sum = Double.parseDouble(lista.get(1));
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    @Override
    public int describeContents() {
        return month + sum.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> lista = new ArrayList<>();

        lista.add(String.valueOf(month));
        lista.add(String.valueOf(sum));

        dest.writeStringList(lista);
    }

    public static final Parcelable.Creator<Balance> CREATOR = new Parcelable.Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel source) {
            return new Balance(source);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
        }
    };
}
