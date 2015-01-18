package glostrainer;

import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * This class contains static helper methods (functions).
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class LibHelpers 
{
    private LibHelpers() {}
    
    /**
     * Retrieves an <code>ImageIcon</code> from the <code>images.jar</code> archive
     * from the given file name. If the file with the given file name does not
     * exist or could not be read, <code>null</code> will be returned.
     * @param fileName the name of the file to get the icon from
     * @return an icon or null if the retrieval failed
     */
    public static ImageIcon getIconFromFileName(String fileName)
    {
        URL iconUrl = LibHelpers.class.getClass().getResource("/" + fileName);
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconUrl));
    }
}
