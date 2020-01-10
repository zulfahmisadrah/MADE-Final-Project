package com.zulfahmi.made_finalproject.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zulfahmi.made_finalproject.R;
import com.zulfahmi.made_finalproject.databases.FilmDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    List<String> listImage;
    private final Context mContext;

    public StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (listImage != null) {
            listImage.clear();
            mWidgetItems.clear();
        }
        final long identityToken = Binder.clearCallingIdentity();
        FilmDatabase db = FilmDatabase.getInstance(mContext);
        listImage = db.filmDAO().getFavoriteFilmImage();
        Binder.restoreCallingIdentity(identityToken);
//        Binder.clearCallingIdentity();
        for (int i = 0; i < listImage.size(); i++) {
            String urlPoster = "https://image.tmdb.org/t/p/w342/" + listImage.get(i);
            try {
                URL ulrn = new URL(urlPoster);
                HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
                InputStream is = con.getInputStream();
                mWidgetItems.add(BitmapFactory.decodeStream(is));
            }catch (IOException e) {
                Log.e("LoadImageError", e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.img_widget, mWidgetItems.get(position));
        Bundle bundle = new Bundle();
        bundle.putInt(FilmWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        rv.setOnClickFillInIntent(R.id.img_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
