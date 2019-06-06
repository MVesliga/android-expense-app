package hr.tvz.android.expenseapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable{

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;


    public User(){}

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public int describeContents() {
        return username.hashCode() + email.hashCode();
    }

    public User(Parcel source){
        List<String> lista = new ArrayList<>();
        if(lista != null && lista.size() == 6){
            this.id = Long.parseLong(lista.get(0));
            this.firstName = lista.get(1);
            this.lastName = lista.get(2);
            this.email = lista.get(3);
            this.username = lista.get(4);
            this.password = lista.get(5);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        List<String> lista = new ArrayList<>();

        lista.add(String.valueOf(id));
        lista.add(firstName);
        lista.add(lastName);
        lista.add(email);
        lista.add(username);
        lista.add(password);

        dest.writeStringList(lista);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
