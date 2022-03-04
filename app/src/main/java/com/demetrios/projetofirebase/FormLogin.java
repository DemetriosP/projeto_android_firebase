package com.demetrios.projetofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        iniciarComponentes();

        text_tela_cadastro.setOnClickListener(view -> {
            Intent intent = new Intent(FormLogin.this, FormCadastro.class);
            startActivity(intent);
        });
    }

    private void iniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);

    }

}