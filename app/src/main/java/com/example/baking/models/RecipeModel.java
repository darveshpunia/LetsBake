package com.example.baking.models;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RecipeModel {

  public RecipeModel() {}

  int id;
  String name;
  List<Ingredients> ingredients;
  List<Steps> steps;
  int servings;
  String image;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredients> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredients> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Steps> getSteps() {
    return steps;
  }

  public void setSteps(List<Steps> steps) {
    this.steps = steps;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
