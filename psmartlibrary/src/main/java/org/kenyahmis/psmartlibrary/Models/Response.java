package org.kenyahmis.psmartlibrary.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GMwasi on 2/10/2018.
 */

public abstract class Response {

    private List<String> empty = new ArrayList<>();
    private List<String> errors;

    public Response(List<String> errors) {
        this.errors = errors != null ? errors : empty;
    }

    public boolean isSuccessful() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public abstract String getMessage();

}
