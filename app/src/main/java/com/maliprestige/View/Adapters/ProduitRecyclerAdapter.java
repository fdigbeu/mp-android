package com.maliprestige.View.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maliprestige.Model.Produit;
import com.maliprestige.Presenter.ChildFrag.ChildFragPresenter;
import com.maliprestige.Presenter.ExoticFrag.ExoticFragPresenter;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.Presenter.ManFrag.ManFragPresenter;
import com.maliprestige.Presenter.WomanFrag.WomanFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.ChildFragView;
import com.maliprestige.View.Interfaces.ExoticFragView;
import com.maliprestige.View.Interfaces.ManFragView;
import com.maliprestige.View.Interfaces.WomanFragView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProduitRecyclerAdapter extends RecyclerView.Adapter<ProduitRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ManFragView.IManFrag iManFrag;
    private WomanFragView.IWomanFrag iWomanFrag;
    private ChildFragView.IChildFrag iChildFrag;
    private ExoticFragView.IExoticFrag iExoticFrag;
    private ArrayList<Produit> produits;

    public ProduitRecyclerAdapter(Context context, ArrayList<Produit> produits, ManFragView.IManFrag iManFrag) {
        this.context = context;
        this.produits = produits;
        this.iManFrag = iManFrag;
    }

    public ProduitRecyclerAdapter(Context context, ArrayList<Produit> produits, WomanFragView.IWomanFrag iWomanFrag) {
        this.context = context;
        this.produits = produits;
        this.iWomanFrag = iWomanFrag;
    }

    public ProduitRecyclerAdapter(Context context, ArrayList<Produit> produits, ChildFragView.IChildFrag iChildFrag) {
        this.context = context;
        this.produits = produits;
        this.iChildFrag = iChildFrag;
    }

    public ProduitRecyclerAdapter(Context context, ArrayList<Produit> produits, ExoticFragView.IExoticFrag iExoticFrag) {
        this.context = context;
        this.produits = produits;
        this.iExoticFrag = iExoticFrag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produit_card, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produit produit = produits.get(position);

        holder.positionItem = position;
        holder.titleTextView.setText(produit.getNom());

        holder.newProduit.setVisibility(produit.isNouveaute() ? View.VISIBLE : View.GONE);

        // If reduction exists
        if(produit.isReduction()){
            // If exotic exists
            String montant = "";
            if(produit.getPrixUnitaireGros() > 0){
                montant = "€"+String.format("%.2f", produit.getPrixReductionTtc());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    montant += " <font color=\"#0f9d58\"><s>"+"€"+String.format("%.2f", produit.getPrixUnitaireGros())+"</s></font>";
                }
                holder.prixTextView.setText(HomePresenter.buildHtml(montant));
            }
            else{
                montant = "€"+String.format("%.2f", produit.getPrixReductionTtc());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    montant += " <font color=\"#0f9d58\"><s>"+"€"+String.format("%.2f", produit.getPrixUnitaireTtc())+"</s></font>";
                }
                holder.prixTextView.setText(HomePresenter.buildHtml(montant));
            }
        }
        else{
            // If exotic exists
            if(produit.getPrixUnitaireGros() > 0){
                holder.prixTextView.setText("€"+String.format("%.2f", produit.getPrixUnitaireGros()));
            }
            else{
                holder.prixTextView.setText("€"+String.format("%.2f", produit.getPrixUnitaireTtc()));
            }
        }

        holder.btnAjouterPanier.setText(context.getResources().getString(R.string.lb_ajouter_au_panier));
        Picasso.with(context).load(produit.getImage1()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomePresenter.getScreenResolution(context).getWidth()/2, (int)((HomePresenter.getScreenResolution(context).getWidth()/2)/0.87f)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View container;
        ImageView imageView;
        ImageView newProduit;
        TextView titleTextView;
        TextView prixTextView;
        Button btnAjouterPanier;
        int positionItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.produit_imageview);
            newProduit = itemView.findViewById(R.id.newProduit);
            titleTextView = itemView.findViewById(R.id.produit_title_textView);
            prixTextView = itemView.findViewById(R.id.prix_produit_textView);
            btnAjouterPanier = itemView.findViewById(R.id.btn_ajouter_au_panier);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iManFrag != null){
                        ManFragPresenter manFragPresenter = new ManFragPresenter(iManFrag);
                        manFragPresenter.showProductDetail(produits.get(positionItem));
                    }
                    else if (iWomanFrag != null){
                        WomanFragPresenter womanFragPresenter = new WomanFragPresenter(iWomanFrag);
                        womanFragPresenter.showProductDetail(produits.get(positionItem));
                    }
                    else if (iChildFrag != null){
                        ChildFragPresenter childFragPresenter = new ChildFragPresenter(iChildFrag);
                        childFragPresenter.showProductDetail(produits.get(positionItem));
                    }
                    else if (iExoticFrag != null){
                        ExoticFragPresenter exoticFragPresenter = new ExoticFragPresenter(iExoticFrag);
                        exoticFragPresenter.showProductDetail(produits.get(positionItem));
                    }
                    else{}
                }
            });

            btnAjouterPanier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iManFrag != null){
                        ManFragPresenter manFragPresenter = new ManFragPresenter(iManFrag);
                        manFragPresenter.addProductToBasket(context, produits.get(positionItem));
                    }
                    else if (iWomanFrag != null){
                        WomanFragPresenter womanFragPresenter = new WomanFragPresenter(iWomanFrag);
                        womanFragPresenter.addProductToBasket(context, produits.get(positionItem));
                    }
                    else if (iChildFrag != null){
                        ChildFragPresenter childFragPresenter = new ChildFragPresenter(iChildFrag);
                        childFragPresenter.addProductToBasket(context, produits.get(positionItem));
                    }
                    else if (iExoticFrag != null){
                        ExoticFragPresenter exoticFragPresenter = new ExoticFragPresenter(iExoticFrag);
                        exoticFragPresenter.addProductToBasket(context, produits.get(positionItem));
                    }
                    else{}
                }
            });
        }
    }
}
