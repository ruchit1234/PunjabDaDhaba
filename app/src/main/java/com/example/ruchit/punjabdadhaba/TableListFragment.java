package com.example.ruchit.punjabdadhaba;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link TableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment{

    private FirebaseFirestore db;
    private ProgressDialog dialog;
    FirebaseFirestore cloudstorage = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    List<String> ids = new ArrayList<>();
    int count;
    public static String tableno = null;
    FloatingActionButton addt;
    //ImageView filter;
    private StaggeredGridLayoutManager _sGridLayoutManager;

    public static TableListFragment newInstance() {
        TableListFragment fragment = new TableListFragment();
        return fragment;
    }
    public TableListFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablelist, container, false);
        recyclerView = view.findViewById(R.id.rectab);
        addt=view.findViewById(R.id.addtab);
        addt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s="Table"+" "+count;

                Map<String, Object> docData = new HashMap<>();
                docData.put("Instructions", Arrays.asList());
                docData.put("Items", Arrays.asList());
                docData.put("Quantity", Arrays.asList());

                db.collection("Order").document(s).set(docData);
            }
        });
       // dialog = new ProgressDialog(getContext());
        //dialog.setMessage("please wait.");
        //dialog.show();
        /*cloudstorage.collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                         for (QueryDocumentSnapshot document : task.getResult()) {
                       ids.add(document.getId());
                             dialog.dismiss();
                             ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),);

                    }
                }
                else {
                    Toast.makeText(getContext(),"Error Getting Document",Toast.LENGTH_LONG).show();

                }

            }

        });*/
        init();
        getFriendList();
        return view;
    }
    private void init(){
        _sGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList(){
        Query query = db.collection("Order");

        FirestoreRecyclerOptions<FriendsResponse> response = new FirestoreRecyclerOptions.Builder<FriendsResponse>()
                .setQuery(query, FriendsResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<FriendsResponse, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, FriendsResponse model) {
               /* for (int i=0;i < ids.size();i++)
                {
                    holder.exname.setText(ids.get(i));
                }*/
//Toast.makeText(getContext(),String.valueOf(getItemCount()),Toast.LENGTH_LONG).show();
               count=getItemCount()+1;
                    int a = position+1;
               // String id = getSnapshots().getSnapshot(position).getId();
                holder.exname.setText("Table NO:"+" "+a);
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment;

            fragment = new OrderItemFragment();
            fm.beginTransaction().replace(R.id.main_frame, fragment).commit();
tableno="Table"+" "+a;
    }
});
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    public class FriendsHolder extends RecyclerView.ViewHolder {

        TextView exname;
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-ExtraBoldItalic.ttf");

        public FriendsHolder(View itemView) {
            super(itemView);
            exname= itemView.findViewById(R.id.topicname);
            exname.setTypeface(custom_font);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
