package org.busywait.mycamera;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
