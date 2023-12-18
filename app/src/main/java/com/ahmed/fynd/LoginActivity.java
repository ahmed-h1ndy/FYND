package com.ahmed.fynd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.fynd.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding b;
    FyndDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);

        CurrentUser c = db.get_current_user();
        if(c!=null) {
            if (c.getRemember().equals("y")) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        }
        //db.delete_all_databases();

        b.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = b.emailValueLogin.getText().toString();
                String password = b.passwordValueLogin.getText().toString();
                Boolean remember_me = b.rememberMeValue.isChecked();

                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"one or more fields is empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean success = db.login(email,password);

                    if(!success){
                        Toast.makeText(getApplicationContext(),"Login wasn't completed, try again!"+email,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        db.set_current_user(new CurrentUser(email,remember_me?"y":"n"));
                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

        b.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });

        b.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoveryQuestion();
            }
        });
    }

    private void showRecoveryQuestion(){
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.forgot_password_popup);

        Button recovery_button = d.findViewById(R.id.recover_button);
        TextView questionView = d.findViewById(R.id.question);
        EditText answerView = d.findViewById(R.id.answer);
        final User[] user = new User[1];

        recovery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recovery_button.getText().toString().charAt(0)=='R'){
                    // he answered the question
                    String answer = answerView.getText().toString();
                    if(answer.equals(user[0].getRecoveryAnswer())){
                        answerView.setText(user[0].getPassword());
                        questionView.setText("Your Password");
                        answerView.setEnabled(false);
                        recovery_button.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Wrong answer!",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                }
                else{
                    // he entered his email
                    recovery_button.setText("Recover Password");
                    String email = answerView.getText().toString();
                    user[0] = db.get_user(email);
                    answerView.setText("");
                    questionView.setText(user[0].getRecoveryQuestion());
                }
            }
        });
        d.show();
    }
}