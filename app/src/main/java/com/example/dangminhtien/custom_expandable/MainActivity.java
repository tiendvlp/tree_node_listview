package com.example.dangminhtien.custom_expandable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout abc12 = (LinearLayout) findViewById(R.id.abc12);
        tiendvlp_expan_tree_listview tiendvlp_expan_tree_listview = new tiendvlp_expan_tree_listview(this, MainActivity.this);
        abc12.addView(tiendvlp_expan_tree_listview);
        tiendvlp_expan_tree_listview.add_tree_node(new tree_node("/Thời trang", "Thời trang"));
        tiendvlp_expan_tree_listview.add_tree_node(new tree_node("/Điện tử", "Điện tử"));
        tiendvlp_expan_tree_listview.add_tree_node(new tree_node("/Thời trang/Nam", "Nam"));
        tiendvlp_expan_tree_listview.add_tree_node(new tree_node("/Thời trang/Nam/Quần", "Quần"));
        tiendvlp_expan_tree_listview.add_tree_node(new tree_node("/Thời trang/Nam/Quần/Quần Kaki", "Quần Kaki"));
        tiendvlp_expan_tree_listview.init();
    }

}
