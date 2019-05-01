package com.example.baking.ui.recipedetails;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baking.R;
import com.example.baking.injection.RecipeApplication;
import com.example.baking.models.RecipeModel;
import com.example.baking.models.Steps;
import com.example.baking.repository.RecipeRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

  RecyclerView recyclerView;

  @Inject
  RecipeRepository recipeRepository;

  private static String RECIPE_ID = "RECIPE_ID";

  private int recipeId;

  public interface IFragListener {
    void updateURL(String Url);
  }

  IFragListener iFragListener;

  public static RecipeListFragment newInstance(int recipeId) {
    Bundle args = new Bundle();
    RecipeListFragment fragment = new RecipeListFragment();
    args.putInt(RECIPE_ID, recipeId);
    fragment.setArguments(args);
    return fragment;
  }

  public RecipeListFragment() {

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getActivity() != null)
      ((RecipeApplication) getActivity().getApplication()).getAppComponent().inject(this);
    if (getArguments() != null)
      recipeId = getArguments().getInt(RECIPE_ID, 0);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    Activity activity = (Activity) context;
    try {
      iFragListener = (IFragListener) activity;
    } catch (ClassCastException castException) {
      throw new ClassCastException(activity.toString() +" must implement IFragListener");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_recipe_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = view.findViewById(R.id.recycler_view);
    RecipeModel recipe = recipeRepository.getDataById(recipeId);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(new RecipesDescAdapter(getContext(), recipe.getSteps()));
  }

  public class RecipesDescAdapter extends RecyclerView.Adapter<RecipesDescAdapter.ViewHolder> {
    private Context context;
    private List<Steps> steps;

    public RecipesDescAdapter(Context context, List<Steps> steps) {
      this.context = context;
      this.steps = steps;
    }

    @Override
    public RecipesDescAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_desc_item, parent, false);
      return new RecipesDescAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.title.setText((position+1) + ". " + steps.get(position).getShortDescription());
      holder.desc.setText(steps.get(position).getDescription());
      if (!TextUtils.isEmpty(steps.get(position).getThumbnailURL())) {
        holder.imageIcon.setVisibility(View.VISIBLE);
        Picasso.get()
          .load(steps.get(position).getThumbnailURL())
          .placeholder(R.drawable.image_placeholder)
          .error(R.drawable.image_error)
          .into(holder.imageIcon);
      }
      holder.itemView.setOnClickListener(__->{
        iFragListener.updateURL(steps.get(position).getVideoURL());
      });
    }

    @Override
    public int getItemCount() {
      return steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      TextView title;
      TextView desc;
      LinearLayout layout;
      ImageView imageIcon;
      ViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.text_title);
        desc = view.findViewById(R.id.text_desc);
        imageIcon = view.findViewById(R.id.image_icon);
      }
    }
  }
  
}
