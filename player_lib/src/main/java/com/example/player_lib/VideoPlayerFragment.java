package com.example.player_lib;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {

  private String URL;
  private static String EXTRA_URL = "EXTRA_URL";

  private SimpleExoPlayer player;
  private PlayerView playerView;

  private long playbackPosition;
  private int currentWindow;
  private boolean playWhenReady = true;

  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  private final String CURRENT_WINDOW = "CURRENT_WINDOW";
  private final String PLAYBACK_POSITION = "PLAYBACK_POSITION";

  TextView tvNoVideo;
  private LinearLayout.LayoutParams params;

  public static VideoPlayerFragment newInstance(String url) {
    Bundle args = new Bundle();
    VideoPlayerFragment fragment = new VideoPlayerFragment();
    args.putString(EXTRA_URL, url);
    fragment.setArguments(args);
    return fragment;
  }

  public VideoPlayerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
      playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
    }
    getFragArguments();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putInt(CURRENT_WINDOW, currentWindow);
    outState.putLong(PLAYBACK_POSITION, playbackPosition);
    super.onSaveInstanceState(outState);
  }

  private void getFragArguments() {
    if (getArguments() != null) {
      URL = getArguments().getString(EXTRA_URL);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_video_player, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    playerView = view.findViewById(R.id.video_view);
    tvNoVideo = view.findViewById(R.id.tv_no_video);
    if (URL == null || URL.equals("")) {
      playerView.setVisibility(View.GONE);
      tvNoVideo.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23 && URL != null && !URL.equals("")) {
      initializePlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || player == null) && URL != null && !URL.equals("")) {
      initializePlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23 && player != null) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23 && player != null) {
      releasePlayer();
    }
  }

  private void initializePlayer() {
    if (player == null) {
      TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
      player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
        new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
      playerView.setPlayer(player);
      player.setPlayWhenReady(playWhenReady);
      player.seekTo(currentWindow, playbackPosition);
    }
    MediaSource mediaSource = buildMediaSource(Uri.parse(URL));
    player.prepare(mediaSource, true, false);
  }

  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      playWhenReady = player.getPlayWhenReady();
      player.release();
      player = null;
    }
  }

  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource.Factory(
      new DefaultHttpDataSourceFactory("exoplayer-codelab")
    ).createMediaSource(uri);
  }


}
