package whj198579.github.io.rxandroiddemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import rx.Observable;
import rx.Subscriber;

public class HttpAsyncFragment extends Fragment implements View.OnClickListener {

    private View mClickMeBtn;
    private TextView mText;

    private final Handler mHandler = new Handler();

    private Observable<Void> mResponseObservable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.http_async_fragment, container, false);

        mClickMeBtn = view.findViewById(R.id.clickMeBtn);
        mText = (TextView) view.findViewById(R.id.text);

        mClickMeBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == mClickMeBtn){
            addText("clear");

            Observable<String> mRequestObservable = Observable.merge( Observable.just("http://www.bing.com"), Observable.just("http://www.bing.com"));
            mRequestObservable.subscribe(new RequestSubscriber());
        }
    }

    private void addText(final String text){
        mHandler.post(() ->  mText.setText(mText.getText() + "\r\n" + text));
    }

    private Observable<Void> createObservable(String url){
        Observable<Void> myObservable = Observable.create((sub) -> {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler(){

                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    sub.onNext(null);
                    sub.onCompleted();;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    sub.onNext(null);
                    sub.onCompleted();;
                }

                @Override
                public void onRetry(int retryNo) {

                }
            });
        });

        return myObservable;
    }

    private class RequestSubscriber extends Subscriber<String>{

        @Override
        public void onCompleted() {
            addText("RequestSubscriber: onCompleted");

            mResponseObservable.subscribe(new ResponseSubscriber());
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String url) {
            addText("RequestSubscriber: onNext");

            if (mResponseObservable == null){
                mResponseObservable = createObservable(url);
            } else{
                mResponseObservable = Observable.merge(mResponseObservable, createObservable(url));
            }
        }

    }

    private class ResponseSubscriber extends Subscriber<Void> {

        @Override
        public void onCompleted() {
            addText("ResponseSubscriber: onCompleted");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Void s) {

        }
    }
}
