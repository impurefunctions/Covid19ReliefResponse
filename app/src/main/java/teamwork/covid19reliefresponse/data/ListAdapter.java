package teamwork.covid19reliefresponse.data;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import teamwork.covid19reliefresponse.R;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerViewClickListener mListener;
    private ArrayList<String> strings;
    private List<TextView> items;
    private int row_index;
    // private List<Data> mDataset = new ArrayList<>();

    public ListAdapter(RecyclerViewClickListener listener,ArrayList<String> strings) {
        mListener = listener;
        this.strings=strings;
        this.items = new ArrayList<>();
    }
    /*
        public void updateData(List<Data> dataset) {
            mDataset.clear();
            mDataset.addAll(dataset);
            notifyDataSetChanged();
        }
    */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.options_row, parent, false);
        return new RowViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.name
        if (holder instanceof RowViewHolder) {

            RowViewHolder rowHolder = (RowViewHolder) holder;

            rowHolder.name.setText(strings.get(position));
            rowHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index=position;
                    notifyDataSetChanged();
                    // mListener.onClick();
                    mListener.onClick(view, position);
                }
            });

            if(row_index==position){
                //  ((RowViewHolder) holder).cardView.setBackgroundColor(Color.parseColor("#567845"));
                // ((RowViewHolder) holder).cardView.setCardBackgroundColor(Color.parseColor("#567845"));\
                rowHolder.name.setBackgroundResource(R.drawable.textview_background);
                rowHolder.name.setTextColor(Color.parseColor("#480674"));

                //   holder.tv1.setTextColor(Color.parseColor("#ffffff"));
            }
            else {
                //((RowViewHolder) holder).cardView.setBackgroundColor(Color.parseColor("#ffffff"));
                //  rowHolder.name.setBackgroundResource(R.drawable.rounded_corner);
                rowHolder.name.setBackgroundResource(R.drawable.white_background);
                rowHolder.name.setTextColor(Color.parseColor("#4e4848"));
                //holder.tv1.setTextColor(Color.parseColor("#000000"));
            }

            //set values of data here
        }

    }
    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private TextView name;
        public LinearLayout linearLayout;


        RowViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);


            mListener = listener;
            name = (TextView) v.findViewById(R.id.options_text);
            linearLayout=v.findViewById(R.id.linearLayout1);
            v.setOnClickListener(this);
/*
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeAllWhite();
                   // items.add(rowHolder.name);
                   // name.setBackgroundColor(Color.parseColor("#d5d5d5"));
                }
            });
*/
        }

        @Override
        public void onClick(View view) {
            //  mListener.onClick(view, getAdapterPosition(),getOldPosition());
            // view.setBackgroundResource(R.drawable.circle_background_dark);
            //  name.setTextColor(Color.parseColor("#ffffff"));


        }


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }
    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }


}