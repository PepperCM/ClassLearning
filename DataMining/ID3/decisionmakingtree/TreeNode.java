package decisionmakingtree;

public class TreeNode {
	TreeNode firstchild = null;
	TreeNode next = null;
	boolean isLeaf = false;
	String att;// �����Ҷ�ӽڵ㣬att��ʾ��������������Ҷ�ӽڵ㣬att��ʾ���ֵ�����
	String kind;

	@Override
	public String toString() {
		if (isLeaf)
			return att + "-" + kind ;
		else
			return att ;
	}
}