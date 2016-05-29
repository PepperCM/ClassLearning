package decisionmakingtree;

public class TreeNode {
	TreeNode firstchild = null;
	TreeNode next = null;
	boolean isLeaf = false;
	String att;// 如果是叶子节点，att表示的是类别，如果不是叶子节点，att表示区分的属性
	String kind;

	@Override
	public String toString() {
		if (isLeaf)
			return att + "-" + kind ;
		else
			return att ;
	}
}