package com.maliprestige.View.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

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

        Produit produit = produits.get(position);;

        holder.positionItem = position;
        holder.titleTextView.setText(produit.getNom());

        holder.newProduit.setVisibility(produit.isNouveaute() ? View.VISIBLE : View.GONE);
        holder.newProduit.setText(produit.isNouveaute() && produit.getPrixUnitaireGros()==0 ? "Nouveauté" : (produit.getPrixUnitaireGros() > 0 ? "Le prix est au kilo": ""));

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

        int totalImage = 0;
        if(produit.getImage1() != null && !produit.getImage1().isEmpty()) { totalImage++; }
        if(produit.getImage2() != null && !produit.getImage2().isEmpty()) { totalImage++; }
        if(produit.getImage3() != null && !produit.getImage3().isEmpty()) { totalImage++; }

        int numerImage = 0;

        holder.relative.setVisibility(View.GONE);
        if(produit.getImage1() != null && !produit.getImage1().isEmpty()) {
            numerImage++;
            holder.relative.setVisibility(View.VISIBLE);
            holder.textView.setText(numerImage+"/"+totalImage);
            Picasso.with(context).load(produit.getImage1()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomePresenter.getScreenResolution(context).getWidth() / 2, (int) ((HomePresenter.getScreenResolution(context).getWidth() / 2) / 0.87f)).into(holder.imageView);
        }
        //--
        holder.relative2.setVisibility(View.GONE);
        if(produit.getImage2() != null && !produit.getImage2().isEmpty()) {
            numerImage++;
            holder.relative2.setVisibility(View.VISIBLE);
            holder.textView2.setText(numerImage+"/"+totalImage);
            Picasso.with(context).load(produit.getImage2()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomePresenter.getScreenResolution(context).getWidth() / 2, (int) ((HomePresenter.getScreenResolution(context).getWidth() / 2) / 0.87f)).into(holder.imageView2);
        }
        //--
        holder.relative3.setVisibility(View.GONE);
        if(produit.getImage3() != null && !produit.getImage3().isEmpty()) {
            numerImage++;
            holder.relative3.setVisibility(View.VISIBLE);
            holder.textView3.setText(numerImage+"/"+totalImage);
            Picasso.with(context).load(produit.getImage3()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomePresenter.getScreenResolution(context).getWidth() / 2, (int) ((HomePresenter.getScreenResolution(context).getWidth() / 2) / 0.87f)).into(holder.imageView3);
        }
    }

    @Override
    public int getItemCount() {
        return produits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View container;
        View relative, relative2, relative3;
        TextView textView, textView2, textView3;
        ImageView imageView, imageView2, imageView3;
        TextView newProduit;
        TextView titleTextView;
        TextView prixTextView;
        Button btnAjouterPanier;
        int positionItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.produit_container);

            relative = itemView.findViewById(R.id.relative_image);
            relative2 = itemView.findViewById(R.id.relative_image2);
            relative3 = itemView.findViewById(R.id.relative_image3);

            textView = itemView.findViewById(R.id.number_textview);
            textView2 = itemView.findViewById(R.id.number_textview2);
            textView3 = itemView.findViewById(R.id.number_textview3);

            imageView = itemView.findViewById(R.id.produit_imageview);
            imageView2 = itemView.findViewById(R.id.produit_imageview2);
            imageView3 = itemView.findViewById(R.id.produit_imageview3);

            newProduit = itemView.findViewById(R.id.newProduit_textView);
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
                        manFragPresenter.addProductToBasket(v, produits.get(positionItem));
                    }
                    else if (iWomanFrag != null){
                        WomanFragPresenter womanFragPresenter = new WomanFragPresenter(iWomanFrag);
                        womanFragPresenter.addProductToBasket(v, produits.get(positionItem));
                    }
                    else if (iChildFrag != null){
                        ChildFragPresenter childFragPresenter = new ChildFragPresenter(iChildFrag);
                        childFragPresenter.addProductToBasket(v, produits.get(positionItem));
                    }
                    else if (iExoticFrag != null){
                        ExoticFragPresenter exoticFragPresenter = new ExoticFragPresenter(iExoticFrag);
                        exoticFragPresenter.addProductToBasket(v, produits.get(positionItem));
                    }
                    else{}
                }
            });
        }
    }
}
