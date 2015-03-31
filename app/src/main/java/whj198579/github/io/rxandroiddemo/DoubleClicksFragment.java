package whj198579.github.io.rxandroiddemo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
	private final PublishSubject<Void> mClickSubject = PublishSubject.create();

	private final static int INTERVAL = 500;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.double_clicks_fragment, container, false);

		mClickMeBtn = view.findViewById(R.id.clickMeBtn);
		mText = (TextView) view.findViewById(R.id.text);

		mClickMeBtn.setOnClickListener(this);

		mClickSubject.buffer(new ClickCountCluster())
			.map(new ClickCountMap())
			.filter(new ClickCountFilter())
			.map(new ClickCountOutputMap())
			.subscribe(new ClickCountSubscriber());

		return view;
	}

	@Override
	public void onClick(View v) {
		if(v == mClickMeBtn){
			mClickSubject.onNext(null);
		}
	}

	private void updateUI(final String text){
		mHandler.post(new Runnable(){

			@Override
			public void run() {
				mText.setText(text);
			}

		});
	}
	
	private class ClickCountCluster implements Func0<Observable<Void>>{

		@Override
		public Observable<Void> call() {
			return mClickSubject.debounce(INTERVAL, TimeUnit.MILLISECONDS);
		}
		
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
	
	private class ClickCountFilter implements Func1<Integer, Boolean>{

		@Override
		public Boolean call(Integer count) {
            return count >= 1;
		}
	}
	
	private class ClickCountOutputMap implements Func1<Integer, String>{

		@Override
		public String call(Integer count) {
			if(count == 1){
				return count + "X Click";
			}
			
			return count + "X Clicks";
		}
		
	}
	
	private class ClickCountSubscriber implements Action1<String>{

		@Override
		public void call(String s) {
			updateUI(s);
		}
		
	}
}
