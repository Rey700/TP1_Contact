package com.example.contact;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ContactPagerAdapter extends FragmentStateAdapter {
    private final Carnet carnet;

    public ContactPagerAdapter(@NonNull FragmentActivity fa, Carnet carnet) {
        super(fa);
        this.carnet = carnet;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Contact contact = carnet.getContacts().get(position);
        return ContactFragment.newInstance(contact.getNum());
    }

    @Override
    public int getItemCount() {
        return carnet.getContacts().size();
    }
}