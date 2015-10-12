package cc.soham.rxjavasample.network;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.soham.rxjavasample.objects.NetworkResponse;
import cc.soham.rxjavasample.objects.Result;
import rx.Observable;

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
                return rhs.getTrackTimeMillis() - lhs.getTrackTimeMillis();
            }
        });
        return musicList;
    }

    public static Observable<List<Result>> getSortedZippedMusicList() {
        return Observable.zip(getMusic("Metallica"), getMusic("Iron Maiden"), (results1, results2) -> addAll(results1, results2))
                .map(results -> sort(results));
    }
}
