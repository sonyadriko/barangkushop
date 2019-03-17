package com.example.xdreamer.barangkushop.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.xdreamer.barangkushop.Object.Favorites;
import com.example.xdreamer.barangkushop.Object.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "BarangkushopDB.db";
    private static final int DB_VER = 2;



    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public boolean checkProductExists(String productId, String userPhone){
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String SQLquery = String.format("SELECT * FROM OrderDetail WHERE UserPhone='%s' AND ProductId='%s'",userPhone,productId);
        cursor = db.rawQuery(SQLquery,null);
        if (cursor.getCount()>0)
            flag = true;
        else
            flag = false;
        cursor.close();
        return flag;
    }

    public void increaseCart(String userPhone, String productId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity= Quantity+1 WHERE UserPhone='%s' AND ProductId='%s'",userPhone,productId);
        db.execSQL(query);
    }

    public List<Order> getCarts(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlselect = {"UserPhone","ProductName", "ProductId", "Quantity", "Price", "Discount", "Image"};
        String sqltable = "OrderDetail";

        queryBuilder.setTables(sqltable);
        Cursor c = queryBuilder.query(db, sqlselect, "UserPhone=?", new String[]{userPhone}, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Image"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String queryy = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone,ProductId,ProductName,Quantity,Price,Discount,Image) VALUES('%s','%s', '%s', '%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        db.execSQL(queryy);
    }

    public void cleanCart(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s'",userPhone);
        db.execSQL(query);
    }

    public void addToFavorites(Favorites product) {
        SQLiteDatabase db = getReadableDatabase();
        String queryes = String.format("INSERT INTO Favorites(" +
                "ProductId,ProductName,ProductPrice,ProductMenuId,ProductImage,ProductDiscount,ProductDescription,UserPhone) " +
                "VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                product.getProductId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductMenuId(),
                product.getProductImage(),
                product.getProductDiscount(),
                product.getProductDescription(),
                product.getUserPhone());
        db.execSQL(queryes);
    }

    public void removeFromFavorites(String productId, String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String queryes = String.format("DELETE FROM Favorites WHERE UserPhone='%s' AND ProductId='%s';", userPhone,productId);
        db.execSQL(queryes);
    }

    public boolean isFavorite(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String queryess = String.format("SELECT * FROM Favorites WHERE ProductId='%s';", productId);
        Cursor cursor = db.rawQuery(queryess, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Favorites> getAllFavorites(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlselect = {"UserPhone","ProductId","ProductName","ProductPrice","ProductMenuId","ProductImage","ProductDiscount","ProductDescription"};
        String sqltable = "Favorites";

        queryBuilder.setTables(sqltable);
        Cursor c = queryBuilder.query(db, sqlselect, "UserPhone=?", new String[]{userPhone}, null, null, null);

        final List<Favorites> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Favorites(
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("ProductPrice")),
                        c.getString(c.getColumnIndex("ProductMenuId")),
                        c.getString(c.getColumnIndex("ProductImage")),
                        c.getString(c.getColumnIndex("ProductDiscount")),
                        c.getString(c.getColumnIndex("ProductDescription")),
                        c.getString(c.getColumnIndex("UserPhone"))
                        ));
            } while (c.moveToNext());
        }
        return result;
    }

    public int getCountCart(String userPhone) {
        int count = 0;


        SQLiteDatabase db = getReadableDatabase();
        String queryess = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserPhone='%s'",userPhone);
        Cursor cursor = db.rawQuery(queryess, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());

        }
        return count;
    }

    public void removeFromCart(String productId, String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s' AND ProductId='%s'",phone,productId);
        db.execSQL(query);
    }
}
