package com.example.dangminhtien.custom_expandable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class tiendvlp_expan_tree_listview extends ScrollView implements View.OnTouchListener{

    private ArrayList<tree_node> tree_nodes = new ArrayList<tree_node>();
    private HashMap<String,LinearLayout> containers = new HashMap<String, LinearLayout>();
    private LinearLayout parent;
    private boolean has_container;
    private boolean is_hide = true;
    private Activity activity;
    private static View view_selected_before = null;

    public tiendvlp_expan_tree_listview(Context context, Activity activity) {
        super(context);
        // khi vừa khởi tạo thì lập tức add layout root vào
        parent = new LinearLayout(getContext());
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setBackgroundColor(getResources().getColor(R.color.color_when_not_selected));
        this.addView(parent);
        this.activity = activity;
        containers.put("/", parent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init () {
      Iterator<tree_node> duyet_tree_node = tree_nodes.iterator();
      tree_node tree_node;
      LinearLayout container = null;
      LinearLayout container_parent = null;
      ArrayList<String> paths = new ArrayList<String>();
            while (duyet_tree_node.hasNext()) {
                tree_node = duyet_tree_node.next();
                paths = split_path_node(tree_node.getNode_path());
                // Bắt đầu duyệt từ 1 là vì ta chắc chắc root (/) đã nằm trong thằng containers
                for (int i = 1; i < paths.size(); i++) {
                    container_parent = get_or_add_container(paths.get(i - 1));
                    container = get_or_add_container(paths.get(i));
                    // Nếu container đã tồn tại thì không cần tạo nữa
                        if (!has_container) {
                    container_parent.addView(container);
                }}
                View row = create_row(tree_node);
                row.setOnTouchListener(this);
                container.addView(row);
            }
    }

    private ArrayList<String> split_path_node (String node_path) {
        String[] paths = node_path.split("/");
        ArrayList<String> new_path_1 = new ArrayList<String>();
        for (String path : paths) {
            new_path_1.add("/" + path);
        }
        ArrayList<String> new_path_2 = new ArrayList<String>();

        for (int i=0; i < new_path_1.size(); i++) {
            if (i == 0) {
                new_path_2.add(new_path_1.get(i));
            } else {
                new_path_2.add(new_path_2.get(i-1)+new_path_1.get(i));
            }
        }
        return new_path_2;
    }

    public void add_tree_node (tree_node item) {
        tree_nodes.add(item);
    }

    private LinearLayout get_or_add_container (String path_tree_node) {
        has_container = true;
        if (containers.get(path_tree_node) == null) {
                containers.put(path_tree_node, create_container());
            has_container = false;
            }
        return containers.get(path_tree_node);
    }

    private LinearLayout create_container () {
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams params_container = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(params_container);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private View create_row (tree_node tree_node) {
        LinearLayout container = get_or_add_container(tree_node.getNode_path());
        View row = LayoutInflater.from(getContext()).inflate(R.layout.row, container, false);
        TextView txt_title_row = (TextView) row.findViewById(R.id.txt_title_row);
        txt_title_row.setText(tree_node.getTitle());
        return row;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (null != view_selected_before) {
            ((TextView)view_selected_before.findViewById(R.id.txt_title_row)).setTextColor(Color.parseColor("#444444"));
        }
        v.setBackgroundColor(Color.RED);
        v.setBackgroundColor(Color.TRANSPARENT);

        view_selected_before = v;
        if (is_hide) {
            hide_view(((LinearLayout)v.getParent()));
            is_hide = false;
        } else {
            show_view(((LinearLayout)v.getParent()));
            ((TextView)v.findViewById(R.id.txt_title_row)).setTextColor(Color.parseColor("#F38F20"));
            is_hide = true;
       }
       // nếu không return false sẽ bị vòng lặp
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hide_view(final ViewGroup container) {
        int count=container.getChildCount();
        final int[] i = {1};
        CountDownTimer countDownTimer = new CountDownTimer(300*count, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                final View view = container.getChildAt(i[0]++);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_hide_row_when_collapse);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                    if (null != view && null != animation) {
                    view.startAnimation(animation);}
            }

            @Override
            public void onFinish() {

            }
        };
        if (null != container.getChildAt(1)) {
            countDownTimer.start();
        }

    }

    private void show_view (final ViewGroup container) {
        int count = container.getChildCount();
        final int[] i = {1};
        CountDownTimer countDownTimer = new CountDownTimer(500*count, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                final View view = container.getChildAt(i[0]++);
                if (view != null) {
                view.setVisibility(VISIBLE);
                }
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_show_row_when_expand);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setBackgroundColor(getResources().getColor(R.color.color_child_when_parent_selected));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                if (null != view && null != animation) {
                    view.startAnimation(animation);
                }
            }

            @Override
            public void onFinish() {

            }
        };
            // check xem view thứ 2 có null hay không nếu có null có nghĩa là thằng này không có child thì không cần start hiệu ứng
            if (null != container.getChildAt(1)) {
                countDownTimer.start();
            }
    }
}
