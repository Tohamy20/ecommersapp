package com.example.talabatend.ui.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.talabatend.CartActivity;
import com.example.talabatend.databinding.FragmentCartBinding;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel mViewModel;
    String type = "";


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel galleryViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCart;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Intent intent = new Intent(getActivity(), CartActivity.class);
        startActivity(intent);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}