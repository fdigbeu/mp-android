package com.maliprestige.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.maliprestige.Model.Commande;
import com.maliprestige.Model.CommandeProduit;
import com.maliprestige.Presenter.OrderFrag.OrderFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.OrderFragView;

import java.util.ArrayList;
import java.util.Hashtable;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.MyViewHolder> {

    private Context context;
    private OrderFragView.IOrderFrag iOrderFrag;
    private ArrayList<Commande> commandes;
    private Hashtable<Integer, Integer> noDuplicate;

    public OrderRecyclerAdapter(Context context, ArrayList<Commande> commandes, OrderFragView.IOrderFrag iOrderFrag) {
        this.context = context;
        this.commandes = commandes;
        this.iOrderFrag = iOrderFrag;
        this.noDuplicate = new Hashtable<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande_card, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Commande commande = commandes.get(position);
        holder.positionItem = position;
        holder.title.setText(OrderFragPresenter.buildHtml("N° <b>"+commande.getNumeroCommande()+"</b>"));

        holder.orderItems.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ArrayList<CommandeProduit> commandeProduits = commande.getCommandeProduits();
        layoutParams.setMargins(10, 10, 10, 10);
        for (int i=0; i<commandeProduits.size(); i++){
            CommandeProduit commandeProduit = commandeProduits.get(i);
            TextView textView  = new TextView(context);
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundResource(R.drawable.order_item_radius);
            textView.setTextSize(17.0f);
            textView.setPadding(20, 20, 20, 20);
            String text = "<font color=\"#000000\">"+commandeProduit.getNomProduit()+"</font><br>x"+commandeProduit.getQteProduit()+"<br>€"+String.format("%.2f", commandeProduit.getPrixQteProduit());
            textView.setText(OrderFragPresenter.buildHtml(text));
            textView.setClickable(false);
            holder.orderItems.addView(textView);
        }

        String colorString = OrderFragPresenter.getColorStatut(commande.getStatutLibelle().trim());
        //Log.i("TAG_COLOR", commande.getStatutLibelle().trim()+" == "+colorString);
        holder.statuts.setText(OrderFragPresenter.buildHtml("<b>Statut : <font color=\""+colorString+"\">"+commande.getStatutLibelle()+"</font></b>"));

    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        View container;
        LinearLayout orderItems;
        TextView title;
        ImageView menuOrder;
        TextView statuts;
        int positionItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.commande_container);
            orderItems = itemView.findViewById(R.id.layout_order_items);
            title = itemView.findViewById(R.id.title_commande_textView);
            menuOrder = itemView.findViewById(R.id.menu_commande_imageView);
            statuts = itemView.findViewById(R.id.statut_commande_textView);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            menuOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderFragPresenter fragPresenter = new OrderFragPresenter(iOrderFrag);
                    fragPresenter.showPopupMenuOrder(v, commandes.get(positionItem).getNumeroCommande(), commandes.get(positionItem).getNumeroFacture(), commandes.get(positionItem).isFactureAcquittee());
                }
            });
        }
    }
}
