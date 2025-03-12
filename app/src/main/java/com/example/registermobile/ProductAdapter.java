package com.example.registermobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private List<Product> productList;
    private Context context;
    private String formatRupiah(String harga) {
        try {
            double hargaDouble = Double.parseDouble(harga);
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            return formatRupiah.format(hargaDouble).replace(",00", "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Rp. 0";
        }
    }


    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewMerk.setText(product.getMerk());
        holder.textViewHargaBeli.setText(formatRupiah(product.getHargaBeli()));
        holder.textViewStok.setText(product.getStok());
        Glide.with(context)
                .load(product.getFoto())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewMerk;
        TextView textViewHargaBeli;
        TextView textViewStok;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewMerk = itemView.findViewById(R.id.textViewMerk);
            textViewHargaBeli = itemView.findViewById(R.id.textViewHargaBeli);
            textViewStok = itemView.findViewById(R.id.textViewStok);
        }
    }
}

class Product {
    private String merk;
    private String hargabeli;
    private String stok;
    private String foto;

    public Product(String merk, String hargabeli, String stok, String foto) {
        this.merk = merk;
        this.hargabeli = hargabeli;
        this.stok = stok;
        this.foto = foto;
    }

    public String getMerk() {
        return merk;
    }

    public String getHargaBeli() {
        return hargabeli;
    }

    public String getStok() {
        return stok;
    }

    public String getFoto() {
        return foto;
    }
}

class ProductResponse {
    @SerializedName("result") // Sesuaikan dengan nama JSON
    private List<Product> result;

    public List<Product> getResult() {
        return result;
    }
}