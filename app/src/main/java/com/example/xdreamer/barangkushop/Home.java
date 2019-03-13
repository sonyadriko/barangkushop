package com.example.xdreamer.barangkushop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Interface.ItemClickListener;
import com.example.xdreamer.barangkushop.Object.Category;
import com.example.xdreamer.barangkushop.Object.Token;
import com.example.xdreamer.barangkushop.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference category;

    private FloatingActionButton fab;

    private TextView txtname;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    loadMenu();
                } else {
                    Toast.makeText(getBaseContext(), "Please check your connection...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(Home.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab =

                findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(Home.this, Cart.class);
                startActivity(cart);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        txtname = headerView.findViewById(R.id.txtFullName);

        recyclerView =

                findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(this))

            loadMenu();
        else {
            Toast.makeText(Home.this, "Please check your connection...", Toast.LENGTH_SHORT).show();
            return;
        }

        //NOTIFICATION --
        UpdateToken(FirebaseInstanceId.getInstance().

                getToken());

        //  startActivity(new Intent(Home.this, ListenOrder.class));

    }

    private void UpdateToken(String token) {
          if (Common.currentUser != null) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(Common.currentUser.getPhone()).setValue(data);
        }
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickitem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent productlist = new Intent(Home.this, ProductList.class);
                        productlist.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(productlist);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_changepwd) {
            showDialogChangePassword();
        } else if (id == R.id.nav_logout) {
            //Delete remember user & password
            Paper.book().destroy();

            //Logout
            Intent ignOut = new Intent(Home.this, SignIn.class);
            ignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ignOut);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogChangePassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_chgpwd = inflater.inflate(R.layout.change_password_layout, null);

        final EditText edtPassword = layout_chgpwd.findViewById(R.id.edtPasswordchg);
        final EditText edtNewPassword = layout_chgpwd.findViewById(R.id.edtNewPasswordchg);
        final EditText edtRepeatPassword = layout_chgpwd.findViewById(R.id.edtRepeatNP);

        alertDialog.setView(layout_chgpwd);

        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
                waitingDialog.show();

                if (edtPassword.getText().toString().equals(Common.currentUser.getPassword())) {
                    if (edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString())) {
                        Map<String, Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("Password", edtNewPassword.getText().toString());

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                        databaseReference.child(Common.currentUser.getPhone())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(Home.this, "Password was update", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Home.this, "New password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Home.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
