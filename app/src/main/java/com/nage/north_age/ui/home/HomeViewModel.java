package com.nage.north_age.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nage.north_age.models.HomeButton;
import com.nage.north_age.models.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<SliderItem>> categoriesLiveData;
    private MutableLiveData<List<HomeButton>> homeButtonsLiveData;

    private FirebaseDatabase database;

    public HomeViewModel() {
        categoriesLiveData = new MutableLiveData<>();
        homeButtonsLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
    }

    public LiveData<List<SliderItem>> getCategories() {
        DatabaseReference myRef = database.getReference("slider");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<SliderItem> items = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        items.add(dataSnapshot.getValue(SliderItem.class));
                    }
                    categoriesLiveData.setValue(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoriesLiveData.setValue(null);
            }
        });
        return categoriesLiveData;
    }

    public LiveData<List<HomeButton>> getHomeButtons() {
        DatabaseReference myRef = database.getReference("buttons");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HomeButton> items = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    items.add(dataSnapshot.getValue(HomeButton.class));
                }
                homeButtonsLiveData.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                homeButtonsLiveData.setValue(null);
            }
        });
        return homeButtonsLiveData;
    }
}