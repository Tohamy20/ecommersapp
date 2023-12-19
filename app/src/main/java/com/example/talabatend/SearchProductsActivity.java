package com.example.talabatend;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talabatend.Model.product;
import com.example.talabatend.viewHolder.productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;

    private ImageButton voiceBtn;
    private ImageButton qrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_btn);
        voiceBtn = findViewById(R.id.search_voice_btn);
        qrBtn = findViewById(R.id.QrScanner);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });
        voiceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                speak(view);
            }
        });
        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<product> options =
                new FirebaseRecyclerOptions.Builder<product>()
                        .setQuery(reference.orderByChild("pname").startAt(searchInput),product.class)
                        .build();
        FirebaseRecyclerAdapter<product, productviewholder> adapter =
                new FirebaseRecyclerAdapter<product, productviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull productviewholder holder, int position, @NonNull product model) {
                        holder.txtproductname.setText(model.getPname());

                        holder.txtproductdescription.setText(model.getDescription());
                        holder.txtproductprice.setText( "Price = " +model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)

                            {
                                Intent intent = new Intent(SearchProductsActivity.this , ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);


                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        productviewholder holder = new productviewholder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
    public void speak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Start Speaking");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode==RESULT_OK){
            inputText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR code or Barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncer.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncer = registerForActivityResult(new ScanContract(),result ->{
        if (result.getContents() != null){
            inputText.setText(result.getContents());
        }
    });
}