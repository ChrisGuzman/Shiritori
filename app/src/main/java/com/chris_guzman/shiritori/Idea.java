package com.chris_guzman.shiritori;

public class Idea {
    private String name;
    private Idea ancestor;
    private boolean isFavorite;

    public Idea(String name, Idea ancestor) {
        this.name = name;
        this.ancestor = ancestor;
    }

    public Idea() {}

    public Idea(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Idea getAncestor() {
        return ancestor;
    }

    public void setAncestor(Idea ancestor) {
        this.ancestor = ancestor;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
