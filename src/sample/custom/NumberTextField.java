package sample.custom;

import javafx.scene.control.TextField;

/**
 * Created by strat on 02.07.15.
 */
public class NumberTextField extends TextField{

    @Override
    public void replaceText(int start, int end, String text) {
        if(text.matches("[0-9]") || text.isEmpty()){
            super.replaceText(start,end,text);
        }
    }

}
