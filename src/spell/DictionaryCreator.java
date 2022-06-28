package spell;

import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class DictionaryCreator
{
    private final Scanner _dictionaryScanner;

    public DictionaryCreator(Path filepath) throws IOException
    {
        _dictionaryScanner = new Scanner(filepath, StandardCharsets.UTF_8);
    }

    public boolean hasNext()
    {
        return _dictionaryScanner.hasNext();
    }

    public String nextWord()
    {
        return _dictionaryScanner.next();
    }
}
