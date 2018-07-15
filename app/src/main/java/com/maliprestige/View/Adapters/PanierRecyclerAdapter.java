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
import com.maliprestige.Presenter.BasketFrag.BasketFragPresenter;
import com.maliprestige.Presenter.Home.HomePresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.BasketFragView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

public class PanierRecyclerAdapter extends RecyclerView.Adapter<PanierRecyclerAdapter.MyViewHolder> {

    private Context context;
    private BasketFragView.IBasketFrag iBasketFrag;
    private ArrayList<Panier> paniers;
    private Hashtable<Integer, Float> prixUnitaires;
    private Hashtable<Integer, MyViewHolder> viewHolders;

    public PanierRecyclerAdapter(Context context, ArrayList<Panier> paniers, BasketFragView.IBasketFrag iBasketFrag) {
        this.context = context;
        this.iBasketFrag = iBasketFrag;
        this.paniers = paniers;
        prixUnitaires = new Hashtable<>();
        viewHolders = new Hashtable<>();
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
        prixUnitaires.put(position, panier.getPrixQuantite()/panier.getQuantite());
        viewHolders.put(position, holder);
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
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Panier panier = paniers.get(positionItem);
                    String value = s.toString();
                    if(value != null && !value.isEmpty()){
                        int newQte = Integer.parseInt(s.toString());
                        if(newQte==0){
                            newQte = 1;
                            viewHolders.get(positionItem).quantite.setText(""+newQte);
                        }
                        float prixU = prixUnitaires.get(positionItem);
                        int id = panier.getId();
                        float newPrixQte = prixU*newQte;
                        //--
                        Panier newPanier = new Panier();
                        newPanier.setId(id);
                        newPanier.setQuantite(newQte);
                        newPanier.setProduitId(panier.getProduitId());
                        newPanier.setPrixQuantite(newPrixQte);
                        newPanier.setImageProduit(panier.getImageProduit());
                        newPanier.setNomProduit(panier.getNomProduit());
                        newPanier.setToken(panier.getToken());
                        //--
                        viewHolders.get(positionItem).prix.setText("€"+String.format("%.2f", newPanier.getPrixQuantite()));
                        paniers.get(positionItem).setQuantite(newPanier.getQuantite());
                        paniers.get(positionItem).setPrixQuantite(newPanier.getPrixQuantite());
                        //--
                        BasketFragPresenter fragPresenter = new BasketFragPresenter(iBasketFrag);
                        fragPresenter.modifyBasketData(context, newPanier, "modifier");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Deleting product
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Panier panier = paniers.get(positionItem);
                    BasketFragPresenter fragPresenter = new BasketFragPresenter(iBasketFrag);
                    fragPresenter.modifyBasketData(context, panier, "supprimer");
                    //--
                    for (int i = paniers.size()-1; i >= 0; i--){
                        if(i==positionItem){
                            paniers.remove(positionItem);
                            prixUnitaires.remove(positionItem);
                            viewHolders.remove(positionItem);
                            notifyItemRemoved(positionItem);
                            notifyItemRangeChanged(positionItem, paniers.size());
                            //Log.i("TAG_ACTION", "DELETE : "+panier.toString());
                        }
                    }
                }
            });
        }
    }
}
