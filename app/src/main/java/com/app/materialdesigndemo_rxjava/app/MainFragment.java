package com.app.materialdesigndemo_rxjava.app;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import com.app.materialdesigndemo_rxjava.app.presenters.MainFragmentPresenter;
import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;
import rx.Observable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pratik on 6/25/16.
 */
public class MainFragment extends Fragment implements MainFragmentPresenter.MainFragmentPresenterInterface {
    MyAdapter myAdapter;
    MainFragmentPresenter mainFragmentPresenter;

    @Bind(R.id.images_list)
    RecyclerView recyclerView;

    @Bind(R.id.editText)
    EditText editText;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        setUpRecyclerView();
        mainFragmentPresenter = new MainFragmentPresenter(this);

        return view;

    }

    private void setUpRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
         searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainFragmentPresenter.startDebounceListenerForSearch(searchView);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void showTrendingList(Pair<GifsData, RandomGifs> gifsData) {
        myAdapter.addData(gifsData.first);
    }

    @Override
    public void displaySearchedGifsList(GifsData gifsData) {
        myAdapter.addData(gifsData);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MainViewHolder> {
        List<GifsData.DataObject> gifsDataList = new ArrayList<>();

        @Override
        public MyAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row, parent, false);
            return new MainViewHolder(v);
        }


        @Override
        public void onBindViewHolder(MyAdapter.MainViewHolder holder, int position) {
            GifsData.DataObject element = gifsDataList.get(position);
            String imageURL = element.imagesObject.fixedHeight.url;
            Picasso.with(getActivity()).load(imageURL).into(holder.gifsImageView);
        }

        @Override
        public int getItemCount() {
            return gifsDataList.size();
        }

        public class MainViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.gifs_image_view)
            ImageView gifsImageView;

            public MainViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        private void addData(GifsData gifsData) {
            gifsDataList.addAll(gifsData.data);
            notifyDataSetChanged();
        }

        private void clear() {
            gifsDataList.clear();
            notifyDataSetChanged();
        }

    }

}
