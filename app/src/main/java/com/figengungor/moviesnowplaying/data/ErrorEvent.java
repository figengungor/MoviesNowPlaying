package com.figengungor.moviesnowplaying.data;

/**
 * Created by figengungor on 12/9/2017.
 */

public class ErrorEvent {

    String message;

    public ErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
