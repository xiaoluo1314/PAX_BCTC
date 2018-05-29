package cn.com.pax.display;


import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ItemEvent;

public class MyComboBoxUI extends BasicComboBoxUI {
    protected ComboPopup createPopup() {
        return new MyComboBoxPopup(comboBox);
    }
}
class MyComboBoxPopup extends BasicComboPopup {
    private static final int POPUP_MAX_WIDTH = 600;

    public MyComboBoxPopup(JComboBox combo) {
        super(combo);
    }

    protected JScrollPane createScroller() {
        JScrollPane sp = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return sp;
    }

    public void show() {
        int selectedIndex = comboBox.getSelectedIndex();
//        if (selectedIndex  == -1 ) {
//            list.clearSelection();
//        }
//        else {
//            list.setSelectedIndex( selectedIndex );
//            list.ensureIndexIsVisible( selectedIndex );
//        }

        Insets insets = getInsets();
        int popupPrefWid = list.getPreferredSize().width + insets.left + insets.right;

        Dimension scrollSize = new Dimension(comboBox.getWidth(), getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
//		if (popupPrefWid > POPUP_MAX_WIDTH) {
//			scrollSize.width = POPUP_MAX_WIDTH;
//			scrollSize.height += scroller.getHorizontalScrollBar().getPreferredSize().height;
//		}
//		else {
//			scrollSize.width = popupPrefWid;
//		}
        if (popupPrefWid > scrollSize.width) {
            scrollSize.height += scroller.getHorizontalScrollBar().getPreferredSize().height;
        }

        scroller.setMaximumSize(scrollSize);
        scroller.setPreferredSize(scrollSize);
        scroller.setMinimumSize(scrollSize);
        list.revalidate();

        show(comboBox, 0, comboBox.getHeight());
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox) e.getSource();
            setListSelection(comboBox.getSelectedIndex());
        }
    }

    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            list.clearSelection();
        } else {
            ((MyList) list).superSetSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
        }
    }

    protected JList createList() {
        return new MyList();
    }

    class MyList extends JList {
        public MyList() {
            super(comboBox.getModel());
        }

        public void superSetSelectedIndex(int index) {
            super.setSelectedIndex(index);
        }

        public void setSelectedIndex(int index) {
            super.setSelectedIndex(index);
        }

        public void setSelectedIndices(int[] indices) {
            super.setSelectedIndices(indices);
        }

        public void setSelectedValue(Object anObject, boolean shouldScroll) {
            super.setSelectedValue(anObject, shouldScroll);
        }

        public void setSelectionInterval(int anchor, int lead) {
            super.setSelectionInterval(anchor, lead);
        }
    }
}
    