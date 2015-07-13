package com.jonathancromie.brisbanecityparks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jonathan on 7/7/2015.
 */
public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;   // Declaring Variable to Understand which View is being worked on
                                                // IF the viaew under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private ArrayList<NavItem> mNavItems;

    private String name;
    private String email;
    private int profile;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int holderID;

        TextView textTitle;
        ImageView imageIcon;

        TextView name;
        TextView email;
        ImageView profile;
        Context contxt;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            if (ViewType == TYPE_ITEM) {
                textTitle = (TextView) itemView.findViewById(R.id.title);
                imageIcon = (ImageView) itemView.findViewById(R.id.icon);
                holderID = 1;

                textTitle.setPadding(32, 6, 0, 0);
                imageIcon.setPadding(16, 6, 0, 0);
            }
            else {
                name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.profile);
                holderID = 0;

            }
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(contxt, "The item clicked is " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public DrawerListAdapter(ArrayList<NavItem> navItems, String name, String email, int profile, Context passedContext) {
        mNavItems = navItems;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.context = passedContext;
    }

    @Override
    public DrawerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType,context); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType,context); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(holder.holderID ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textTitle.setText(mNavItems.get(position-1).title); // Setting the Text with the array of our Titles
            holder.imageIcon.setImageResource(mNavItems.get(position-1).icon);// Settimg the image with array of our icons
        }
        else{

            holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            holder.name.setText(name);
            holder.email.setText(email);
        }
    }

    @Override
    public int getItemCount() {
        return mNavItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
