package fr.m2i.webservicesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


// A savoir dans le langage json:
// entre { } c'est un objet
// entre [] c'est une liste

public class MainActivity extends AppCompatActivity {

    Button btCode, btTout;
    EditText etCode, etPays;
    ListView lvTout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btCode = findViewById(R.id.btCode);
        btTout = findViewById(R.id.btTout);
        etCode = findViewById(R.id.etCode);
        etPays = findViewById(R.id.etPays);
        lvTout = findViewById(R.id.lvPays);
    }

    public void btCode(View v) {

        String code = etCode.getText().toString();
        JSONObject json = null;
        MonThread th = new MonThread();
        th.setAdress("http://demo@services.groupkt.com/country/get/iso2code/" + code);
        th.start(); // permet de démarrer le traitement dans le thread séparé

        try {
            th.join(); // attend la fin du traitment du Thread avant l'affichage
        } catch (InterruptedException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            json = new JSONObject(th.getRes());
            JSONObject dept = json.getJSONObject("RestResponse"); //creation du premeier objet
            JSONObject result = dept.getJSONObject("result");//creation du deuxieme objet contenu dans le premier
            String nom = result.getString("name"); // on récupère le paramètre "name" pour ensuite le faire afficher
            etPays.setText(nom);

        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void btTout(View v) {

        JSONObject json = null;
        MonThread th = new MonThread();
        th.setAdress("http://services.groupkt.com/country/get/all");
        th.start();

        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            json = new JSONObject(th.getRes());
            JSONObject restReponse = json.getJSONObject("RestResponse"); // creation du premier objet restReponse qui est définit sur la page web à l'URL transmise
            JSONArray result = restReponse.getJSONArray("result"); // création de la liste result qui se trouve dans l'objet restReponse

            ArrayList<String> nomListe = new ArrayList<String>(); // création d'une liste pour remplir le nombre de pays trouvé

            //permet de parcourrir l'ensemble de la liste pour récupérer le nom du pays
            for (int i = 0; i < result.length(); i++) {

                JSONObject listeObjet = result.getJSONObject(i);
                nomListe.add(listeObjet.getString("name"));
            }
            // permet de faire le lien entre la liste et le xml pour les ranger au niveau de l'affichage
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nomListe);
            lvTout.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}





