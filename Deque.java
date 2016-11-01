import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node<Item> first;
    private Node<Item> last;
    
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    public Deque() // construct an empty deque
    {
        n = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() // is the deque empty?
    {
        return first == null;
    }

    public int size() // return the number of items on the deque
    {
        return n;
    }
    
    public void addFirst(Item item) // add the item to the front
    {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        
        if (oldfirst == null) {
            last = first;
        } else {
            oldfirst.prev = first;
        }
        n++;
    }

    public void addLast(Item item) // add the item to the end
    {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.prev = oldlast;

        if (oldlast == null) {
            first = last;
        } else {
            oldlast.next = last;
        }
        n++;
    }

    public Item removeFirst() // remove and return the item from the front
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;

        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        n--;
        return item;
    }

    public Item removeLast() // remove and return the item from the end
    {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = last.item;
        last = last.prev;

        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        n--;
        return item;
    }

    public Iterator<Item> iterator() // return an iterator over items
    {
        return new DequeIterator<Item>(first);
    }
    
    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        public DequeIterator(Node<Item> first) {
            current = first;
        }
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) // unit testing
     {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("+");
        deque.addFirst("*");
        StdOut.print(deque.removeFirst());
        StdOut.print(deque.removeFirst());
        deque.addLast("1");
        deque.addLast("2");
        StdOut.print(deque.removeLast());
        StdOut.print(deque.removeLast()); 
        StdOut.println("length=" + deque.size()+")");
    }
    

}