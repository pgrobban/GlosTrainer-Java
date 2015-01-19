package glostrainer;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * This class contains static helper methods (functions).
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class LibHelpers
{

    private LibHelpers()
    {
    }

    /**
     * Retrieves an <code>ImageIcon</code> from the <code>images.jar</code>
     * archive from the given file name. If the file with the given file name
     * does not exist or could not be read, <code>null</code> will be returned.
     *
     * @param fileName the name of the file to get the icon from
     * @return an icon or null if the retrieval failed
     */
    public static ImageIcon getIconFromFileName(String fileName)
    {
        /*
         Currently the only way I found that works consistently across platforms.
         Put the images into the images.jar file and add it as a source library
         to the NetBeans project.
         */
        URL iconUrl = LibHelpers.class.getClass().getResource("/" + fileName);
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconUrl));
    }
    
    /**
     * Patch the behaviour of a component. 
     * TAB transfers focus to the next focusable component,
     * SHIFT+TAB transfers focus to the previous focusable component.
     * 
     * @param c The component to be patched.
     */
    public static void patchFocus(Component c) {
        Set<KeyStroke> strokes;
        strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }
}
