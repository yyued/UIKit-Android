package com.yy.codex.uikit.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.yy.codex.uikit.CGRect;
import com.yy.codex.uikit.CGSize;
import com.yy.codex.uikit.NSIndexPath;
import com.yy.codex.uikit.UIColor;
import com.yy.codex.uikit.UIConstraint;
import com.yy.codex.uikit.UILabel;
import com.yy.codex.uikit.UIScrollView;
import com.yy.codex.uikit.UITableView;
import com.yy.codex.uikit.UITableViewCell;
import com.yy.codex.uikit.UITableViewCellStyle;
import com.yy.codex.uikit.UIView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by it on 17/1/10.
 */

public class MyMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyTestView(this));
    }

}

class MyTestView extends UIView implements UITableView.UITableViewDataSource {

    public MyTestView(Context context, View view) {
        super(context, view);
    }

    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void init() {
        super.init();
//        UIScrollView scrollView = new UIScrollView(getContext());
//        UIConstraint constraint = new UIConstraint();
//        constraint.setCenterHorizontally(true);
//        constraint.setCenterVertically(true);
//        constraint.setWidth("100%");
//        constraint.setHeight("100%");
//        scrollView.setConstraint(constraint);
//        scrollView.setBounces(true);
////        scrollView.setPagingEnabled(true);
//        scrollView.setBackgroundColor(UIColor.Companion.getBlackColor().colorWithAlpha(0.1));
//        for (int i = 0; i < 100; i++) {
//            UIView redView = new UIView(getContext());
//            redView.setFrame(new CGRect(100 * i, 0,20,20));
//            redView.setBackgroundColor(new UIColor(1, 0, 0, 1.0 - (i / 100.0)));
//            if (i == 0) {
//                redView.setBackgroundColor(UIColor.Companion.getBlueColor());
//            }
//            scrollView.addSubview(redView);
//        }
//        scrollView.setContentSize(new CGSize(100 * 100, 0));

        UITableView tableView = new UITableView(getContext());
        tableView.setDataSource(this);
        tableView.setBounces(true);
        UIConstraint constraint = new UIConstraint();
        constraint.setCenterHorizontally(true);
        constraint.setCenterVertically(true);
        constraint.setWidth("100%");
        constraint.setHeight("100%");
        tableView.setConstraint(constraint);

        addSubview(tableView);
    }

    @Override
    public int tableViewNumberOfRowsInSection(@NotNull UITableView tableView, int section) {
        return 100;
    }

    @NotNull
    @Override
    public UITableViewCell tableViewCellForRowAtIndexPath(@NotNull UITableView tableView, @NotNull NSIndexPath indexPath) {

        UITableViewCell cell = tableView.dequeueReusableCellWithIdentifier("test");
        if (cell == null) {
            cell = new UITableViewCell(getContext(), UITableViewCellStyle.UITableViewCellStyleDefault, "test");
            UILabel label = new UILabel(getContext());
            label.setText("" + indexPath.getRow());
            label.setFrame(new CGRect(0, 0, 100, 100));
            cell.addSubview(label);
        }
        return cell;
    }

    @Override
    public int numberOfSectionsInTableView(@NotNull UITableView tableView) {
        return 1;
    }

    @Nullable
    @Override
    public String tableViewTitleForHeaderInSection(@NotNull UITableView tableView, int section) {
        return "TTT";
    }
}
