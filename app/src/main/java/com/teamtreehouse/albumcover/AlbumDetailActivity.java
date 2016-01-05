package com.teamtreehouse.albumcover;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlbumDetailActivity extends Activity {

    public static final String EXTRA_ALBUM_ART_RESID = "EXTRA_ALBUM_ART_RESID";

    @Bind(R.id.album_art) ImageView albumArtView;
    @Bind(R.id.fab) ImageButton fab;
    @Bind(R.id.title_panel) ViewGroup titlePanel;
    @Bind(R.id.track_panel) ViewGroup trackPanel;
    @Bind(R.id.detail_container) ViewGroup detailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        ButterKnife.bind(this);
        populate();
    }

    @OnClick(R.id.album_art)
    public void onAlbumArtClick(View view) {

    }

    private void populate() {
        int albumArtResId = getIntent().getIntExtra(EXTRA_ALBUM_ART_RESID, R.drawable.mean_something_kinder_than_wolves);
        albumArtView.setImageResource(albumArtResId);

        // Reduce bitmap size
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;

        // asynchronously generate panel colors from Palette
        Bitmap albumBitmap = BitmapFactory.decodeResource(getResources(), albumArtResId, options);
        if (albumBitmap != null && !albumBitmap.isRecycled()) {
            new Palette.Builder(albumBitmap).generate(paletteListener);
        }
    }

    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
        public void onGenerated(Palette palette) {
            // access palette colors here
            int defaultPanelColor = 0xFF808080;
            int defaultFabColor = 0xFFEEEEEE;
            titlePanel.setBackgroundColor(palette.getDarkVibrantColor(defaultPanelColor));
            trackPanel.setBackgroundColor(palette.getLightMutedColor(defaultPanelColor));

            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled },
                    new int[] { android.R.attr.state_pressed }
            };

            int[] colors = new int[] {
                    palette.getVibrantColor(defaultFabColor),
                    palette.getLightVibrantColor(defaultFabColor)
            };
            fab.setBackgroundTintList(new ColorStateList(states, colors));
        }
    };
}
