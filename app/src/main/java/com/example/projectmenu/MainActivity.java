package com.example.projectmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectmenu.DAOs.UserDAO;
import com.example.projectmenu.Entities.User;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.deleteDatabase(DBDefinition.DATADABE_NAME);
        final DataBaseManager db = new DataBaseManager(this);
        User nu = new User();
        nu.setID(1001);
        nu.setName("Rafael Olivares");
        nu.setPassword("1234");
        nu.setEmail("riolivaresv@gmail.com");
        long res = new UserDAO(db).onInsert(nu);
        Toast toast = Toast.makeText(this, ""+res+"", Toast.LENGTH_SHORT);
        toast.show();

        Button b = (Button) findViewById(R.id.btnEnter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText userEmaail = (EditText) findViewById(R.id.txtUser);
                    EditText password = (EditText) findViewById(R.id.txtPassword);

                    User resutl = new UserDAO(db).onSelectUser(userEmaail.getText().toString());

                    if (resutl == null){
                        Snackbar make = Snackbar.make(v, "Email or password is incorrect, please try with another.", Snackbar.LENGTH_LONG);
                        make.setAction("Action", null).show();
//                        Context context = getApplicationContext();
//                        Toast toast = Toast.makeText(context, "Email or password is incorrect, please try with another.", Toast.LENGTH_SHORT);
//                        toast.show();
                    } else {
                        Intent i =new Intent(MainActivity.this,Home.class );
                        startActivity(i);
                    }
                } catch (Exception ex){
                    Snackbar make = Snackbar.make(v, ex.getMessage(), Snackbar.LENGTH_LONG);
                    make.setAction("Action", null).show();
//                    Context context = getApplicationContext();
//                    Toast toast = Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT);
//                    toast.show();
                }

            }
        });
    }
}
