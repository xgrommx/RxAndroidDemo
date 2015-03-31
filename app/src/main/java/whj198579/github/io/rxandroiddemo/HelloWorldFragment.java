package whj198579.github.io.rxandroiddemo;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelloWorldFragment extends Fragment implements OnClickListener{

	private View mComplexHelloBtn;
	private View mNeatHelloBtn;
	private View mMapHelloBtn;
	private View mCalculateNeatHelloBtn;
	private View mCalculateMapHelloBtn;
	private TextView mText;
	
	private Observable<String> mComplexHelloObservable;
	private ComplexHelloSubscriber mComplexHelloSubscriber;
	private Observable<String> mNeatHelloObservable;
	private NeatHelloSubscriber mNeatHelloSubscriber;
	
	private Observable<String> mMapHelloObservable;
	private Observable<String> mCaculateNeatHelloObservable;
	private Observable<String> mCaculateMapHelloObservable;
	
	private final Handler mHandler = new Handler();
	private final String HELLO_WORLD = "Hello, world!";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hello_world_fragment, container, false);
		
		mComplexHelloBtn = view.findViewById(R.id.complexHelloBtn);
		mNeatHelloBtn = view.findViewById(R.id.neatHelloBtn);		
		mMapHelloBtn = view.findViewById(R.id.mapHelloBtn);		
		mCalculateNeatHelloBtn = view.findViewById(R.id.calculateNeatHelloBtn);
		mCalculateMapHelloBtn = view.findViewById(R.id.calculateMapHelloBtn);
		mText = (TextView) view.findViewById(R.id.text);
		
		mComplexHelloBtn.setOnClickListener(this);
		mNeatHelloBtn.setOnClickListener(this);
		mMapHelloBtn.setOnClickListener(this);
		mCalculateNeatHelloBtn.setOnClickListener(this);
		mCalculateMapHelloBtn.setOnClickListener(this);
		
		mComplexHelloObservable = Observable.create(new ComplexHelloOnSubscribe());
		mComplexHelloSubscriber = new ComplexHelloSubscriber();
		
		mNeatHelloObservable = Observable.just("Neat: " + HELLO_WORLD);
		mNeatHelloSubscriber = new NeatHelloSubscriber();
		
		mMapHelloObservable = mNeatHelloObservable.map(new NeatHelloWithMap());
		mCaculateNeatHelloObservable = mNeatHelloObservable.map(new CaculateStringMap()).map(new IntegerToStringMap());
		mCaculateMapHelloObservable = mMapHelloObservable.map(new CaculateStringMap()).map(new IntegerToStringMap());
		
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v == mComplexHelloBtn){
			mComplexHelloObservable.subscribe(mComplexHelloSubscriber);
		} else if(v == mNeatHelloBtn){
			mNeatHelloObservable.subscribe(mNeatHelloSubscriber);
		} else if(v == mMapHelloBtn){
			mMapHelloObservable.subscribe(mNeatHelloSubscriber);
		} else if(v == mCalculateNeatHelloBtn){
			mCaculateNeatHelloObservable.subscribe(mNeatHelloSubscriber);
		} else if(v == mCalculateMapHelloBtn){
			mCaculateMapHelloObservable.subscribe(mNeatHelloSubscriber);
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
	
	private class ComplexHelloOnSubscribe implements OnSubscribe<String>{

		@Override
		public void call(Subscriber<? super String> sub) {
			sub.onNext("Complex: " + HELLO_WORLD);  
			sub.onCompleted();
		}
		
	}
	
	private class ComplexHelloSubscriber extends Subscriber<String>{

		@Override
		public void onCompleted() {
			
		}

		@Override
		public void onError(Throwable e) {
			
		}

		@Override
		public void onNext(String s) {
			updateUI(s);
		}
		
	}
	
	private class NeatHelloSubscriber implements Action1<String>{

		@Override
		public void call(String s) {
			updateUI(s);
		}
		
	}
	
	private class NeatHelloWithMap implements Func1<String, String>{

		@Override
		public String call(String s) {
			return "After map: " + s;
		}
		
	}
	
	private class CaculateStringMap implements Func1<String, Integer>{

		@Override
		public Integer call(String s) {
			return s.length();
		}
		
	}
	
	private class IntegerToStringMap implements Func1<Integer, String>{

		@Override
		public String call(Integer i) {
			return Integer.toString(i);
		}
		
	}
}
