package whj198579.github.io.rxandroiddemo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DoubleClicksFragment extends Fragment implements OnClickListener{

	private View mClickMeBtn;
	private TextView mText;

	private final Handler mHandler = new Handler();
	private final PublishSubject<Void> mDoubleClickSubject = PublishSubject.create();
    private final PublishSubject<Void> mSingleClickSubject = PublishSubject.create();

	private final static int INTERVAL = 500;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.double_clicks_fragment, container, false);

		mClickMeBtn = view.findViewById(R.id.clickMeBtn);
		mText = (TextView) view.findViewById(R.id.text);

		mClickMeBtn.setOnClickListener(this);

        mDoubleClickSubject.buffer(() ->  mDoubleClickSubject.debounce(INTERVAL, TimeUnit.MILLISECONDS))
			.map(new ClickCountMap())
			.filter(count -> count > 1)
			.map(count -> count + "X Clicks")
			.subscribe(new ClickCountSubscriber());

        mSingleClickSubject.buffer(() ->  mDoubleClickSubject.debounce(INTERVAL, TimeUnit.MILLISECONDS))
                .map(new ClickCountMap())
                .filter(count -> count == 1)
                .map(count -> count + "X Click")
                .subscribe(new ClickCountSubscriber());

		return view;
	}

	@Override
	public void onClick(View v) {
		if(v == mClickMeBtn){
            mDoubleClickSubject.onNext(null);
            mSingleClickSubject.onNext(null);
		}
	}

	private void updateUI(final String text){
		mHandler.post(() -> mText.setText(text));
	}

	private class ClickCountMap implements Func1<List<Void>, Integer>{

		@Override
		public Integer call(List<Void> list) {
			if(list == null){
				return 0;
			}

			return list.size();
		}

	}

	private class ClickCountSubscriber implements Action1<String>{

		@Override
		public void call(String s) {
			updateUI(s);
		}

	}
}
