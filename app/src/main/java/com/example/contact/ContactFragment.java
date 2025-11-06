package com.example.contact;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ContactFragment extends Fragment {

    private static final String ARG_NUM = "num";
    private OnAvatarChangeListener listener;

    public static ContactFragment newInstance(int num) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM, num);
        fragment.setArguments(args);
        return fragment;
    }

    private void move(int i, Contact contact, ImageView avatarView){
        int current_avatar = contact.getAvatarId() + i;
        int last = Carnet.getInstance().getAvatars().length - 1;
        if(current_avatar > last){
            current_avatar = 0;
        }
        else if (current_avatar < 0)
        {
            current_avatar = last;
        }
        contact.setAvatarId(current_avatar);
        avatarView.setImageResource(Carnet.getInstance().getAvatars()[contact.getAvatarId()]);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        int num = requireArguments().getInt(ARG_NUM);
        Contact contact = Carnet.getInstance().getContactByNum(num);

        if (contact != null) {
            ImageView avatarView = view.findViewById(R.id.imageViewAvatar);
            avatarView.setImageResource(Carnet.getInstance().getAvatars()[contact.getAvatarId()]);

            LinearLayout layout = view.findViewById(R.id.layoutChampsDynamiques);

            Button btnPrev = view.findViewById(R.id.buttonImagePrecedent);
            Button btnNext = view.findViewById(R.id.buttonImageSuivant);


            btnPrev.setOnClickListener(v -> {
                move(-1,contact,avatarView);
            });

            btnNext.setOnClickListener(v -> {
                move(1,contact,avatarView);
            });

            for (String champ : contact.getAttributs().keySet()) {
                if (!champ.equals("avatarId")) {
                    TextView label = new TextView(getContext());
                    label.setText(champ);
                    label.setTextSize(16f);

                    EditText field = new EditText(getContext());
                    field.setText(contact.getAttribut(champ));
                    field.setTextSize(15f);

                    layout.addView(label);
                    layout.addView(field);
                }
            }
            LinearLayout layoutChampPerso = new LinearLayout(getContext());
            layoutChampPerso.setOrientation(LinearLayout.HORIZONTAL);
            layoutChampPerso.setPadding(8, 8, 8, 8);

            Button btnChampPerso = new Button(getContext());
            EditText editTextPerso = new EditText(getContext());

            btnChampPerso.setText("Créer champ perso");
            editTextPerso.setHint("Nom du nouveau champ");

            layoutChampPerso.addView(editTextPerso);
            layoutChampPerso.addView(btnChampPerso);

            layout.addView(layoutChampPerso);

            btnChampPerso.setOnClickListener(v -> {
                String nomChamp = editTextPerso.getText().toString().trim();
                if (nomChamp.isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez entrer un nom de champ",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                contact.ajouterChampPersonalise(nomChamp);
                editTextPerso.setText("");
                contact.updateChamps();
                ((MainActivity) requireActivity()).refreshCurrentFragment();
                //gérer le rafraishissement de l'UI
            });
        }

        return view;
    }
}