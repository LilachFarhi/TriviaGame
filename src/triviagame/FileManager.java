package triviagame;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    private String fileName;
    
    public FileManager(String fileName)
    {
        this.fileName = fileName;
    }
    
    public List<Object> GetAllDataFromFile() 
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        List<Object> list = new ArrayList();
        ObjectInputStream objectStream = null;
        
        try
        {
            objectStream = new ObjectInputStream(new FileInputStream(fileName));
            Object currentObject = objectStream.readObject();
            
            while (currentObject != null)
            {
                list.add(currentObject);
            }
        }
        catch (EOFException exception)
        {
            // End of file
        }
        finally
        {
            if (objectStream != null)
            {
                objectStream.close();
            }
        }
        
        return list;
    }
    
    public void WriteAllDataToFile(List<Object> data) 
            throws FileNotFoundException, IOException
    {
        ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(fileName));

        for (int i = 0; i < data.size(); i++)
        {
            Object currentObject = data.get(i);
            objectStream.writeObject(currentObject);
            objectStream.flush();
        }
        
        objectStream.close();
    }
}
