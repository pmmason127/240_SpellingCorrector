package spell;

import java.io.IOException;
import java.util.Vector;
import java.util.Locale;
import java.nio.file.Path;

public class SpellCorrector implements ISpellCorrector
{
    private Trie _dictionary;

    public SpellCorrector() {}

    @Override
    public void useDictionary(String dictionaryFile) throws IOException
    {
        Path dictionaryPath = Path.of(dictionaryFile);
        DictionaryCreator scanner = new DictionaryCreator(dictionaryPath);
        _dictionary = new Trie();

        while (scanner.hasNext())
        {
            String word = scanner.nextWord();
            _dictionary.add(word);
        }

        System.out.println(_dictionary.getWordCount() + " words loaded.");
    }

    private Vector<String> getDeletionWords(Vector<String> words)
    {
        Vector<String> deletionWords = new Vector<>();
        for (String word : words)
        {
            for (int i = 0; i < word.length(); i++)
            {
                String deletionWord = word.substring(0, i) + word.substring(i + 1);
                deletionWords.add(deletionWord);
            }
        }
        return deletionWords;
    }

    private Vector<String> getTranspositionWords(Vector<String> words)
    {
        Vector<String> transpositionWords = new Vector<>();
        for (String word : words)
        {
            for (int i = 0; i < word.length() - 1; i++)
            {
                String transpositionWord = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2);
                transpositionWords.add(transpositionWord);
            }
        }
        return transpositionWords;
    }

    private Vector<String> getAlterationWords(Vector<String> words)
    {
        Vector<String> alterationWords = new Vector<>();
        for (String word : words)
        {
            for (int i = 0; i < word.length(); i++)
            {
                for (char c = 'a'; c <= 'z'; c++)
                {
                    if (c != word.charAt(i))
                    {
                        String alterationWord = word.substring(0, i) + c + word.substring(i + 1);
                        alterationWords.add(alterationWord);
                    }
                }
            }
        }
        return alterationWords;
    }

    private Vector<String> getInsertionWords(Vector<String> words)
    {
        Vector<String> insertionWords = new Vector<>();
        for (String word : words)
        {
            for (int i = 0; i < word.length() + 1; i++)
            {
                for (char c = 'a'; c <= 'z'; c++)
                {
                    String insertionWord = word.substring(0, i) + c + word.substring(i);
                    insertionWords.add(insertionWord);
                }
            }
        }
        return insertionWords;
    }

    @Override
    public String suggestSimilarWord(String inputWord)
    {
        String word = inputWord.toLowerCase(Locale.ENGLISH);
        if (_dictionary.find(word) != null)
        {
            return word;
        }

        final int MAX_EDIT_DISTANCE = 2;
        Vector<String> words = new Vector<>(1);
        words.add(word);
        for (int i = 0; i < MAX_EDIT_DISTANCE; i++)
        {
            Vector<String> deletionVariants = getDeletionWords(words);
            Vector<String> transpositionVariants = getTranspositionWords(words);
            Vector<String> alterationVariants = getAlterationWords(words);
            Vector<String> insertionVariants = getInsertionWords(words);

            words.clear();

            words.addAll(deletionVariants);
            words.addAll(transpositionVariants);
            words.addAll(alterationVariants);
            words.addAll(insertionVariants);
            words.sort(String::compareTo);

            Vector<String> matches = new Vector<>();
            for (String candidate : words)
            {
                if (_dictionary.find(candidate) != null)
                {
                    matches.add(candidate);
                }
            }
            if (matches.size() > 0)
            {
                String topMatch = matches.get(0);
                INode topMatchNode = _dictionary.find(matches.get(0));
                for (String match : matches)
                {
                    INode matchNode = _dictionary.find(match);
                    if (matchNode.getValue() > topMatchNode.getValue())
                    {
                        topMatch = match;
                        topMatchNode = matchNode;
                    }
                }
                return topMatch;
            }
        }
        return null;
    }
}