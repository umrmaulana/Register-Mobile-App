package com.example.registermobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Fragment fragment;
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

    public ProductAdapter(Fragment fragment, List<Product> productList) {
        this.fragment = fragment;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewMerk.setText(product.getMerk());
        holder.textViewHargaJual.setText(formatRupiah(product.getHargaJual()));
        Glide.with(fragment.getContext())
                .load(product.getFoto())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageViewProduct);

        holder.itemView.setOnClickListener(v -> {
            ProductDetailDialog dialog = new ProductDetailDialog(product);
            dialog.show(fragment.getChildFragmentManager(), "ProductDetailDialog");
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewMerk;
        TextView textViewHargaJual;
        TextView textViewStok;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewMerk = itemView.findViewById(R.id.textViewMerk);
            textViewHargaJual = itemView.findViewById(R.id.textViewHargaJual);
        }
    }
}

class Product{
    @SerializedName("kode")
    private String kode;

    @SerializedName("merk")
    private String merk;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("hargabeli")
    private String hargaBeli;

    @SerializedName("hargajual")
    private String hargaJual;

    @SerializedName("stok")
    private String stok;

    @SerializedName("foto")
    private String foto;

    @SerializedName("deskripsi")
    private String deskripsi;

    public String getKode() { return kode; }
    public String getMerk() { return merk; }
    public String getKategori() { return kategori; }
    public String getHargaBeli() { return hargaBeli; }
    public String getHargaJual() { return hargaJual; }
    public String getStok() { return stok; }
    public String getFoto() { return foto; }
    public String getDeskripsi() { return deskripsi; }
}

class ProductResponse {
    @SerializedName("result") // Sesuaikan dengan nama JSON
    private List<Product> result;

    public List<Product> getResult() {
        return result;
    }
}