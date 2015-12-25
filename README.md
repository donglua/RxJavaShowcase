# RxJavaShowcase
RxJava示例

* 统计连续点击
```java
mClickCountSubject.asObservable() 
    .buffer(mClickCountSubject.throttleWithTimeout(300, TimeUnit.MILLISECONDS)) 
    .map(List::size) 
    .observeOn(AndroidSchedulers.mainThread()) 
    .subscribe(count -> { 
      Snackbar.make(mButton, "连续点击了" + count + "次", LENGTH_SHORT).show();
    }); 
```

* 倒计时

```java
Observable.interval(1, TimeUnit.SECONDS)
        .take(11) 
        .map(i -> { 
          return 10 - i; // i为0~10 11次 
        }) 
        .observeOn(AndroidSchedulers.mainThread()) 
        .subscribe(i -> { 
          btCountDown.setText(i == 0 ? "倒计时" : "" + i);
          if (i == 0) btCountDown.setEnabled(true);
        }); 
```
