package glostrainer.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;

/**
 * This class contains static helper methods (functions) for GUI components.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class GUIHelpers
{
    
    /**
     * Indent size for GUI components in pixels.
     */
    public static int INDENT_SIZE = 8;
    
    /**
     * Size for GUI checkboxes in pixels.
     */
    public static int CHECKBOX_SIZE = 20;
    
    private GUIHelpers()
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
        URL iconUrl = GUIHelpers.class.getClass().getResource("/" + fileName);
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconUrl));
    }

    /**
     * Patch the behaviour of a component. TAB transfers focus to the next
     * focusable component, SHIFT+TAB transfers focus to the previous focusable
     * component.
     *
     * @param c The component to be patched.
     */
    public static void patchFocus(Component c)
    {
        Set<KeyStroke> strokes;
        strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }

    /**
     * Binds key presses to the given JTextComponent (such as a JTextField and
     * JTextArea to perform selection, copy, cut and paste events. The shortcut
     * mask is set to the value of
     * <code>Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()</code> which
     * means that by default, for OS X-based systems, the shortcut accelerator
     * key will be set to the Command key, and for Windows or Linux-based
     * systems, the key will be set to the Control key.
     *
     * @param c
     */
    public static void bindEditEvents(JTextComponent c)
    {
        // bind Ctrl key on windows and Cmd key on OSX.
        int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JTextComponent.KeyBinding[] newBindings =
        {
            new JTextComponent.KeyBinding(
            KeyStroke.getKeyStroke(KeyEvent.VK_A, shortcutMask),
            DefaultEditorKit.selectAllAction),
            new JTextComponent.KeyBinding(
            KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcutMask),
            DefaultEditorKit.copyAction),
            new JTextComponent.KeyBinding(
            KeyStroke.getKeyStroke(KeyEvent.VK_V, shortcutMask),
            DefaultEditorKit.pasteAction),
            new JTextComponent.KeyBinding(
            KeyStroke.getKeyStroke(KeyEvent.VK_X, shortcutMask),
            DefaultEditorKit.cutAction),
        };

        Keymap k = c.getKeymap();
        JTextComponent.loadKeymap(k, newBindings, c.getActions());
    }
    
    /**
     * Get a list of all components from a given Container.
     * @author Richard Bair
     * @param c
     * @return 
     */
    public static List<Component> getAllComponents(Container c)
    {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps)
        {
            compList.add(comp);
            if (comp instanceof Container) // recursion
            {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }
}
