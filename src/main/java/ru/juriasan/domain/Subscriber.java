package ru.juriasan.domain;

import java.util.Objects;
public class Subscriber {

    private String url;

    public Subscriber(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subscriber)) {
            return false;
        }
        Subscriber other = (Subscriber) o;
        return Objects.equals(other.getUrl(), this.url);
    }

    @Override
    public String toString() {
        return this.url;
    }
}
