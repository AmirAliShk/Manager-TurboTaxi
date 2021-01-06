package ir.taxi1880.manager.helper;

import android.content.Intent;

import ir.taxi1880.manager.activity.MainActivity;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.fragment.LoginFragment;

public class ContinueProcessing {

    public static void runMainActivity() {
        //if this not checked, the program will be closed when login fragment open from mainActivity

            if (MyApplication.currentActivity.toString().contains(MainActivity.TAG)) {
                FragmentHelper
                        .taskFragment(MyApplication.currentActivity, LoginFragment.TAG)
                        .remove();
                return;
            }
        MyApplication.handler.post(() -> {
            MyApplication.currentActivity.startActivity(new Intent(MyApplication.currentActivity, MainActivity.class));
            MyApplication.currentActivity.finish();
        });

    }
}
