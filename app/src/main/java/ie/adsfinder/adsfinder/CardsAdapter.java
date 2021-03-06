package ie.adsfinder.adsfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ie.adsfinder.adsfinder.api.CarResult;

import static ie.adsfinder.adsfinder.AdsfinderUtils.capitalizeWord;

public class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<CarResult> mData;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;



    public CardsAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    private void openDetailView(String searchType, Integer id) {
        Intent detail_intent = new Intent(this.mContext, DetailViewActivity.class);
        detail_intent.putExtra("SType", searchType);
        detail_intent.putExtra("id", id);
        this.mContext.startActivity(detail_intent);
    }

    public List<CarResult> getMData() {
        return mData;
    }

    public void setMData(List<CarResult> mData) {
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
        private TextView tv_header,tv_donor,tv_scrapedate,tv_county,tv_param1,tv_param2,tv_extradata, tv_price, tv_year_text;
        private ProgressBar tv_Progress;
        private Button more_button;
        private ConstraintLayout cardView;
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
            more_button = itemView.findViewById(R.id.cardMoreButton);
            cardView = itemView.findViewById(R.id.cardView);
            tv_year_text = itemView.findViewById(R.id.cardyear);
            tv_extradata.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fueltype, 0, 0, 0);
            tv_param2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_transmission, 0);
            tv_extradata.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fueltype, 0, 0, 0);
            tv_year_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_car_year, 0);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public class IMageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CarResult result = mData.get(position);
        IMageDownloader task = new IMageDownloader();
        Bitmap myImage;
        switch (getItemViewType(position)) {
            case ITEM:
                String[] date = result.getScrapedate().split("T");
                date = date[0].split("-");
                String scrapdate = date[2]+"."+date[1]+"."+date[0].substring(2);
                final MovieVH movieVH = (MovieVH) holder;
                movieVH.tv_header.setText(capitalizeWord(result.getHeader()));
                String donor_text = new String("<a href='").concat(result.getUrl()).concat("' target='_top'> ").concat(StringUtils.capitalize(result.getDonorwebsite())).concat("</a>");
                movieVH.tv_donor.setText(StringUtils.capitalize(result.getDonorwebsite()));
                movieVH.tv_donor.setClickable(true);
                movieVH.tv_donor.setMovementMethod(LinkMovementMethod.getInstance());
                movieVH.tv_donor.setLinkTextColor(Color.BLUE);
                movieVH.tv_scrapedate.setText(StringUtils.capitalize(scrapdate)+" ");
                movieVH.tv_year_text.setText(Integer.toString(result.getFirstregyear())+" ");
                if (result.getLightaddress().getTown().trim().isEmpty()) {
                    movieVH.tv_county.setText(capitalizeWord(result.getLightaddress().getCounty().trim()));
                } else {
                    movieVH.tv_county.setText(capitalizeWord(result.getLightaddress().getTown().trim() + ", " + result.getLightaddress().getCounty().trim()));
                }
                if (result.getKilometer()==null) {
                    movieVH.tv_param1.setText("Unknown ");
                } else {
                    movieVH.tv_param1.setText(Integer.toString(result.getKilometer())+"KM ");
                }
                movieVH.tv_param2.setText(StringUtils.capitalize(result.getTransmission())+" ");
                movieVH.tv_extradata.setText(StringUtils.capitalize(result.getFueltype()));
                if (result.getPrice()==null) {
                    movieVH.tv_price.setText("Unknown");
                } else {
                    movieVH.tv_price.setText(result.getPrice() + "€");
                }
                Glide.with(mContext).load(result.getMainimageurl()).apply(new RequestOptions().dontAnimate().skipMemoryCache(true)).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieVH.tv_Progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                movieVH.tv_Progress.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(movieVH.card_photo);

                movieVH.more_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDetailView("Cars", result.getId());
                    }
                });
                movieVH.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDetailView("Cars", result.getId());
                    }
                });
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

    public void add(CarResult r) {
        mData.add(r);
        notifyItemInserted(mData.size() - 1);
    }

    public void addAll(List<CarResult> moveResults) {
        for (CarResult result : moveResults) {
            add(result);
        }
    }

    public void remove(CarResult r) {
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
        add(new CarResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mData.size() - 1;
        CarResult result = getItem(position);

        if (result != null) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CarResult getItem(int position) {
        return mData.get(position);
    }

}
