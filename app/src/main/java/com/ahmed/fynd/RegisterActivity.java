package com.ahmed.fynd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.ahmed.fynd.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding b;
    FyndDatabase db;
    String birthdate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        db = new FyndDatabase(this);

        b.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = b.nameValueRegister.getText().toString();
                String password = b.passwordValueRegister.getText().toString();
                String email = b.emailValueRegister.getText().toString();
                String recovery_question = b.passwordRecoveryQuestionValue.getText().toString();
                String recovery_answer = b.passwordRecoveryAnswerValue.getText().toString();
                String admin = b.radioAdmin.isChecked()?"y":"n";

                User u = new User(name,email,password,birthdate,recovery_question,recovery_answer,admin);

                if(name.isEmpty()||password.isEmpty()||email.isEmpty()||birthdate.isEmpty()||recovery_question.isEmpty()||recovery_answer.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "one or more fields are empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    //db.register(user);
                    Boolean success = db.register_a_user(u);

                    if(!success) {
                        Toast.makeText(getApplicationContext(), "email was not created, try again!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //if success ->
                        db.set_current_user(new CurrentUser(u.getEmail(),"n"));
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

        b.addBirthdateRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });
    }

    private void showCalender(){

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.birthday_calender_popup);

        CalendarView c = d.findViewById(R.id.calendar);
        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                birthdate = year+","+month+","+dayOfMonth;
            }
        });
        d.show();
    }
}