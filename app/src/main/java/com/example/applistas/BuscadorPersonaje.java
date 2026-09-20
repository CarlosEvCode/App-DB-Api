package com.example.applistas;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BuscadorPersonaje extends AppCompatActivity {
    RequestQueue requestQueue;
    final String URL ="https://dragonball-api.com/api/characters/";
    EditText edtIdPersonaje, edtNombre, edtKi, edtRaza, edtGenero;
    ImageView imgPersonaje;
    Button btnBuscaPersonaje;

    private void loadUI(){
        edtIdPersonaje = findViewById(R.id.edtIdPersonaje);
        edtNombre = findViewById(R.id.edtNombre);
        edtKi = findViewById(R.id.edtKi);
        edtRaza = findViewById(R.id.edtRaza);
        edtGenero = findViewById(R.id.edtGenero);
        imgPersonaje = findViewById(R.id.imgPersonaje);

        btnBuscaPersonaje = findViewById(R.id.btnBuscarPersonaje);
    }

    private void resetUI(){
        edtNombre.setText("");
        edtKi.setText("");
        edtRaza.setText("");
        edtGenero.setText("");
        imgPersonaje.setImageDrawable(null);
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

        //Reiniciar cajas antes de la nueva busqueda
        resetUI();

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

            //Cargar imagen del personaje
            String urlImagen = jsonObject.getString("image");
            cargarImagen(urlImagen);
        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }
    }

    private void cargarImagen(String urlImagen){
        ImageRequest imageRequest = new ImageRequest(
                urlImagen,
                bitmap -> imgPersonaje.setImageBitmap(bitmap),
                0,
                0,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.RGB_565,
                error -> Log.e("ErrorImagen", error.toString())
        );
        requestQueue.add(imageRequest);
    }

    //this.errowWS() se activa con respuestas 4XX
    private void errowWS(VolleyError e) {

        //Log.e("ErrorWS", e.toString());

        //Limpiar interfaz si ocurrio un error
        resetUI();

        //Para gestionar errores, necesitamos de un objeto
        NetworkResponse response = e.networkResponse;

        //Si existe una respuest ( existe un error)
        if (response != null && response.data != null) {
            //¿Cual es el codigo de error?
            int statusCode = response.statusCode;

            //No lo encontramos
            if (statusCode == 400 || statusCode == 404){
                String dataError = new String(response.data);
                try {
                    JSONObject jsonError = new JSONObject(dataError);
                    String mensaje = jsonError.optString("message", "Personaje no encontrado");
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                    Log.e("ErrorWS", dataError);
                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Personaje no encontrado", Toast.LENGTH_LONG).show();
                    Log.e("ErrorWS", ex.toString());
                }

            }
            //Log.e("ErrorWS",String.valueOf(statusCode));
        } else {
            Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }


} //BuscadorPersonaje