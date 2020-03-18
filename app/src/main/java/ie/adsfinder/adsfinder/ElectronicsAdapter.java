package ie.adsfinder.adsfinder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.text.HtmlCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import ie.adsfinder.adsfinder.api.ElectronicResult;

public class ElectronicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ElectronicResult> mData;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public ElectronicsAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public List<ElectronicResult> getMData() {
        return mData;
    }

    public void setMData(List<ElectronicResult> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(viewGroup, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.card_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView card_photo;
        private TextView tv_header,tv_donor,tv_scrapedate,tv_county,tv_param1,tv_param2,tv_extradata, tv_price;
        private ProgressBar tv_Progress;

        public MovieVH(View itemView) {
            super(itemView);
            card_photo = itemView.findViewById(R.id.cardImageView);
            tv_header = itemView.findViewById(R.id.cardHeaderView);
            tv_donor = itemView.findViewById(R.id.cardDonorView);
            tv_scrapedate = itemView.findViewById(R.id.cardScrapDate);
            tv_county = itemView.findViewById(R.id.cardCountyView);
            tv_param1 = itemView.findViewById(R.id.cardAdParam1View);
            tv_param2 = itemView.findViewById(R.id.cardParam2View);
            tv_extradata = itemView.findViewById(R.id.cardExtraDataView);
            tv_price = itemView.findViewById(R.id.cardPriceVIew);
            tv_Progress = itemView.findViewById(R.id.ad_progress);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ElectronicResult result = mData.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                String[] date = result.getScrapedate().split("T");
                date = date[0].split("-");
                String scrapdate = date[2]+"."+date[1]+"."+date[0].substring(2);

                Log.d("Results",scrapdate);
                movieVH.tv_header.setText(AdsfinderUtils.capitalizeWord(result.getDescription().replaceAll("^[ \t]+|[ \t]+$", "") ));
                String donor_text = new String("<a href='").concat(result.getUrl()).concat("' target='_top'>").concat(StringUtils.capitalize(result.getDonor())).concat("</a>");
                movieVH.tv_donor.setText(HtmlCompat.fromHtml(donor_text, HtmlCompat.FROM_HTML_MODE_LEGACY));
                movieVH.tv_donor.setClickable(true);
                movieVH.tv_donor.setMovementMethod(LinkMovementMethod.getInstance());
                movieVH.tv_donor.setLinkTextColor(Color.BLUE);
                movieVH.tv_scrapedate.setText(StringUtils.capitalize(scrapdate));
                movieVH.tv_county.setText(AdsfinderUtils.capitalizeWord(result.getLightaddress().getTown().trim()+", "+result.getLightaddress().getCounty().trim()));
                movieVH.tv_param2.setText("");
                movieVH.tv_extradata.setText("");
                if (result.getElectronic_category() == null) {
                    movieVH.tv_param1.setText("Unknown");
                } else {
                    movieVH.tv_param1.setText(result.getElectronic_category().getDisplayName());
                }
                if (result.getPrice()==null) {
                    movieVH.tv_price.setText("Unknown");
                } else {
                    movieVH.tv_price.setText(result.getPrice() + "€");
                }
                Glide.with(mContext).load(result.getMainimageurl()).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                movieVH.tv_Progress.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.tv_Progress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.card_photo);
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == mData.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    public void add(ElectronicResult r) {
        mData.add(r);
        notifyItemInserted(mData.size() - 1);
    }

    public void addAll(List<ElectronicResult> moveResults) {
        for (ElectronicResult result : moveResults) {
            add(result);
        }
    }

    public void remove(ElectronicResult r) {
        int position = mData.indexOf(r);
        if (position > -1) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ElectronicResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mData.size() - 1;
        ElectronicResult result = getItem(position);

        if (result != null) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }
    public ElectronicResult getItem(int position) {
        return mData.get(position);
    }

}
