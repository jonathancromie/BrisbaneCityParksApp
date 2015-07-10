package com.jonathancromie.brisbanecityparks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterEmailActivity extends ActionBarActivity implements View.OnClickListener {

    EditText textEmail;
    EditText textPassword;

    Button buttonCreateAccount;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);

        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        buttonCreateAccount.setOnClickListener(this);

        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // refactor
            case R.id.buttonCreateAccount:
                Intent intentRegister = new Intent(RegisterEmailActivity.this, RegisterActivity.class);
                intentRegister.putExtra("email", textEmail.getText().toString());
                intentRegister.putExtra("password", textPassword.getText().toString());
                startActivity(intentRegister);
                break;
            case R.id.login:
                Intent intentLogin = new Intent(RegisterEmailActivity.this, LoginActivity.class);
                startActivity(intentLogin);
        }
    }
}
