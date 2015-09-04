package me.iwf.app.rxjava;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

  @InjectView(R.id.button1)
  Button mButton;

  @InjectView(R.id.bt_count_down)
  Button btCountDown;

  @InjectView(R.id.edittext)
  EditText mEditText;

  @InjectView(R.id.tv_status)
  TextView tvStatus;


  private PublishSubject<View> mClickCountSubject;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    ButterKnife.inject(this);

    mClickCountSubject = PublishSubject.create();

    mClickCountSubject.asObservable()
        .buffer(mClickCountSubject.throttleWithTimeout(300, TimeUnit.MILLISECONDS))
        .map(List::size)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(count -> {
          Snackbar.make(mButton, "连续点击了" + count + "次", LENGTH_SHORT).show();
        });

    RxTextView.textChangeEvents(mEditText)
        .throttleWithTimeout(300, TimeUnit.MILLISECONDS)
        .map((ev) -> ev.text().length())
        .map(length -> length == 11)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(b -> tvStatus.setText(b ? "提交" : ""));

  }


  @OnClick(R.id.button1)
  public void buttonClick(View v) {
    mClickCountSubject.onNext(v);
  }

  @OnClick(R.id.bt_count_down)
  public void countDownClick(final View v) {
    btCountDown.setEnabled(false);

    Subscription subscription = Observable.interval(1, TimeUnit.SECONDS)
        .take(11)
        .map(i -> {
          return 10 - i; // i为0~10 11次
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(i -> {
          btCountDown.setText(i == 0 ? "倒计时" : "" + i);
          if (i == 0) btCountDown.setEnabled(true);
        });

  }


  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
