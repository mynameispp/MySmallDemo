package com.ffzxnet.mysmall.app.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import net.wequick.small.Small;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), String.format(getString(R.string.txt), "贵啊"), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //跳转
//                Small.setUp(v.getContext(), new net.wequick.small.Small.OnCompleteListener() {
//                    @Override
//                    public void onComplete() {
                Small.openUri("seconde", v.getContext());
//                    }
//                });
            }
        });
    }
}
