package ru.unit.barsdiary.components.bottomnavigationbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BottomBarParser {

    private final Context context;
    private final XmlResourceParser parser;

    private final static String ITEM_TAG = "item";
    private final static String ICON_ATTRIBUTE = "icon";
    private final static String TITLE_ATTRIBUTE = "title";
    private final static String COLOR_ATTRIBUTE = "color";

    public BottomBarParser(Context context, int res) {
        this.context = context;
        this.parser = context.getResources().getXml(res);
    }

    public List<BottomBarItem> parse() throws IOException, XmlPullParserException {
        List<BottomBarItem> items = new ArrayList<>();
        int eventType;

        do {
            eventType = parser.next();
            if (eventType == XmlResourceParser.START_TAG && parser.getName().equals(ITEM_TAG)) {
                items.add(getTabConfig(parser));
            }
        } while (eventType != XmlResourceParser.END_DOCUMENT);

        return items;
    }

    private BottomBarItem getTabConfig(XmlResourceParser parser) {
        int attributeCount = parser.getAttributeCount();
        String itemText = null;
        Drawable itemDrawable = null;
        Integer itemColor = null;

        for (int i = 0; i < attributeCount; i++) {
            switch (parser.getAttributeName(i)) {
                case (ICON_ATTRIBUTE):
                    itemDrawable = ContextCompat.getDrawable(context, parser.getAttributeResourceValue(i, 0));
                    break;

                case (TITLE_ATTRIBUTE):
                    try {
                        itemText = context.getString(parser.getAttributeResourceValue(i, 0));
                    } catch (Resources.NotFoundException notFoundException) {
                        itemText = parser.getAttributeValue(i);
                    }
                    break;

                case (COLOR_ATTRIBUTE):
                    try {
                        String value = parser.getAttributeValue(i);
                        if (value.charAt(0) == '?') {
                            value = value.substring(1);
                            TypedValue typedValue = new TypedValue();
                            Resources.Theme theme = context.getTheme();
                            theme.resolveAttribute(Integer.parseInt(value), typedValue, true);
                            itemColor = typedValue.data;
                        } else {
                            itemColor = context.getColor(Integer.parseInt(value));
                        }
                    } catch (Exception exception) {
                        itemColor = Color.parseColor(parser.getAttributeValue(i));
                    }
                    break;
            }
        }


        if (itemDrawable == null)
            throw new RuntimeException("Item icon can not be null!");
        if (itemColor == null)
            throw new RuntimeException("Item color can not be null!");

        return new BottomBarItem(itemText == null ? "" : itemText, itemDrawable, itemColor);
    }
}
