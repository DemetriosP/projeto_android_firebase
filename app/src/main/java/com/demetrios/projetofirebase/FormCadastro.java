package com.demetrios.projetofirebase;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_senha;
    private Button bt_cadsatrar;
    final String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        Objects.requireNonNull(getSupportActionBar()).hide();

        iniciarComponentes();

        bt_cadsatrar.setOnClickListener(view -> {

            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                CadastrarUsuario(view);
            }
        });
    }

    private void CadastrarUsuario(View view) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        SalvarDadosUsuario();
                        Snackbar snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else {
                        String erro;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Digite uma senha com no mínimo 6 caracteres";
                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = "Esta conta já foi cadastrada";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "E-mail inválido";
                        } catch (Exception e) {
                            erro = "Erro ao cadastrar usuário";
                        }

                        Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                });
    }

    private void SalvarDadosUsuario() {
        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference referencia = db.collection("Usuarios").document(usuarioID);

        referencia.set(usuarios).addOnSuccessListener(aVoid ->
                Log.d("db", "Sucesso ao salvar os dados")).addOnFailureListener
                (e -> Log.d("db_error", "Erro ao salvar os dados" + e));
    }

    private void iniciarComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadsatrar = findViewById(R.id.bt_cadastrar);
    }
}