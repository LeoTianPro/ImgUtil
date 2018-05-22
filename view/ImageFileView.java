/**
 * @author Titanlbr520
 */
package view;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;
import java.util.Hashtable;

public class ImageFileView extends FileView {
    private Hashtable icons            = new Hashtable(5);
    private Hashtable fileDescriptions = new Hashtable(5);
    private Hashtable typeDescriptions = new Hashtable(5);

    public String getName(File f) {
        return null;
    }


    public String getDescription(File f) {
        return (String) fileDescriptions.get(f);
    }


    public String getTypeDescription(File f) {
        return (String) typeDescriptions.get(getExtension(f));
    }

    public String getExtension(File f) {
        String name = f.getName();
        if (name != null) {
            int extensionIndex = name.lastIndexOf('.');
            if (extensionIndex < 0)
                return null;
            return name.substring(extensionIndex + 1).toLowerCase();
        }
        return null;
    }


    public Icon getIcon(File f) {
        Icon icon = null;
        String extension = getExtension(f);
        if (extension != null) {
            icon = (Icon) icons.get(extension);
        }
        return icon;
    }


    public Boolean isTraversable(File f) {
        return null; // Use default from FileSystemView
    }
}