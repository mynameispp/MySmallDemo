package com.ffzxnet.mysmall.app.seconde;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ffzxnet.mysmall.lib.utils.ToasUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Glide.with(this).load("https://timgsa.baidu.com/timg?" +
                "image&quality=80&size=b9999_10000&sec=1492412103&" +
                "di=d2595404d2a9d0e945448f3527110220&imgtype=jpg&er=1&src=" +
                "http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%" +
                "3D430a67ba932397ddd679980c6983b216%2F431c6d381f30e9249e" +
                "abc26c4e086e061d95f770.jpg").into((ImageView) findViewById(R.id.img));
//        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.fresco);
//        simpleDraweeView.setImageURI(Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492412103&di=d2595404d2a9d0e945448f3527110220&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D430a67ba932397ddd679980c6983b216%2F431c6d381f30e9249eabc26c4e086e061d95f770.jpg"));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToasUtil.show(v.getContext(), "引用lib toast");
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), PiePolylineChartActivity.class));
            }
        });


        //线形图
        LineChart chart = (LineChart) findViewById(R.id.chart);

//        chart.setBackgroundColor(Color.DKGRAY);//整个空间的背景色

        Description description = new Description();//描述文字对象
        description.setText("测试");//设置图表的描述文字
        description.setTextColor(Color.RED);//文字颜色
//        description.setPosition();//位置，像素单位
        description.setTextSize(16f);//大小
        chart.setDescription(description);// : 设置图表的描述文字，会显示在图表的右下角。

        chart.setNoDataText("暂时没有数据哦");

//        chart.setDrawGridBackground(boolean enabled);// : 如果启用，chart 绘图区后面的背景矩形将绘制。
//        chart.setGridBackgroundColor(int color);// : 设置网格背景应与绘制的颜色

//        chart.setDrawBorders(boolean enabled);// : 启用/禁用绘制图表边框（chart周围的线）。
//        chart.setBorderColor(int color);// : 设置 chart 边框线的颜色。
//        chart.setBorderWidth(float width);// : 设置 chart 边界线的宽度，单位 dp。


        //设置数据
        ArrayList<Entry> entryList1 = new ArrayList<Entry>();
        ArrayList<Entry> entryList2 = new ArrayList<Entry>();
        Entry c1e1 = new Entry(40.000f, (float) 0.8); // 0 == quarter 1
        entryList1.add(c1e1);
        Entry c1e2 = new Entry(60.000f, (float) 0.6);  // 1 == quarter 2 ...
        entryList1.add(c1e2);
        Entry c1e3 = new Entry(70.000f, (float) 0.7);  // 1 == quarter 2 ...
        entryList1.add(c1e3);

        Entry c2e1 = new Entry(40.000f, (float) 0.76); // 0 == quarter 1
        entryList2.add(c2e1);
        Entry c2e2 = new Entry(60.000f, (float) 0.6); // 1 == quarter 2 ...
        entryList2.add(c2e2);
        Entry c2e3 = new Entry(70.000f, (float) 0.68); // 1 == quarter 2 ...
        entryList2.add(c2e3);

        ArrayList<ILineDataSet> allLinesList = new ArrayList<ILineDataSet>();
        //LineDataSet可以看做是一条线
        LineDataSet dataSet1 = new LineDataSet(entryList1, "dataLine1");
        LineDataSet dataSet2 = new LineDataSet(entryList2, "dataLine2");

        // 上面左效果图的代码
        // dataSet1.setColor(Color.RED);
        // dataSet2.setColor(Color.GREEN);

        // sets colors for the dataset,
        // resolution of the resource name to a "real" color is done internally
        dataSet1.setColors(new int[]{R.color.colorAccent}, this);
        dataSet2.setColors(new int[]{R.color.colorPrimary}, this);

        allLinesList.add(dataSet1);
        allLinesList.add(dataSet2);

        List<String> quarterStrs = new ArrayList<String>();
        quarterStrs.add("第一季度");
        quarterStrs.add("第二季度");
        quarterStrs.add("第三季度");
        quarterStrs.add("第四季度");
        //LineData表示一个LineChart的所有数据(即一个LineChart中所有折线的数据)
        LineData mChartData = new LineData(allLinesList);
        chart.setData(mChartData);

    }
}
