package com.uottawa.despacithree.despacithree;

public class Rating {
    private String homeownerId;
    private String homeownerName;
    private String comment;
    private int rating;

    public Rating() {
    }

    public Rating(String homeownerId, String homeownerName, String comment, int rating) {
        this.homeownerId = homeownerId;
        this.homeownerName = homeownerName;
        this.comment = comment;
        this.rating = rating;
    }

    public String getHomeownerId() {
        return homeownerId;
    }

    public void setHomeownerId(String homeownerId) {
        if (homeownerId == null) {
            throw new NullPointerException();
        }

        this.homeownerId = homeownerId;
    }

    public String getHomeownerName() {
        return homeownerName;
    }

    public void setHomeownerName(String homeownerName) {
        if (homeownerName == null) {
            throw new NullPointerException();
        }

        this.homeownerName = homeownerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment == null) {
            throw new NullPointerException();
        }

        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
