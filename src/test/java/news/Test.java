package news;

import java.util.Iterator;

public class Test {
	public static void main(String[] args) {

		// Queue<String> s2 = new Queue<String>();
		// Scanner scanner = new Scanner(System.in);
		// while (scanner.hasNext()) {
		// String string = scanner.next();
		// if (string.equals("-")) {
		// System.out.print(s2.dequeue() + " ");
		// } else {
		// s2.enqueue(string);
		// }
		// }
		// scanner.close();
		//
		Stack<String> s = new Stack<String>();
		s.push("a");
		s.push("b");
		s.push("c");
		s.push("d");
		s.push("e");
		for (String string : s) {
			System.out.println(string);
		}
		System.out.println("--" + s.pop());

		Queue<String> s2 = new Queue<String>();
		s2.enqueue("a");
		s2.enqueue("b");
		s2.enqueue("c");
		s2.enqueue("d");
		s2.enqueue("e");
		Iterator<String> iterator = s2.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println("--" + s2.dequeue());
	}

}

// 栈，数组实现
class CapacityStack<Item> implements Iterable<Item> {
	private Item[] a;

	private int n;

	@SuppressWarnings("unchecked")
	public CapacityStack() {
		a = (Item[]) new Object[1];
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	public void push(Item item) {
		if (n == a.length) {
			resize(a.length * 2);
		}
		a[n++] = item;
	}

	public Item pop() {
		Item item = a[--n];
		a[n] = null;
		if (n > 0 && n == a.length / 4) {
			resize(a.length / 2);
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	private void resize(int max) {
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < n; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	public Iterator<Item> iterator() {
		return new ReverseArrayIterator();
	}

	private class ReverseArrayIterator implements Iterator<Item> {

		private int i = n;

		public boolean hasNext() {
			return i > 0;
		}

		public Item next() {
			return a[--i];
		}

		public void remove() {
		}

	}
}

// 栈，先进后出，封装对象实现
class Stack<Item> implements Iterable<Item> {
	private Node first;

	private int n;

	private class Node {
		Item item;

		Node next;
	}

	public boolean isEmpty() {
		return first == null; // 或n==0
	}

	public int size() {
		return n;
	}

	public void push(Item item) {
		// 自己写的
		Node newNode = new Node();
		newNode.item = item;
		newNode.next = first;
		first = newNode;
		// 书上写的
		// Node oldFirst = first;
		// first = new Node();
		// first.item = item;
		// first.next = oldFirst;
		n++;
	}

	public Item pop() {
		Item item = first.item;
		first = first.next;
		n--;
		return item;
	}

	public Iterator<Item> iterator() {
		return new ReverseArrayIterator();
	}

	private class ReverseArrayIterator implements Iterator<Item> {

		private Node temp = first;

		public boolean hasNext() {
			return temp != null;
		}

		public Item next() {
			Item item = temp.item;
			temp = temp.next;
			return item;
		}

	}

}

// 队列，先进先出
class Queue<Item> implements Iterable<Item> {
	private Node first;

	private Node last;

	private int n;

	private class Node {
		Item item;

		Node next;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	public void enqueue(Item item) {
		// 书上写的
		// Node oldLast = last;
		// last = new Node();
		// last.item = item;
		// last.next = null;

		Node newNode = new Node();
		newNode.item = item;
		if (isEmpty()) {
			first = last = newNode;
		} else {
			last.next = newNode;
			last = newNode;
			// oldLast.next = last;
		}
		n++;
	}

	public Item dequeue() {
		Item item = first.item;
		first = first.next;
		if (isEmpty()) {
			last = null;
		}
		n--;
		return item;
	}

	public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<Item> {

		private Node temp = first;

		@Override
		public boolean hasNext() {
			return temp != null;
		}

		@Override
		public Item next() {
			Item item = temp.item;
			temp = temp.next;
			return item;
		}

	}

}
