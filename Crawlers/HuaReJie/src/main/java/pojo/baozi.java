package pojo;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-19 14:11
 */

public class baozi {
    private int id;
    private String year;
    private String month;
    private String day;
    private String title;
    private String type;
    private String time;
    private String subheading;
    private String author;
    private String text;
    private String figure;
    private String url;

    public baozi(int id, String year, String month, String day, String title, String type, String time,
                 String subheading, String author, String text, String figure, String url) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.type = type;
        this.time = time;
        this.subheading = subheading;
        this.author = author;
        this.text = text;
        this.figure = figure;
        this.url = url;
    }

    public baozi(String year, String month, String day, String title, String type, String time, String subheading,
                 String author, String text, String figure, String url) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.type = type;
        this.time = time;
        this.subheading = subheading;
        this.author = author;
        this.text = text;
        this.figure = figure;
        this.url = url;
    }

    @Override
    public String toString() {
        return "baozi{" +
                "id=" + id +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", subheading='" + subheading + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", figure='" + figure + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public baozi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
