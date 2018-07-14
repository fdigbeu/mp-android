package com.maliprestige.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maliprestige.Model.DAOProduit;
import com.maliprestige.Model.Panier;
import com.maliprestige.Model.Produit;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.BasketFragView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PanierRecyclerAdapter extends RecyclerView.Adapter<PanierRecyclerAdapter.MyViewHolder> {

    private Context context;
    private BasketFragView.IBasketFrag iBasketFrag;
    private ArrayList<Panier> paniers;

    public PanierRecyclerAdapter(Context context, ArrayList<Panier> paniers, BasketFragView.IBasketFrag iBasketFrag) {
        this.context = context;
        this.iBasketFrag = iBasketFrag;
        this.paniers = paniers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panier_card, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Panier panier = paniers.get(position);
        holder.positionItem = position;
        if(panier.getImageProduit() != null && !panier.getImageProduit().isEmpty()) {
            Picasso.with(context).load(panier.getImageProduit()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomePresenter.getScreenResolution(context).getWidth() / 3, (int) ((HomePresenter.getScreenResolution(context).getWidth() / 3) / 0.87f)).into(holder.icone);
        }
        holder.titre.setText(panier.getNomProduit());
        holder.quantite.setText(""+panier.getQuantite());
        holder.prix.setText("€"+String.format("%.2f", panier.getPrixQuantite()));
    }

    @Override
    public int getItemCount() {
        return paniers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View container;
        ImageView icone;
        ImageView delete;
        TextView titre;
        EditText quantite;
        TextView prix;
        int positionItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.panier_container);
            icone = itemView.findViewById(R.id.panier_produit_imageview);
            delete = itemView.findViewById(R.id.delete_produit_imageview);
            titre = itemView.findViewById(R.id.panier_produit_title_textview);
            quantite = itemView.findViewById(R.id.panier_produit_qte_textview);
            prix = itemView.findViewById(R.id.panier_produit_prix_textview);

            // Detail image
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // Changing quantity value
           quantite.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i("TAG_NEW_QTE", "QTE = "+s.toString());
                }
            });

            // Deleting product
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
