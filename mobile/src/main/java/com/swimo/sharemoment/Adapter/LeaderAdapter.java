package com.swimo.sharemoment.Adapter;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.CircularImageView;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.SquareImageView;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.model.Leader;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.view.fragment.ShowPublicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swimo on 21/10/15.
 */
public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.ViewHolder> {


    ImageLoader imageLoader;
    Context context;
    private List<Leader> mItems = null;
    private ArrayList<Leader> arraylist;

    int device_width;
    public boolean in=true;
    ProgressDialog mProgressDialog;
    public static String url;
    public static ParseUser owner;
    int k;



    public LeaderAdapter(Context context) {
        super();

        this.context = context;
        this.mItems = Home.mItems;
        this.arraylist = new ArrayList<Leader>();
        this.arraylist.addAll(mItems);
        imageLoader = new ImageLoader(context);
        mProgressDialog = new ProgressDialog(context);
        // Set progressdialog title
        mProgressDialog.setTitle("Share Moments ");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaderitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        device_width = size.x;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if(mItems.get(i).isMe()){
          viewHolder.exchange.setVisibility(View.VISIBLE);

        }else{
            viewHolder.exchange.setVisibility(View.GONE);

        }


         imageLoader.DisplayImage(mItems.get(i).getUrl(), viewHolder.imgThumbnail);
        viewHolder.name.setText(mItems.get(i).getUsername());
        viewHolder.points.setText(mItems.get(i).getpLead() + "");
        viewHolder.rank.setText((i+1) + "");




       /* Picasso.with(context)
                .load(mItems.get(i).getimageurl())
                .placeholder(R.drawable.ic_collections_white_18dp) // optional
                .error(R.drawable.ic_error_white_18dp)
                .into(viewHolder.imgThumbnail);*/


        viewHolder.exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    mProgressDialog.hide();





    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CircularImageView imgThumbnail;
        public Button exchange;
        public TextView name;
        public TextView points;
        public TextView rank;





        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (CircularImageView)itemView.findViewById(R.id.img_thumbnail_lead);
            exchange=(Button)itemView.findViewById(R.id.Bexchange);
            name=(TextView)itemView.findViewById(R.id.namelead);
            points=(TextView)itemView.findViewById(R.id.points);
            rank=(TextView)itemView.findViewById(R.id.rank);

        }
    }



}


