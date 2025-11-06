package com.example.contact;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnAvatarChangeListener{
    private Carnet c;
    private String filePath;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ContactPagerAdapter adapter;
    private EditText numChercheField;
    private Button buttonDebut;
    private Button buttonSuivant;
    private Button buttonPrecedent;
    private Button buttonFin;
    private Button buttonMilieu;
    private Button buttonNumero;
    private Button buttonSupprimer;
    private Button buttonNouveau;
    private Button buttonSauvegarder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        c = Carnet.getInstance();
        filePath = getFilesDir() + "/contacts.txt";
        try {
            c.loadFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(c.getContacts().isEmpty()){
            c.newContact();
        }

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayoutContacts);
        adapter = new ContactPagerAdapter(this,c);
        viewPager.setAdapter(adapter);

        numChercheField = findViewById(R.id.editTextNumero);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    Contact contact = c.getContacts().get(position);
                    tab.setText("Contact " + contact.getNum());
                }
        ).attach();


        buttonDebut = findViewById(R.id.buttonDebut);
        buttonSuivant = findViewById(R.id.buttonSuivant);
        buttonPrecedent = findViewById(R.id.buttonPrecedent);
        buttonFin = findViewById(R.id.buttonFin);
        buttonMilieu = findViewById(R.id.buttonMilieu);
        buttonNumero = findViewById(R.id.buttonNumero);
        buttonSupprimer = findViewById(R.id.buttonSupprimer);
        buttonNouveau = findViewById(R.id.buttonNouveau);
        buttonSauvegarder = findViewById(R.id.buttonSauvegarder);

        buttonDebut.setOnClickListener(v -> viewPager.setCurrentItem(0, true));

        buttonFin.setOnClickListener(v ->
                viewPager.setCurrentItem(c.getContacts().size() - 1, true));

        buttonSuivant.setOnClickListener(v -> {
            move(1);
        });
        buttonPrecedent.setOnClickListener(v -> {
            move(-1);
        });


        buttonMilieu.setOnClickListener(v -> {
            int mid = c.getContacts().size() / 2;
            viewPager.setCurrentItem(mid, true);
        });

        buttonNumero.setOnClickListener(v -> {
            String input = numChercheField.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer un ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(input);
            int index = c.getIndexById(id);
            if (index != -1) {
                viewPager.setCurrentItem(index, true);
            } else {
                Toast.makeText(this, "Aucun contact avec l'ID " + id,
                        Toast.LENGTH_SHORT).show();
            }
        });

        buttonSupprimer.setOnClickListener(v -> {
            int pos = viewPager.getCurrentItem();
            c.removeCurrent();
            adapter.notifyDataSetChanged();
            int newPos = Math.max(0, pos - 1);
            viewPager.setCurrentItem(newPos, true);
            Toast.makeText(this, "Contact supprimé", Toast.LENGTH_SHORT).show();
        });

        buttonNouveau.setOnClickListener(v -> {
            c.newContact();
            adapter.notifyItemInserted(c.size() - 1);
            viewPager.setCurrentItem(c.size() - 1, true);
        });

        buttonSauvegarder.setOnClickListener(v ->{
            sauvegarderContact();
            Toast.makeText(this,"Sauvegarde effectuée",Toast.LENGTH_SHORT).show();
        });
    }

    public ContactPagerAdapter getAdapter() {
        return adapter;
    }
    public void refreshCurrentFragment() {
        int position = viewPager.getCurrentItem();
        ContactPagerAdapter adapter = getAdapter();
        adapter.notifyItemChanged(position);
    }
    private void move(int direction) {
        assert(direction == -1 || direction == 1);
        int current = viewPager.getCurrentItem();
        int total = adapter.getItemCount();

        int next = current + direction;

        if (next < 0) next = total - 1;
        else if (next >= total) next = 0;

        viewPager.setCurrentItem(next, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        c.saveToFile(filePath);
    }

    @Override
    public void onAvatarChange(int direction) {
        c.moveAvatar(direction);
        Toast.makeText(this,"bouton appuyé",Toast.LENGTH_SHORT).show();
    }

    public void sauvegarderContact() {
        Contact contact = c.getCurrent();

        LinearLayout layout = findViewById(R.id.layoutChampsDynamiques);
        int childCount = layout.getChildCount();

        for (int i = 0; i < childCount; i += 2) {
            TextView label = (TextView) layout.getChildAt(i);
            EditText field = (EditText) layout.getChildAt(i + 1);

            String cle = label.getText().toString().trim();
            String valeur = field.getText().toString().trim();

            contact.setAttribut(cle, valeur);
        }

        c.saveToFile(filePath);

        Toast.makeText(this, "Contact sauvegardé avec succès",
                Toast.LENGTH_SHORT).show();
    }
}