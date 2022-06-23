package com.papb.todo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.papb.todo.data.model.user.ResponseLogin;
import com.papb.todo.data.state.SimpleState;
import com.papb.todo.databinding.ActivityLoginBinding;
import com.papb.todo.root.App;
import com.papb.todo.ui.main.MainActivity;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new LoginViewModel();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListener();

        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");

        binding.btnDaftar.setText(Html.fromHtml("Bulum punya akun? <b>Daftar</b>"));
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Silahkan email dan password", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.postLogin(email, password);
            }
        });
    }

    private void initListener() {
        viewModel.getState().observe(this, simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                spotsDialog.show();
            } else if (simpleState instanceof SimpleState.Result) {
                spotsDialog.dismiss();
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseLogin) {
                    ResponseLogin responseLogin = (ResponseLogin) ((SimpleState.Result<?>) simpleState).getData();
                    if (responseLogin.getStatus() != null && responseLogin.getStatus()) {
                        App.session.doLogin(responseLogin.getData());
                        Toast.makeText(this, "Berhasil Login", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal Login", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (simpleState instanceof SimpleState.Error) {
                spotsDialog.dismiss();
                Toast.makeText(this, "Gagal Login", Toast.LENGTH_LONG).show();
            }
        });
    }
}