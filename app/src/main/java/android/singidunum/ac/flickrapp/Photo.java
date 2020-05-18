package android.singidunum.ac.flickrapp;

//sadrzi selektovane podatke json objekta o nekoj slici

import java.io.Serializable;

class Photo implements Serializable {

    //klasa koja predstavlja konkretnu fotografiju sa svim informacijama koje preuzimamo

    private String title, author, authorId, link, tags, image, description;

    public Photo(String title, String author, String authorId, String link, String tags, String image, String description) {
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.link = link;
        this.tags = tags;
        this.image = image;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getLink() {
        return this.link;
    }

    public String getTags() {
        return this.tags;
    }

    public String getImage() {
        return this.image;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
