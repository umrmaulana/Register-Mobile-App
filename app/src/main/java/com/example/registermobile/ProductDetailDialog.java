package com.example.registermobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProductDetailDialog extends BottomSheetDialogFragment {
    private Product product;

    public ProductDetailDialog(Product product) {
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_product_detail, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewDetail);
        TextView textViewMerk = view.findViewById(R.id.textViewMerkDetail);
        TextView textViewHarga = view.findViewById(R.id.textViewHargaDetail);
        TextView textViewDeskripsi = view.findViewById(R.id.textViewDeskripsiDetail);
        TextView textViewStok = view.findViewById(R.id.textViewStokDetail);
        ImageButton imageButtonCart = view.findViewById(R.id.imageButtonCart);
        imageButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        Button buttonClose = view.findViewById(R.id.buttonClose);

        // Set Data
        Glide.with(requireContext()).load(product.getFoto()).into(imageView);
        textViewMerk.setText(product.getMerk());
        textViewHarga.setText("Harga: Rp. " + product.getHargaJual());
        textViewDeskripsi.setText(product.getDeskripsi());
        textViewStok.setText("Stok: " + product.getStok());

        buttonClose.setOnClickListener(v -> dismiss());

        return view;
    }
}
