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
import rx.subjects.PublishSubject;

public class HttpAsyncFragment extends Fragment implements View.OnClickListener {

    private View mClickMeBtn;
    private TextView mText;

    private final Handler mHandler = new Handler();
    private String mComplete = "";

    private PublishSubject<Void> mFirstNetClientSubject;
    private PublishSubject<Void> mSecondNetClientSubject;

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
            mFirstNetClientSubject = PublishSubject.create();
            mSecondNetClientSubject = PublishSubject.create();
            Observable.merge(mFirstNetClientSubject, mSecondNetClientSubject).subscribe(new AsyncHttpSubscriber());

            mComplete = "";
            updateUI(mComplete);

            AsyncHttpClient firstClient = new AsyncHttpClient();
            firstClient.get("http://www.bing.com", new FirstAsyncHttpResponseHandler());

            AsyncHttpClient secondClient= new AsyncHttpClient();
            secondClient.get("http://www.bing.com", new SecondAsyncHttpResponseHandler());
        }
    }

    private void updateUI(final String text){
        mHandler.post(() ->  mText.setText(text));
    }

    private class FirstAsyncHttpResponseHandler extends AsyncHttpResponseHandler{

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            mComplete += "The first Async Http: Success.\r\n";
            mFirstNetClientSubject.onNext(null);
            mFirstNetClientSubject.onCompleted();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            mComplete += "The first Async Http: Failure.\r\n";
            mFirstNetClientSubject.onNext(null);
            mFirstNetClientSubject.onCompleted();
        }

        @Override
        public void onRetry(int retryNo) {

        }
    }

    private class SecondAsyncHttpResponseHandler extends AsyncHttpResponseHandler{

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            mComplete += "The second Async Http: Success.\r\n";
            mSecondNetClientSubject.onNext(null);
            mSecondNetClientSubject.onCompleted();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            mComplete += "The second Async Http: Failure.\r\n";
            mSecondNetClientSubject.onNext(null);
            mSecondNetClientSubject.onCompleted();
        }

        @Override
        public void onRetry(int retryNo) {

        }
    }

    private class AsyncHttpSubscriber extends Subscriber<Void> {

        @Override
        public void onCompleted() {
            updateUI(mComplete);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Void s) {

        }
    }
}
