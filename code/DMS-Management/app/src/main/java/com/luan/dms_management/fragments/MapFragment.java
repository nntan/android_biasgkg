package com.luan.dms_management.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.luan.dms_management.R;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.service.BasicService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LatLng position;

    @BindView(R.id.mapViewStaff)
    protected MapView mapView;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        configMap();
        return rootView;
    }

    public void configMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mapboxMap) {
                mapboxMap.clear();
                List<MarkerOptions> markers = new ArrayList<>();
                for (int i = 0; i < BasicService.staffLocationList.size(); i++) {
                    StaffLocation location = BasicService.staffLocationList.get(i);
                    position = new LatLng(Double.parseDouble(location.getCurrLat()),
                            Double.parseDouble(location.getCurrLong()));
                    markers.add(new MarkerOptions()
                            .title(i + "")
                            .position(position));
                }

                for (MarkerOptions option : markers) {
                    mapboxMap.addMarker(option);
                }

                CameraPosition camera = new CameraPosition.Builder()
                        .target(position)// Sets the new camera position
                        .zoom(12) // Sets the zoom
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camera));
                mapboxMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        showDialog(BasicService.staffLocationList.get(Integer.parseInt(marker.getTitle())));
                        return true;
                    }
                });

                mapView.onResume();
            }
        });
    }

    private void showDialog(StaffLocation staffLocation) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_location);
        final ImageView avatar = (ImageView) dialog.findViewById(R.id.avatarMarkerStaff);
        final TextView name = (TextView) dialog.findViewById(R.id.nameMarkerStaff);
        final TextView phone = (TextView) dialog.findViewById(R.id.phoneMarkerStaff);
        final TextView checkinplan = (TextView) dialog.findViewById(R.id.checkinPlanMarkerStaff);
        final TextView checkinFinish = (TextView) dialog.findViewById(R.id.checkinFinishMarkerStaff);
        final TextView total = (TextView) dialog.findViewById(R.id.totalOrderMarkerStaff);

        name.setText(staffLocation.getEmpFullName());
        phone.setText("-");
        checkinplan.setText("" + staffLocation.getCheckInPlan());
        checkinFinish.setText("" + staffLocation.getCheckInFinish());
        total.setText("" + staffLocation.getNoOrder());

        Glide.with(getActivity()).load(staffLocation.getPicLink())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar);

        dialog.show();
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
}
