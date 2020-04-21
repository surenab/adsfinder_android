package ie.adsfinder.adsfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ie.adsfinder.adsfinder.AdsfinderUtils.capitalizeWord;

public class GeneralInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<List<String>> mData;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;



    public GeneralInfoAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }


    public List<List<String>> getMData() {
        return mData;
    }

    public void setMData(List<List<String>> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.general_info_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView tv_key, tv_value;

        public MovieVH(View itemView) {
            super(itemView);
            tv_key = itemView.findViewById(R.id.general_key);
            tv_value = itemView.findViewById(R.id.general_value);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List<String> result = mData.get(position);
        MovieVH movieVH = (MovieVH) holder;
        movieVH.tv_key.setText(capitalizeWord(result.get(0)));
        movieVH.tv_value.setText(capitalizeWord(result.get(1)));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == mData.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(List<String> r) {
        mData.add(r);
        notifyItemInserted(mData.size() - 1);
    }

    public void addAll(List<List<String>> moveResults) {
        for (List<String> result : moveResults) {
            add(result);
        }
    }

    public List<String> getItem(int position) {
        return mData.get(position);
    }

}
