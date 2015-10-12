package cc.soham.rxjavasample.network;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.soham.rxjavasample.objects.NetworkResponse;
import cc.soham.rxjavasample.objects.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sohammondal on 12/10/15.
 */
public class Music {
    public static Observable<List<Result>> getMusic(String music) {
        Observable<NetworkResponse> networkResponseObservable = MusicApi.getApi().getMusicObservable(music);
        return networkResponseObservable.map(networkResponse -> networkResponse.getResults());
    }

    public static List<Result> addAll(List<Result> musicList1, List<Result> musicList2) {
        if (musicList1 == null || musicList2 == null)
            throw new IllegalArgumentException("The list parameter is null");
        musicList1.addAll(musicList2);
        return musicList1;
    }

    public static List<Result> sort(List<Result> musicList) {
        Collections.sort(musicList, new Comparator<Result>() {
            @Override
            public int compare(Result lhs, Result rhs) {
                return lhs.getTrackTimeMillis() - rhs.getTrackTimeMillis();
            }
        });
        return musicList;
    }

    public static void start() {
        Observable.zip(getMusic("Metallica"), getMusic("Beyonce"), (results1, results2) -> {
            return addAll(results1, results2);
        }).map(results -> sort(results))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    for (Result result : results) {
                        Log.d("Music", result.getTrackName() + ", " + result.getTrackTimeMillis());
                    }
                });
    }
}
