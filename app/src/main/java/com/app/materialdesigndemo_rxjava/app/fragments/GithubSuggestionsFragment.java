package com.app.materialdesigndemo_rxjava.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.materialdesigndemo_rxjava.app.R;
import com.app.materialdesigndemo_rxjava.app.model.GithubUsers;
import com.app.materialdesigndemo_rxjava.app.presenters.GithubSuggestionsFragmentPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;


public class GithubSuggestionsFragment extends Fragment implements GithubSuggestionsFragmentPresenter.GithubSuggestionsFragmentPresenterInterface {
	@Bind(R.id.recycler_view)
	RecyclerView recyclerView;


	@OnClick(R.id.refresh_button)
	public void makeCall() {
		showUsers();
	}


	CompositeSubscription compositeSubscription;
	GithubSuggestionsFragmentPresenter githubSuggestionsFragmentPresenter;
	GithubSuggestionAdapter githubSuggestionAdapter;
	List<GithubUsers> githubUsers;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		compositeSubscription = new CompositeSubscription();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_github, container, false);
		ButterKnife.bind(this, view);
		githubSuggestionsFragmentPresenter = new GithubSuggestionsFragmentPresenter(this, compositeSubscription);
		showUsers();

		return view;
	}

	private void showUsers() {
		githubSuggestionsFragmentPresenter.getGithubUsers();
	}


	private void setupRecyclerView(List<GithubUsers> githubUsers) {
		githubSuggestionAdapter = new GithubSuggestionAdapter(githubUsers);
		LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayout);
		recyclerView.setAdapter(githubSuggestionAdapter);
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
		githubSuggestionsFragmentPresenter.onDestroyView();
	}

	public static GithubSuggestionsFragment newInstance() {
		return new GithubSuggestionsFragment();
	}

	@Override
	public void setUserDetails(List<GithubUsers> githubUsers) {
		setupRecyclerView(githubUsers);
		this.githubUsers = githubUsers;

	}

	@Override
	public void setRandomUser(int position, GithubUsers githubUsers) {
		this.githubUsers.set(position,githubUsers);
		githubSuggestionAdapter.notifyItemChanged(position,null);

	}

	public class GithubSuggestionAdapter extends RecyclerView.Adapter<GithubSuggestionAdapter.MainViewHolder> {
		List<GithubUsers> gitHubUsersList;
		public GithubSuggestionAdapter(List<GithubUsers> githubUsersList){
			this.gitHubUsersList = githubUsersList;

		}
		@Override
		public GithubSuggestionAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
			ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.item_row,parent,false);
			return new MainViewHolder(viewGroup);
		}

		@Override
		public void onBindViewHolder(GithubSuggestionAdapter.MainViewHolder holder, int position) {
			MainViewHolder listViewHolder = holder;
			listViewHolder.textView.setText(githubUsers.get(position).login);
			Picasso.with(getActivity()).load(githubUsers.get(position).avatar_url).into(listViewHolder.imageView);
		}

		@Override
		public int getItemCount() {
			return gitHubUsersList.size();
		}

		public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			@Bind(R.id.text_view)
			TextView textView;

			@Bind(R.id.close_button)
			Button closeButton;

			@Bind(R.id.image_view)
			ImageView imageView;

			public MainViewHolder(View itemView) {
				super(itemView);
				ButterKnife.bind(this, itemView);
				textView.setOnClickListener(this);
				closeButton.setOnClickListener(this);
			}


			@Override
			public void onClick(View v) {
			if(v.getId() == R.id.text_view){
				Toast.makeText(getActivity(),"Will redirect user to: " + githubUsers.get(getAdapterPosition()).html_url,Toast.LENGTH_SHORT).show();
			}else if(v.getId() == R.id.close_button){
				githubSuggestionsFragmentPresenter.getRandomUser(getAdapterPosition());
			}
			}
		}
	}
}



