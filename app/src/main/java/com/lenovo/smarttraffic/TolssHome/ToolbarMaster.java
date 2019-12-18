package com.lenovo.smarttraffic.TolssHome;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import com.lenovo.smarttraffic.R;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class ToolbarMaster extends LinearLayout {
    private static Toolbar toolbar;
    private static TextView title;
    private static Context mContext;

    public ToolbarMaster(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        title = findViewById(R.id.toolbar_title);
    }

    public static void MenuCreate() {
        toolbar.setNavigationOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
        });
    }

    public static void setTitle(String T) {
        title.setText(T); /*设置Title*/
    }
}
