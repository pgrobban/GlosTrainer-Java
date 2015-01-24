package glostrainer.view;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.border.TitledBorder;

/**
 * A TitleBorder with custom insets, to reduce padding.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class CustomTitledBorder extends TitledBorder
{

    private Insets customInsets = new Insets(15, 5, 5, 5);

    public CustomTitledBorder(String title)
    {
        super(title);
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return customInsets;
    }
};
