package com.kuldeep.serweservernew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kuldeep.serweservernew.Common.Common;
import com.kuldeep.serweservernew.Interface.ItemClickListener;
import com.kuldeep.serweservernew.Model.Request;
import com.kuldeep.serweservernew.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder>adapter;

    FirebaseDatabase db;
    DatabaseReference requests;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");


        recyclerView = findViewById(R.id.listOrders);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders()
    {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,R.layout.order_layout,OrderViewHolder.class,requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {

                orderViewHolder.txtOrderId.setText("Order ID: "+adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText("Order Status: "+request.getStatus());
                orderViewHolder.txtOrAddress.setText("Order Address: "+request.getAddress());
                if(request.getPhone() == null)
                {
                    orderViewHolder.txtOrderPhone.setText("No Phone Number");
                    orderViewHolder.txtOrderPhone.setTextColor(Color.RED);


                }
                else {
                    orderViewHolder.txtOrderPhone.setText("Order Phone: "+request.getPhone());
                }
                orderViewHolder.txtOrderAmount.setText("Order Amount: "+request.getTotal());
                orderViewHolder.txtOrderName.setText("Order Name: "+request.getName());
                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
            showUpdateDialogue(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        else if(item.getTitle().equals(Common.DELETE))
            deleteOreder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);
    }

    private void deleteOreder(String key) {
        requests.child(key).removeValue();
    }

    private void showUpdateDialogue(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose Status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view  = inflater.inflate(R.layout.updae_order_layout,null);
        spinner =view.findViewById(R.id.spinner);

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(spinner.getSelectedItem().toString());

                requests.child(localKey).setValue(item);

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();

    }
}
