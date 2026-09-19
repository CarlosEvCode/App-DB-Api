package com.example.applistas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BuscadorPersonaje extends AppCompatActivity {
    RequestQueue requestQueue;
    final String URL ="https://dragonball-api.com/api/characters/";
    EditText edtIdPersonaje, edtNombre, edtKi, edtRaza, edtGenero;
    Button btnBuscaPersonaje;

    private void loadUI(){
        edtIdPersonaje = findViewById(R.id.edtIdPersonaje);
        edtNombre = findViewById(R.id.edtNombre);
        edtKi = findViewById(R.id.edtKi);
        edtRaza = findViewById(R.id.edtRaza);
        edtGenero = findViewById(R.id.edtGenero);

        btnBuscaPersonaje = findViewById(R.id.btnBuscarPersonaje);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscador_personaje);

        this.loadUI();

        //Eventos
        btnBuscaPersonaje.setOnClickListener(v -> {getDataCharacter();});
    }//OnCreate

    private void getDataCharacter(){
        //Comunicacion Dragon Ball API
        if (edtIdPersonaje.getText().toString().isEmpty()) {
            edtIdPersonaje.setError("Escriba un ID");
            edtIdPersonaje.requestFocus();
            return;
        }

        String endPoint = URL + edtIdPersonaje.getText().toString(); //Se agrega el id
        //Abrir canal de comunicacion
        requestQueue = Volley.newRequestQueue(this);

        //¿Que tipo de dato me devuelve el API?
        //Volley las solicitudes tienen 5 partes:
        //Verbo, URL, JSONEnviado, Resultado, Error
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                endPoint,
                null,
                this::showData,
                this::errowWS

        );

        //Enviamos la solicitud
        requestQueue.add(jsonObjectRequest);
    }

    //this::showData() se activa cando el servicio retorna 2XX
    private void showData(JSONObject jsonObject) {
        Log.d("ResultadoWS", jsonObject.toString());

        //Java puede gestionar Json solo en entornos seguros
        try {
            edtNombre.setText(jsonObject.getString("name"));
            edtKi.setText(jsonObject.getString("ki"));
            edtRaza.setText(jsonObject.getString("race"));
            edtGenero.setText(jsonObject.getString("gender"));
        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }
    }

    //this.errowWS() se activa con respuestas 4XX
    private void errowWS(VolleyError e) {

        //Log.e("ErrorWS", e.toString());

        //Para gestionar errores, necesitamos de un objeto
        NetworkResponse response = e.networkResponse;

        //Si existe una respuest ( existe un error)
        if (response != null && response.data != null) {
            //¿Cual es el codigo de error?
            int statusCode = response.statusCode;

            //No lo encontramos
            if (statusCode == 400){
                String dataError = new String(response.data);
                try {
                    JSONObject jsonError = new JSONObject(dataError);
                    Toast.makeText(getApplicationContext(),jsonError.getString("message"), Toast.LENGTH_LONG);
                    Log.e("ErrorWS", dataError);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }

            }
            //Log.e("ErrorWS",String.valueOf(statusCode));
        }
    }


} //BuscadorPersonaje