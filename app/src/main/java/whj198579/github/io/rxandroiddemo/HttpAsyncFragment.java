package whj198579.github.io.rxandroiddemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HttpAsyncFragment extends Fragment implements View.OnClickListener {

    private View mClickMeBtn;
    private TextView mText;

    private final Handler mHandler = new Handler();

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

        }
    }

    private void updateUI(final String text){
        mHandler.post(() ->  mText.setText(text));
    }
}
