package spell;

public class Node implements INode
{
    public Node[] _children;
    private int _numNodes;

    public Node()
    {
        _numNodes = 0;
        _children = new Node[26];
    }

    @Override
    public int getValue()
    {
        return _numNodes;
    }

    @Override
    public void incrementValue()
    {
        _numNodes++;
    }

    @Override
    public INode[] getChildren()
    {
        return _children;
    }

    public void add(String word)
    {
        // if the word is empty, then this word already in the tree
        if (word.length() == 0)
        {
            this.incrementValue();
            return;
        }
        int index = word.charAt(0) - 'a';
        // if the child is null, then create a new node
        if (_children[index] == null)
        {
            _children[index] = new Node();
        }
        _children[index].add(word.substring(1));
    }

    public int getChildNodeCount()
    {
        int numChildren = 0;

        for (int i = 0; i < 26; i++)
        {
            if (this._children[i] != null)
            {
                numChildren++;
                numChildren += this._children[i].getChildNodeCount();
            }
        }
        return numChildren;
    }

    public void generateString(StringBuilder stringBuilder, String word)
    {
        if (this.getValue() > 0)
        {
            stringBuilder.append(word).append("\n");
            if (this.getChildNodeCount() == 0)
            {
                return;
            }
        }
        for (int i = 0; i < 26; i++)
        {
            Node child = this._children[i];
            if (child != null)
            {
                child.generateString(stringBuilder, word + (char) ('a' + i));
            }
        }
    }

    public boolean equalsHelper(Node otherNode)
    {
        for (int i = 0; i < 26; i++)
        {
            Node child = this._children[i];
            Node otherChild = otherNode._children[i];
            if ((child != null && otherChild == null) || (child == null && otherChild != null))
            {
                return false;
            } else if (child != null)
            {
                if (child.getValue() != otherChild.getValue())
                {
                    return false;
                }
                if (!child.equalsHelper(otherChild))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public int getWordCount()
    {
        int wordCount = 0;
        if (this.getValue() > 0)
        {
            wordCount++;
        }
        for (int i = 0; i < 26; i++)
        {
            Node child = this._children[i];
            if (child != null)
            {
                wordCount += child.getWordCount();
            }
        }
        return wordCount;
    }
}
