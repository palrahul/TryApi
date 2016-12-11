package com.watbots.tryapi.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.watbots.tryapi.R;
import com.watbots.tryapi.model.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemView extends RelativeLayout {

    @BindView(R.id.item_logo)
    ImageView itemLogo;
    @BindView(R.id.item_name)
    TextView itemName;
    @BindView(R.id.item_description)
    TextView itemDesc;
    @BindView(R.id.yelp_stars)
    TextView rating;
    @BindView(R.id.status)
    TextView status;

    private final CircleStrokeTransformation avatarTransformation;
    private final int descriptionColor;

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
        descriptionColor = outValue.data;

        // TODO: Make this a singleton.
        avatarTransformation =
                new CircleStrokeTransformation(context, ContextCompat.getColor(context, R.color.img_stroke), 1);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(Item item, Picasso picasso) {
        String imgUrl = "http://l.yimg.com/a/i/us/we/52/" + item.code + ".gif";
        picasso.load(imgUrl)
                .placeholder(R.drawable.doordash_def)
                .fit()
                //.transform(avatarTransformation)
                .into(itemLogo);
        itemName.setText(item.date + item.day);
        rating.setText(String.valueOf(item.low));
        status.setText(String.valueOf(item.high));
        itemDesc.setText(item.text);
    }
}
