package com.app.materialdesigndemo_rxjava.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.app.materialdesigndemo_rxjava.app.model.GifsData;
import com.app.materialdesigndemo_rxjava.app.model.RandomGifs;
import com.app.materialdesigndemo_rxjava.app.presenters.MainFragmentPresenter;
import com.squareup.picasso.Picasso;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ButterKnife.bind(this, view);
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



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void showTrendingList(Pair<GifsData,RandomGifs> gifsData) {
        myAdapter.addData(gifsData.first);
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

        private void clear(){
            gifsDataList.clear();
            notifyDataSetChanged();
        }
    }

}
