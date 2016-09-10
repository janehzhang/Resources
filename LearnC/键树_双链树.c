/*
	键树也叫做数字查找树：
		就像字典的书边标目：先标出首字母是A,B,C...的，再对各部分标出第二个字母是A,B,C...的

	1.双链树（用树的孩子，兄弟链表来表示）
		synbol域：存放关键字
		son域：存放指向第一棵字树的根的指针
		brother域：存放指向右兄弟的指针

	2.字典树（用多重链表来表示）
*/





import java.util.Arrays;

public class TrieNode {

	public TrieNode() {
		ptr = new TrieNode[BRANCH];
		Arrays.fill(ptr, null);
	}

	public static final int BRANCH = 27;
	public TrieNode[] ptr = null;
	public int nptr = 0;
}


public class Trie {

	public Trie() {
		root = new TrieNode();
	}

	public void Insert(String key) {
		TrieNode p = root;
		for (int i = 0; i < key.length(); i++) {
			int offset = key.charAt(i) - 'a';
			if (p.ptr[offset] == null) {
				p.ptr[offset] = new TrieNode();
				p.nptr++;
			}
			p = p.ptr[offset];
		}
	}

	public boolean Search(String key) {
		TrieNode p = root;
		for (int i = 0; i < key.length(); i++) {
			int offset = key.charAt(i) - 'a';
			if (p.ptr[offset] == null) {
				return false;
			}
			p = p.ptr[offset];
		}
		return true;
	}

	private TrieNode root = null;

	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.Insert("liheyuan");
		trie.Insert("liheyuan");
		System.out.println(trie.Search("liheyuan"));
	}
}