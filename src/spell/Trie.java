package spell;

public class Trie implements ITrie
{
    private final Node _root;
    private int _wordCount;

    public Trie()
    {
        _root = new Node();
        _wordCount = 0;
    }

    @Override
    public void add(String word)
    {
        _root.add(word);
    }

    @Override
    public INode find(String word)
    {
        Node current = _root;
        for (int i = 0; i < word.length(); i++)
        {
            char c = word.charAt(i);
            int index = c - 'a';
            if (current.getChildren()[index] == null)
            {
                return null;
            } else
            {
                current = current._children[index];
            }
        }
        if (current.getValue() > 0)
        {
            return current;
        } else
        {
            return null;
        }
    }

    @Override
    public int getWordCount()
    {
        return _root.getWordCount();
    }

    @Override
    public int getNodeCount()
    {
        int nodeCount = 1;
        nodeCount += _root.getChildNodeCount();
        return nodeCount;
    }

    @Override
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        _root.generateString(outputString, "");
        return outputString.toString();
    }

    @Override
    public int hashCode()
    {
        INode[] children = _root.getChildren();
        int first_index = 0;
        for (int i = 0; i < 26; i++)
        {
            if (children[i] != null)
            {
                first_index = i;
                break;
            }
        }
        int numNodes = this.getNodeCount();
        int wordCount = this.getWordCount();
        return first_index * numNodes * wordCount;
    }

    public Node getRoot()
    {
        return _root;
    }

    @Override
    public boolean equals(Object otherTrie)
    {
        if (otherTrie == null)
        {
            return false;
        }

        if (this == otherTrie)
        {
            return true;
        }

        Node otherRoot = ((Trie) otherTrie).getRoot();
        return _root.equalsHelper(otherRoot);
    }
}
