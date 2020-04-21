package ie.adsfinder.adsfinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import ie.adsfinder.adsfinder.api.ElectronicResult;
import ie.adsfinder.adsfinder.api.ElectronicsModel;
import ie.adsfinder.adsfinder.api.HouseResult;
import ie.adsfinder.adsfinder.api.HousesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HouseSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HouseSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AdsfinderCarListService adsfinderService;
    private RecyclerView recyclerViewRecent;
    private RecyclerView recyclerViewFeauture;
    private RecyclerView recyclerViewLatest;
    private HousesAdapter adapterRecent;
    private HousesAdapter adapterFeauture;
    private HousesAdapter adapterLatest;
    private ProgressBar progressBarLoading;
    private LinearLayout mainContentLinear;
    public HouseSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseSearch newInstance(String param1, String param2) {
        HouseSearch fragment = new HouseSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house_search, container, false);
        adsfinderService = AdsfinderApi.getClient().create(AdsfinderCarListService.class);

        recyclerViewLatest = view.findViewById(R.id.latest_list);
        recyclerViewFeauture = view.findViewById(R.id.feuture_list);
        recyclerViewRecent = view.findViewById(R.id.recent_list);

        mainContentLinear = view.findViewById(R.id.carMainSearchLinear);
        progressBarLoading = view.findViewById(R.id.main_page_progress);
        mainContentLinear.setVisibility(View.INVISIBLE);
        progressBarLoading.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManagerLatest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerFeauture = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerRecent = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewLatest.setLayoutManager(layoutManagerLatest);
        recyclerViewLatest.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeauture.setLayoutManager(layoutManagerFeauture);
        recyclerViewFeauture.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecent.setLayoutManager(layoutManagerRecent);
        recyclerViewRecent.setItemAnimator(new DefaultItemAnimator());

        adapterLatest = new HousesAdapter(getContext());
        recyclerViewLatest.setAdapter(adapterLatest);

        adapterFeauture = new HousesAdapter(getContext());
        recyclerViewFeauture.setAdapter(adapterFeauture);

        adapterRecent = new HousesAdapter(getContext());
        recyclerViewRecent.setAdapter(adapterRecent);
        callElectronicsApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                List<HouseResult> results = fetchResults(response);
                adapterLatest.addAll(results);
                adapterRecent.addAll(results);
            }
            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        callFeatureElectronicsApi().enqueue(new Callback<HousesModel>() {
            @Override
            public void onResponse(Call<HousesModel> call, Response<HousesModel> response) {
                List<HouseResult> results = fetchResults(response);
                adapterFeauture.addAll(results);
                progressBarLoading.setVisibility(View.GONE);
                mainContentLinear.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<HousesModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private Call<HousesModel> callFeatureElectronicsApi() {
        return adsfinderService.getFeatureHouses();
    }
    private Call<HousesModel> callElectronicsApi() {
        return adsfinderService.getHouses(0,null, null, null, null, null, null, null, null, null,null,null, null);
    }
    private List<HouseResult> fetchResults(Response<HousesModel> response) {
        HousesModel data = response.body();
        return data.getResults();
    }
}
