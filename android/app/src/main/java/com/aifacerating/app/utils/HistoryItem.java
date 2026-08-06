package com.aifacerating.app.utils;

public class HistoryItem {
    private String id;
    private int score;
    private String date;
    private String imageUri;
    private String title;
    private int symmetry;
    private int skin;
    private int eyes;
    private int jaw;
    private int golden;
    private int thirds;

    public HistoryItem(String id, int score, String date, String imageUri, String title, 
                       int symmetry, int skin, int eyes, int jaw, int golden, int thirds) {
        this.id = id;
        this.score = score;
        this.date = date;
        this.imageUri = imageUri;
        this.title = title;
        this.symmetry = symmetry;
        this.skin = skin;
        this.eyes = eyes;
        this.jaw = jaw;
        this.golden = golden;
        this.thirds = thirds;
    }

    public String getId() { return id; }
    public int getScore() { return score; }
    public String getDate() { return date; }
    public String getImageUri() { return imageUri; }
    public String getTitle() { return title; }
    public int getSymmetry() { return symmetry; }
    public int getSkin() { return skin; }
    public int getEyes() { return eyes; }
    public int getJaw() { return jaw; }
    public int getGolden() { return golden; }
    public int getThirds() { return thirds; }
}
