package com.example.ruchit.punjabdadhaba;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link Itemdetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Itemdetails extends Fragment{

    FirebaseFirestore db;
    TextView name,counter;
    ImageView add,del;
    ImageView img;
    Button confirm;
    int i=0;

    public static Itemdetails newInstance() {
        Itemdetails fragment = new Itemdetails();
        return fragment;
    }
    public Itemdetails() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemdet, container, false);
        name=view.findViewById(R.id.itemname);
        img=view.findViewById(R.id.itemdetimg);
        counter=view.findViewById(R.id.counter);
        add=view.findViewById(R.id.add);
        del=view.findViewById(R.id.sub);
        confirm=view.findViewById(R.id.confirm);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-ExtraBoldItalic.ttf");
        name.setTypeface(custom_font);
        add.setOnClickListener(v -> counter.setText(String.valueOf(i++)));
        del.setOnClickListener(v -> counter.setText(String.valueOf(i--)));
        String url = getArguments().getString("url");
        String item = getArguments().getString("name");
        name.setText(item);
        Glide.with(getContext()).load(url).into(img);
        confirm.setOnClickListener(v -> {
            String qty=counter.getText().toString();
            if (qty.equals("0")){
                Toast.makeText(getContext(),"Please Specify Quantity",Toast.LENGTH_LONG).show();
            }
            else {
                db=FirebaseFirestore.getInstance();
                db.collection("Order").document(TableListFragment.tableno)
                        .update(
                                "Items", FieldValue.arrayUnion("ABCD"),
                                "Quantity", FieldValue.arrayUnion("34")
                        ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Item Added",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                   Log.i("WhatdFuck:",e.toString());
                    }
                });

            }
            });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
