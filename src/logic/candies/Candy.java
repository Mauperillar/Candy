package logic.candies;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Candy {
    final String name;
    final String sign;
    final String srcIcon;
    final String color;
    final Icon icon;

    Candy(String name, String sign, String srcIcon, String color){
        this.name = name;
        this.sign = sign;
        this.srcIcon = srcIcon;
        this.color = color;
        ImageIcon iconImagen = new ImageIcon(getClass().getResource("/logic/candies"+srcIcon));
        this.icon = (Icon) new ImageIcon(iconImagen.getImage().getScaledInstance(69, 67, Image.SCALE_AREA_AVERAGING));
        
    }

    public Icon getIcon(){
        return this.icon;
    }
}