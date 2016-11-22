package com.androidrinomediarino.mediaplayerino;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Note:
 * Use the {@link CurrentPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentPlaylistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MusicScanner scanner = MusicScanner.getInstance();

    private OnFragmentInteractionListener mListener;

    public CurrentPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentPlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentPlaylistFragment newInstance(String param1, String param2) {
        CurrentPlaylistFragment fragment = new CurrentPlaylistFragment();
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
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final List<String> songList = new ArrayList<>();

        for (SongList.Song song : scanner.getMusicFiles()) {
            songList.add(song.songName + " - " + song.artistName);
        }

        SongAdapter adapter = new SongAdapter(getContext(), R.layout.songlist_listview_item_layout, scanner.getMusicFiles());

        ListView playlist = (ListView) view.findViewById(R.id.listView_playlist);
        playlist.setAdapter(adapter);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongList.Song selectedSong = (SongList.Song) parent.getItemAtPosition(position);
               if (mListener != null) {
                   mListener.btn_playlistSongSelect_onClick(selectedSong);
               }
            }
        });

        view.findViewById(R.id.btn_switchFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.btn_fragmentSwitch_onClick();
                }
            }
        });
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
     */
    public interface OnFragmentInteractionListener {
        void btn_fragmentSwitch_onClick();
        void btn_playlistSongSelect_onClick(SongList.Song song);
    }

}
