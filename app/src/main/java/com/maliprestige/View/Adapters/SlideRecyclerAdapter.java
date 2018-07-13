package com.maliprestige.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maliprestige.Model.Slide;
import com.maliprestige.Presenter.HomeFag.HomeFragPresenter;
import com.maliprestige.R;
import com.maliprestige.View.Interfaces.HomeFragView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

public class SlideRecyclerAdapter extends RecyclerView.Adapter<SlideRecyclerAdapter.MyViewHolder> {

    private Context context;
    private HomeFragView.IHomeFrag iHomeFrag;
    private ArrayList<Slide> slides;
    private Hashtable<Integer, MyViewHolder> mViewHolder;

    public SlideRecyclerAdapter(Context context, ArrayList<Slide> slides, HomeFragView.IHomeFrag iHomeFrag) {
        this.context = context;
        this.slides = slides;
        this.iHomeFrag = iHomeFrag;
        mViewHolder = new Hashtable<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accueil_card, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Slide slide = slides.get(position);

        holder.positionItem = position;
        holder.textView.setText(slide.getTitre());

        Picasso.with(context).load(slide.getUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).resize(HomeFragPresenter.getScreenResolution(context).getWidth(), (int)(HomeFragPresenter.getScreenResolution(context).getWidth()/2.7f)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        View container;
        TextView textView;
        int positionItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.slide_imageview);
            textView = itemView.findViewById(R.id.slide_title_textView);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeFragPresenter fragPresenter = new HomeFragPresenter(iHomeFrag);
                    fragPresenter.notifySlideIsSelected(slides.get(positionItem));
                }
            });
        }
    }
}
