package com.example.xdreamer.barangkushop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xdreamer.barangkushop.Common.Common;
import com.example.xdreamer.barangkushop.Common.Config;
import com.example.xdreamer.barangkushop.Database.Database;
import com.example.xdreamer.barangkushop.Helper.RecyclerItemTouchHelper;
import com.example.xdreamer.barangkushop.Interface.RecyclerItemTouchHelperListener;
import com.example.xdreamer.barangkushop.Object.MyResponse;
import com.example.xdreamer.barangkushop.Object.Notification;
import com.example.xdreamer.barangkushop.Object.Order;
import com.example.xdreamer.barangkushop.Object.Request;
import com.example.xdreamer.barangkushop.Object.Sender;
import com.example.xdreamer.barangkushop.Object.Token;
import com.example.xdreamer.barangkushop.Remote.APIService;
import com.example.xdreamer.barangkushop.ViewHolder.CartAdapter;
import com.example.xdreamer.barangkushop.ViewHolder.CartViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final int PAYPAL_REQUEST_CODE = 9999;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    RelativeLayout rootLayout;

    TextView totalPrice;
    Button placeOrder;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    APIService mService;

    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    String address, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");

        rootLayout = findViewById(R.id.rootLayout);

        //active paypal
        //Intent intpay = new Intent(this, PayPalService.class);
        //intpay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //startService(intpay);

        mService = Common.getFCMService();

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        totalPrice = findViewById(R.id.totalprice);
        placeOrder = findViewById(R.id.btnPlaceOrder);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0)
                    showDialogAlert();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    private void showDialogAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter Your IGN");

        /*final EditText editText = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        */

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_ordercomment = inflater.inflate(R.layout.order_confirm_layout, null);

        final EditText edtIgn = layout_ordercomment.findViewById(R.id.edtIGNOrder);
        final EditText edtComment = layout_ordercomment.findViewById(R.id.edtCommentOrder);

        alertDialog.setView(layout_ordercomment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Using paypal
               /* address = edtIgn.getText().toString();
                comment = edtComment.getText().toString();

                String formatamount = totalPrice.getText().toString()
                        .replace("$", "")
                        .replace(",", "");
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatamount),
                        "USD",
                        "Barangku Order",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE); */
                //end of paypal


                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        address,
                        totalPrice.getText().toString(),
                        "0",
                        comment,
                        cart
                );

                String order_number = String.valueOf(System.currentTimeMillis());
                requests.child(order_number).setValue(request);

                new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());

                sendNotificationOrder(order_number);
                // Toast.makeText(Cart.this, "Thank You, Order place", Toast.LENGTH_SHORT).show();
                //sendPayment();
                showPaymentRekening();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void showPaymentRekening() {
    }

    private void sendPayment() {
        if (Common.currentUser != null)
            startActivity(new Intent(Cart.this, Payment.class));
    }


    //when using paypal, use onActivityResult :))
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);


                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getName(),
                                address,
                                totalPrice.getText().toString(),
                                "0",
                                comment,
                                jsonObject.getJSONObject("responese").getString("state"),
                                cart

                        );

                        String order_number = String.valueOf(System.currentTimeMillis());
                        requests.child(order_number).setValue(request);

                        new Database(getBaseContext()).cleanCart();

                        sendNotificationOrder(order_number);
                        Toast.makeText(Cart.this, "Thank You, Order place", Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment cancel", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    */

    private void sendNotificationOrder(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverTokenss = postSnapshot.getValue(Token.class);

                    Notification notifications = new Notification("Barangku", "You have new order " + order_number);
                    Sender content = new Sender(serverTokenss.getToken(), notifications);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().succes == 1) {
                                            Toast.makeText(Cart.this, "Thank you, order place :) ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Cart.this, "Thank you, order place :) ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood() {
        cart = new Database(this).getCarts(Common.currentUser.getPhone());
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("id", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        totalPrice.setText(format.format(total));

    }

    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    */

    private void deleteCart(int order) {

        cart.remove(order);

        new Database(this).cleanCart(Common.currentUser.getPhone());

        for (Order item : cart) {
            new Database(this).addToCart(item);
        }
        loadListFood();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder){
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(),Common.currentUser.getPhone());

            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("id","ID");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            totalPrice.setText(fmt.format(total));

            Snackbar snackbar = Snackbar.make(rootLayout,name +  " removed from cart",Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                    for (Order item : orders)
                        total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("id","ID");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    totalPrice.setText(fmt.format(total));

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
