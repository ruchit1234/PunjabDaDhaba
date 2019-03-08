package com.example.ruchit.punjabdadhaba;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * Use the {@link OrderItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderItemFragment extends Fragment {

    Context context;
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_CAMERA = 100;
    TextView name,counter,inst;
    EditText etinst;
    ImageView add,del;
    ImageView img;
    Button confirm;
    ImageView filter;
    int i=0;
   // private List<Ex> exlist;
    RecyclerView recyclerView;
     FirebaseFirestore db;
     FirestoreRecyclerAdapter adapter;
    private StorageReference mStorageRef;
    //private DatabaseReference mDatabase;
    ArrayList<String> list = new ArrayList<String>();
       int [] a;
       String s=null;
    StorageReference storageRef;
    String docname;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StaggeredGridLayoutManager _sGridLayoutManager;

    public static OrderItemFragment newInstance() {
        OrderItemFragment fragment = new OrderItemFragment();
        return fragment;
    }
    public OrderItemFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        ImageView fil=getActivity().findViewById(R.id.mfilter);
        fil.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderitem, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recorder);
          //  _sGridLayoutManager = new StaggeredGridLayoutManager(2,
            //        StaggeredGridLayoutManager.VERTICAL);
            //recyclerView.setLayoutManager(_sGridLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mStorageRef = FirebaseStorage.getInstance().getReference("images/");
        filter=getActivity().findViewById(R.id.mfilter);
        filter.setVisibility(View.VISIBLE);
            TextView tv=getActivity().findViewById(R.id.title);
            db = FirebaseFirestore.getInstance();
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS_NAME", 0);
        if(settings.contains("name")){
            s=settings.getString("name","");
           // Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            initfilter();
            tv.setText(s);
        }
        else {
            tv.setText("All Items");
            initializeData();
        }
       //initializeAdapter();
        db=FirebaseFirestore.getInstance();
        FloatingActionButton fb=view.findViewById(R.id.additem);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edtnm = (EditText) dialogView.findViewById(R.id.etname);
                final EditText edtpr = (EditText) dialogView.findViewById(R.id.etprice);
                Spinner sp=dialogView.findViewById(R.id.spcat);

                dialogBuilder.setTitle("New Item");
                dialogBuilder.setMessage("Enter Item Details");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();
                        String sel=sp.getSelectedItem().toString();
                        String nm=edtnm.getText().toString();
                        String pr=edtpr.getText().toString();
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("price", pr);
                        docData.put("imageurl", "");
                        //docData.put("Quantity", Arrays.asList());
                        db.collection(sel).document(nm).set(docData);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
              /*  Map<String, Object> docData = new HashMap<>();
                docData.put("Price", Arrays.asList());
                docData.put("Items", Arrays.asList());
                docData.put("Quantity", Arrays.asList());

                db.collection("Items").document(s).set(docData);
            */
            }
        });
return view;
    }
       private void initializeData()
    {
      //Query query = null;
        //if (s==null) {
            db = FirebaseFirestore.getInstance();
            db = FirebaseFirestore.getInstance();
            Query  query = db.collection("Items");
        s="Items";
        //}
        //else {
            //db = FirebaseFirestore.getInstance();
            //query = db.collection(s);
        //}

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

                // String id = getSnapshots().getSnapshot(position).getId();
                String id = getSnapshots().getSnapshot(position).getId();
                holder.exname.setText(id);
                db.collection("Items").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String url= (String) document.getData().get("imageurl");
                        // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                        Glide.with(getContext()).load(url).into(holder.imgmenu);
                    }
                });
                holder.itemView.setOnClickListener(v -> {
                    docname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.menuname)).getText().toString();
                    db.collection("Items").document(docname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            String url= (String) document.getData().get("imageurl");
                            // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                           /* FragmentManager fm = getActivity().getSupportFragmentManager();
                            Fragment fragment;
                            fragment = new Itemdetails();
                            Bundle args = new Bundle();
                            args.putString("name", docname);
                            args.putString("url",url);
                            fragment.setArguments(args);
                            fm.beginTransaction().replace(R.id.main_frame, fragment).commit();
*/
                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.fragment_itemdet, null);
                            dialogBuilder.setView(dialogView);

                            name=dialogView.findViewById(R.id.itemname);
                            img=dialogView.findViewById(R.id.itemdetimg);
                            inst=dialogView.findViewById(R.id.tvinst);
                            etinst=dialogView.findViewById(R.id.etinst);
                            counter=dialogView.findViewById(R.id.counter);
                            add=dialogView.findViewById(R.id.add);
                            del=dialogView.findViewById(R.id.sub);
                            confirm=dialogView.findViewById(R.id.confirm);
                            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Poppins-ExtraBoldItalic.ttf");
                            name.setTypeface(custom_font);
                            final AlertDialog alertDialog = dialogBuilder.create();
                            add.setOnClickListener(v -> counter.setText(String.valueOf(i++)));
                            del.setOnClickListener(v -> counter.setText(String.valueOf(i--)));
                            name.setText(docname);
                            Glide.with(getContext()).load(url).into(img);
                            confirm.setOnClickListener(v->{
                                String qty=counter.getText().toString();
                                if (qty.equals("0")){
                                    Toast.makeText(getContext(),"Please Specify Quantity",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    db=FirebaseFirestore.getInstance();
                                    db.collection("Order").document(TableListFragment.tableno)
                                            .update(
                                                    "Items", FieldValue.arrayUnion(docname),
                                                    "Quantity", FieldValue.arrayUnion(qty)
                                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(),"Item Added",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("WhatdFuck:",e.toString());
                                        }
                                    });

                                }
                            });
                            alertDialog.setTitle("Order");
                            alertDialog.setMessage("Enter Order Details");
                            alertDialog.show();
                        }
                    });

                });

                holder.imgmenu.setOnClickListener(v -> {
                    //docname = holder.exname.getText(position).toString();
                    docname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.menuname)).getText().toString();
                    //Toast.makeText(getContext(),docname,Toast.LENGTH_LONG).show();
                    PickImageDialog.build(new PickSetup()).show(getActivity());
                    PickSetup setup = new PickSetup();
                    PickImageDialog.build(setup)
                            .setOnClick(new IPickClick() {
                                @Override
                                public void onGalleryClick() {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                }

                                @Override
                                public void onCameraClick() {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, REQUEST_CAMERA);

                                }
                            }).show(getActivity());

                });


            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.menu_item, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        //int spaceInPixels = 200;
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(adapter);

    }
    private void initfilter()
    {
        //Query query = null;
        //if (s==null) {
        db = FirebaseFirestore.getInstance();
        Query  query = db.collection(s);
        //}
        //else {
        //db = FirebaseFirestore.getInstance();
        //query = db.collection(s);
        //}

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

                // String id = getSnapshots().getSnapshot(position).getId();
                String id = getSnapshots().getSnapshot(position).getId();
                holder.exname.setText(id);
               // docname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.menuname)).getText().toString();
                db.collection(s).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String url= (String) document.getData().get("imageurl");
                       // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                        Glide.with(getContext()).load(url).into(holder.imgmenu);
                    }
                });
                holder.itemView.setOnClickListener(v -> {
                    docname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.menuname)).getText().toString();
                    db.collection(s).document(docname).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            String url= (String) document.getData().get("imageurl");
                            // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                           /* FragmentManager fm = getActivity().getSupportFragmentManager();
                            Fragment fragment;
                            fragment = new Itemdetails();
                            Bundle args = new Bundle();
                            args.putString("name", docname);
                            args.putString("url",url);
                            fragment.setArguments(args);
                            fm.beginTransaction().replace(R.id.main_frame, fragment).commit();
*/
                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.fragment_itemdet, null);
                            dialogBuilder.setView(dialogView);

                            name=dialogView.findViewById(R.id.itemname);
                            img=dialogView.findViewById(R.id.itemdetimg);
                            inst=dialogView.findViewById(R.id.tvinst);
                            etinst=dialogView.findViewById(R.id.etinst);
                            counter=dialogView.findViewById(R.id.counter);
                            add=dialogView.findViewById(R.id.add);
                            del=dialogView.findViewById(R.id.sub);
                            confirm=dialogView.findViewById(R.id.confirm);
                            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/futura.OTF");
                            name.setTypeface(custom_font);
                            final AlertDialog alertDialog = dialogBuilder.create();
                            add.setOnClickListener(v -> counter.setText(String.valueOf(i++)));
                            del.setOnClickListener(v -> counter.setText(String.valueOf(i--)));
                            name.setText(docname);
                            Glide.with(getContext()).load(url).into(img);
                            confirm.setOnClickListener(v->{
                                String qty=counter.getText().toString();
                                if (qty.equals("0")){
                                    Toast.makeText(getContext(),"Please Specify Quantity",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    db=FirebaseFirestore.getInstance();
                                    db.collection("Order").document(TableListFragment.tableno)
                                            .update(
                                                    "Items", FieldValue.arrayUnion(docname),
                                                    "Quantity", FieldValue.arrayUnion(qty)
                                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                      Toast.makeText(getContext(),"Item Added",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("WhatdFuck:",e.toString());
                                        }
                                    });

                                }
                            });
                            alertDialog.setTitle("Order");
                            alertDialog.setMessage("Enter Order Details");
                         alertDialog.show();
                        }
                    });

                });

 holder.imgmenu.setOnClickListener(v -> {
 //docname = holder.exname.getText(position).toString();
    docname = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.menuname)).getText().toString();
    //Toast.makeText(getContext(),docname,Toast.LENGTH_LONG).show();
    PickImageDialog.build(new PickSetup()).show(getActivity());
    PickSetup setup = new PickSetup();
    PickImageDialog.build(setup)
            .setOnClick(new IPickClick() {
                @Override
                public void onGalleryClick() {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }

                @Override
                public void onCameraClick() {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);

                }
            }).show(getActivity());

});
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.menu_item, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(adapter);


    }
    public class FriendsHolder extends RecyclerView.ViewHolder {

        TextView exname;//tvqty;
       CircleImageView imgmenu;
       Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/futura.OTF");

        public FriendsHolder(View itemView) {
            super(itemView);
            imgmenu=itemView.findViewById(R.id.imgmenu);
            exname= itemView.findViewById(R.id.menuname);
            exname.setTypeface(custom_font);
         //   add=itemView.findViewById(R.id.additem);
         //   remove=itemView.findViewById(R.id.removeitem);
         //   tvqty=itemView.findViewById(R.id.tvqty);

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

/*
    public class Ex {
        String name;
        int logoId;
        int count=0;

        Ex(String name, int logoId) {
            this.name = name;
            this.logoId = logoId;
        }
    }

*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            String fname = getFileName(selectedfile);
            java.io.File file = FileUtils.getFile(getActivity(),selectedfile);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(selectedfile));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] dat = baos.toByteArray();
            final UploadTask uploadTask = fileReference.putBytes(dat);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("whatTheFuck:",exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (!task.isSuccessful()) {
                            }

                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAu.getInstance().getCurrentUser().getUid());
                                String url= downloadUri.toString();
                                Log.i("seeThisUri", downloadUri.toString());// This is the one you should store
                                db.collection(s).document(docname)
                                        .update(
                                                "imageurl", url
                                        );

                             //   Upload upload = new Upload(editTextName.getText().toString().trim(),

                                     // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                              //  String uploadId = ref.push().getKey();
                                //ref.child(uploadId).setValue(upload);


                            } else {
                                Toast.makeText(getContext(),"Image Not Selected",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });



    }
        else if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap selectedfile = (Bitmap) extras.get("data");
            Uri imageuri=getImageUri(getActivity(),selectedfile);
            String fname = getFileName(imageuri);
            java.io.File file = FileUtils.getFile(getActivity(),imageuri);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageuri));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] dat = baos.toByteArray();
            final UploadTask uploadTask = fileReference.putBytes(dat);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("whatTheFuck:",exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (!task.isSuccessful()) {
                            }

                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAu.getInstance().getCurrentUser().getUid());
                                String url= downloadUri.toString();
                                Log.i("seeThisUri", downloadUri.toString());// This is the one you should store
                                db.collection(s).document(docname)
                                        .update(
                                                "imageurl", url
                                        );

                                //   Upload upload = new Upload(editTextName.getText().toString().trim(),

                                // Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                                //  String uploadId = ref.push().getKey();
                                //ref.child(uploadId).setValue(upload);


                            } else {
                                Toast.makeText(getContext(),"Image Not Selected",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });


        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
