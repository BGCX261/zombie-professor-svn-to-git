package edu.purdue.cs.sigai.heap;

// This class represents one particular node in the heap
public class HeapNode<T extends Comparable<T>>
{
    final T node;       // The contents of this particular node
	HeapNode<T> next;   // The next node in this sibling list
	HeapNode<T> prev;   // The prev node in this sibling list
    HeapNode<T> child;  // The youngest of this node's children
	HeapNode<T> par;    // This node's parent

    // Constructor
    public HeapNode(final T node)
    {
        this.node = node;
        next = prev = par = child = null;
    }

	// Remove this node from any child list it might be in, cleanly.
	public void extract()
	{
		if (prev != null)
			prev.next = next;
		else if (par != null && par.child == this)
			par.child = next;
		if (next != null) next.prev = prev;
	}

    // Adds a node to this node's children
    public void addChild(HeapNode<T> newChild)
    {
		newChild.next = child;
		newChild.prev = null;
		newChild.par = this;

		if (child != null)
			child.prev = newChild;
		child = newChild;
    }

	// Accessors
	public T           getNode()  { return node; }
	public HeapNode<T> getNext()  { return next; }
	public HeapNode<T> getPrev()  { return prev; }
	public HeapNode<T> getChild() { return child; }
	public HeapNode<T> getPar()   { return par; }
}

